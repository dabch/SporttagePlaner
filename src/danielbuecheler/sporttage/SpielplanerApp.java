package danielbuecheler.sporttage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;


public class SpielplanerApp {
	
	static Properties properties;
	
	public static void main(String[] args) {
		System.out.println("SporttagePlaner Beta by Daniel Bücheler");
		System.out.println(new Date());
		System.out.println();
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
		System.out.println("Was möchten Sie tun? (h, help oder hilfe für Hilfe)");
		// FIXME Variablen nicht initialisieren
		String sportart = "BM"; // Sportart für Spielpläne
		String stufe = "MS"; // Stufe für Spielpläne
		schleife: // Zum späteren Ausstieg aus der Endlosschleife
		while(true) { // Endlosschleife
			System.out.print("> "); // Hier soll der User etwas eingeben!
			String input = scn.nextLine(); // Befehl einlesen
			
			// Eingegebenen String in Befehln Argumente zerlegen
			int[] leerzeichen = new int[7]; // Positionen der Leerzeichen
			for(int i = 0; i < leerzeichen.length; i++) {
				if(i  == 0) {
					leerzeichen[0] = input.indexOf(" ");
					continue;
				}
				leerzeichen[i] = input.indexOf(" ", leerzeichen[i-1] + 1);
			}
			
			String cmd = leerzeichen[0] >= 0 ? input.substring(0, leerzeichen[0]) : input; // Erstes Wort
			String[] argumente = new String[leerzeichen.length - 1]; // bis zu sechs Argumente
			for(int i = 0; i < argumente.length; i++) {
				if(leerzeichen[i] < 0){
					break;
				}
				argumente[i] = leerzeichen[i+1] >= 0 ? input.substring(leerzeichen[i] + 1, leerzeichen[i+1]) : input.substring(leerzeichen[i] + 1, input.length());
			}
			switch (cmd.toLowerCase()) { // je nach Befehl etwas anderes tun
			case "einlesen": // Einlesen aus einer xls-Datei
				if(argumente[0] == null || argumente[0].isEmpty()) { // Check, ob Dateiname angegeben wurde
					System.out.println("Bitte den Dateinamen angeben!");
					break;
				}
				System.out.println("Einlesen von " + argumente[0]);
				Einleser einleser = null;
				try {
					einleser = new Einleser(argumente[0]); // Einleser erstellen
					einleser.readAll(); // alle Sportarten einlesen
				} catch (FileNotFoundException e) {
					System.out.println("FEHLER: Datei wurde nicht gefunden");
					break;
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
				// einleser schließen
				try {
					einleser.close();
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
				break;
			case "sportart":
				sportart = argumente[0];
				System.out.printf("Sportart auf %s festgelegt\n", argumente[0]);
				break;
			case "stufe":
				stufe = argumente[0];
				System.out.printf("Stufe auf %s festgelegt\n", argumente[0]);
				break;
			case "spielplanerstellen": // Spielplan erstellen für bis zu sechs Mannschaften, drei Kommandos möglich, deshalb durchfallen
			case "plane":
			case "planen":
				if(!kannPlanungBeginnen(sportart, stufe)) {
					System.out.println("Bitte erst Sportart und Stufe festlegen!");
					break;
				}
				System.out.println("Es werden noch ein paar Infos benötigt");
				// Uhrzeit für den Start des Blocks eingeben
				System.out.println("Bitte geben Sie die Startzeit ein: (HH:MM)");
				String startzeit = scn.next();
				if(startzeit == null || startzeit.isEmpty()) {
					System.out.println("Keine Uhrzeit eingegeben");
					continue;
				}
				if(!startzeit.contains(":")) {
					System.out.println("Bitte Uhrzeit im folgenden Format eingeben: \"HH:MM\"");
					continue;
				}
				int startzeitStd = Integer.parseInt(startzeit.substring(0, 2)); // Zeit in int Minuten und int Stunden zerlegen
				int startzeitMin = Integer.parseInt(startzeit.substring(3, 5));
				System.out.println("Dauer eines Spiels:");
				int spieldauer = scn.nextInt(); // Spieldauer abfragen
				System.out.println("Dauer der Pause zwischen den Spielen:");
				int pausendauer = scn.nextInt(); // Pausendauer abfragen
				System.out.println("Bitte Feld eingeben:");
				int feld = scn.nextInt(); // Feld abfragen
				try {
					Spielplanmaker sm = new Spielplanmaker(sportart, stufe, feld); // SpielplanMaker erstellen
					for(String mannschaft : argumente) { // Teams hinzufügen...
						if(mannschaft == null || mannschaft.isEmpty()) // ... natürlich nur wenn ein Team angegeben wurde
							continue;
						sm.addMannschaft(mannschaft);
					}
					sm.plane(startzeitStd, startzeitMin, spieldauer, pausendauer); // Eigentliche Planung starten
					System.out.println("Spielplan erstellt und hochgeladen");
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("FEHLER: Datenbankfehler");
					System.out.println("Kein Spielplan erstellt");
				} catch (IllegalArgumentException e) {
					System.out.println("FEHLER: Minimal vier gültige Mannschaften angeben!"); // Bei weniger als vier Mannschaften Fehler ausgeben
					System.out.println("Kein Spielplan erstellt");
				}
				scn.next(); // verhindern, dass das nächste nextLine() einen leeren String zurückliefert
				break;
			case "ausgeben":
			case "ausgabe":
				if(!kannPlanungBeginnen(sportart, stufe)) {
					System.out.println("Bitte erst Sportart und Stufe festlegen!");
					break;
				}
				try {
					SpielplanWriter sw = new SpielplanWriter(argumente[0], sportart, stufe);
					sw.close();
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e) {
					System.out.println("FEHLER: " + e.getMessage());
				}
				break;
			case "help": // Hilfe anzeigen, auch hier durchfallen FINAL: bei mehr möglichen Befehlen Hilfe anpassen
			case "hilfe":
			case "h":
				System.out.println("Mögliche Kommandos: ([...]: Pflichtargument; <...>: Optionales Argument)");
				System.out.println(" einlesen [Dateiname] - Liest Mannschaften von der Excel-Tabelle ein und trägt sie in die Datenbank ein");
				System.out.println(" sportart [Sportart (z.B. BB)] - Legt die Sportart fest, für die in den folgenden Schritten Pläne erstellt oder ausgegeben werden");
				System.out.println(" stufe [Stufe (US / MS / OS)] - Legt die Stufe fest, für die in den folgenden Schritten Pläne erstellt oder ausgegeben werden");
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
		}
		System.out.println();
		System.out.println("Programm wird beendet. Auf Wiedersehen!");
		scn.close();
	}
	
	private static boolean kannPlanungBeginnen(String sportart, String stufe) { // legt fest, ob Planung beginnen kann
		return (stufe != null && sportart != null);
	}

}
