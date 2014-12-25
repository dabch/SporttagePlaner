package danielbuecheler.sporttage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class ZeitMaker {

	Connection con;
	String tableZeiten;
	static Calendar beginn;
	static Calendar ende;

	public ZeitMaker(Sportart sportart, Stufe stufe, String tag) throws SQLException {
		this.tableZeiten = String.format("%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz()); // Tabellennamen festlegen

		// SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3 FROM MS_BM_MO_zeiten z
		// LEFT JOIN MS_BM_MO_feld2 f2 ON f2.ID = z.ID LEFT JOIN MS_BM_MO_feld1 f1 ON f1.ID = z.ID LEFT JOIN MS_BM_MO_feld3 f3 ON f3.ID = z.ID WHERE f1.Paarung
		// != '' OR f2.Paarung != '' OR f3.Paarung != '';

		tableZeiten = String.format("%s_%s_%s_zeiten", stufe.getStufeKurz(), sportart.getSportartKurz(), tag);

		con = DriverManager.getConnection(
				String.format(
						"jdbc:mysql://%s/%s", // Verbindung zur DB aufbauen
						SpielplanerApp.properties.getProperty("database_ip_address"),
						SpielplanerApp.properties.getProperty("database_name")),
				SpielplanerApp.properties.getProperty("database_username"),
				SpielplanerApp.properties.getProperty("database_password"));
	}

	public void close() throws SQLException {
		con.close();
	}

	public void addZeiten(Calendar startzeit, int spieldauerMin, int pausendauerMin) throws SQLException {
		java.sql.Time beginn = new java.sql.Time(startzeit.getTimeInMillis()); // Startzeit des 1. Spiels
		java.sql.Time ende = new java.sql.Time(startzeit.getTime().getTime() + (60000 * spieldauerMin)); // Zeit des Endes des 1. Spiels

		// Zeit-Tabelle leeren
		PreparedStatement leereTabelle = con.prepareStatement(String.format("TRUNCATE TABLE %s", tableZeiten)); // Die alten Zeiten müssen erst gelöscht werden
		leereTabelle.execute();

		// Zeiten eintragen (30x)
		PreparedStatement addZeit = con.prepareStatement(String.format(
				"INSERT INTO %s (Spielbeginn, Spielende) VALUES (?, ?)", tableZeiten)); // PreparedStatement zum Einfügen einer neuen Zeit
		for (int i = 0; i < 30; i++) { // vorsorglich Zeiten für 30 Spiele hinzufügen
			addZeit.setTime(1, beginn);
			addZeit.setTime(2, ende);
			addZeit.execute();
			beginn.setTime(beginn.getTime() + (spieldauerMin * 60000 + pausendauerMin * 60000));
			ende.setTime(ende.getTime() + (spieldauerMin * 60000 + pausendauerMin * 60000));
		}
	}
}
