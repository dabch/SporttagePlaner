package danielbuecheler.sporttage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * Liest einen Spielplan von einer MySQL-DB und schreibt ihn in eine Excel-Tabelle
 * 
 * @author Daniel Bücheler
 *
 */
public class SpielplanWriter {

	private Connection con; // Datenbankverbindung

	private FileOutputStream fos; // FileOutputStream, auf den die Excel-Tabelle geschrieben wird
	private HSSFWorkbook wb; // Excel-Datei
	private Sheet sheet1; // Tabelle

	private CellStyle csTitel; // CellStyles (Formatierungen) für die Excel-Tabelle
	private CellStyle csUntertitel;
	private CellStyle csTabellenueberschrift;
	private CellStyle csSpieleEtc;
	
	private int currentRow = 5; // Zähler, in welcher Reihe wir 

	private int hoechstesBespieltesFeld;
	private int hoechstesBespieltesFeldMo;
	
	private String tableStamm;
	private Stufe stufe;
	private Sportart sportart;

	public SpielplanWriter(Stufe stufe, Sportart sportart) throws SQLException,
			IOException, IllegalArgumentException {
		this.stufe = stufe;
		this.sportart = sportart;
		
		String filename = String.format("%s/Plan_%s_%s.xls", SpielplanerApp.dirSpielplaene.getCanonicalPath(), stufe.getStufeKurz(), sportart.getSportartKurz());
		
		fos = new FileOutputStream(filename);

		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", // Verbindung zur Datenbank herstellen
				SpielplanerApp.properties.getProperty("database_ip_address"), // IP-Adresse des DB-Servers
				SpielplanerApp.properties.getProperty("database_name")), // Name der DB
				SpielplanerApp.properties.getProperty("database_username"), // Username
				SpielplanerApp.properties.getProperty("database_password")); // Passwort
	}

	public void write() throws SQLException {

		wb = new HSSFWorkbook();

		sheet1 = wb.createSheet("Spielplan"); // Tabelle1 erstellen und "Spielplan" taufen
		
		setStyles(); // Tabellen-Styles setzen
		
		Statement stmt = con.createStatement();
		
		// Montag		
		tableStamm = String.format("%s_%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz(), "MO"); // Tabellennamen festlegen
		
		
		ResultSet hoechstesBespieltesFeldRS = stmt.executeQuery("SELECT MAX(Feld) FROM " + tableStamm + "_spiele"); // hoechstes feld holen
		hoechstesBespieltesFeldRS.next();
		hoechstesBespieltesFeld = hoechstesBespieltesFeldRS.getInt(1); // INT extrahieren (yay :D)
		
		titelEintragen(); // "Spielplan für xxxxxxxx" usw.
		if(hoechstesBespieltesFeld > 0) { // nichts eintragen wenn gar nicht gespielt wird
			ueberschriftenEintragen("Montag");
			spieleEintragen();
		}
		
		currentRow += 3; // 5 Reihen frei zwischen den Tagen
		
		
		// Dienstag
		tableStamm = String.format("%s_%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz(), "DI"); // Tabellennamen festlegen
		
		hoechstesBespieltesFeldMo = hoechstesBespieltesFeld;
		hoechstesBespieltesFeldRS = stmt.executeQuery("SELECT MAX(Feld) FROM " + tableStamm + "_spiele"); // hoechstes feld holen
		hoechstesBespieltesFeldRS.next();
		hoechstesBespieltesFeld = hoechstesBespieltesFeldRS.getInt(1); // INT extrahieren (yay :D)
		
		if(hoechstesBespieltesFeld > 0) { // Nur wenn überhaupt am Dienstag gespielt wird auch was machen
			ueberschriftenEintragen("Dienstag");
			spieleEintragen();
		}
	}

	/**
	 * Schließt alle durch den SpielplanWriter geöffneten Ressourcen und schreibt die Excel-Datei
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public void close() throws IOException, SQLException {
		// Spaltenbreiten anpassen
		hoechstesBespieltesFeld = (hoechstesBespieltesFeld > hoechstesBespieltesFeldMo) ? hoechstesBespieltesFeld : hoechstesBespieltesFeldMo; 
		for (int spalte = 0; spalte < 1 + hoechstesBespieltesFeld * 3; spalte++) {
			sheet1.autoSizeColumn(spalte);
		}
		sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_ROTATED_PAPERSIZE); // A4 Querformat
		wb.write(fos); // Excel-Datei schreiben
		fos.flush();
		fos.close(); // FileOutputStream schließen
		con.close(); // Datenbankverbindung schließen
	}

	private void titelEintragen() throws SQLException {
		// Row und Cell aus dem Sheet erstellen
		Row row;
		Cell cell;

		// Titel
		row = sheet1.createRow(0);
		cell = row.createCell(0);
		row.setHeightInPoints(30); // Höhe 30pt
		cell.setCellStyle(csTitel); // cs anwenden
		cell.setCellValue(String.format("Spielplan für %s %s", sportart.getSportartLang(), stufe.getStufeLang()));
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, hoechstesBespieltesFeld * 3)); // Titel geht über mehrere Zellen

		// "Made by Daniel Bücheler"
		row = sheet1.createRow(1);
		cell = row.createCell(0);
		cell.setCellStyle(csUntertitel);
		cell.setCellValue("Erstellt von Spielplan_beta by Daniel Bücheler"); // muss sein :D
		sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, hoechstesBespieltesFeld * 3)); // Geht auch über mehrere Zellen
	}
	
	private void ueberschriftenEintragen(String tag) {
		// Mo / Di
		Row row = sheet1.createRow(currentRow++);
		Cell cell = row.createCell(0);
		row.setHeightInPoints(15);
		cell.setCellStyle(csTabellenueberschrift);
		cell.setCellValue(tag);
		// Überschriften für Felder
		// Zeit
		row = sheet1.createRow(currentRow++);
		cell = row.createCell(0);
		row.setHeightInPoints(15);
		cell.setCellStyle(csTabellenueberschrift);
		cell.setCellValue("Zeit");
		// Feld X, Schiri, Ergebnis für jedes Feld
		for (int feld = 0; feld < hoechstesBespieltesFeld; feld++) { // Variable anzahl Felder
			// Feld
			cell = row.createCell(1 + feld * 3); // 1 + Feld * 3, da jedes Feld 3 Spalten braucht und die Uhrzeit am Anfang eine
			cell.setCellStyle(csTabellenueberschrift);
			cell.setCellValue("Feld " + (feld + 1)); // feld fängt bei 0 an, deshalb + 1
			// Schiri
			cell = row.createCell(2 + feld * 3); // jetzt eins weiter rechts
			cell.setCellStyle(csTabellenueberschrift);
			cell.setCellValue("Schiedsrichter");
			// Ergebnis
			cell = row.createCell(3 + feld * 3); // noch eins weiter rechts
			cell.setCellStyle(csTabellenueberschrift);
			cell.setCellValue("Ergebnis");
		}
	}

	private void spieleEintragen() throws SQLException {
		PreparedStatement holeSpiele = con.prepareStatement(
				"SELECT TimeID Nr, Paarung " // nur die Nummer und die Paarung sind interessant
				+ "FROM " + tableStamm + "_spiele " // richtige Tabelle wählen
				+ "WHERE Feld = ? ORDER BY TimeID"); // nur ein Feld; sortiert nach Nummer
		
		
		PreparedStatement holeZeiten = con.prepareStatement(
				"SELECT ID Nr, Spielbeginn Beginn, Spielende Ende " // Nummer, Beginn und Ende sind interessant
				+ "FROM " + tableStamm + "_zeiten " // richtige Tabelle wählen
				+ "WHERE ID <= (SELECT MAX(TimeID) FROM " + tableStamm + "_spiele) " // nur so viele Zeiten wie auch Spiele gebraucht werden holen
				+ "ORDER BY Nr"); // nach Beginn sortiert
		
		Row row;
		Cell cell;
		
		ResultSet zeiten = holeZeiten.executeQuery(); // spiele holen
		
		int beginnReihe = currentRow;
		
		while (zeiten.next()) {
			row = sheet1.createRow(currentRow++);

			// Zeit schreiben
			cell = row.createCell(0);
			cell.setCellStyle(csSpieleEtc);
			Date beginn = new Date(zeiten.getTime("Beginn").getTime());
			Date ende = new Date(zeiten.getTime("Ende").getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Zur formatierung -> damit die :00 Sekunden weggehen
			cell.setCellValue(String.format("%s - %s", sdf.format(beginn), sdf.format(ende))); // Von - Bis	
		}
		
		int endReihe = currentRow;
		
		// Felder schreiben (ohne Formatierung)
		for (int feld = 0; feld < hoechstesBespieltesFeld; feld++) {
			holeSpiele.setInt(1, feld + 1);
			ResultSet spiele = holeSpiele.executeQuery();
			// Formatierung
			for(int reihe = beginnReihe; reihe < endReihe; reihe++) {
				row = sheet1.getRow(reihe);
				if(row == null) {
					row = sheet1.createRow(reihe);
				}
				// Paarung
				cell = row.createCell(1 + feld * 3); // neue cell
				cell.setCellStyle(csSpieleEtc);
				cell.setCellValue("");
				// Schiri (bisher Leerstring) TODO Schiri-Verwaltung
				cell = row.createCell(2 + feld * 3);
				cell.setCellStyle(csSpieleEtc);
				cell.setCellValue("");
				// Ergebnis (immer Leerstring
				cell = row.createCell(3 + feld * 3);
				cell.setCellStyle(csSpieleEtc);
				cell.setCellValue("");
			}
			// Werte eintragen ohne Formatierung
			while(spiele.next()) {
				row = sheet1.getRow(beginnReihe + spiele.getInt("Nr") - 1);
				if(row == null) {
					row = sheet1.createRow(beginnReihe + spiele.getInt("Nr") - 1);
				}
				// Paarung
				cell = row.getCell(1 + feld * 3); // neue cell
				String paarung = spiele.getString("Paarung");
				if(paarung == null || paarung.isEmpty()) {  // Wenn kein Spiel stattfindet
					cell.setCellValue(""); // Leerstring um merge zu verhindern und neu anfangen
				} else {
					cell.setCellValue(paarung); // Spiel eintragen
				}
			}
			spiele.close(); // RS schließen
		}
	}

	private void setStyles() {
		csSpieleEtc = wb.createCellStyle(); // Stil für Spiele, Schiris, Zeiten etc.
		csTitel = wb.createCellStyle(); // Stil für Titel
		csUntertitel = wb.createCellStyle(); // Stil für Untertitel
		csTabellenueberschrift = wb.createCellStyle(); // Stil für die Tabellenüberschriften (wie für Spiele, nur fett)

		DataFormat dataFormat = wb.createDataFormat(); // Um das Datenformat auf Text zu setzen

		// Schriftart für Titel
		Font fTitel = wb.createFont();
		fTitel.setFontHeightInPoints((short) 24); // größe 24pt
		fTitel.setColor(Font.COLOR_NORMAL); // schwarz
		fTitel.setBoldweight(Font.BOLDWEIGHT_BOLD); // fett
		// CellStyle für den Titel
		csTitel.setFont(fTitel); // Schrift setzen
		csTitel.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // Mittige Anordnung
		csTitel.setDataFormat(dataFormat.getFormat("text")); // Datenformat Text

		// Schriftart für den Untertitel
		Font fUntertitel = wb.createFont();
		fUntertitel.setFontHeightInPoints((short) 10); // Schriftgröße auf 10pt setzen
		fUntertitel.setColor(Font.COLOR_NORMAL); // Schriftfarbe schwarz
		fUntertitel.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		// CellStyle für den Untertitel
		csUntertitel.setFont(fUntertitel); // Schriftart
		csUntertitel.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // Mittige Anordnung
		csUntertitel.setDataFormat(dataFormat.getFormat("text")); // Datenformat Text
		// Schriftart für Tabellenüberschriften
		Font fTabellenueberschrift = wb.createFont();
		fTabellenueberschrift.setFontHeightInPoints((short) 11); // größe 11pt
		fTabellenueberschrift.setColor(Font.COLOR_NORMAL); // schwarz
		fTabellenueberschrift.setBoldweight(Font.BOLDWEIGHT_BOLD); // fett
		// CellStyle für die Tabellenüberschriften
		csTabellenueberschrift.setFont(fTabellenueberschrift); // Schriftart
		csTabellenueberschrift.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // Mittige Anordnung
		csTabellenueberschrift.setBorderBottom(CellStyle.BORDER_THIN); // Umrandung unten einschalten
		csTabellenueberschrift.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung unten in schwarz
		csTabellenueberschrift.setBorderLeft(CellStyle.BORDER_THIN); // Umrandung links einschalten
		csTabellenueberschrift.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung links in schwarz
		csTabellenueberschrift.setBorderTop(CellStyle.BORDER_THIN); // Umrandung oben einschalten
		csTabellenueberschrift.setTopBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung oben in schwarz
		csTabellenueberschrift.setBorderRight(CellStyle.BORDER_THIN); // Umrandung rechts einschalten
		csTabellenueberschrift.setRightBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung rechts in schwarz
		csTabellenueberschrift.setDataFormat(dataFormat.getFormat("text")); // Datenformat Text

		// Schriftart für Spiele, Schiris, Zeiten etc.
		Font fSpieleEtc = wb.createFont();
		fSpieleEtc.setFontHeightInPoints((short) 10); // Schriftgröße auf 10pt setzen
		fSpieleEtc.setColor(Font.COLOR_NORMAL); // Schriftfarbe schwarz
		fSpieleEtc.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		// CellStyle für Spiele, Schiris, Zeiten etc.
		csSpieleEtc.setFont(fSpieleEtc); // Schriftart
		csSpieleEtc.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // Mittige Anordnung
		csSpieleEtc.setBorderBottom(CellStyle.BORDER_THIN); // Umrandung unten einschalten
		csSpieleEtc.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung unten in schwarz
		csSpieleEtc.setBorderLeft(CellStyle.BORDER_THIN); // Umrandung links einschalten
		csSpieleEtc.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung links in schwarz
		csSpieleEtc.setBorderTop(CellStyle.BORDER_THIN); // Umrandung oben einschalten
		csSpieleEtc.setTopBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung oben in schwarz
		csSpieleEtc.setBorderRight(CellStyle.BORDER_THIN); // Umrandung rechts einschalten
		csSpieleEtc.setRightBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung rechts in schwarz
		csSpieleEtc.setDataFormat(dataFormat.getFormat("text")); // Datenformat Text
	}

}
