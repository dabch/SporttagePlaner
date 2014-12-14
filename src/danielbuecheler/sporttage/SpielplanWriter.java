package danielbuecheler.sporttage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	private ResultSet spielplan; // Spielplan als ResulSet

	private FileOutputStream fos; // FileOutputStream, auf den die Excel-Tabelle geschrieben wird
	private HSSFWorkbook wb; // Excel-Datei
	private Sheet sheet1; // Tabelle

	private CellStyle csTitel; // CellStyles (Formatierungen) für die Excel-Tabelle
	private CellStyle csUntertitel;
	private CellStyle csTabellenueberschrift;
	private CellStyle csSpieleEtc;

	private int anzahlFelder;
	private String tablePlan;
	private Stufe stufe;
	private Sportart sportart;

	public SpielplanWriter(String filename, Sportart sportart, Stufe stufe) throws SQLException, IOException, IllegalArgumentException {
		this.stufe = stufe;
		this.sportart = sportart;
		if(filename == null || filename.isEmpty())
			throw new IllegalArgumentException("Bitte Dateinamen angeben!");
		
		tablePlan = String.format("%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz()); // Tabellennamen festlegen

		fos = new FileOutputStream(filename);

		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", // Verbindung zur Datenbank herstellen
				SpielplanerApp.properties.getProperty("database_ip_address"), // IP-Adresse des DB-Servers
				SpielplanerApp.properties.getProperty("database_name")), // Name der DB
				SpielplanerApp.properties.getProperty("database_username"), // Username
				SpielplanerApp.properties.getProperty("database_password")); // Passwort
		
		
		anzahlFelder = getAnzahlFelder(); // Anzahl der Felder auslesen

		wb = new HSSFWorkbook();

		sheet1 = wb.createSheet("Spielplan"); // Tabelle1 erstellen und "Spielplan" taufen

		PreparedStatement holeSpielplan1Feld = con.prepareStatement(String.format(
				"SELECT Spielbeginn, Spielende, Feld1, Feld1Schiri FROM %s", tablePlan));
		
		System.out.println(holeSpielplan1Feld);
		spielplan = holeSpielplan1Feld.executeQuery();

		while (spielplan.next()) {
			System.out.print(spielplan.getTime(1) + " - " + spielplan.getTime(2) + ": ");
			System.out.println(spielplan.getString(3) + " Schiri: " + spielplan.getString(4));
		}

		setStyles();
		ueberschriftenEintragen();
		spieleEintragen();
	}
	
	/**
	 * Schließt alle durch den SpielplanWriter geöffneten Ressourcen und schreibt die Excel-Datei
	 * @throws IOException
	 * @throws SQLException
	 */
	public void close() throws IOException, SQLException {
		// Spaltenbreiten anpassen
		for (int spalte = 0; spalte < 1 + anzahlFelder * 3; spalte++) {
			sheet1.autoSizeColumn(spalte);
		}
		sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_ROTATED_PAPERSIZE); // A4 Querformat
		wb.write(fos); // Excel-Datei schreiben
		fos.flush();
		fos.close(); // FileOutputStream schließen
		spielplan.close(); // ResultSet schließen
		con.close(); // Datenbankverbindung schließen
	}
	
	private void ueberschriftenEintragen() {
		// Row und Cell aus dem Sheet erstellen
		Row row;
		Cell cell;

		// Titel
		row = sheet1.createRow(0);
		cell = row.createCell(0);
		row.setHeightInPoints(30); // Höhe 30pt
		cell.setCellStyle(csTitel); // cs anwenden
		switch(stufe.getStufeKurz()) { // IDEA: Klasse für Stufe einführen, um Kurz- und Langnamen zu managen
		case "US":
		case "MS":
			break;
		case "OS":
			break;
		}
		cell.setCellValue(String.format("Spielplan für %s %s", sportart.getSportartLang(), stufe.getStufeLang()));
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, anzahlFelder * 3)); // Titel geht über mehrere Zellen

		// "Made by Daniel Bücheler"
		row = sheet1.createRow(1);
		cell = row.createCell(0);
		cell.setCellStyle(csUntertitel);
		cell.setCellValue("Erstellt von Spielplan_beta by Daniel Bücheler"); // muss sein :D
		sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, anzahlFelder * 3)); // Geht auch über mehrere Zellen

		// Überschriften für Felder
		// Zeit
		row = sheet1.createRow(4);
		cell = row.createCell(0);
		row.setHeightInPoints(15);
		cell.setCellStyle(csTabellenueberschrift);
		cell.setCellValue("Zeit");
		// Feld X, Schiri, Ergebnis für jedes Feld
		for (int feld = 0; feld < anzahlFelder; feld++) { // Variable anzahl Felder
			// Feld
			cell = row.createCell(1 + feld * 3); // 1 + Feld * 3, da jedes Feld 3 Spalten braucht und die Uhrzeit am Anfang eine
			cell.setCellStyle(csTabellenueberschrift);
			cell.setCellValue("Feld " + (feld + 1)); // feld fängt bei 0 an, deshalb + 1
			// Schiri
			cell = row.createCell(2 + feld * 3); // jetzt eins weiter rechts
			cell.setCellStyle(csTabellenueberschrift);
			cell.setCellValue("Schiri");
			// Ergebnis
			cell = row.createCell(3 + feld * 3); // noch eins weiter rechts
			cell.setCellStyle(csTabellenueberschrift);
			cell.setCellValue("Ergebnis");
		}
	}
	
	
	private void spieleEintragen() throws SQLException {
		Row row;
		Cell cell;
		int currentRow = 5;
		spielplan.absolute(0);
		while(spielplan.next()) {
			// IDEA: EXTRACT() benutzen, um die :00 Sekunden wegzukriegen
			System.out.println("Spiel wird eingetragen");
			row = sheet1.createRow(currentRow++);
			// Zeit schreiben
			cell = row.createCell(0);
			cell.setCellStyle(csSpieleEtc);
			cell.setCellValue(String.format("%s - %s", spielplan.getTime(1), spielplan.getTime(2))); // Von - Bis
			// Spiel auf Feld 1
			cell = row.createCell(1);
			cell.setCellStyle(csSpieleEtc);
			cell.setCellValue(spielplan.getString(3));
			// Schiri Feld 1
			cell = row.createCell(2);
			cell.setCellStyle(csSpieleEtc);
			cell.setCellValue(spielplan.getString(4));
			// Leere Zelle für Ergebnis
			cell = row.createCell(3);
			cell.setCellStyle(csSpieleEtc);
			cell.setCellValue(""); // komplett leere Zellen werden zusammengeführt -> Leerstring
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
	
	private int getAnzahlFelder() throws SQLException {
		PreparedStatement selectAnzahlFelder = con.prepareStatement("SELECT COUNT(*) FROM INFORMATION_SCHEMA.Columns WHERE TABLE_NAME = ?");
		selectAnzahlFelder.setString(1, tablePlan);
		ResultSet infosZumTable = selectAnzahlFelder.executeQuery();
		infosZumTable.next();
		return (infosZumTable.getInt(1) -2) / 3 ; // Zwei Spalten durch Zeiten belegt, drei Spalten pro Feld
	}
}
