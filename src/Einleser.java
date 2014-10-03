import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class Einleser {
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
	private String table = "Mittelstufe";
	private String klasse;
	private Connection con;
	private FileInputStream fis;
	private Sheet sheet1;
	private PreparedStatement holeSchuelerID;
	private PreparedStatement addSchueler;
	private PreparedStatement addSportartToSchueler;
	
	/**
	 * Initialisiert Einleser-Objekt
	 * @param xlsname Name des Excel-Dokuments
	 * @param tableName Name der SQL-Tabelle in der Datenbank, in die geschrieben werden soll
	 * @throws IOException
	 * @throws SQLException
	 */
	public Einleser(String xlsname, String tableName) throws IOException, SQLException {
		// Dokumentname wird bei Aufruf übergeben
		fis = new FileInputStream(xlsname);
		this.table = tableName;
		// Excel-Tabelle als Workbook
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		// Alles interessante steht in Tabelle 1
		sheet1 = wb.getSheetAt(0);
		// Row und Cell sind die "Zeiger"
		Row row = null;
		Cell cell = null;
		// Verbindung zu SQL-Server aufbauen
		con = DriverManager.getConnection("jdbc:mysql://" + sqlServer + '/' + dbName, username, password);
		// SQL-Querys vorbereiten
		holeSchuelerID = con.prepareStatement(String.format("SELECT ID, Vorname, Name FROM %s WHERE Vorname = ? AND Name = ? AND Klasse = ?", this.table));
		addSchueler = con.prepareStatement(String.format("INSERT INTO %s (Vorname, Name, Klasse) VALUES (?, ?, ?)", this.table));
		row = sheet1.getRow(0);
		cell = row.getCell(9);
		klasse = cell.getStringCellValue();
		System.out.println(klasse);
	}
	
	/**
	 * Liefert die interne ID des Schülers zurück
	 * @param vorname
	 * @param name
	 * @param klasse
	 * @return 0 wenn noch nicht in der Tabelle vorhanden
	 * @throws SQLException
	 */
	public int getSchuelerID(String vorname, String name, String klasse) throws SQLException {
		holeSchuelerID.setString(1, vorname);
		holeSchuelerID.setString(2, name);
		holeSchuelerID.setString(3, klasse);
		ResultSet rs = holeSchuelerID.executeQuery();
		if(!rs.next())
			return 0;
		return rs.getInt(1);
	}
	
	/**
	 * Liest alle Sportarten vom Tabellenblatt ein (nutzt {@code readMannschaftsSportart()} und {@code readZweierSportart()}
	 * @throws SQLException
	 */
	public void readAll() throws SQLException {
		readMannschaftsSportart("FB");
		readMannschaftsSportart("BB");
		readMannschaftsSportart("VB");
		readZweierSportart("TT");
		readZweierSportart("BM");
		readMannschaftsSportart("ST");
	}
	
	/** Liest die Mannschaftssportarten (Fußball, Basketball, Volleyball und Staffellauf) von der Excel-Tabelle ein
	 * @param mannschaft "FB1": Fußball1, "FB2": Fußball2, "BB": Basketball, "VB": Volleyball oder "ST": Staffellauf 
	 * @throws SQLException
	 */
	public void readMannschaftsSportart(String mannschaft) throws SQLException {
		// Die Namen stehen an verschiedenen Positionen je Sportart
		int ersteZeile = 0;
		int letzteZeile = 0;
		int spalteVorname = 0;
		int spalteNachname = 0;
		switch(mannschaft) {
		case "BB":
			ersteZeile = 17;
			letzteZeile = 25;
			spalteVorname = 0;
			spalteNachname = 1;
			break;
		case "FB1":
			ersteZeile = 5;
			letzteZeile = 13;
			spalteNachname = 1;
			spalteVorname = 0;
			break;
		case "FB2":
			ersteZeile = 5;
			letzteZeile = 13;
			spalteVorname = 3;
			spalteNachname = 5;
			break;
		case "VB":
			ersteZeile = 29;
			letzteZeile = 37;
			spalteVorname = 0;
			spalteNachname = 1;
			break;
		case "ST":
			ersteZeile = 40;
			letzteZeile = 44;
			spalteVorname = 3;
			spalteNachname = 5;
			break;
		default:
			throw new IllegalArgumentException("Unbekannte Sportart (Mannschaftssportarten sind: \"FB1\", \"FB2\", \"BB\", \"VB\" und \"ST\")");
		}
		addSportartToSchueler = con.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE ID = ?", this.table, mannschaft.substring(0, 2))); // die zahl bei FB1 / 2 abschneiden
		for(int r = ersteZeile; r < letzteZeile; r++) {
			Row row = sheet1.getRow(r);
			// Vorname
			Cell cell = row.getCell(spalteVorname);
			String vorname = cell.getStringCellValue();
			// Nachname
			cell = row.getCell(spalteNachname);
			String name = cell.getStringCellValue();
			// Nur wenn jemand drinsteht weitermachen
			if(name.isEmpty() && vorname.isEmpty())
				continue;
			// Schüler hinzufügen wenn er noch nicht vorhanden ist
			int id = getSchuelerID(vorname, name, klasse);
			if(id == 0) {
				addSchueler.setString(1, vorname);
				addSchueler.setString(2, name);
				addSchueler.setString(3, this.klasse);
				addSchueler.executeUpdate();
				id = getSchuelerID(vorname, name, klasse);
			}
			// Die Mannschaft bei dem Schüler in der Betreffenden Spalte eintragen
			addSportartToSchueler.setString(1, klasse + mannschaft.substring(2));
			addSportartToSchueler.setInt(2, id);
			addSportartToSchueler.executeUpdate();
		}
	}
	
	/**
	 * Liest die Paarsportarten (Tischtennis und Badminton) ein
	 * @param sportart
	 * @throws SQLException
	 */
	public void readZweierSportart(String sportart) throws SQLException {
		// Die Namen stehen an verschiedenen Positionen je Sportart
		int ersteZeile = 0;
		int letzteZeile = 0;
		int[] spalteVorname = {4, 8}; // Bei TT und BM stehen die Namen nicht alle untereinander, sondern in zwei Blöcken nebeneinander
		int[] spalteNachname = {5, 9};
		switch(sportart) {
		case "TT":
			ersteZeile = 17;
			letzteZeile = 24;
			break;
		case "BM":
			ersteZeile = 29;
			letzteZeile = 36;
			break;
		default:
			throw new IllegalArgumentException("Unbekannte Sportart (Zweiersportarten sind: \"BM\" und \"TT\")");
		}
		int teamnr = 0;
		addSportartToSchueler = con.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE ID = ?", this.table, sportart));
		for(int block = 0; block < 2; block++) {
			naechstesTeam:
			for(int r = ersteZeile; r < letzteZeile; r += 2) {
				teamnr++;
				for(int teammitglied = 0; teammitglied < 2; teammitglied++) {
					Row row = sheet1.getRow(r + teammitglied);
					// Vorname 1. Person
					Cell cell = row.getCell(spalteVorname[block]);
					String vorname = cell.getStringCellValue();
					// Nachname 1. Person
					cell = row.getCell(spalteNachname[block]);
					String name = cell.getStringCellValue();
					// Nur wenn jemand drinsteht weitermachen
					System.out.println(name + ", " + vorname + " " + teamnr);
					if(name.isEmpty() && vorname.isEmpty())
						continue naechstesTeam; // TODO: Hier wäre es möglich, einen Test, ob genügen Personen in der Mannschaft sind, durchzuführen
					// Schüler hinzufügen wenn er noch nicht vorhanden ist
					int id = getSchuelerID(vorname, name, klasse);
					if(id == 0) {
						addSchueler.setString(1, vorname);
						addSchueler.setString(2, name);
						addSchueler.setString(3, this.klasse);
						addSchueler.executeUpdate();
						id = getSchuelerID(vorname, name, klasse);
					}
					// Die Mannschaft bei dem Schüler in der Betreffenden Spalte eintragen
					addSportartToSchueler.setString(1, klasse + teamnr);
					addSportartToSchueler.setInt(2, id);
					addSportartToSchueler.executeUpdate();
				}
			}
		}
	}
	
	/**
	 * Schließt alle geöffneten Ressourcen
	 * @throws IOException
	 * @throws SQLException
	 */
	public void close() throws IOException, SQLException {
		con.close();
		fis.close();
	}

}
