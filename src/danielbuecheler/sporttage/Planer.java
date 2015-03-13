package danielbuecheler.sporttage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Planer {

	Connection con;
	String tablePlan;
	String tableTeams;
	Sportart sportart;
	Stufe stufe;
	static Calendar beginn;
	static Calendar ende;
	ArrayList<String> teams = new ArrayList<>();
	PreparedStatement addSpiel = null;
	PreparedStatement addZeit = null;
	int spieldauer;	
	int pausendauer;
	public Planer(Sportart sportart, Stufe stufe) throws SQLException, IllegalArgumentException {
		this.stufe = stufe;
		this.sportart = sportart; // Sportart setzen

		this.tableTeams = String.format("Mannschaften_%s", stufe.getStufeKurz());

		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",
				SpielplanerApp.properties.getProperty("database_ip_address"),
				SpielplanerApp.properties.getProperty("database_name")),
				SpielplanerApp.properties.getProperty("database_username"),
				SpielplanerApp.properties.getProperty("database_password"));		
	}

	public void addMannschaft(String mannschaftsname) throws SQLException {
		PreparedStatement getMannschaften = con.prepareStatement(String.format("SELECT DISTINCT %s FROM %s WHERE %s LIKE ? ORDER BY %s", sportart.getSportartKurz(), tableTeams, sportart.getSportartKurz(), sportart.getSportartKurz())); // PreparedStatement zum Abfragen der Mannschaften
		getMannschaften.setString(1, mannschaftsname); // SQL-Abfrage einstellen
		ResultSet rs = getMannschaften.executeQuery(); // SQL-Abfrage ausführen
		if (!rs.next()) {
			System.out.printf("FEHLER: Keine Mannschaft namens %s in der Datenbank\n", mannschaftsname); // Fehler bei nicht vorhandener Mannschaft
			return;
		}
		teams.add(rs.getString(1)); // Mannschaft zur ArrayList hinzufügen
		System.out.printf("Mannschaft hinzugefügt: %s\n", mannschaftsname);
		rs.close(); // ResultSet schließen
	}
	
	public void holeAlleMannschaften() throws SQLException {
		PreparedStatement holeMannschaften = con.prepareStatement(String.format("SELECT DISTINCT %s FROM %s WHERE %s != ''", sportart.getSportartKurz(), tableTeams, sportart.getSportartKurz()));
		ResultSet rs = holeMannschaften.executeQuery();
		ArrayList<String> alleMannschaften = new ArrayList<>();
		System.out.println(holeMannschaften.toString());
		rs.last();
		System.out.println(rs.getRow());
		rs.beforeFirst();
		while(rs.next()) { // alle Teams aus dem rs in die ArrayList einfügen
			alleMannschaften.add(rs.getString(1));
		}
		Collections.shuffle(alleMannschaften); // Mannschaftsverteilung zufällig
		for(String team : alleMannschaften) {
			System.out.println(team);
		}
//		String[][] gruppen = new String[sportart.getAnzahlFelder()][]; // so viele gruppen wie es felder gibt, die anzahl der teams pro gruppe bleiben noch offen
//		gruppen[1][4] = "Jo";
//		System.out.println(gruppen[1][4]);
	}
	

	public void plane(int feldnr, String tag) throws SQLException, TableNotExistentException {
		this.tablePlan = String.format("%s_%s_%s_feld%d", stufe.getStufeKurz(), sportart.getSportartKurz(), tag, feldnr); // Tabellennamen festlegen
		addSpiel = con.prepareStatement(String.format("INSERT INTO %s (Paarung) VALUES (?)", tablePlan)); // PreparedStatement zum Einfügen eines Spiels auf Feld X zu einer bestimmten Zeit
		if (teams.size() < 4) { // Minimal 4 Teams akzeptieren
			throw new IllegalArgumentException("Minimal vier Mannschaften erlaubt");
		}
		PreparedStatement tabellenAbfragen = con
				.prepareStatement("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = ? && TABLE_SCHEMA = ?");
		tabellenAbfragen.setString(1, tablePlan);
		tabellenAbfragen.setString(2, SpielplanerApp.properties.getProperty("database_name"));
		ResultSet tablesInDB = tabellenAbfragen.executeQuery();
		boolean tabelleExistiert = tablesInDB.next(); // Wenn die Tabelle noch nicht existiert ist das ResultSet leer
		tablesInDB.close();
		if(!tabelleExistiert)
			throw new TableNotExistentException("Die SQL-Tabelle für den Spielplan existiert nicht. Bitte zuerst erstellen!");
		System.out.printf("Anzahl Teams: %d\n", teams.size());
		int n = teams.size();// Anzahl Teams

		for (int i = 1; i <= (n - 1); i++) {
			con.setAutoCommit(false); // SQL-Transaktion beginnen
			String team1 = teams.get(i - 1); // Teams abfragen
			String team2 = teams.get(n - 1);
			if (!team1.isEmpty() && !team2.isEmpty()) { // nichts tun wenn Dummy-Team spielt
				insertSpiel(team1, team2);
			}

			for (int k = 1; k <= ((n / 2) - 1); k++) {

				int t1 = (i + k) % (n - 1);
				int t2 = (i - k) % (n - 1);
				if (t1 == 0) // 0 soll als n - 1 interpretiert werden
					t1 = n - 1;
				if (t1 < 0) { // Korrektur, damit die Mannschaftsnummern nicht negativ werden
					int subZ = t1;
					t1 = n - 1 + subZ;
				}
				if (t2 == 0)
					t2 = n - 1;
				if (t2 < 0) {
					int subZ = t2;
					t2 = n - 1 + subZ;
				}

				team1 = teams.get(t1 - 1); // Teams aus ArrayList auslesen
				team2 = teams.get(t2 - 1);
				if (!team1.isEmpty() && !team2.isEmpty()) { // nichts tun wenn Dummy-Team spielt
					insertSpiel(team1, team2); // Spiel hochladen
				}
			}
			con.commit(); // Änderungen in der DB committen
			con.setAutoCommit(true);
		}
	}

	/**
	 * @param team1
	 *            Erster Gegner
	 * @param team2
	 *            Zweiter Gegner
	 * @throws SQLException
	 */
	private void insertSpiel(String team1, String team2) throws SQLException {
		addSpiel.setString(1, String.format("%s : %s", team1, team2)); // Spiel-String setzen
		if (addSpiel.executeUpdate() == 0) { // Update ausführen
			System.out.println("FEHLER: Spiel konnte nicht hinzugefügt werden");
		}
	}
}
