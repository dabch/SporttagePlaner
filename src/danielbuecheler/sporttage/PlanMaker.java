package danielbuecheler.sporttage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class PlanMaker {

	Connection con;
	String tablePlan;
	static Calendar beginn;
	static Calendar ende;

	public PlanMaker(Sportart sportart, Stufe stufe) throws SQLException {
		this.tablePlan = String.format("%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz()); // Tabellennamen festlegen

		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", // Verbindung zur DB aufbauen
				SpielplanerApp.properties.getProperty("database_ip_address"), SpielplanerApp.properties.getProperty("database_name")),
				SpielplanerApp.properties.getProperty("database_username"), SpielplanerApp.properties.getProperty("database_password"));
	}

	public void addTable(Calendar startzeit, int spieldauerMin, int pausendauerMin, boolean vorhandenenUeberschreiben) throws SQLException {
		java.sql.Time beginn = new java.sql.Time(startzeit.getTimeInMillis()); // Startzeit des 1. Spiels
		java.sql.Time ende = new java.sql.Time(startzeit.getTime().getTime() + (60000 * spieldauerMin)); // Zeit des Endes des 1. Spiels

		PreparedStatement tabellenAbfragen = con
				.prepareStatement("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_NAME = ? && TABLE_SCHEMA = ?");
		tabellenAbfragen.setString(1, tablePlan);
		tabellenAbfragen.setString(2, SpielplanerApp.properties.getProperty("database_name"));
		ResultSet tablesInDB = tabellenAbfragen.executeQuery();
		boolean tabelleExistiert = tablesInDB.next(); // Wenn die Tabelle noch nicht existiert ist das ResultSet leer
		tablesInDB.close();
		if (tabelleExistiert) { // Wenn Tabelle schon vorhanden aussteigen
			if(!vorhandenenUeberschreiben) {
				System.out.println("Tabelle exisitiert bereits, wird nicht überschrieben");
				return;
			}
			PreparedStatement dropTable = con.prepareStatement(String.format("DROP TABLE %s", tablePlan));
			dropTable.execute();
			System.out.println("Tabelle existiert bereits, wird überschrieben");
		}
		// Tabelle erstellen
		PreparedStatement tabelleHinzufuegen = con.prepareStatement(String.format(
				"CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTO_INCREMENT, Spielbeginn TIME NOT NULL UNIQUE, Spielende TIME NOT NULL)", tablePlan));
		System.out.println(tabelleHinzufuegen);
		tabelleHinzufuegen.execute(); // Neue Tabelle erstellen

		// Zeiten eintragen (20x)
		PreparedStatement addZeit = con.prepareStatement(String.format("INSERT INTO %s (Spielbeginn, Spielende) VALUES (?, ?)", tablePlan)); // PreparedStatement
																																				// zum Einfügen
																																				// einer neuen
																																				// Zeit
		for (int i = 0; i < 20; i++) { // vorsorglich 20 Spiele hinzufügen
			addZeit.setTime(1, beginn);
			addZeit.setTime(2, ende);
			addZeit.execute();
			beginn.setTime(beginn.getTime() + (spieldauerMin * 60000 + pausendauerMin * 60000));
			ende.setTime(ende.getTime() + (spieldauerMin * 60000 + pausendauerMin * 60000));
		}
	}
}
