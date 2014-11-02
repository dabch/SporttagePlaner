package danielbuecheler.sporttage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;


public class SpielplanerApp {
	
	static Properties properties;
	
	public static void main(String[] args) {
		properties = new Properties();
		FileReader reader = null;
		try {
			reader = new FileReader("sporttageplaner.properties");
			properties.load(reader);
		} catch (FileNotFoundException e) {
			System.out.println("sporttageplaner.properties nicht gefunden");
			properties.setProperty("database_ip_address", "192.168.2.105");
			properties.setProperty("database_username", "root");
			properties.setProperty("database_password", "");
			properties.setProperty("database_name", "TestFuerSporttage");
			try {
				FileWriter writer = new FileWriter("sporttageplaner.properties");
				properties.store(writer, "Automatisch erstellte Properties. Bitte anpassen!");
				System.out.println("Properties-Datei wurde erstellt. Bitte anpassen und dann das Programm nochmal starten.");
				System.exit(0);
			} catch (IOException e1) { e1.printStackTrace(); System.exit(1); }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//#####################################
		Scanner scn = new Scanner(System.in);
		System.out.println("SporttagePlaner Beta by Daniel Bücheler");
		System.out.println();
		System.out.println("Was möchten Sie tun? (h, help oder hilfe für Hilfe)");
		schleife: // Zum späteren Ausstieg aus der Endlosschleife
		while(true) { // Endlosschleife
			System.out.print("> "); // Hier soll der User etwas eingeben!
			String input = scn.nextLine(); // Befehl einlesen
			
			// Eingegebenen String in Befehl und bis zu sieben Argumente zerlegen
			int[] leerzeichen = new int[7]; // Positionen der sieben Leerzeichen
			for(int i = 0; i < leerzeichen.length; i++) {
				if(i  == 0) {
					leerzeichen[0] = input.indexOf(" ");
					continue;
				}
				leerzeichen[i] = input.indexOf(" ", leerzeichen[i-1] + 1);
			}
			
			String cmd = leerzeichen[0] >= 0 ? input.substring(0, leerzeichen[0]) : input; // Erstes Wort
			String[] argumente = new String[6]; // bis zu sechs Argumente
			for(int i = 0; i < argumente.length; i++) {
				if(leerzeichen[i] < 0){
					break;
				}
				argumente[i] = leerzeichen[i+1] >= 0 ? input.substring(leerzeichen[i] + 1, leerzeichen[i+1]) : input.substring(leerzeichen[i] + 1, input.length());
			}
			
			switch (cmd.toLowerCase()) { // je nach Befehl etwas anderes tun
			case "einlesen": // Einlesen aus einer xls-Datei
				if(argumente[0] == null) { // Check, ob Dateiname angegeben wurde
					System.out.println("Bitte den Dateinamen angeben!");
					break;
				}
				System.out.println("Einlesen von " + argumente[0]);
				Einleser einleser = null;
				try {
					einleser = new Einleser(argumente[0]); // Einleser erstellen
					einleser.readAll(); // alle Sportarten einlesen
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						einleser.close();
					} catch (IOException | SQLException e) {
						e.printStackTrace();
					}
				}
				break;
			case "spielplanerstellen": // Spielplan erstellen für bis zu sechs Mannschaften, drei Kommandos möglich, deshalb durchfallen
			case "plane":
			case "planen":
				try {
					Spielplanmaker sm = new Spielplanmaker(); // SpielplanMaker erstellen
					for(String mannschaft : argumente) { // Teams hinzufügen...
						if(mannschaft == null || mannschaft.isEmpty()) // ... natürlich nur wenn ein Team angegeben wurde
							continue;
						sm.addMannschaft(mannschaft);
					}
					sm.plane(13, 00, 5, 2); // Eigentliche Planung starten FIXME: Variable Start- / Endzeit und Spiel- / Pausendauer
					System.out.println("Spielplan erstellt und hochgeladen");
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("Kein Spielplan erstellt");
				} catch (IllegalArgumentException e) {
					System.out.println("FEHLER: Minimal vier gültige Mannschaften angeben!"); // Bei weniger als vier Mannschaften Fehler ausgeben
				}
				break;
			case "ausgeben":
			case "ausgabe":
				try {
					SpielplanWriter sw = new SpielplanWriter(argumente[0]);
					sw.close();
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e) {
					System.out.println("FEHLER: " + e.getMessage());
				}
				break;
			case "help": // Hilfe anzeigen, auch hier durchfallen TODO: bei mehr möglichen Befehlen Hilfe anpassen
			case "hilfe":
			case "h":
				System.out.println("Mögliche Kommandos: ([...]: Pflichtargument; <...>: Optionales Argument)");
				System.out.println(" einlesen [Dateiname] - Liest Mannschaften von der Excel-Tabelle ein und trägt sie in die Datenbank ein");
				System.out.println(" planen [team1] [team2] [team3] [team4] <team5> <team6> - Erstellt einen Spielplan für vier bis sechs Teams in einer Gruppe");
				System.out.println(" ausgeben [tabellenname] - Schreibt den Spielplan in eine Excel-Tabelle");
				System.out.println(" h - Diese Hilfe anzeigen (auch help oder hilfe)");
				System.out.println(" exit - Programm beenden");
				break;
			case "exit": // Programm komplett beenden
				break schleife;
			default: // Bei unbekannten Befehlen Fehler ausgeben
				System.out.println("FEHLER: Befehl nicht erkannt");
				break;
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Programm wird beendet. Auf Wiedersehen!");
		scn.close();
	}

}
