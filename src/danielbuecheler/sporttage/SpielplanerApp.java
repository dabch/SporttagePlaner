package danielbuecheler.sporttage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;


public class SpielplanerApp {
	
	static Properties properties;
	
	public static void main(String[] args) {
		System.out.println("SporttagePlaner Beta by Daniel Bücheler");
		SimpleDateFormat sdf = new SimpleDateFormat("EE, dd. MMMM yyyy, HH:mm:ss zzz"); // Datum auf Deutsch in der richtigen Reihenfolge (z.B. Fr, 31. Dezember 2000, 19:48:12 MEZ)
		System.out.println(sdf.format(new Date()));
		System.out.println();
		properties = new Properties();
		FileReader reader = null;
		try {
			reader = new FileReader("sporttageplaner.properties");
			properties.load(reader);
		} catch (FileNotFoundException e) {
			System.out.println("sporttageplaner.properties nicht gefunden");
			properties.setProperty("database_ip_address", "127.0.0.0");
			properties.setProperty("database_username", "root");
			properties.setProperty("database_password", "");
			properties.setProperty("database_name", "sporttage");
			try {
				FileWriter writer = new FileWriter("sporttageplaner.properties");
				properties.store(writer, "Automatisch erstellte Properties. Bitte anpassen!");
				System.out.println("Properties-Datei wurde erstellt. Bitte anpassen und dann das Programm nochmal starten.");
				writer.close();
				System.exit(0);
			} catch (IOException e1) { 
				e1.printStackTrace();
				System.exit(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
		
		//#####################################
		Scanner scn = new Scanner(System.in);
		System.out.println("Was möchten Sie tun? (h, help oder hilfe für Hilfe)");
		
		// FINAL Variablen nicht initialisieren
		Sportart sportart = new Sportart("BM"); // Sportart für Spielpläne
		Stufe stufe = new Stufe("MS"); // Stufe für Spielpläne
		Calendar beginnZeit = Calendar.getInstance();; // Calendar-Objekt für die Beginnzeit
		beginnZeit.set(2015, 5, 12, 11, 7);
		int spieldauer = 9; // Dauer eines Spiels
		int pausendauer = 2;
		
		Spielplanmaker sm;
		
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
				sportart = new Sportart(argumente[0]);
				System.out.printf("Sportart auf %s festgelegt\n", sportart.getSportartLang());
				break;
			case "stufe":
				stufe = new Stufe(argumente[0]);
				System.out.printf("Stufe auf %s festgelegt\n", stufe.getStufeLang());
				break;
			case "zeit":
			case "zeitsetzen":
			case "setzezeit":
				beginnZeit = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("HH:mm"); // DateFormat für die Eingabe
				try {
					beginnZeit.setTime(df.parse(argumente[0])); // Date-Objekt aus Eingabe erstellen und als Zeit für beginnZeit setzen
					spieldauer = Integer.parseInt(argumente[1])	; // Die Pausendauer und Spieldauer setzen
					pausendauer = Integer.parseInt(argumente[2]);
				} catch (ParseException e2) {
					System.out.println("FEHLER: Zeit im falschen Format angegeben!");  // Wenn der Nutzer nicht "HH:MM" eingegeben hat
				} catch (NumberFormatException e) {
					System.out.println("FEHLER: Spiel- oder Pausendauer bitte in Minuten als einfache Zahlen angeben!");
				}
				System.out.printf("Zeit gesetzt auf: %s Uhr\n", df.format(beginnZeit.getTime()));
				System.out.printf("Spieldauer gesetzt auf: %d min\n", spieldauer);
				System.out.printf("Pausendauer gesetzt auf: %d min\n", pausendauer);
				break;
			case "spielplanerstellen": // Spielplan erstellen für bis zu sechs Mannschaften, drei Kommandos möglich, deshalb durchfallen
			case "plane":
			case "planen":
				int feld = 0; // Feld hier deklarieren, damit es auch außerhalb des trys gilt
				try {
					feld = Integer.parseInt(argumente[0]);
				} catch (NumberFormatException e) {
					System.out.println("FEHLER: Bitte Feld angeben!");
					continue;
				}
				if(!kannPlanungBeginnen(sportart, stufe, beginnZeit, spieldauer, pausendauer)) {
					System.out.println("Bitte erst Sportart, Stufe und Zeit festlegen!");
					break;
				}
				try {
					sm = new Spielplanmaker(sportart, stufe); // SpielplanMaker erstellen
					for(int i = 1; i < argumente.length; i++) { // Teams hinzufügen... (dabei den ersten String in argumente überspringen, da er das Feld angibt)
						if(argumente[i] == null || argumente[i].isEmpty()) // ... natürlich nur wenn ein Team angegeben wurde
							continue;
						sm.addMannschaft(argumente[i]);
					}
					sm.plane(feld, beginnZeit.get(Calendar.HOUR_OF_DAY), beginnZeit.get(Calendar.MINUTE), spieldauer, pausendauer); // Eigentliche Planung starten, erstellt Tabelle wenn nötig
					System.out.println("Spielplan erstellt und hochgeladen");
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("FEHLER: Datenbankfehler");
					System.out.println("Kein Spielplan erstellt");
				} catch (IllegalArgumentException e) {
					System.out.println("FEHLER: Minimal vier gültige Mannschaften angeben!"); // Bei weniger als vier Mannschaften Fehler ausgeben
					System.out.println("Kein Spielplan erstellt");
				} catch (TableNotExistentException e) {
					System.out.println("FEHLER: Teams-Table in der DB existiert nicht!");
				}
				break;
			case "ausgeben":
			case "ausgabe":
				if(!kannPlanungBeginnen(sportart, stufe, beginnZeit, spieldauer, pausendauer)) {
					System.out.println("Bitte erst Sportart und Stufe festlegen!");
					break;
				}
				try {
					SpielplanWriter sw = new SpielplanWriter(argumente[0], sportart, stufe);
					sw.write();
					sw.close();
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e) {
					System.out.println("FEHLER: " + e.getMessage());
				}
				break;
			case "kontrolliste":
				try {
					KontrollistenWriter checklistmaker = new KontrollistenWriter(argumente[0], sportart, stufe);
					System.out.println("Erstelle Kontrolliste");
					checklistmaker.spielerEintragen();
					checklistmaker.close();
					System.out.println("Eintragen in " + argumente[0]);
				} catch (SQLException e) {
					System.out.println("FEHLER: Datenbankfehler");
					
				} catch (FileNotFoundException e) {
					System.out.println("FEHLER: Datei nicht gefunden");
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				break;
			case "help": // Hilfe anzeigen, auch hier durchfallen FINAL: bei mehr möglichen Befehlen Hilfe anpassen
			case "hilfe":
			case "h":
				System.out.println("Mögliche Kommandos: ([...]: Pflichtargument; <...>: Optionales Argument)");
				System.out.println(" einlesen [Dateiname] - Liest Mannschaften von der Excel-Tabelle ein und trägt sie in die Datenbank ein");
				System.out.println(" sportart [Sportart (z.B. BB)] - Legt die Sportart fest, für die in den folgenden Schritten Pläne erstellt oder ausgegeben werden");
				System.out.println(" stufe [Stufe (US / MS / OS)] - Legt die Stufe fest, für die in den folgenden Schritten Pläne erstellt oder ausgegeben werden");
				System.out.println(" zeit [HH:MM] [Spieldauer in min] [Pausendauer in min] - Setzt die Zeit, zu der ein Spielplan beginnt und die Dauer der Spiele und der Pause");
				System.out.println(" planen [Feld] [team1] [team2] [team3] [team4] <team5> <team6> - Erstellt einen Spielplan für vier bis sechs Teams in einer Gruppe");
				System.out.println(" ausgeben [Dateiname] - Schreibt den Spielplan in eine Excel-Tabelle");
				System.out.println(" kontrolliste [Dateiname] - Erstellt eine Kontrolliste und schreibt sie in eine Excel-Tabelle");
				System.out.println(" h - Diese Hilfe anzeigen (auch help oder hilfe)");
				System.out.println(" exit - Programm beenden");
				System.out.println();
				System.out.println(" Bevor ein Spielplan erstellt werden kann müssen Sportart, Stufe und Zeit gesetzt werden!");
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
	
	private static boolean kannPlanungBeginnen(Sportart sportart, Stufe stufe, Calendar zeit, int spieldauer, int pausendauer) { // legt fest, ob Planung beginnen kann
		return (stufe != null && sportart != null && zeit != null && spieldauer != 0 && pausendauer != 0);
	}

}
