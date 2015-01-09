package danielbuecheler.sporttage;

import java.io.FileNotFoundException;
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

public class KontrollistenWriter {
	private Connection con; // Datenbankverbindung
	private ResultSet plan; // Spielplan als ResulSet
	PreparedStatement holeTeams1;
	PreparedStatement holeTeams2;

	private FileOutputStream fos; // FileOutputStream, auf den die Excel-Tabelle geschrieben wird
	private HSSFWorkbook wb; // Excel-Datei
	private Sheet sheet1; // Tabelle

	private CellStyle csUeberschriften; // CellStyles (Formatierungen) für die Excel-Tabelle
	private CellStyle csInfos;
	private CellStyle csNormal1;
	private CellStyle csNormal2;
	
	private int aktReihe = 3; // Letzte Reihe, in die eingetragen wurde
	private int ueberschriftenReihe;
	private int maxFelder;
	private int hoechstesBespieltesFeld;
	
	private String tag;
	private Sportart sportart;
	private Stufe stufe;
	private String tablePlanStamm; 

	public KontrollistenWriter(Sportart sportart, Stufe stufe, String tag) throws SQLException, FileNotFoundException, IllegalArgumentException {
		
		String filename = String.format("Kontrolliste_%s_%s_%s.xls", stufe.getStufeKurz(), sportart.getSportartKurz(), tag); // der Dateiname ist automatisch generiert
		
		tablePlanStamm = String.format("%s_%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz(), tag); // Tabellennamen festlegen
		String.format("Mannschaften_%s", stufe.getStufeKurz());
		
		this.tag = tag;
		this.sportart = sportart;
		this.stufe = stufe;
		
		fos = new FileOutputStream(filename);

		con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s", // Verbindung zur Datenbank herstellen
				SpielplanerApp.properties.getProperty("database_ip_address"), // IP-Adresse des DB-Servers
				SpielplanerApp.properties.getProperty("database_name")), // Name der DB
				SpielplanerApp.properties.getProperty("database_username"), // Username
				SpielplanerApp.properties.getProperty("database_password")); // Passwort

		wb = new HSSFWorkbook();

		sheet1 = wb.createSheet("Kontrolliste"); // Tabelle1 erstellen und "Spielplan" taufen
		
		PreparedStatement holePlan = null;
		
		switch(sportart.getSportartKurz()) {
		case "BM": // sechs Felder
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3, f4.Paarung AS Feld4, f5.Paarung AS Feld5, f6.Paarung AS Feld6 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID LEFT JOIN %s_feld4 f4 ON f4.ID = z.ID LEFT JOIN %s_feld5 f5 ON f5.ID = z.ID LEFT JOIN %s_feld6 f6 ON f6.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != '' OR f4.Paarung != '' OR f5.Paarung != '' OR f6.Paarung != ''",
					tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm));
			maxFelder = 6;
			break;
		case "FB": // drei Felder
		case "BB":
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != ''",
					tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm));
			maxFelder = 3;
			break;
		case "TT": // vier Felder
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2, f3.Paarung AS Feld3, f4.Paarung AS Feld4 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID LEFT JOIN %s_feld4 f4 ON f4.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != '' OR f4.Paarung != ''",
					tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm));
			maxFelder = 4;
			break;
		case "VB": // zwei Felder
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1, f2.Paarung AS Feld2 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != ''",
					tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm));
			maxFelder = 2;
			break;
		default: // ein Feld
			holePlan = con.prepareStatement(String.format(
					"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende, f1.Paarung AS Feld1 FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID WHERE f1.Paarung != ''",
					tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm, tablePlanStamm));
			maxFelder = 1;
		}
		plan = holePlan.executeQuery();
		
		holeTeams1 = con.prepareStatement(String.format("SELECT Vorname, Name FROM Mannschaften_%s WHERE %s = ?", stufe.getStufeKurz(), sportart.getSportartKurz())); // SQL-Statement, um Spieler einer bestimmten Mannschaft abzufragen
		holeTeams2 = con.prepareStatement(String.format("SELECT Vorname, Name FROM Mannschaften_%s WHERE %s = ?", stufe.getStufeKurz(), sportart.getSportartKurz())); // SQL-Statement, um Spieler einer bestimmten Mannschaft abzufragen
		plan = holePlan.executeQuery();
		hoechstesBespieltesFeld = hoechstesBespieltesFeld(maxFelder);
		
		setStyles();
	}
	
	public void close() throws IOException, SQLException {
		// Spaltenbreiten anpassen
		for (int spalte = 0; spalte < 1 + hoechstesBespieltesFeld * 8; spalte++) {
			sheet1.autoSizeColumn(spalte);
		}
		sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE); // A4 Querformat
		wb.setPrintArea(  // Druckbereich setzen
				0, // sheet index
				0, // start column
				hoechstesBespieltesFeld * 8, // end column
				0, // start row
				aktReihe - 1 // end row
				);
		wb.write(fos); // Excel-Datei schreiben
		fos.flush();
		fos.close(); // FileOutputStream schließen
		plan.close(); // ResultSet schließen
		con.close(); // Datenbankverbindung schließen
	}
	
	public void eintragen() throws SQLException {
		titelEintragen();
		for(int i = 0; i < hoechstesBespieltesFeld; i++) {
			ueberschriftenEintragen(i);
		}
		spielerEintragen();
	}
	
	private void titelEintragen() throws SQLException {
		Row row = sheet1.createRow(0);
		Cell cell = row.createCell(0);
		
		// Info in der ersten Zeile eintragen
		cell.setCellStyle(csInfos);
		cell.setCellValue("Die Spiele beginnen und enden jeweils mit der Durchsage. Bitte an die vorgegebenen Zeiten halten!");
		sheet1.addMergedRegion(new CellRangeAddress(0,0,0,10));
		row.setHeightInPoints((short) 15);
		
		int reihe; // ich brauche mehrfach eine Referenz auf die gleiche Reihe
		
		Font fInfosNichtFett = wb.createFont();
		fInfosNichtFett.setFontHeightInPoints((short) 11); // 11pt
		fInfosNichtFett.setColor(Font.COLOR_NORMAL); // schwarz
		fInfosNichtFett.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		CellStyle csInfosNichtFett = wb.createCellStyle();
		csInfosNichtFett.cloneStyleFrom(csInfos);
		csInfosNichtFett.setFont(fInfosNichtFett);
		
		// Lehrer IDEA Lehrer-management
		reihe = aktReihe++;
		row = sheet1.createRow(reihe);
		row.setHeightInPoints((short) 15);
		// Linker Teil 
		cell = row.createCell(1);
		cell.setCellStyle(csInfos);
		cell.setCellValue("Lehrer");
		sheet1.addMergedRegion(new CellRangeAddress(reihe, reihe, 1, 3));
		// Rechter Teil
		cell = row.createCell(4);
		cell.setCellStyle(csInfosNichtFett);
		cell.setCellValue("");
		sheet1.addMergedRegion(new CellRangeAddress(reihe, reihe, 4, 7));
		
		// Sportart / Stufe
		reihe = aktReihe++;
		row = sheet1.createRow(reihe);
		row.setHeightInPoints((short) 15);
		// Linker Teil 
		cell = row.createCell(1);
		cell.setCellStyle(csInfos);
		cell.setCellValue("Sportart und Stufe");
		sheet1.addMergedRegion(new CellRangeAddress(reihe, reihe, 1, 3));
		// Rechter Teil
		cell = row.createCell(4);
		cell.setCellStyle(csInfosNichtFett);
		cell.setCellValue(String.format("%s %s", sportart.getSportartLang(), stufe.getStufeLang()));
		sheet1.addMergedRegion(new CellRangeAddress(reihe, reihe, 4, 7));
		
		// Sportart / Stufe
		reihe = aktReihe++;
		row = sheet1.createRow(reihe);
		row.setHeightInPoints((short) 15);
		// Linker Teil 
		cell = row.createCell(1);
		cell.setCellStyle(csInfos);
		cell.setCellValue("Tag und Uhrzeit");
		sheet1.addMergedRegion(new CellRangeAddress(reihe, reihe, 1, 3));
		// Rechter Teil
		cell = row.createCell(4);
		cell.setCellStyle(csInfosNichtFett);
		String tagLang; // "Montag" oder "Dienstag" ausgeben statt "MO" und "DI"
		if(this.tag.equals("MO"))
			tagLang = "Montag";
		else // eigentlich gibts noch mehr aber nur zwei werden bei setzen akzeptiert
			tagLang = "Dienstag";
		plan.first();
		Date beginnBlock = plan.getTime("Beginn");
		plan.last();
		Date endeBlock = plan.getTime("Ende");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		cell.setCellValue(String.format("%s, %s - %s", tagLang, df.format(beginnBlock), endeBlock));
		sheet1.addMergedRegion(new CellRangeAddress(reihe, reihe, 4, 7));
		
		
		// Zwischen dem "Info-Block" und der eigentlichen Liste noch etwas Abstand machen
		aktReihe += 2;
		
		// Uhrzeit
		ueberschriftenReihe = aktReihe++;
		row = sheet1.createRow(ueberschriftenReihe);
		row.setHeightInPoints(15);;
		cell = row.createCell(0);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Uhrzeit");
	}
	
	private void ueberschriftenEintragen(int fuerFeld) {
		Row row = sheet1.getRow(ueberschriftenReihe);
		Cell cell;
		
		// Tabellenüberschriften		
		// Team 1
		cell = row.createCell(1 + fuerFeld * 8);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Team 1");
		cell = row.createCell(2 + fuerFeld * 8);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Name");
		cell = row.createCell(3 + fuerFeld * 8);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Vorname");
		cell = row.createCell(4 + fuerFeld * 8);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("anw");
		// Team 2
		cell = row.createCell(5 + fuerFeld * 8);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Team 2");
		cell = row.createCell(6 + fuerFeld * 8);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Name");
		cell = row.createCell(7 + fuerFeld * 8);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Vorname");
		cell = row.createCell(8 + fuerFeld * 8);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("anw");
	}
	
	public void spielerEintragen() throws SQLException {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		boolean gerade = false;
		CellStyle cs = null;
		plan.absolute(0); // wieder an den Anfang gehen
		while(plan.next()) {
			if(gerade) { // Immer abwechselnd mit weißem und grauem Hintergrund
				cs = csNormal1;
				gerade = !gerade;
			} else if(!gerade) {
				cs = csNormal2;
				gerade = !gerade;
			}
			Row row = sheet1.createRow(aktReihe); // In der aktuellen Reihe
			Cell cell = row.createCell(0);
			cell.setCellStyle(cs);
			// Zeit eintragen
			cell.setCellStyle(cs);
			cell.setCellValue(String.format("%s - %s", df.format(plan.getTime("Beginn")), df.format(plan.getTime("Ende"))));
			
			final int startReihe = aktReihe;
			int endReihe = 0;
			for(int i = 0; i < hoechstesBespieltesFeld; i++) {
				aktReihe = startReihe;
				row = sheet1.getRow(aktReihe++);
				String spiel = plan.getString(String.format("Feld%d", i + 1));
				if(spiel == null) { // nur weitermachen wenn auch gespielt wird, aber trotzdem cellstyle anwenden
					cell = row.createCell(1 + i * 8);
					cell.setCellStyle(cs);
					cell = row.createCell(5 + i * 8);
					cell.setCellStyle(cs);
					cell = row.createCell(1 + i * 8);
					cell.setCellStyle(cs);cell = row.createCell(5 + i * 8);
					cell.setCellStyle(cs);
					continue;
				}
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
				cell = row.createCell(1 + i * 8);
				cell.setCellStyle(cs);
				cell.setCellValue(team1);
				// Team 2 Name eintragen
				cell = row.createCell(5 + i * 8);
				cell.setCellStyle(cs);
				cell.setCellValue(team2);
				
				// erste Person eintragen ohne in neue Reihe zu wechseln
				// Team 1
				spielerTeam1.next();
				cell = row.createCell(2 + i * 8); // Zelle für Nachname
				cell.setCellStyle(cs);
				cell.setCellValue(spielerTeam1.getString("Vorname"));
				cell = row.createCell(3 + i * 8); // Zelle für Nachname
				cell.setCellStyle(cs);
				cell.setCellValue(spielerTeam1.getString("Name"));
				cell = row.createCell(4 + i * 8); // Leerzelle für Anwesenheit
				cell.setCellStyle(cs); // mit csNormal formatieren, damit eine Umrandung da ist
				// Team 2
				spielerTeam2.next();
				cell = row.createCell(6 + i * 8); // Zelle für Nachname
				cell.setCellStyle(cs);
				cell.setCellValue(spielerTeam2.getString("Vorname"));
				cell = row.createCell(7 + i * 8); // Zelle für Nachname
				cell.setCellStyle(cs);
				cell.setCellValue(spielerTeam2.getString("Name"));
				cell = row.createCell(8 + i * 8); // Leerzelle für Anwesenheit
				cell.setCellStyle(cs); // mit csNormal formatieren, damit eine Umrandung da ist
				
				// Alle weiteren Spieler darunter eintragen
				while(spielerTeam1.next() | spielerTeam2.next()) { // nicht-kurzschluss! (sonst gehen nicht beide eins weiter)
					spielerTeam1.previous(); // gleich kommt nochmal next(), deshalb eins zurück
					spielerTeam2.previous();
					// Neue Reihe erstellen
					row = sheet1.getRow(aktReihe);
					if(row == null) {
						row = sheet1.createRow(aktReihe);
					}
					aktReihe++;
					
					// Leerzellen für Uhrzeit, Teamname und Anwesenheit mit csNormal formatieren, damit sie Umrandungen haben
					cell = row.createCell(0);
					cell.setCellStyle(cs);
					cell = row.createCell(1 + i * 8);
					cell.setCellStyle(cs);
					cell = row.createCell(4 + i * 8);
					cell.setCellStyle(cs); // mit csNormal formatieren, damit eine Umrandung da ist
					cell = row.createCell(5 + i * 8);
					cell.setCellStyle(cs);
					cell = row.createCell(8 + i * 8);
					cell.setCellStyle(cs);

					String vorname = "";
					String name = "";
					
					// Team 1
					if(spielerTeam1.next()) {
						vorname = spielerTeam1.getString("Vorname");
						name = spielerTeam1.getString("Name");
					}
					
					// Spieler eintragen
					cell = row.createCell(2 + i * 8); // Zelle für Nachname
					cell.setCellStyle(cs);
					cell.setCellValue(vorname);
					cell = row.createCell(3 + i * 8); // Zelle für Nachname
					cell.setCellStyle(cs);
					cell.setCellValue(name);
					
					vorname = "";
					name = "";
					// Team 2
					if(spielerTeam2.next()) {
						vorname = spielerTeam2.getString("Vorname");
						name = spielerTeam2.getString("Name");
					}
					// Spieler eintragen
					cell = row.createCell(6 + i * 8); // Zelle für Nachname
					cell.setCellStyle(cs);
					cell.setCellValue(vorname);
					cell = row.createCell(7 + i * 8); // Zelle für Nachname
					cell.setCellStyle(cs);
					cell.setCellValue(name);
				}
				if(aktReihe > endReihe) {
					endReihe = aktReihe;
				}
			}
			aktReihe = endReihe;
			for(int i = startReihe; i < endReihe; i++) {
				row = sheet1.getRow(i);
				row.setRowStyle(cs);
			}
		}
	}
	
	private void setStyles() {
		csNormal1 = wb.createCellStyle(); // Stil für Uhrzeiten, Namen etc. (gerade Spiele)
		csNormal2 = wb.createCellStyle(); // ungerade Spiele
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
		csNormal1.setFont(fNormal);
		csNormal1.setAlignment(CellStyle.ALIGN_LEFT); // linksbündig
		csNormal1.setBorderBottom(CellStyle.BORDER_THIN); // Umrandung unten einschalten
		csNormal1.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung unten in schwarz
		csNormal1.setBorderLeft(CellStyle.BORDER_THIN); // Umrandung links einschalten
		csNormal1.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung links in schwarz
		csNormal1.setBorderTop(CellStyle.BORDER_THIN); // Umrandung oben einschalten
		csNormal1.setTopBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung oben in schwarz
		csNormal1.setBorderRight(CellStyle.BORDER_THIN); // Umrandung rechts einschalten
		csNormal1.setRightBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung rechts in schwarz
		csNormal1.setDataFormat(dataFormat.getFormat("text"));
		
		csNormal2.cloneStyleFrom(csNormal1); // Gleich wie Normal für Gerade, nur der Hintergrund ist anders
		csNormal2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // leicht grau
		csNormal2.setFillPattern(CellStyle.SOLID_FOREGROUND);
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
