package danielbuecheler.sporttage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private ResultSet plan; // Spielplan als ResulSet

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
		
		String filename = String.format("Sporttage Planung/Spielpläne/Plan_%s_%s.xls", stufe.getStufeKurz(), sportart.getSportartKurz());
		
		fos = new FileOutputStream(filename);

		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", // Verbindung zur Datenbank herstellen
				SpielplanerApp.properties.getProperty("database_ip_address"), // IP-Adresse des DB-Servers
				SpielplanerApp.properties.getProperty("database_name")), // Name der DB
				SpielplanerApp.properties.getProperty("database_username"), // Username
				SpielplanerApp.properties.getProperty("database_password")); // Passwort
	}

	public void write() throws SQLException {
		int maxFelder;

		wb = new HSSFWorkbook();

		sheet1 = wb.createSheet("Spielplan"); // Tabelle1 erstellen und "Spielplan" taufen
		
		setStyles(); // Tabellen-Styles setzen
		// f
		tableStamm = String.format("%s_%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz(), "MO"); // Tabellennamen festlegen
		
		PreparedStatement holePlan = null;
		
		switch(sportart.getSportartKurz()) {
		case "BM": // sechs Felder
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3, f4.Paarung AS Feld4, f5.Paarung AS Feld5, f6.Paarung AS Feld6 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID LEFT JOIN %s_feld4 f4 ON f4.ID = z.ID LEFT JOIN %s_feld5 f5 ON f5.ID = z.ID LEFT JOIN %s_feld6 f6 ON f6.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != '' OR f4.Paarung != '' OR f5.Paarung != '' OR f6.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 6;
			break;
		case "FB": // drei Felder
		case "BB":
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 3;
			break;
		case "TT": // vier Felder
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3, f4.Paarung AS Feld4 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID LEFT JOIN %s_feld4 f4 ON f4.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != '' OR f4.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 4;
			break;
		case "VB": // zwei Felder
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 2;
			break;
		default: // ein Feld
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID WHERE f1.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 1;
		}
		plan = holePlan.executeQuery();
		hoechstesBespieltesFeld = hoechstesBespieltesFeld(maxFelder);

		titelEintragen(); // "Spielplan für xxxxxxxx" usw.
		ueberschriftenEintragen("Montag");
		spieleEintragen();
		
		currentRow += 3; // 5 Reihen frei zwischen den Tagen
		
		// Dienstag
		holePlan = null;
		tableStamm = String.format("%s_%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz(), "DI"); // Tabellennamen festlegen
		
		switch(sportart.getSportartKurz()) {
		case "BM": // sechs Felder
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3, f4.Paarung AS Feld4, f5.Paarung AS Feld5, f6.Paarung AS Feld6 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID LEFT JOIN %s_feld4 f4 ON f4.ID = z.ID LEFT JOIN %s_feld5 f5 ON f5.ID = z.ID LEFT JOIN %s_feld6 f6 ON f6.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != '' OR f4.Paarung != '' OR f5.Paarung != '' OR f6.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 6;
			break;
		case "FB": // drei Felder
		case "BB":
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 3;
			break;
		case "TT": // vier Felder
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3, f4.Paarung AS Feld4 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID LEFT JOIN %s_feld4 f4 ON f4.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != '' OR f4.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 4;
			break;
		case "VB": // zwei Felder
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 2;
			break;
		default: // ein Feld
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID WHERE f1.Paarung != ''",
					tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
			maxFelder = 1;
		}
		plan = holePlan.executeQuery();
		hoechstesBespieltesFeldMo = hoechstesBespieltesFeld; // speichern
		hoechstesBespieltesFeld = hoechstesBespieltesFeld(maxFelder);
		ueberschriftenEintragen("Dienstag");
		spieleEintragen();
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
		plan.close(); // ResultSet schließen
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
		plan.absolute(0);
		while (plan.next()) {
			row = sheet1.createRow(currentRow++);

			// Zeit schreiben
			cell = row.createCell(0);
			cell.setCellStyle(csSpieleEtc);
			Date beginn = new Date(plan.getTime("Beginn").getTime());
			Date ende = new Date(plan.getTime("Ende").getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Zur formatierung -> damit die :00 Sekunden weggehen
			cell.setCellValue(String.format("%s - %s", sdf.format(beginn), sdf.format(ende))); // Von - Bis
			
			// Felder schreiben
			for (int feld = 0; feld < hoechstesBespieltesFeld; feld++) {
				// Paarung
				cell = row.createCell(1 + feld * 3); // neue cell
				cell.setCellStyle(csSpieleEtc);
				String paarung = plan.getString("Feld" + (feld + 1));
				if(paarung == null || paarung.isEmpty()) {  // Wenn kein Spiel stattfindet
					cell.setCellValue(""); // Leerstring um merge zu verhindern und neu anfangen
				} else {
					cell.setCellValue(paarung); // Spiel eintragen
				}
				// Schiri (bisher Leerstring) TODO Schiri-Verwaltung
				cell = row.createCell(2 + feld * 3);
				cell.setCellStyle(csSpieleEtc);
				cell.setCellValue("");
				// Ergebnis (immer Leerstring
				cell = row.createCell(3 + feld * 3);
				cell.setCellStyle(csSpieleEtc);
				cell.setCellValue("");
			}
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

	/**
	 * Ermittelt die Anzahl der beplanten Felder aus dem resultset
	 * 
	 * @return
	 * @throws SQLException
	 */
	private int hoechstesBespieltesFeld(int maxFelder) throws SQLException {
		if (!plan.next()) // Wenn das rs leer ist
			return 0;
		int hoechstes = 0;
		for (int i = maxFelder; i > 0; i--) { // Jedes Feld abchecken, ob es bespielt wird
			if (plan.getString("Feld" + (i)) != null) {
				hoechstes = i;
				break;
			}
		}
		plan.first(); // An den Anfang gehen
		return hoechstes;
	}
}
