package danielbuecheler.sporttage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;


public class Spielplanmaker {
	/**
	 * IP-Adresse des MySQL-Servers
	 */
	final String sqlServer = "192.168.2.105";
	/**
	 * Name der Datenbank
	 */
	final String dbName = "TestFuerSporttage";
	/**
	 * Benutzername für die Datenbank
	 */
	final String username = "root";
	/**
	 * Passwort für den Benutzer der Datenbank
	 */
	final String password = "";
	
	Connection con;
	String tablePlan = "Testspielplan2";
	String tableTeams = "Mittelstufe";
	String sportart = "BM";
	PreparedStatement getMannschaften;
	PreparedStatement addZeit;
	PreparedStatement addSpielF1;
	static Calendar beginn;
	static Calendar ende;
	final static int spieldauer = 5;
	final static int pausedauer = 2;
	ArrayList<String> teams = new ArrayList<>();
	
	public Spielplanmaker() throws SQLException {
		
		con = DriverManager.getConnection("jdbc:mysql://" + sqlServer + '/' + dbName, username, password);
		
		// #################
		Statement stmt = con.createStatement();
		stmt.execute("TRUNCATE TABLE Testspielplan2");
		// #################
		addZeit = con.prepareStatement(String.format("INSERT INTO %s (Spielbeginn, Spielende) VALUES (?, ?)", tablePlan)); // PreparedStatement zum Einfügen einer neuen Zeit
		getMannschaften = con.prepareStatement(String.format("SELECT DISTINCT %s FROM %s WHERE %s LIKE ? ORDER BY %s", sportart, tableTeams, sportart, sportart)); // PreparedStatement zum Abfragen der Mannschaften
		addSpielF1 = con.prepareStatement(String.format("UPDATE %s SET Feld1 = ? WHERE Spielbeginn = ?", tablePlan)); // PreparedStatement zum Einfügen eines Spiels auf Feld 1 zu einer bestimmten Zeit
		getMannschaften.setString(1, "9a%"); // TODO später noch automatisch erkennen
		ResultSet rs = getMannschaften.executeQuery(); // Mannschaften als ResultSet vom Server holen
		if(!rs.next())
			System.out.println("FEHLER: Keine Mannschaften in der Datenbank");;
		rs.absolute(0); // RS wieder zum Anfang setzen, rs.first() funktioniert nicht
		while(rs.next()) { // ArrayList mit Teams erstellen
			teams.add(rs.getString(1));
			System.out.println(rs.getString(1));
		}
		System.out.println("vorher: " + teams.size());
		rs.last(); // RS nach ganz hinten setzen, um die größe abzulesen
		if(rs.getRow() % 2 != 0) { // bei ungerader Anzahl an Teams einen Dummy hinzufügen
			teams.add("");
		}
		System.out.println("nacher: " + teams.size());
		rs.close(); // RS schließen, um Ressourcen zu sparen
		beginn = Calendar.getInstance(); // Zeiten setzen
		beginn.set(Calendar.HOUR_OF_DAY, 13);
		beginn.set(Calendar.MINUTE, 0);
		ende = (Calendar) beginn.clone();
		ende.add(Calendar.MINUTE, spieldauer);
		
		int n = teams.size();// Anzahl Teams

		for (int i = 1; i <= (n - 1); i++) {
			String team1 = teams.get(i - 1); // Teams abfragen
			String team2 = teams.get(n - 1);
			Time beginnAsSQLTime = Time.valueOf(String.format("%s:%s:00", beginn.get(Calendar.HOUR_OF_DAY), beginn.get(Calendar.MINUTE))); // Startzeit zwischenspeichern
			addZeit.setTime(1, beginnAsSQLTime);
			addZeit.setTime(2, Time.valueOf(String.format("%s:%s:00", ende.get(Calendar.HOUR_OF_DAY), ende.get(Calendar.MINUTE))));
			addSpielF1.setString(1, String.format("%s : %s", team1, team2));
			addSpielF1.setTime(2, beginnAsSQLTime);
			System.out.println(team1 + " : " + team2 + " ");
			if(!team1.isEmpty() && !team2.isEmpty()) { // Pause wenn Dummy-Team spielt
				addZeit.executeUpdate(); // Zeit hinzufügen
				addSpielF1.executeUpdate();
				zeitFuerNaechstesSpiel();
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
				team1 = teams.get(t1 - 1); // Teams einlesen
				team2 = teams.get(t2 - 1);
				beginnAsSQLTime = Time.valueOf(String.format("%s:%s:00", beginn.get(Calendar.HOUR_OF_DAY), beginn.get(Calendar.MINUTE)));
				addZeit.setTime(1, beginnAsSQLTime);
				addZeit.setTime(2, Time.valueOf(String.format("%s:%s:00", ende.get(Calendar.HOUR_OF_DAY), ende.get(Calendar.MINUTE))));
				addSpielF1.setString(1, String.format("%s : %s", team1, team2));
				addSpielF1.setTime(2, beginnAsSQLTime); // Spielzeit setzen
				System.out.println(team1 + " : " + team2 + " ");
				if(!team1.isEmpty() && !team2.isEmpty()) { // Pause wenn Dummy spielt
					addZeit.executeUpdate(); // Zeit einfügen
					addSpielF1.executeUpdate(); // Spiel einfügen
					zeitFuerNaechstesSpiel();
				}
			}
		}
		
		

	}
	
	public static void zeitFuerNaechstesSpiel() {
		beginn.add(Calendar.MINUTE, spieldauer + pausedauer);
		ende.add(Calendar.MINUTE, spieldauer + pausedauer);
	}
}
