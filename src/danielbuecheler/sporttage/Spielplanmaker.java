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
	String tableTeams = "Mittelstufe";
	String sportart = "BM";
	PreparedStatement getMannschaften;
	PreparedStatement addZeit;
	PreparedStatement addSpielF1;
	static Calendar beginn;
	static Calendar ende;
	ArrayList<String> teams = new ArrayList<>();
	int spieldauer;
	int pausendauer;

	public Spielplanmaker(String sportart, String stufe, int feld) throws SQLException, IllegalArgumentException {
		// TODO: automatisch Tabelle erstellen
		
		this.sportart = sportart; // Sportart setzen
		this.tablePlan = String.format("%s_%s", stufe, sportart); // Tabellennamen festlegen
		
		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",
				SpielplanerApp.properties.getProperty("database_ip_address"),
				SpielplanerApp.properties.getProperty("database_name")), SpielplanerApp.properties
				.getProperty("database_username"), SpielplanerApp.properties.getProperty("database_password"));

		erstelleTabelleWennNoetig(); // Wenn nötig eine neue Tabell erstellen
		addZeit = con.prepareStatement(String
				.format("INSERT INTO %s (Spielbeginn, Spielende) VALUES (?, ?)", tablePlan)); // PreparedStatement zum Einfügen einer neuen Zeit
		getMannschaften = con.prepareStatement(String.format("SELECT DISTINCT %s FROM %s WHERE %s LIKE ? ORDER BY %s",
				sportart, tableTeams, sportart, sportart)); // PreparedStatement zum Abfragen der Mannschaften
		addSpielF1 = con.prepareStatement(String.format("UPDATE %s SET Feld1 = ? WHERE Spielbeginn = ?", tablePlan)); // PreparedStatement zum Einfügen eines
																														// Spiels auf Feld 1 zu einer bestimmten
																														// Zeit
	}

	public void addMannschaft(String mannschaftsname) throws SQLException {
		getMannschaften.setString(1, mannschaftsname); // SQL-Abfrage einstellen
		ResultSet rs = getMannschaften.executeQuery(); // SQL-Abfrage ausführen
		if (!rs.next()) {
			System.out.printf("FEHLER: Keine Mannschaft namens %s in der Datenbank\n", mannschaftsname); // Fehler bei nicht vorhandener Mannschaft
			return;
		}
		teams.add(rs.getString(1)); // Mannschaft zur ArrayList hinzufügen
		System.out.printf("Mannschaft gefunden: %s\n", mannschaftsname);
		rs.close(); // ResultSet schließen
	}

	public void plane(int startH, int startM, int spieldauer, int pausendauer) throws SQLException {
		if (teams.size() < 4) { // Minimal 4 Teams akzeptieren
			throw new IllegalArgumentException("Minimal vier Mannschaften erlaubt");
		}
		System.out.printf("Anzahl Teams: %d\n", teams.size());
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
		addSpielF1.setString(1, String.format("%s : %s", team1, team2)); // Spiel-String setzen
		addSpielF1.setTime(2, beginnAsSQLTime); // Abfrageparameter einsetzenaddZeit.executeUpdate(); // Zeit hinzufügen
		addZeit.executeUpdate();
		if(addSpielF1.executeUpdate() > 0) { // Update ausführen
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

	private void erstelleTabelleWennNoetig() throws SQLException {
		System.out.println(tablePlan);
		PreparedStatement tabellenAbfragen = con.prepareStatement(String.format("SHOW TABLES WHERE \"Tables_in_%s\" = ?", SpielplanerApp.properties.getProperty("database_name"))); 
		tabellenAbfragen.setString(1, tablePlan);
		ResultSet tablesInDB = tabellenAbfragen.executeQuery();
		boolean tabelleExistiert = tablesInDB.next();
		tablesInDB.close();
		if(tabelleExistiert) // Wenn Tabelle schon vorhanden aussteigen
			return;
		else {
			PreparedStatement tabelleHinzufuegen = con.prepareStatement(String.format("CREATE TABLE %s (Spielbeginn TIME NOT NULL PRIMARY KEY, Spielende TIME NOT NULL, Feld1 VARCHAR(15) DEFAULT '', Feld1Schiri VARCHAR(30) DEFAULT '', Feld1Ergebnis VARCHAR(10) DEFAULT '')", tablePlan));
			System.out.println(tabelleHinzufuegen);
			tabelleHinzufuegen.execute(); // Neue Tabelle erstellen
		}
	}

}
