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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

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

public class PfeiflistenMaker {

	private FileOutputStream fos; // FileOutputStream, auf den die Excel-Tabelle geschrieben wird
	private HSSFWorkbook wb; // Excel-Datei
	private Sheet sheet1; // Tabelle
	Connection con;

	private CellStyle csTitel; // CellStyles (Formatierungen) für die Excel-Tabelle
	private CellStyle csUntertitel;
	private CellStyle csTabelleFett;
	private CellStyle csTabelleNormal;

	HashMap<Long, String> zeiten = new HashMap<>();
	int aktReihe = 2;

	Sportart[] sportartenInnen = { new Sportart("VB"), new Sportart("BM") };
	Sportart[] sportartenDraussen = { new Sportart("VB"), new Sportart("FB"), new Sportart("BB") };
	Stufe stufe;
	String tag;

	public PfeiflistenMaker(String filename) throws SQLException, FileNotFoundException, IOException {
		if(!filename.endsWith(".xls")) // .xls-Endung anfügen wenn nicht vorhanden
			filename = filename.concat(".xls");

		fos = new FileOutputStream(SpielplanerApp.dirPfeiflisten.getCanonicalPath() + "/" + filename);
		wb = new HSSFWorkbook();
		sheet1 = wb.createSheet("Pfeifliste");

		con = DriverManager.getConnection(
				String.format(
						"jdbc:mysql://%s/%s", // Verbindung zur DB aufbauen
						SpielplanerApp.properties.getProperty("database_ip_address"),
						SpielplanerApp.properties.getProperty("database_name")),
				SpielplanerApp.properties.getProperty("database_username"),
				SpielplanerApp.properties.getProperty("database_password"));
		setStyles();
	}
	
	public void close() throws SQLException, IOException {
		con.close();
		for(int i = 0; i < 3; i++) { // Spaltenbreiten anpassen
			sheet1.autoSizeColumn(i);
		}
		sheet1.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
		wb.write(fos);
		fos.flush();
		fos.close();
	}

	/**
	 * 
	 * @param tag
	 * @param stufe1 die Stufe, die VB spielt
	 * @param stufe2 die stufe, die BM spielt (für drinnen) / FB spielt (für draußen)
	 * @param stufe3
	 *            Wenn null: Pfeifliste für drinnen, wenn nicht null: Pfeifliste für draußen
	 *            Für draußen: Basketball, für drinnen: {@code null}
	 * @throws SQLException
	 */
	public void erstellePfeifliste(String tag, Stufe... stufen) throws SQLException {
		Sportart[] sportarten;
		if(stufen.length > 3) {
			throw new IllegalArgumentException("Maximal drei Stufen angeben!");
		} else if(stufen[2] == null) {
			sportarten = sportartenInnen;
		} else {
			sportarten = sportartenDraussen;
		}
		for (int i = 0; i < sportarten.length; i++) {
			String tableStamm = String.format("%s_%s_%s", stufen[i].getStufeKurz(), sportarten[i].getSportartKurz(), tag);
			PreparedStatement holeZeiten;
			switch(sportarten[i].getSportartKurz()) {
			case "BM": // sechs Felder
				holeZeiten = con.prepareStatement(String.format(
						"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID LEFT JOIN %s_feld4 f4 ON f4.ID = z.ID LEFT JOIN %s_feld5 f5 ON f5.ID = z.ID LEFT JOIN %s_feld6 f6 ON f6.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != '' OR f4.Paarung != '' OR f5.Paarung != '' OR f6.Paarung != ''",
						tableStamm, tableStamm, tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
				break;
			case "FB": // drei Felder
			case "BB":
				holeZeiten = con.prepareStatement(String.format(
						"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != ''",
						tableStamm, tableStamm, tableStamm, tableStamm));
				break;
			case "TT": // vier Felder
				holeZeiten = con.prepareStatement(String.format(
						"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID LEFT JOIN %s_feld3 f3 ON f3.ID = z.ID LEFT JOIN %s_feld4 f4 ON f4.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != '' OR f3.Paarung != '' OR f4.Paarung != ''",
						tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
				break;
			case "VB": // zwei Felder
				holeZeiten = con.prepareStatement(String.format(
						"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID LEFT JOIN %s_feld2 f2 ON f2.ID = z.ID WHERE f1.Paarung != '' OR f2.Paarung != ''",
						tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
				break;
			default: // ein Feld
				holeZeiten = con.prepareStatement(String.format(
						"SELECT z.ID AS ID, z.Spielbeginn AS Beginn, z.Spielende AS Ende FROM %s_zeiten z LEFT JOIN %s_feld1 f1 ON f1.ID = z.ID WHERE f1.Paarung != ''",
						tableStamm, tableStamm, tableStamm, tableStamm, tableStamm));
			}
			System.out.println(holeZeiten);
			ResultSet rsZeiten = holeZeiten.executeQuery();

			while (rsZeiten.next()) {
				zeiten.put(rsZeiten.getTime("Beginn").getTime() + sportarten[i].getZeitDiff(),
						String.format("+ %s", sportarten[i].getSportartLang()));
				zeiten.put(rsZeiten.getTime("Ende").getTime() + sportarten[i].getZeitDiff(), String.format("- %s", sportarten[i].getSportartLang()));
			}
		}
		titelEintragen();
		zeitenUndSportartenEintragen();
	}

	private void zeitenUndSportartenEintragen() {
		Row row;
		Cell cell;

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		ArrayList<Long> zeitenList = new ArrayList<>();

		zeitenList.addAll(zeiten.keySet());
		Collections.sort(zeitenList);

		for (long millis : zeitenList) {
			row = sheet1.createRow(aktReihe++);
			row.setHeightInPoints(13);
			// Zeit
			cell = row.createCell(0);
			cell.setCellStyle(csTabelleFett);
			cell.setCellValue(df.format(new Date(millis)));
			// Sportart
			cell = row.createCell(1);
			cell.setCellStyle(csTabelleNormal);
			cell.setCellValue(zeiten.get(millis).substring(2)); // In der HashMap kann immer nur ein Wert gespeichert werden, deshalb wird der String vorher
																// zusammengesetzt und hier wieder auseinandergenommen
			// An- / Abpfiff
			int pos = zeiten.get(millis).substring(0, 1).equals("+") ? 2 : 3;
			cell = row.createCell(pos);
			cell.setCellStyle(csTabelleNormal);
			cell.setCellValue("x");
			// andere Zelle leer formatieren
			cell = row.createCell(5 - pos);
			cell.setCellStyle(csTabelleNormal);
			cell.setCellValue("");
		}
	}

	private void titelEintragen() {
		Row row;
		Cell cell;

		// Titel "Pfeifliste"
		row = sheet1.createRow(0);
		row.setHeightInPoints(26);
		cell = row.createCell(0);
		cell.setCellStyle(csTitel);
		cell.setCellValue("Pfeifliste");
		sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

		// Tabellenüberschriften
		row = sheet1.createRow(aktReihe++);
		row.setHeightInPoints(13);
		// Uhrzeit
		cell = row.createCell(0);
		cell.setCellStyle(csTabelleFett);
		cell.setCellValue("Uhrzeit");
		// Sportart
		cell = row.createCell(1);
		cell.setCellStyle(csTabelleFett);
		cell.setCellValue("Sportart");
		// Anpfiff
		cell = row.createCell(2);
		cell.setCellStyle(csTabelleFett);
		cell.setCellValue("Anpfiff");
		// Abpfiff
		cell = row.createCell(3);
		cell.setCellStyle(csTabelleFett);
		cell.setCellValue("Abpfiff");
	}

	// cellstyles setzen
	private void setStyles() {
		csTitel = wb.createCellStyle();
		csUntertitel = wb.createCellStyle();
		csTabelleFett = wb.createCellStyle();
		csTabelleNormal = wb.createCellStyle();

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
		Font fTabelleFett = wb.createFont();
		fTabelleFett.setFontHeightInPoints((short) 10); // größe 11pt
		fTabelleFett.setColor(Font.COLOR_NORMAL); // schwarz
		fTabelleFett.setBoldweight(Font.BOLDWEIGHT_BOLD); // fett
		// CellStyle für die Tabellenüberschriften
		csTabelleFett.setFont(fTabelleFett); // Schriftart
		csTabelleFett.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // Mittige Anordnung
		csTabelleFett.setAlignment(CellStyle.ALIGN_CENTER);
		csTabelleFett.setBorderBottom(CellStyle.BORDER_THIN); // Umrandung unten einschalten
		csTabelleFett.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung unten in schwarz
		csTabelleFett.setBorderLeft(CellStyle.BORDER_THIN); // Umrandung links einschalten
		csTabelleFett.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung links in schwarz
		csTabelleFett.setBorderTop(CellStyle.BORDER_THIN); // Umrandung oben einschalten
		csTabelleFett.setTopBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung oben in schwarz
		csTabelleFett.setBorderRight(CellStyle.BORDER_THIN); // Umrandung rechts einschalten
		csTabelleFett.setRightBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung rechts in schwarz
		csTabelleFett.setDataFormat(dataFormat.getFormat("text")); // Datenformat Text

		// Schriftart für Spiele, Schiris, Zeiten etc.
		Font fTabelleNormal = wb.createFont();
		fTabelleNormal.setFontHeightInPoints((short) 10); // Schriftgröße auf 10pt setzen
		fTabelleNormal.setColor(Font.COLOR_NORMAL); // Schriftfarbe schwarz
		fTabelleNormal.setBoldweight(Font.BOLDWEIGHT_NORMAL);
		// CellStyle für Spiele, Schiris, Zeiten etc.
		csTabelleNormal.cloneStyleFrom(csTabelleFett);
		csTabelleNormal.setFont(fTabelleNormal); // Schriftart
	}

}
