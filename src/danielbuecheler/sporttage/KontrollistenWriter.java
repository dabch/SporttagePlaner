package danielbuecheler.sporttage;

import java.io.FileNotFoundException;
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

public class KontrollistenWriter {
	private Connection con; // Datenbankverbindung
	private ResultSet spielplan; // Spielplan als ResulSet
	PreparedStatement holeTeams1;
	PreparedStatement holeTeams2;

	private FileOutputStream fos; // FileOutputStream, auf den die Excel-Tabelle geschrieben wird
	private HSSFWorkbook wb; // Excel-Datei
	private Sheet sheet1; // Tabelle

	private CellStyle csUeberschriften; // CellStyles (Formatierungen) für die Excel-Tabelle
	private CellStyle csInfos;
	private CellStyle csNormal;
	
	private int aktReihe = 9; // Letzte Reihe, in die eingetragen wurde

	private String tablePlan;
	
	public KontrollistenWriter(String filename, Sportart sportart, Stufe stufe) throws SQLException, FileNotFoundException, IllegalArgumentException {
		if(filename == null || filename.isEmpty())
			throw new IllegalArgumentException("Bitte Dateinamen angeben!"); // Fehler bei leerem Dateinamen
		
		tablePlan = String.format("%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz()); // Tabellennamen festlegen

		fos = new FileOutputStream(filename);

		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", // Verbindung zur Datenbank herstellen
				SpielplanerApp.properties.getProperty("database_ip_address"), // IP-Adresse des DB-Servers
				SpielplanerApp.properties.getProperty("database_name")), // Name der DB
				SpielplanerApp.properties.getProperty("database_username"), // Username
				SpielplanerApp.properties.getProperty("database_password")); // Passwort

		wb = new HSSFWorkbook();

		sheet1 = wb.createSheet("Kontrolliste"); // Tabelle1 erstellen und "Spielplan" taufen

		this.tablePlan = String.format("%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz()); // Tabellennamen festlegen

		PreparedStatement holeSpielplan1Feld = con.prepareStatement(String.format(
				"SELECT Spielbeginn, Spielende, Feld1 FROM %s", tablePlan));
		
		holeTeams1 = con.prepareStatement(String.format("SELECT Vorname, Name FROM Teams_%s WHERE %s = ?", stufe.getStufeKurz(), sportart.getSportartKurz())); // SQL-Statement, um Spieler einer bestimmten Mannschaft abzufragen
		holeTeams2 = con.prepareStatement(String.format("SELECT Vorname, Name FROM Teams_%s WHERE %s = ?", stufe.getStufeKurz(), sportart.getSportartKurz())); // SQL-Statement, um Spieler einer bestimmten Mannschaft abzufragen
		spielplan = holeSpielplan1Feld.executeQuery();
		setStyles();
		ueberschriftenEintragen();
	}
	
	public void close() throws IOException, SQLException {
		// Spaltenbreiten anpassen
		for (int spalte = 0; spalte < 9; spalte++) { // FINAL hier automatisch anzahl spalten erkennen
			sheet1.autoSizeColumn(spalte);
		}
		sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_ROTATED_PAPERSIZE); // A4 Querformat
		wb.write(fos); // Excel-Datei schreiben
		fos.flush();
		fos.close(); // FileOutputStream schließen
		spielplan.close(); // ResultSet schließen
//		spieler.close();
		con.close(); // Datenbankverbindung schließen
	}
	
	private void ueberschriftenEintragen() {
		Row row = sheet1.createRow(0);
		Cell cell = row.createCell(0);
		
		// Info in der ersten Zeile eintragen
		cell.setCellStyle(csInfos);
		cell.setCellValue("Die Spiele beginnen und enden jeweils mit der Durchsage. Bitte an die vorgegebenen Zeiten halten!");
		sheet1.addMergedRegion(new CellRangeAddress(0,0,0,10));
		row.setHeightInPoints((short) 15);
		
		// TODO Feld und Tag eintragen
		
		// Tabellenüberschriften
		// Uhrzeit
		row = sheet1.createRow(aktReihe++);
		row.setHeightInPoints(15);;
		cell = row.createCell(0);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Uhrzeit");
		// Team 1
		cell = row.createCell(1);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Team 1");
		cell = row.createCell(2);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Name");
		cell = row.createCell(3);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Vorname");
		cell = row.createCell(4);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("anw");
		// Team 2
		cell = row.createCell(5);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Team 2");
		cell = row.createCell(6);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Name");
		cell = row.createCell(7);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Vorname");
		cell = row.createCell(8);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("anw");
	}
	
	public void spielerEintragen() throws SQLException {
		while(spielplan.next()) {
//			int startReihe = aktReihe;
			Row row = sheet1.createRow(aktReihe++); // In der aktuellen Reihe, danach um 1 erhöhen
			Cell cell = row.createCell(0);
			cell.setCellStyle(csNormal);
			// Zeit eintragen
			cell.setCellStyle(csNormal);
			cell.setCellValue(String.format("%s - %s", spielplan.getTime(1), spielplan.getTime(2)));
			String spiel = spielplan.getString("Feld1");
			
			// Spiel-String in Teams zerlegen
			String team1 = spiel.substring(0, spiel.indexOf(':')).trim();
			String team2 = spiel.substring(spiel.indexOf(':') + 2).trim();
			// Spieler aus Team 1 vom Server holen
			holeTeams1.setString(1, team1); // Nur nach Personen in team 1 suchen
			ResultSet spielerTeam1 = holeTeams1.executeQuery();
			// Spieler aus Team 2 vom Server holen
			holeTeams2.setString(1, team2);
			ResultSet spielerTeam2 = holeTeams2.executeQuery();
			
			// Team 1 Name eintragen
			cell = row.createCell(1);
			cell.setCellStyle(csNormal);
			cell.setCellValue(team1);
			// Team 2 Name eintragen
			cell = row.createCell(5);
			cell.setCellStyle(csNormal);
			cell.setCellValue(team2);
			
			// erste Person eintragen ohne in neue Reihe zu wechseln
			// Team 1
			spielerTeam1.next();
			cell = row.createCell(2); // Zelle für Nachname
			cell.setCellStyle(csNormal);
			cell.setCellValue(spielerTeam1.getString("Vorname"));
			cell = row.createCell(3); // Zelle für Nachname
			cell.setCellStyle(csNormal);
			cell.setCellValue(spielerTeam1.getString("Name"));
			cell = row.createCell(4); // Leerzelle für Anwesenheit
			cell.setCellStyle(csNormal); // mit csNormal formatieren, damit eine Umrandung da ist
			// Team 2
			spielerTeam2.next();
			cell = row.createCell(6); // Zelle für Nachname
			cell.setCellStyle(csNormal);
			cell.setCellValue(spielerTeam2.getString("Vorname"));
			cell = row.createCell(7); // Zelle für Nachname
			cell.setCellStyle(csNormal);
			cell.setCellValue(spielerTeam2.getString("Name"));
			cell = row.createCell(8); // Leerzelle für Anwesenheit
			cell.setCellStyle(csNormal); // mit csNormal formatieren, damit eine Umrandung da ist
			
			// Alle weiteren Spieler darunter eintragen
			while(spielerTeam1.next() | spielerTeam2.next()) {
				// Neue Reihe erstellen
				row = sheet1.createRow(aktReihe++);
				
				// Leerzellen für Uhrzeit, Teamname und Anwesenheit mit csNormal formatieren, damit sie Umrandungen haben
				cell = row.createCell(0);
				cell.setCellStyle(csNormal);
				cell = row.createCell(1);
				cell.setCellStyle(csNormal);
				cell = row.createCell(4);
				cell.setCellStyle(csNormal); // mit csNormal formatieren, damit eine Umrandung da ist
				cell = row.createCell(5);
				cell.setCellStyle(csNormal);
				cell = row.createCell(8);
				cell.setCellStyle(csNormal);
				
				// Team 1
				// Spieler eintragen
				cell = row.createCell(2); // Zelle für Nachname
				cell.setCellStyle(csNormal);
				cell.setCellValue(spielerTeam1.getString("Vorname"));
				cell = row.createCell(3); // Zelle für Nachname
				cell.setCellStyle(csNormal);
				cell.setCellValue(spielerTeam1.getString("Name"));
				
				// Team 2
				// Spieler eintragen
				cell = row.createCell(6); // Zelle für Nachname
				cell.setCellStyle(csNormal);
				cell.setCellValue(spielerTeam2.getString("Vorname"));
				cell = row.createCell(7); // Zelle für Nachname
				cell.setCellStyle(csNormal);
				cell.setCellValue(spielerTeam2.getString("Name"));
			}
			
		}
	}
	
	private void setStyles() {
		csNormal = wb.createCellStyle(); // Stil für Uhrzeiten, Namen etc.
		csInfos = wb.createCellStyle(); // Stil für Infos über der eigentlichen Liste
		csUeberschriften = wb.createCellStyle(); // Stil für Überschriften
		
		DataFormat dataFormat = wb.createDataFormat(); // Textformat in den Zellen
		
		// Schriftart für Überschriften
		Font fUeberschriften = wb.createFont();
		fUeberschriften.setFontHeightInPoints((short) 11); // 12pt
		fUeberschriften.setColor(Font.COLOR_NORMAL); // schwarz
		fUeberschriften.setBoldweight(Font.BOLDWEIGHT_BOLD); // fett
		// CellStyle für Überschriften
		csUeberschriften.setFont(fUeberschriften); 
		csUeberschriften.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // zentrieren
		csUeberschriften.setBorderBottom(CellStyle.BORDER_THIN); // Umrandung unten einschalten
		csUeberschriften.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung unten in schwarz
		csUeberschriften.setBorderLeft(CellStyle.BORDER_THIN); // Umrandung links einschalten
		csUeberschriften.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung links in schwarz
		csUeberschriften.setBorderTop(CellStyle.BORDER_THIN); // Umrandung oben einschalten
		csUeberschriften.setTopBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung oben in schwarz
		csUeberschriften.setBorderRight(CellStyle.BORDER_THIN); // Umrandung rechts einschalten
		csUeberschriften.setRightBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung rechts in schwarz
		csUeberschriften.setDataFormat(dataFormat.getFormat("text")); // als Text formatiert
		
		// Schriftart für Infos am Anfang
		Font fInfos = wb.createFont();
		fInfos.setFontHeightInPoints((short) 11); // 11pt
		fInfos.setColor(Font.COLOR_NORMAL); // schwarz
		fInfos.setBoldweight(Font.BOLDWEIGHT_BOLD);
		// CellStyle für Infos
		csInfos.setFont(fInfos);
		csInfos.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // zentriert
		csInfos.setDataFormat(dataFormat.getFormat("text")); // text
		
		// Schriftart für normales
		Font fNormal = wb.createFont();
		fNormal.setFontHeightInPoints((short) 10); // 11pt
		fNormal.setColor(Font.COLOR_NORMAL);
		fNormal.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		// CellStyle normal
		csNormal.setFont(fNormal);
		csNormal.setAlignment(CellStyle.ALIGN_LEFT); // linksbündig
		csNormal.setBorderBottom(CellStyle.BORDER_THIN); // Umrandung unten einschalten
		csNormal.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung unten in schwarz
		csNormal.setBorderLeft(CellStyle.BORDER_THIN); // Umrandung links einschalten
		csNormal.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung links in schwarz
		csNormal.setBorderTop(CellStyle.BORDER_THIN); // Umrandung oben einschalten
		csNormal.setTopBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung oben in schwarz
		csNormal.setBorderRight(CellStyle.BORDER_THIN); // Umrandung rechts einschalten
		csNormal.setRightBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung rechts in schwarz
		csNormal.setDataFormat(dataFormat.getFormat("text"));
	}
}
