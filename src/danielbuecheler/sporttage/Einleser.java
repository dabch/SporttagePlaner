package danielbuecheler.sporttage;
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
	private String table = "";
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
	 * @param tableName Name der SQL-Tabelle in der Datenbank, in die geschrieben werden
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public Einleser(String xlsname) throws SQLException, IOException {
		// Dokumentname wird bei Aufruf übergeben
		fis = new FileInputStream(xlsname);
		// Excel-Tabelle als Workbook
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		// Alles interessante steht in Tabelle 1
		sheet1 = wb.getSheetAt(0);
		
		// Row und Cell sind die "Zeiger"
		Row row = null;
		Cell cell = null;
		row = sheet1.getRow(0);
		cell = row.getCell(9);
		klasse = cell.getStringCellValue();
		// Erkennung der Stufe
		if(klasse.contains("10") || klasse.contains("K1") || klasse.contains("K2"))
			table = "Mannschaften_OS";
		else if(klasse.contains("9") || klasse.contains("8") || klasse.contains("7") || klasse.equals("A1"))
			table = "Mannschaften_MS";
		else if(klasse.contains("7") || klasse.contains("6") || klasse.contains("5"))
			table = "Mannschaften_US";
		else {
			throw new IllegalArgumentException("Keine Stufe erkannt, bitte Angabe der Klasse in der Tabelle prüfen");
		}
		System.out.println(table);
		System.out.println("Klasse " + klasse);
		
		
		// Verbindung zu SQL-Server aufbauen
		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",
				SpielplanerApp.properties.getProperty("database_ip_address"),
				SpielplanerApp.properties.getProperty("database_name")),
				SpielplanerApp.properties.getProperty("database_username"),
				SpielplanerApp.properties.getProperty("database_password"));
		// SQL-Querys vorbereiten
		holeSchuelerID = con.prepareStatement(String.format("SELECT ID, Vorname, Name FROM %s WHERE Vorname = ? AND Name = ? AND Klasse = ?", this.table));
		addSchueler = con.prepareStatement(String.format("INSERT INTO %s (Vorname, Name, Klasse) VALUES (?, ?, ?)", this.table));		
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
		con.setAutoCommit(false); // Beginn Transaktion
		System.out.println("Fußball 1 einlesen...");
		readMannschaftsSportart("FB1");
		System.out.println("Fußball 2 einlesen...");
		readMannschaftsSportart("FB2");
		System.out.println("Basketball einlesen...");
		readMannschaftsSportart("BB");
		System.out.println("Volleyball einlesen...");
		readMannschaftsSportart("VB");
		System.out.println("Tischtennis einlesen...");
		readZweierSportart("TT");
		System.out.println("Badminton einlesen...");
		readZweierSportart("BM");
		System.out.println("Staffellauf einlesen...");
		readMannschaftsSportart("ST");
		con.commit(); // Ende Transaktion
		con.setAutoCommit(true);
	}
	
	/** Liest die Mannschaftssportarten (Fußball, Basketball, Volleyball und Staffellauf) von der Excel-Tabelle ein
	 * @param mannschaftZumEinlesen "FB1": Fußball1, "FB2": Fußball2, "BB": Basketball, "VB": Volleyball oder "ST": Staffellauf 
	 * @throws SQLException
	 */
	public void readMannschaftsSportart(String mannschaftZumEinlesen) throws SQLException {
		// Die Namen stehen an verschiedenen Positionen je Sportart
		int ersteZeile = 0;
		int letzteZeile = 0;
		int spalteVorname = 0;
		int spalteNachname = 0;
		// Der Übersicht halber hier zwei Variablen für die Sportart und die Nummer der Mannschaft (bei FB1 / 2)
		String sportart = mannschaftZumEinlesen.substring(0, 2);
		String teamnr = mannschaftZumEinlesen.substring(2); // Hier als String, damit ich nicht Integer.valueOf() aufrufen muss (potenzielle Fehlerquelle wenn keine Nummer angegeben ist)
		switch(mannschaftZumEinlesen) {
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
		addSportartToSchueler = con.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE ID = ?", this.table, sportart)); // die zahl bei FB1 / 2 abschneiden
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
			// Wenn die Klasse mit einer Zahl endet (z.B. K1) und eine Nummer in der Mannschaft angefügt werden muss (z.B. K1_1 oder 7a1), wird ein Unterstrich zwischen Mannschaft und Nummer eingefügt
			String teamString = Character.isDigit(klasse.charAt(klasse.length() - 1)) && !teamnr.isEmpty() ? klasse + '_' + teamnr : klasse +  teamnr;
			addSportartToSchueler.setString(1, teamString);
			addSportartToSchueler.setInt(2, id);
			addSportartToSchueler.executeUpdate();
		}
	}
	
	/**
	 * Liest die Paarsportarten (Tischtennis und Badminton) ein
	 * @param sportartZumEinlesen
	 * @throws SQLException
	 */
	public void readZweierSportart(String sportartZumEinlesen) throws SQLException {
		// Die Namen stehen an verschiedenen Positionen je Sportart
		int ersteZeile = 0;
		int letzteZeile = 0;
		int[] spalteVorname = {4, 8}; // Bei TT und BM stehen die Namen nicht alle untereinander, sondern in zwei Blöcken nebeneinander
		int[] spalteNachname = {5, 9};
		switch(sportartZumEinlesen) {
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
		addSportartToSchueler = con.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE ID = ?", this.table, sportartZumEinlesen));
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
					if(name.isEmpty() && vorname.isEmpty())
						continue naechstesTeam; // IDEA: Hier wäre es möglich, einen Test, ob genügen Personen in der Mannschaft sind, durchzuführen
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
					String teamString = Character.isDigit(klasse.charAt(klasse.length() - 1)) ? klasse + '_' + teamnr : klasse +  teamnr;
					addSportartToSchueler.setString(1, teamString);
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
