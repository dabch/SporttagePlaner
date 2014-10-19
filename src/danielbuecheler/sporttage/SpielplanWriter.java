package danielbuecheler.sporttage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


/**
 * Liest einen Spielplan von einer MySQL-DB und schreibt ihn in eine Excel-Tabelle
 * @author Daniel Bücheler
 *
 */
public class SpielplanWriter {
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
	
	private Connection con; // Datenbankverbindung
	
	private FileOutputStream fos; // FileOutputStream, auf den die Excel-Tabelle geschrieben wird
	private HSSFWorkbook wb; // Excel-Datei
	private Sheet sheet1; // Tabelle
	
	private CellStyle csTitel;
	private CellStyle csTabellenueberschrift;
	private CellStyle csSpieleEtc;
	
	public SpielplanWriter(String filename) throws SQLException, IOException {
		String tablePlan = "Testspielplan2";
		
		fos = new FileOutputStream(filename);
		
		wb = new HSSFWorkbook();
		
		Row row = null;
		Cell cell = null;
		
		
		sheet1 = wb.createSheet("Spielplan"); // Tabelle1 erstellen und "Spielplan" taufen
		
		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", sqlServer, dbName), username, password); // Verbindung zur Datenbank herstellen
		
		setStyles(); // Stile der Tabelle erstellen
	}

	private void close() throws IOException, SQLException {
		wb.write(fos); // Excel-Datei schreiben
		fos.close(); // FileOutputStream schließen
		con.close(); // Datenbankverbindung schließen
	}

	private void setStyles() {
		csSpieleEtc = wb.createCellStyle(); // Stil für Spiele, Schiris, Zeiten etc.
		csTitel = wb.createCellStyle(); // Stil für Titel
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
