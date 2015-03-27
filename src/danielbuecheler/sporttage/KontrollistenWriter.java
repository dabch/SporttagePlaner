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

public class KontrollistenWriter {
	private Connection con; // Datenbankverbindung
	PreparedStatement holeTeams1;
	PreparedStatement holeTeams2;

	private FileOutputStream fos; // FileOutputStream, auf den die Excel-Tabelle geschrieben wird
	private HSSFWorkbook wb; // Excel-Datei
	private Sheet sheet1; // Tabelle

	private CellStyle csUeberschriften; // CellStyles (Formatierungen) für die Excel-Tabelle
	private CellStyle csInfos;
	private CellStyle csNormal1;
	private CellStyle csNormal2;
	
	private int currentRow = 3; // Letzte Reihe, in die eingetragen wurde
	private int ueberschriftenReihe;
	private int hoechstesBespieltesFeld;
	private int anzahlSpielrunden;
	
	private String tag;
	private Sportart sportart;
	private Stufe stufe;
	private String tablePlanStamm; 

	public KontrollistenWriter(Sportart sportart, Stufe stufe, String tag) throws SQLException, IllegalArgumentException, IOException {
		
		String filename = String.format("%s/Kontrolliste_%s_%s_%s.xls", SpielplanerApp.dirKontrollisten.getCanonicalPath(), stufe.getStufeKurz(), sportart.getSportartKurz(), tag); // der Dateiname ist automatisch generiert
		
		this.tablePlanStamm = String.format("%s_%s_%s", stufe.getStufeKurz(), sportart.getSportartKurz(), tag); // Tabellennamen festlegen
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
		
		
		holeTeams1 = con.prepareStatement(String.format("SELECT Vorname, Name FROM Mannschaften_%s WHERE %s = ?", stufe.getStufeKurz(), sportart.getSportartKurz())); // SQL-Statement, um Spieler einer bestimmten Mannschaft abzufragen
		holeTeams2 = con.prepareStatement(String.format("SELECT Vorname, Name FROM Mannschaften_%s WHERE %s = ?", stufe.getStufeKurz(), sportart.getSportartKurz())); // SQL-Statement, um Spieler einer bestimmten Mannschaft abzufragen
		
		Statement stmt = con.createStatement();
		
		ResultSet hoechstesBespieltesFeldRS = stmt.executeQuery("SELECT MAX(Feld) FROM " + tablePlanStamm + "_spiele"); // hoechstes feld holen
		hoechstesBespieltesFeldRS.next();
		hoechstesBespieltesFeld = hoechstesBespieltesFeldRS.getInt(1); // INT extrahieren (yay :D)
		
		ResultSet anzahlSpielrundenRS = stmt.executeQuery("SELECT MAX(TimeID) FROM " + tablePlanStamm + "_spiele"); // anzahl der spiele
		anzahlSpielrundenRS.next();
		this.anzahlSpielrunden = anzahlSpielrundenRS.getInt(1); // INT extrahieren :D
		
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
				currentRow - 1 // end row
				);
		wb.write(fos); // Excel-Datei schreiben
		fos.flush();
		fos.close(); // FileOutputStream schließen
		con.close(); // Datenbankverbindung schließen
	}
	
	public void eintragen() throws SQLException {
		titelEintragen();
		ueberschriftenEintragen();
		spielerEintragen();
	}
	
	private void titelEintragen() throws SQLException {
		Row row = sheet1.createRow(0);
		Cell cell = row.createCell(0);
		
		// Info in der ersten Zeile eintragen
		cell.setCellStyle(csInfos);
		cell.setCellValue("Die Spiele beginnen und enden jeweils mit der Durchsage. Bitte an die vorgegebenen Zeiten halten!");
		sheet1.addMergedRegion(new CellRangeAddress(0,0,0,hoechstesBespieltesFeld * 8));
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
		reihe = currentRow++;
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
		reihe = currentRow++;
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
		reihe = currentRow++;
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
		PreparedStatement holeZeiten = con.prepareStatement(
				"SELECT ID Nr, Spielbeginn Beginn, Spielende Ende " // Nummer, Beginn und Ende sind interessant
				+ "FROM " + tablePlanStamm + "_zeiten " // richtige Tabelle wählen
				+ "WHERE ID <= (SELECT MAX(TimeID) FROM " + tablePlanStamm + "_spiele) " // nur so viele Zeiten wie auch Spiele gebraucht werden holen
				+ "ORDER BY Nr"); // nach Beginn sortiert
		ResultSet zeiten = holeZeiten.executeQuery();
		
		if(!zeiten.next()) // Zeiten eins weiter
			return; // aufhören wenn keine Zeiten mehr da
		Date beginn = new Date(zeiten.getTime("Beginn").getTime()); // Beginn extrahieren
		zeiten.last(); // letztes spiel anschauen
		Date ende = new Date(zeiten.getTime("Ende").getTime()); // Ende extrahieren
		zeiten.close(); // RS schließen
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Zur formatierung -> damit die :00 Sekunden weggehen
		cell.setCellValue(String.format("%s, %s - %s", tagLang, sdf.format(beginn), sdf.format(ende))); // Von - Bis

		sheet1.addMergedRegion(new CellRangeAddress(reihe, reihe, 4, 7));
		
		// Zwischen dem "Info-Block" und der eigentlichen Liste noch etwas Abstand machen
		currentRow += 2;
		
		// Feldnummern
		row = sheet1.createRow(currentRow++);
		row.setRowStyle(csNormal1);
		for(int feld = 0; feld < hoechstesBespieltesFeld; feld++) {
			cell = row.createCell(feld * 8 + 1); // jedes feld braucht 8 spalten
			cell.setCellStyle(csNormal1); // CS normal nicht-grau
			cell.setCellValue("Feld " + (feld + 1)); // Feldnummer (Feld 1 ....... x)
			sheet1.addMergedRegion(new CellRangeAddress(currentRow - 1, currentRow - 1, feld * 8 + 1, feld * 8 + 8));
		}
		
		// Uhrzeit
		ueberschriftenReihe = currentRow++;
		row = sheet1.createRow(ueberschriftenReihe);
		row.setHeightInPoints(15);;
		cell = row.createCell(0);
		cell.setCellStyle(csUeberschriften);
		cell.setCellValue("Uhrzeit");
	}
	
	private void ueberschriftenEintragen() {
		Row row = sheet1.getRow(ueberschriftenReihe);
		row.setRowStyle(csUeberschriften);
		Cell cell;
		
		// Tabellenüberschriften		
		for(int feld = 0; feld < hoechstesBespieltesFeld; feld++) {
			// 1. Team
			cell = row.createCell(1 + feld * 8);
			cell.setCellStyle(csUeberschriften);
			cell.setCellValue("Team 1");
			cell = row.createCell(2 + feld * 8);
			cell.setCellStyle(csUeberschriften);
			cell.setCellValue("Name");
			cell = row.createCell(3 + feld * 8);
			cell.setCellStyle(csUeberschriften);
			cell.setCellValue("Vorname");
			cell = row.createCell(4 + feld * 8);
			cell.setCellStyle(csUeberschriften);
			cell.setCellValue("anw");
			// Team 2
			cell = row.createCell(5 + feld * 8);
			cell.setCellStyle(csUeberschriften);
			cell.setCellValue("Team 2");
			cell = row.createCell(6 + feld * 8);
			cell.setCellStyle(csUeberschriften);
			cell.setCellValue("Name");
			cell = row.createCell(7 + feld * 8);
			cell.setCellStyle(csUeberschriften);
			cell.setCellValue("Vorname");
			cell = row.createCell(8 + feld * 8);
			cell.setCellStyle(csUeberschriften);
			cell.setCellValue("anw");
		}
	}
	
	public void spielerEintragen() throws SQLException {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		boolean gerade = false;
		CellStyle cs = null;
		PreparedStatement holeSpieleMitGleicherID = con.prepareStatement(
				"SELECT TimeID Nr, Feld, Paarung " // nur die Nummer und die Paarung sind interessant
				+ "FROM " + tablePlanStamm + "_spiele " // richtige Tabelle wählen
				+ "WHERE TimeID = ? ORDER BY TimeID"); // nur ein Feld; sortiert nach Nummer
		
		PreparedStatement holeZeiten = con.prepareStatement(
				"SELECT ID Nr, Spielbeginn Beginn, Spielende Ende " // Nummer, Beginn und Ende sind interessant
				+ "FROM " + tablePlanStamm + "_zeiten " // richtige Tabelle wählen
				+ "WHERE ID <= (SELECT MAX(TimeID) FROM " + tablePlanStamm + "_spiele) " // nur so viele Zeiten wie auch Spiele gebraucht werden holen
				+ "ORDER BY Nr"); // nach Beginn sortiert
		
		int beginnReihe = currentRow; // oberste Reihe zwischenspeichern für später
		
		Row row;
		Cell cell;
		
		ResultSet zeiten = holeZeiten.executeQuery(); // Zeiten holen
		
		int endReihe = currentRow; // letzte Reihe zwischenspeichern
		
		// Spiele Feld fuer Feld eintragen
		for(int rundennr = 0; rundennr < anzahlSpielrunden; rundennr++) { // Für jedes Feld
			if(gerade) { // Immer abwechselnd mit weißem und grauem Hintergrund
				cs = null;
				cs = csNormal1;
				gerade = !gerade;
			} else if(!gerade) {
				cs = null;
				cs = csNormal2;
				gerade = !gerade;
			}
			currentRow = endReihe; // nächste Runde, deshalb currentRow neu setzen (das ist die reihe zu der wir nacher zurückgehen)
			beginnReihe = currentRow; // beginnReihe ist jetzt eine Reihe weiter unten
			holeSpieleMitGleicherID.setInt(1, rundennr + 1); // TimeID auswählen (i ist 0-basiert, deshalb +1)
			ResultSet spieleMitGleicherID = holeSpieleMitGleicherID.executeQuery(); // Spiele holen
			row = sheet1.getRow(currentRow); // rundennr Zeilen unter dem Beginn
			if(row == null) {
				row = sheet1.createRow(currentRow);
			}
			row.setRowStyle(cs);
//			currentRow++;//  (konnte ja eben nicht erhöht werden)
			
			// Zeit eintragen
			if(!zeiten.next()) // Zeiten eins weiter
				continue; // aufhören wenn keine Zeiten mehr da
			cell = row.createCell(0); // Immer die erste Spalte ist fuer die Uhrzeiton
			cell.setCellStyle(cs); // CS setzen
			Date beginn = new Date(zeiten.getTime("Beginn").getTime()); // Beginn extrahieren
			Date ende = new Date(zeiten.getTime("Ende").getTime()); // Ende extrahieren
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); // Zur formatierung -> damit die :00 Sekunden weggehen
			cell.setCellValue(String.format("%s - %s", sdf.format(beginn), sdf.format(ende))); // Von - Bis
			
			if(!spieleMitGleicherID.next()) {
				currentRow++;
				if(currentRow > endReihe) { // endReihe aktualisieren, wenn wir tiefer als die aktuelle endReihe sind
					endReihe = currentRow;
				}
			}
			
			spieleMitGleicherID.previous(); // eins zurück gehen weil ich gerade eben next() aufgerufen habe
			
			
			// Spiele mit Spielern eintragen
			while(spieleMitGleicherID.next()) {
				currentRow = beginnReihe;
				row = sheet1.getRow(currentRow);
				if(row == null) {
					row = sheet1.createRow(currentRow);
				}
				currentRow++;
				row.setRowStyle(cs); // row stylen
				
				// Feld speichern
				int feld = spieleMitGleicherID.getInt("Feld");
				
				String spielString = spieleMitGleicherID.getString("Paarung");
				// Spiel-String in Teams zerlegen
				String team1 = spielString.substring(0, spielString.indexOf(':')).trim();
				String team2 = spielString.substring(spielString.indexOf(':') + 2).trim();
				// Spieler aus Team 1 vom Server holen
				holeTeams1.setString(1, team1); // Nur nach Personen in team 1 suchen
				ResultSet spielerTeam1 = holeTeams1.executeQuery();
				// Spieler aus Team 2 vom Server holen
				holeTeams2.setString(1, team2); // Nur Personen aus Team 2
				ResultSet spielerTeam2 = holeTeams2.executeQuery();
				
				// Team 1 Name eintragen
				cell = row.createCell(1 + (feld - 1) * 8);
				cell.setCellStyle(cs);
				cell.setCellValue(team1);
				// Team 2 Name eintragen
				cell = row.createCell(5 + (feld - 1) * 8);
				cell.setCellStyle(cs);
				cell.setCellValue(team2);
				
				// erste Person eintragen ohne in neue Reihe zu wechseln
				// Team 1
				if(spielerTeam1.next()) { // nur wenn es auch mind. einen Spieler gibt
					cell = row.createCell(2 + (feld - 1) * 8); // Zelle für Vorname
					cell.setCellStyle(cs);
					cell.setCellValue(spielerTeam1.getString("Vorname"));
					cell = row.createCell(3 + (feld - 1) * 8); // Zelle für Nachname
					cell.setCellStyle(cs);
					cell.setCellValue(spielerTeam1.getString("Name"));
				}
				// Team 2
				if(spielerTeam2.next()) {
					cell = row.createCell(6 + (feld - 1) * 8); // Zelle für Vorname
					cell.setCellStyle(cs);
					cell.setCellValue(spielerTeam2.getString("Vorname"));
					cell = row.createCell(7 + (feld - 1) * 8); // Zelle für Nachname
					cell.setCellStyle(cs);
					cell.setCellValue(spielerTeam2.getString("Name"));
				}
				
				// Personen zwei bis x eintragen
				while(spielerTeam1.next() | spielerTeam2.next()) { // nicht-kurzschluss! (sonst gehen nicht beide eins weiter)
					spielerTeam1.previous(); // gleich kommt nochmal next(), deshalb eins zurück
					spielerTeam2.previous();
					// Neue Reihe erstellen
					row = sheet1.getRow(currentRow);
					if(row == null) {
						row = sheet1.createRow(currentRow);
					}
					row.setRowStyle(cs);
					currentRow++;
					
					String vorname = ""; // Leerstring für den Fall, dass es keinen Spieler gibt
					String name = "";
					
					// Team 1
					if(spielerTeam1.next()) {
						vorname = spielerTeam1.getString("Vorname");
						name = spielerTeam1.getString("Name");
					}
					
					// Spieler eintragen
					cell = row.createCell(2 + (feld - 1) * 8); // Zelle für Nachname
					cell.setCellStyle(cs);
					cell.setCellValue(vorname);
					cell = row.createCell(3 + (feld - 1) * 8); // Zelle für Nachname
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
					cell = row.createCell(6 + (feld - 1) * 8); // Zelle für Nachname
					cell.setCellStyle(cs);
					cell.setCellValue(vorname);
					cell = row.createCell(7 + (feld - 1) * 8); // Zelle für Nachname
					cell.setCellStyle(cs);
					cell.setCellValue(name);
				}
				if(currentRow > endReihe) { // endReihe aktualisieren, wenn wir tiefer als die aktuelle endReihe sind
					endReihe = currentRow;
				}
//				// Reihen formatieren
//				for(int i = beginnReihe; i < endReihe; i++) {
//					row = sheet1.getRow(i);
//					row.setRowStyle(cs);
//				}
			}
		}
	}
		
//		while(plan.next()) {
//			if(gerade) { // Immer abwechselnd mit weißem und grauem Hintergrund
//				cs = csNormal1;
//				gerade = !gerade;
//			} else if(!gerade) {
//				cs = csNormal2;
//				gerade = !gerade;
//			}
//			Row row = sheet1.createRow(currentRow); // In der aktuellen Reihe
//			Cell cell = row.createCell(0);
//			cell.setCellStyle(cs);
//			// Zeit eintragen
//			cell.setCellStyle(cs);
//			cell.setCellValue(String.format("%s - %s", df.format(plan.getTime("Beginn")), df.format(plan.getTime("Ende"))));
//			
//			final int startReihe = currentRow;
//			int endReihe = 0;
//			for(int i = 0; i < hoechstesBespieltesFeld; i++) {
//				currentRow = startReihe;
//				row = sheet1.getRow(currentRow++);
//				String spiel = plan.getString(String.format("Feld%d", i + 1));
//				if(spiel == null) { // nur weitermachen wenn auch gespielt wird, aber trotzdem cellstyle anwenden
//					cell = row.createCell(1 + i * 8);
//					cell.setCellStyle(cs);
//					cell = row.createCell(5 + i * 8);
//					cell.setCellStyle(cs);
//					cell = row.createCell(1 + i * 8);
//					cell.setCellStyle(cs);cell = row.createCell(5 + i * 8);
//					cell.setCellStyle(cs);
//					continue;
//				}
//				// Spiel-String in Teams zerlegen
//				String team1 = spiel.substring(0, spiel.indexOf(':')).trim();
//				String team2 = spiel.substring(spiel.indexOf(':') + 2).trim();
//				// Spieler aus Team 1 vom Server holen
//				holeTeams1.setString(1, team1); // Nur nach Personen in team 1 suchen
//				ResultSet spielerTeam1 = holeTeams1.executeQuery();
//				// Spieler aus Team 2 vom Server holen
//				holeTeams2.setString(1, team2);
//				ResultSet spielerTeam2 = holeTeams2.executeQuery();
//				
//				// Team 1 Name eintragen
//				cell = row.createCell(1 + i * 8);
//				cell.setCellStyle(cs);
//				cell.setCellValue(team1);
//				// Team 2 Name eintragen
//				cell = row.createCell(5 + i * 8);
//				cell.setCellStyle(cs);
//				cell.setCellValue(team2);
//				
//				// erste Person eintragen ohne in neue Reihe zu wechseln
//				// Team 1
//				spielerTeam1.next();
//				cell = row.createCell(2 + i * 8); // Zelle für Nachname
//				cell.setCellStyle(cs);
//				cell.setCellValue(spielerTeam1.getString("Vorname"));
//				cell = row.createCell(3 + i * 8); // Zelle für Nachname
//				cell.setCellStyle(cs);
//				cell.setCellValue(spielerTeam1.getString("Name"));
//				cell = row.createCell(4 + i * 8); // Leerzelle für Anwesenheit
//				cell.setCellStyle(cs); // mit csNormal formatieren, damit eine Umrandung da ist
//				// Team 2
//				spielerTeam2.next();
//				cell = row.createCell(6 + i * 8); // Zelle für Nachname
//				cell.setCellStyle(cs);
//				cell.setCellValue(spielerTeam2.getString("Vorname"));
//				cell = row.createCell(7 + i * 8); // Zelle für Nachname
//				cell.setCellStyle(cs);
//				cell.setCellValue(spielerTeam2.getString("Name"));
//				cell = row.createCell(8 + i * 8); // Leerzelle für Anwesenheit
//				cell.setCellStyle(cs); // mit csNormal formatieren, damit eine Umrandung da ist
//				
//				// Alle weiteren Spieler darunter eintragen
//				while(spielerTeam1.next() | spielerTeam2.next()) { // nicht-kurzschluss! (sonst gehen nicht beide eins weiter)
//					spielerTeam1.previous(); // gleich kommt nochmal next(), deshalb eins zurück
//					spielerTeam2.previous();
//					// Neue Reihe erstellen
//					row = sheet1.getRow(currentRow);
//					if(row == null) {
//						row = sheet1.createRow(currentRow);
//					}
//					currentRow++;
//					
//					// Leerzellen für Uhrzeit, Teamname und Anwesenheit mit csNormal formatieren, damit sie Umrandungen haben
//					cell = row.createCell(0);
//					cell.setCellStyle(cs);
//					cell = row.createCell(1 + i * 8);
//					cell.setCellStyle(cs);
//					cell = row.createCell(4 + i * 8);
//					cell.setCellStyle(cs); // mit csNormal formatieren, damit eine Umrandung da ist
//					cell = row.createCell(5 + i * 8);
//					cell.setCellStyle(cs);
//					cell = row.createCell(8 + i * 8);
//					cell.setCellStyle(cs);
//
//					String vorname = "";
//					String name = "";
//					
//					// Team 1
//					if(spielerTeam1.next()) {
//						vorname = spielerTeam1.getString("Vorname");
//						name = spielerTeam1.getString("Name");
//					}
//					
//					// Spieler eintragen
//					cell = row.createCell(2 + i * 8); // Zelle für Nachname
//					cell.setCellStyle(cs);
//					cell.setCellValue(vorname);
//					cell = row.createCell(3 + i * 8); // Zelle für Nachname
//					cell.setCellStyle(cs);
//					cell.setCellValue(name);
//					
//					vorname = "";
//					name = "";
//					// Team 2
//					if(spielerTeam2.next()) {
//						vorname = spielerTeam2.getString("Vorname");
//						name = spielerTeam2.getString("Name");
//					}
//					// Spieler eintragen
//					cell = row.createCell(6 + i * 8); // Zelle für Nachname
//					cell.setCellStyle(cs);
//					cell.setCellValue(vorname);
//					cell = row.createCell(7 + i * 8); // Zelle für Nachname
//					cell.setCellStyle(cs);
//					cell.setCellValue(name);
//				}
//				if(currentRow > endReihe) {
//					endReihe = currentRow;
//				}
//			}
//			currentRow = endReihe;
//			// Reihen formatieren
//			for(int i = startReihe; i < endReihe; i++) {
//				row = sheet1.getRow(i);
//				row.setRowStyle(cs);
//			}
//		}
	
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
	
	
}
