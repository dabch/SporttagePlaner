package danielbuecheler.sporttage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public class Spielplanmaker {

	Connection con;
	String tablePlan;
	String tableTeams = "";
	Sportart sportart;
	static Calendar beginn;
	static Calendar ende;
	ArrayList<String> teams = new ArrayList<>();
	PreparedStatement addSpiel = null;
	PreparedStatement addZeit = null;
	int spieldauer;	
	int pausendauer;

	public Spielplanmaker(Sportart sportart, Stufe stufe) throws SQLException, IllegalArgumentException {

		this.sportart = sportart; // Sportart setzen
		this.tablePlan = String.format("%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz()); // Tabellennamen festlegen
		this.tableTeams = String.format("Teams_%s", stufe.getStufeKurz());

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
	
	public void addTable() throws SQLException {
		System.out.println(tablePlan);
		PreparedStatement tabellenAbfragen = con.prepareStatement("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = ? && TABLE_SCHEMA = ?");
		tabellenAbfragen.setString(1, tablePlan);
		tabellenAbfragen.setString(2, SpielplanerApp.properties.getProperty("database_name"));
		ResultSet tablesInDB = tabellenAbfragen.executeQuery();
		boolean tabelleExistiert = tablesInDB.next();
		System.out.println(tabellenAbfragen);
		tablesInDB.close();
		if (tabelleExistiert) // Wenn Tabelle schon vorhanden aussteigen
			return;
		else {
			PreparedStatement tabelleHinzufuegen = con
					.prepareStatement(String
							.format("CREATE TABLE %s (Spielbeginn TIME NOT NULL PRIMARY KEY, Spielende TIME NOT NULL)",
									tablePlan));
			System.out.println(tabelleHinzufuegen);
			tabelleHinzufuegen.execute(); // Neue Tabelle erstellen
		}
	}

	public void plane(int feldnr, int startH, int startM, int spieldauer, int pausendauer) throws SQLException, TableNotExistentException {
		addSpiel = con.prepareStatement(String.format("UPDATE %s SET Feld%d = ? WHERE Spielbeginn = ?", tablePlan, feldnr)); // PreparedStatement zum Einfügen eines Spiels auf Feld X zu einer bestimmten Zeit
		addZeit = con.prepareStatement(String.format("INSERT INTO %s (Spielbeginn, Spielende) VALUES (?, ?)", tablePlan)); // PreparedStatement zum Einfügen einer neuen Zeit
		if (teams.size() < 4) { // Minimal 4 Teams akzeptieren
			throw new IllegalArgumentException("Minimal vier Mannschaften erlaubt");
		}
		System.out.printf("Anzahl Teams: %d\n", teams.size());
		if(!checkObTableExistiert()) { // Wenn nötig eine neue Tabelle erstellen
			throw new TableNotExistentException(String.format("Spielplan %s existiert noch nicht. Bitte zuerst hinzufügen!", tablePlan));
		}
		erstelleFeld(feldnr); // Feld erstellen (wenn nötig)
		this.spieldauer = spieldauer;
		this.pausendauer = pausendauer;
		beginn = Calendar.getInstance(); // Zeiten setzen
		beginn.set(Calendar.HOUR_OF_DAY, startH);
		beginn.set(Calendar.MINUTE, startM);
		ende = (Calendar) beginn.clone();
		ende.add(Calendar.MINUTE, spieldauer); // Ende = Beginn + Spieldauer
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
		Time beginnAsSQLTime = Time.valueOf(String.format("%s:%s:00", beginn.get(Calendar.HOUR_OF_DAY),
				beginn.get(Calendar.MINUTE))); // Startzeit zwischenspeichern
		Time endeAsSQLTime = Time.valueOf(String.format("%s:%s:00", ende.get(Calendar.HOUR_OF_DAY),
				ende.get(Calendar.MINUTE))); // Endzeit zwischenspeichern
			addZeit.setTime(1, beginnAsSQLTime); // Zeit für Spielbeginn setzen
			addZeit.setTime(2, endeAsSQLTime); // Zeit für Spielende setzen
			addZeit.executeUpdate();
		addSpiel.setString(1, String.format("%s : %s", team1, team2)); // Spiel-String setzen
		addSpiel.setTime(2, beginnAsSQLTime); // Abfrageparameter einsetzenaddZeit.executeUpdate(); // Zeit hinzufügen
		if (addSpiel.executeUpdate() > 0) { // Update ausführen
			System.out.println("Spiel erfolgreich hinzugefügt");
		} else {
			System.out.println("FEHLER: Spiel konnte nicht hinzugefügt werden");
		}
		zeitFuerNaechstesSpiel();
	}

	private void zeitFuerNaechstesSpiel() {
		beginn.add(Calendar.MINUTE, this.spieldauer + this.pausendauer);
		ende.add(Calendar.MINUTE, this.spieldauer + this.pausendauer);
	}
	
	private boolean checkObTableExistiert() throws SQLException {
		PreparedStatement tabellenAbfragen = con.prepareStatement("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = ? && TABLE_SCHEMA = ?");
		tabellenAbfragen.setString(1, tablePlan);
		tabellenAbfragen.setString(2, SpielplanerApp.properties.getProperty("database_name"));
		ResultSet tablesInDB = tabellenAbfragen.executeQuery();
		boolean tabelleExistiert = tablesInDB.next();
		tablesInDB.close();
		return tabelleExistiert;
	}

	private void erstelleFeld(int feldnr) throws SQLException {
		PreparedStatement feldAbfragen = con.prepareStatement("SELECT TABLE_NAME, COLUMN_NAME FROM information_schema.COLUMNS WHERE TABLE_SCHEMA LIKE ? && TABLE_Name = ? && COLUMN_NAME = ?"); // Schauen, ob schon eine Spalte FeldX existiert
		feldAbfragen.setString(1, SpielplanerApp.properties.getProperty("database_name"));
		feldAbfragen.setString(2, tablePlan);
		feldAbfragen.setString(3, String.format("Feld%d", feldnr));
		ResultSet feldXinDB = feldAbfragen.executeQuery();
		boolean feldExistiert = feldXinDB.next();
		feldXinDB.close();
		if(!feldExistiert) {
			PreparedStatement addFeld = con.prepareStatement(String.format("ALTER TABLE %s ADD Feld%d VARCHAR(15) DEFAULT '', ADD Feld%dSchiri VARCHAR(30) DEFAULT '', ADD Feld%dErgebnis VARCHAR(10) DEFAULT ''", tablePlan, feldnr, feldnr, feldnr));
			addFeld.executeUpdate();
		}
	}

}
