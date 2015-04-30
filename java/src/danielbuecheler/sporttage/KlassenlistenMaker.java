package danielbuecheler.sporttage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class KlassenlistenMaker {
    private Connection con; // Datenbankverbindung

    private FileOutputStream fos; // FileOutputStream, auf den die Excel-Tabelle geschrieben wird
    private HSSFWorkbook wb; // Excel-Datei
    private Sheet sheet1; // Tabelle

    private CellStyle csTitel; // CellStyles (Formatierungen) für die Excel-Tabelle
    private CellStyle csUeberschrift;
    private CellStyle csNormal;
    
    private int currentRow = 3; // Zähler, in welcher Reihe wir
    private String table;
    
    public KlassenlistenMaker(String klasse) throws IllegalArgumentException, SQLException, IOException {
        
        // Erkennung der Stufe
        if(klasse.contains("10") || klasse.contains("K1") || klasse.contains("K2"))
            table = "Mannschaften_OS";
        else if(klasse.contains("9") || klasse.contains("8") || klasse.contains("7") || klasse.equals("A1"))
            table = "Mannschaften_MS";
        else if(klasse.contains("7") || klasse.contains("6") || klasse.contains("5"))
            table = "Mannschaften_US";
        else {
            throw new IllegalArgumentException("Diese Klasse kann es nicht geben");
        }
        
        // Connection öffnen
        con = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s",
                SpielplanerApp.properties.getProperty("database_ip_address"),
                SpielplanerApp.properties.getProperty("database_name")),
                SpielplanerApp.properties.getProperty("database_username"),
                SpielplanerApp.properties.getProperty("database_password"));
        
        String filename = SpielplanerApp.dirAusgegebeneKlassenlisten.getCanonicalPath() + "/" + klasse + ".xls";
        
        fos = new FileOutputStream(filename);
        
        wb = new HSSFWorkbook();
        sheet1 = wb.createSheet("Klassenliste");
        
        setStyles();
        
        PreparedStatement getSchueler = con.prepareStatement(
                "SELECT Vorname, Name, BM, FB, BB, VB, TT, ST, FT "
                + "FROM " +  table + " "
                + "WHERE Klasse = ?"
                + " ORDER BY Name, Vorname");
        
        System.out.println("Erstelle Klassenliste für " + klasse + " (" + table.substring(13) + ")");  // Klasse und Stufe ausgeben
        
        getSchueler.setString(1, klasse);
        
        ResultSet schueler = getSchueler.executeQuery();
        
        // Fehlermeldung und Abbruch wenn leer
        if(!schueler.next()) {
            System.out.println("FEHLER: Keine Schüler dieser Klasse in der Datenbank!");
            return;
        }
        schueler.previous();
        
        Row row;
        Cell cell;
        
        String[] bezeichnungen = {"Name", "Vorname", "BM", "FB", "BB", "VB", "TT", "ST", "FT"};
        
        // Titel
        row = sheet1.createRow(0); // 3 abstand
        row.setHeightInPoints(30); // Höhe 30pt
        cell = row.createCell(0);
        cell.setCellStyle(csTitel);
        cell.setCellValue("Klassenliste " + klasse);
        sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
        
        // "Über-Überschrift"
        row = sheet1.createRow(currentRow++);
        row.setHeightInPoints(15);
        cell = row.createCell(3);
        cell.setCellStyle(csUeberschrift);
        cell.setCellValue("Spielt ... in Mannschaft ...");
        sheet1.addMergedRegion(new CellRangeAddress(currentRow - 1, currentRow - 1, 3, 9));
        
        //CellStyles anwenden
        for(int i = 3; i < 9; i++) {
	        cell = row.createCell(i + 1);
	        cell.setCellStyle(csUeberschrift);
        }
        
        row = sheet1.createRow(currentRow++);
        row.setHeightInPoints(15);
        // ueberschriften eintragen
        for(int i = 0; i < bezeichnungen.length; i++) {
            cell = row.createCell(i + 1);
            cell.setCellStyle(csUeberschrift);
            cell.setCellValue(bezeichnungen[i]);
        }        
        
        int nummer = 1; // Nummer des Schülers
        // schueler eintragen        
        while(schueler.next()) {
            // neue Zeile
            row = sheet1.createRow(currentRow++);
            
            // TODO Nummer des Schülers 1......x
            cell = row.createCell(0);
            cell.setCellStyle(csNormal);
            cell.setCellValue(nummer++);
            
            for(int i = 0; i < bezeichnungen.length; i++) {
                cell = row.createCell(i + 1);
                cell.setCellStyle(csNormal);
                cell.setCellValue(schueler.getString(bezeichnungen[i]));
            }
        }
        for(int i = 0; i < bezeichnungen.length + 1; i++) { // plus eins wegen nr-spalte
            sheet1.autoSizeColumn(i);
        }
        
        wb.write(fos);
        fos.flush();
        fos.close();
        con.close();
    }
    
    private void setStyles() {
        csNormal = wb.createCellStyle();
        csUeberschrift = wb.createCellStyle();
        csTitel = wb.createCellStyle();
        
        DataFormat dataFormat = wb.createDataFormat();
        
        // Schriftart Schüler
        Font fNormal = wb.createFont();
        fNormal.setFontHeightInPoints((short) 10); // Schriftgröße auf 10pt setzen
        fNormal.setColor(Font.COLOR_NORMAL); // Schriftfarbe schwarz
        fNormal.setBoldweight(Font.BOLDWEIGHT_NORMAL);
        // CellStyle für Schüler
        csNormal.setFont(fNormal); // Schriftart
        csNormal.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // Mittige Anordnung
        csNormal.setBorderBottom(CellStyle.BORDER_THIN); // Umrandung unten einschalten
        csNormal.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung unten in schwarz
        csNormal.setBorderLeft(CellStyle.BORDER_THIN); // Umrandung links einschalten
        csNormal.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung links in schwarz
        csNormal.setBorderTop(CellStyle.BORDER_THIN); // Umrandung oben einschalten
        csNormal.setTopBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung oben in schwarz
        csNormal.setBorderRight(CellStyle.BORDER_THIN); // Umrandung rechts einschalten
        csNormal.setRightBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung rechts in schwarz
        csNormal.setDataFormat(dataFormat.getFormat("text")); // Datenformat Text
        
        // Fett
        Font fUeberschrift = wb.createFont();
        fUeberschrift.setFontHeightInPoints((short) 11);
        fUeberschrift.setColor(Font.COLOR_NORMAL);
        fUeberschrift.setBoldweight(Font.BOLDWEIGHT_BOLD);
        csUeberschrift.setFont(fUeberschrift);
        csUeberschrift.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // Mittige Anordnung
        csUeberschrift.setBorderBottom(CellStyle.BORDER_THIN); // Umrandung unten einschalten
        csUeberschrift.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung unten in schwarz
        csUeberschrift.setBorderLeft(CellStyle.BORDER_THIN); // Umrandung links einschalten
        csUeberschrift.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung links in schwarz
        csUeberschrift.setBorderTop(CellStyle.BORDER_THIN); // Umrandung oben einschalten
        csUeberschrift.setTopBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung oben in schwarz
        csUeberschrift.setBorderRight(CellStyle.BORDER_THIN); // Umrandung rechts einschalten
        csUeberschrift.setRightBorderColor(IndexedColors.BLACK.getIndex()); // Umrandung rechts in schwarz
        csUeberschrift.setDataFormat(dataFormat.getFormat("text")); // Datenformat Text
        
        // Schriftart für Titel
        Font fTitel = wb.createFont();
        fTitel.setFontHeightInPoints((short) 24); // größe 24pt
        fTitel.setColor(Font.COLOR_NORMAL); // schwarz
        fTitel.setBoldweight(Font.BOLDWEIGHT_BOLD); // fett
        // CellStyle für den Titel
        csTitel.setFont(fTitel); // Schrift setzen
        csTitel.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // Mittige Anordnung
        csTitel.setDataFormat(dataFormat.getFormat("text")); // Datenformat Text
    }
}
