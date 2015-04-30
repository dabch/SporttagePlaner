package danielbuecheler.sporttage;

import java.io.File;
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
	
	static File dirMannschaftslisten;
	static File dirAusgegebeneKlassenlisten;
	static File dirSpielplaene;
	static File dirKontrollisten;
	static File dirPfeiflisten;
	
	public static void main(String[] args) {
		System.out.println("SporttagePlaner Beta by Daniel Bücheler");
		SimpleDateFormat sdf = new SimpleDateFormat("EE, dd. MMMM yyyy, HH:mm:ss zzz"); // Datum auf Deutsch in der richtigen Reihenfolge (z.B. Fr, 31. Dezember 2000, 19:48:12 MEZ)
		System.out.println(sdf.format(new Date()));
		System.out.println();

		String dirRoot = "Sporttage Planung";
		
		// Ordnerstruktur erstellen
		dirMannschaftslisten = new File(dirRoot + "/Mannschaftslisten zum Einlesen/");
		if(!dirMannschaftslisten.canWrite()) {
			System.out.println("Mannschaftslisten-Ordner nicht vorhanden. Wird erstellt.");
			if(!dirMannschaftslisten.mkdirs()) {
				System.out.println("Mannschaftslisten-Ordner konnte nicht erstellt werden. Bitte von Hand erstellen, ohne kann das Programm nicht starten.");
				System.exit(2);
			}
		}
		dirAusgegebeneKlassenlisten = new File(dirRoot + "/Ausgegebene Klassenlisten/");
        if(!dirAusgegebeneKlassenlisten.canWrite()) {
            System.out.println("Klassenlisten-Ordner nicht vorhanden. Wird erstellt.");
            if(!dirAusgegebeneKlassenlisten.mkdirs()) {
                System.out.println("Klassenlisten-Ordner konnte nicht erstellt werden. Bitte von Hand erstellen, ohne kann das Programm nicht starten.");
                System.exit(2);
            }
        }
		dirKontrollisten = new File(dirRoot + "/Kontrollisten/");
		if(!dirKontrollisten.canWrite()) {
			System.out.println("Kontrollisten-Ordner nicht vorhanden. Wird erstellt.");
			if(!dirKontrollisten.mkdirs()) {
				System.out.println("Kontrollisten-Ordner konnte nicht erstellt werden. Bitte von Hand erstellen, ohne kann das Programm nicht starten.");
				System.exit(2);
			}
		}
		dirSpielplaene = new File(dirRoot + "/Spielpläne/");
		if(!dirSpielplaene.canWrite()) {
			System.out.println("Spielplan-Ordner nicht vorhanden. Wird erstellt.");
			if(!dirSpielplaene.mkdirs()) {
				System.out.println("Spielplan-Ordner konnte nicht erstellt werden. Bitte von Hand erstellen, ohne kann das Programm nicht starten.");
				System.exit(2);
			}
		}
		dirPfeiflisten = new File(dirRoot + "/Pfeiflisten/");
		if(!dirPfeiflisten.canWrite()) {
			System.out.println("Pfeiflisten-Ordner nicht vorhanden. Wird erstellt.");
			if(!dirPfeiflisten.mkdirs()) {
				System.out.println("Pfeiflisten-Ordner konnte nicht erstellt werden. Bitte von Hand erstellen, ohne kann das Programm nicht starten.");
				System.exit(2);
			}
		}
		
		System.out.println();
		
		// Properties aus Datei lesen oder erstellen
		properties = new Properties();
		FileReader reader = null;
		try {
			reader = new FileReader(dirRoot + "/sporttageplaner.properties");
			properties.load(reader);
		} catch (FileNotFoundException e) {
			System.out.println("sporttageplaner.properties nicht gefunden");
			properties.setProperty("database_ip_address", "127.0.0.0");
			properties.setProperty("database_username", "root");
			properties.setProperty("database_password", "");
			properties.setProperty("database_name", "sporttage");
			try {
				FileWriter writer = new FileWriter(dirRoot + "/sporttageplaner.properties");
				properties.store(writer, "Automatisch erstellte Properties. Bitte anpassen!");
				System.out.printf("Properties-Datei wurde im Ordner \"%s\" erstellt. Bitte anpassen und dann das Programm nochmal starten.%n", dirRoot);
				writer.close();
				System.exit(0);
			} catch (IOException e1) { 
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
		String tag = "MO"; // True = Montag, False = Dienstag
		
		endlosschleife: // Zum späteren Ausstieg aus der Endlosschleife
		while(true) { // Endlosschleife
			System.out.print("> "); // Hier soll der User etwas eingeben!
			String input = scn.nextLine(); // Befehl einlesen
			
			// Eingegebenen String in Befehln Argumente zerlegen
			String[] inputArray = input.split("\\s+"); // bis zu sechs Argumente
			
			switch (inputArray[0].toLowerCase()) { // je nach Befehl etwas anderes tun
			case "einlesen": // Einlesen aus einer xls-Datei
				if(inputArray[1] == null || inputArray[1].isEmpty()) { // Check, ob Dateiname angegeben wurde
					System.out.println("Bitte den Dateinamen angeben!");
					break;
				}
				System.out.println("Einlesen von " + inputArray[0]);
				Einleser einleser = null;
				try {
					einleser = new Einleser(inputArray[1]); // Einleser erstellen
					einleser.readAll(); // alle Sportarten einlesen
				} catch (FileNotFoundException e) {
					System.out.printf("FEHLER: Datei wurde nicht gefunden. Mannschaftslisten müssen sich im Ordner %s/Mannschaftslisten befinden%n", dirRoot);
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
			case "setze":
			case "set":
				if((inputArray[1] == null || inputArray[1].isEmpty()) || (inputArray[2] == null || inputArray[2].isEmpty()) || (inputArray[3] == null || inputArray[3].isEmpty())) { // Check, ob Dateiname angegeben wurde
					System.out.println("Mindestens eine Angabe fehlt!");
					break;
				}
				// Stufe setzen
				stufe = new Stufe(inputArray[1]);
				// Sportart setzen
				sportart = new Sportart(inputArray[2]);
				// Tag setzen
				switch(inputArray[3].toLowerCase()) {
					case "montag":
					case "mo":
						tag = "MO";
						break;
					case "di":
					case "dienstag":
						tag = "DI";
						break;
					default:
						System.out.println("FEHLER: Unbekannter Tag (nur Montag oder Dienstag)");
				}
				 // fällt durch, damit die Info noch angezeigt wird
			case "info": // einfach nur kurz die umgebungsvariable anzeigen lassen
				System.out.printf("Stufe: %s%nSportart: %s%nTag: %s%n", stufe.getStufeKurz(), sportart.getSportartKurz(), tag);
				break;
			case "block": // xx_xx_xx_zeiten-Tabelle erstellen
			case "blockzeiten":
			case "zeiten":
				if((inputArray[1] == null || inputArray[1].isEmpty()) || (inputArray[2] == null || inputArray[2].isEmpty()) || (inputArray[3] == null || inputArray[3].isEmpty())) { // Check, ob Dateiname angegeben wurde
					System.out.println("Mindestens eine Angabe fehlt!");
					break;
				}
				beginnZeit = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("HH:mm"); // DateFormat für die Eingabe
				try {
					beginnZeit.setTime(df.parse(inputArray[1])); // Date-Objekt aus Eingabe erstellen und als Zeit für beginnZeit setzen
					spieldauer = Integer.parseInt(inputArray[2])	; // Die Pausendauer und Spieldauer setzen
					pausendauer = Integer.parseInt(inputArray[3]);
				} catch (ParseException e2) {
					System.out.println("FEHLER: Zeit im falschen Format angegeben!");  // Wenn der Nutzer nicht "HH:MM" eingegeben hat
				} catch (NumberFormatException e) {
					System.out.println("FEHLER: Spiel- oder Pausendauer bitte in Minuten als einfache Zahlen angeben!");
				}
				ZeitMaker pm;
				try {
					pm = new ZeitMaker(sportart, stufe, tag);
					pm.addZeiten(beginnZeit, spieldauer, pausendauer);
				} catch (SQLException e2) {
					System.out.println("FEHLER: Datenbankfehler");
				}
				System.out.printf("Zeiten erstellt: %s Uhr, Spieldauer %d min, Pausendauer %d min\n", df.format(beginnZeit.getTime()), spieldauer, pausendauer);
				break;
			case "spielplanerstellen": // Spielplan erstellen für bis zu sechs Mannschaften, drei Kommandos möglich, deshalb durchfallen
			case "plane":
			case "planen":
				System.out.println("Fehler: Momentan kann nicht geplant werden, bitte die Spiele �ber die Webseite eingeben");
//				int feld = 0; // Feld hier deklarieren, damit es auch außerhalb des trys gilt
//				try {
//					feld = Integer.parseInt(inputArray[0]);
//				} catch (NumberFormatException e) {
//					System.out.println("FEHLER: Bitte Feld angeben!");
//					continue;
//				}
//				if(!kannPlanungBeginnen(sportart, stufe, beginnZeit, spieldauer, pausendauer)) {
//					System.out.println("Bitte erst Sportart, Stufe und Zeit festlegen!");
//					break;
//				}
//				try {
//					Planer spielplaner = new Planer(sportart, stufe); // SpielplanMaker erstellen
//					for(int i = 1; i < inputArray.length; i++) { // Teams hinzufügen... (dabei den ersten String in argumente überspringen, da er das Feld angibt)
//						if(inputArray[i] == null || inputArray[i].isEmpty()) // ... natürlich nur wenn ein Team angegeben wurde
//							continue;
//						spielplaner.addMannschaft(inputArray[i]);
//					}
//					spielplaner.plane(feld, tag); // Eigentliche Planung starten, erstellt Tabelle wenn nötig
//					System.out.println("Spielplan erstellt und hochgeladen");
//				} catch (SQLException e) {
//					e.printStackTrace();
//					System.out.println("FEHLER: Datenbankfehler");
//					System.out.println("Kein Spielplan erstellt");
//				} catch (IllegalArgumentException e) {
//					System.out.println("FEHLER: Minimal vier gültige Mannschaften angeben!"); // Bei weniger als vier Mannschaften Fehler ausgeben
//					System.out.println("Kein Spielplan erstellt");
//				} catch (TableNotExistentException e) {
//					System.out.println("FEHLER: Teams-Table in der DB existiert nicht!");
//				}
				break;
			case "ausgeben": // Spielplan in Excel-Tabelle schreiben
			case "ausgabe":
				if(!kannPlanungBeginnen(sportart, stufe, beginnZeit, spieldauer, pausendauer)) {
					System.out.println("Bitte erst Sportart und Stufe festlegen!");
					break;
				}
				try {
                    System.out.printf("Schreibe Spielplan in \"Plan_%s_%s.xls\"%n", stufe.getStufeKurz(), sportart.getSportartKurz());
					@SuppressWarnings("unused")
					SpielplanWriter sw = new SpielplanWriter(stufe, sportart);
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					System.out.println("FEHLER: " + e.getMessage());
				}
				break;
			case "kontrolliste": // KL in Excel-Tabelle schreiben
				try {
					// Montag
				    System.out.printf("Erstelle Kontrolliste in \"Kontrolliste_%s_%s_%s.xls\"%n", stufe.getStufeKurz(), sportart.getSportartKurz(), "MO");
					@SuppressWarnings("unused")
					KontrollistenWriter checklistmaker = new KontrollistenWriter(sportart, stufe, "MO");
					// Dienstag
                    System.out.printf("Erstelle Kontrolliste in \"Kontrolliste_%s_%s_%s.xls\"%n", stufe.getStufeKurz(), sportart.getSportartKurz(), "DI");
					checklistmaker = new KontrollistenWriter(sportart, stufe, "DI");
				} catch (SQLException e) {
					System.out.println("FEHLER: Datenbankfehler");
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					System.out.println("FEHLER: Datei nicht gefunden");
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
				break;
			case "klassenliste":
			    String klasse = inputArray[1];
			    try {
                    new KlassenlistenMaker(klasse);
                } catch (IllegalArgumentException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
			    break;
			case "pfeifliste":
				try {
					Stufe stufe1 = new Stufe(inputArray[1].trim());
					Stufe stufe2 = new Stufe(inputArray[2].trim());
					Stufe stufe3 = inputArray[3]== null || inputArray[3].trim().isEmpty() ? null : new Stufe(inputArray[3]); // Null übergeben wenn keine dritte Stufe angegeben wurde
					PfeiflistenMaker pl = new PfeiflistenMaker(inputArray[0]);
					pl.erstellePfeifliste(tag, stufe1, stufe2, stufe3);
					pl.close();
				} catch (SQLException e) {
					System.out.println("FEHLER: Datenbankfehler");
				} catch (FileNotFoundException e) {
					System.out.println("FEHLER: Datei nicht gefunden / konnte nicht erstellt werden.");
				} catch (IOException e) {
					System.out.println("FEHLER: Konnte keine Pfeifliste erstellen");
				}
				break;
			case "help": // Hilfe anzeigen, auch hier durchfallen FINAL: bei mehr möglichen Befehlen Hilfe anpassen
			case "hilfe":
			case "h":
				System.out.println("Mögliche Kommandos: ([...]: Pflichtargument; <...>: Optionales Argument)");
				System.out.println(" einlesen [Dateiname] - Liest Mannschaften von der Excel-Tabelle ein und trägt sie in die Datenbank ein");
				System.out.println(" setze [Stufe] [Sportart] [Tag] - Legt fest, mit welcher Stufe, Sportart und Tag gearbeitet wird (als Tag nur \"Mo\" oder \"Di\"");
				System.out.println(" info - zeigt gesetzte Stufe, Sportart und Tag an");
				System.out.println(" blockzeiten [Startzeit als \"HH:MM\"] [Spieldauer in min] [Pausendauer in min] - Erstellt die Zeiten für den Spielplan (überschreibt alte Zeiten!)");
				System.out.println(" planen [Feld] [team1] [team2] [team3] [team4] <team5> <team6> - Erstellt einen Spielplan für vier bis sechs Teams in einer Gruppe");
				System.out.println(" ausgeben - Schreibt den Spielplan in eine Excel-Tabelle, der Dateiname wird automatisch generiert");
				System.out.println(" kontrolliste - Erstellt eine Kontrolliste und schreibt sie in eine Excel-Tabelle, der Dateiname wird automatisch generiert");
				System.out.println(" pfeifliste [Dateiname] [VB-Stufe] [BM- oder FB-Stufe] <BB-Stufe> - Erstellt eine Pfeifliste und schreibt sie in eine Excel-Tabelle");
				System.out.println(" h - Diese Hilfe anzeigen (auch help oder hilfe)");
				System.out.println(" exit - Programm beenden");
				System.out.println();
				System.out.println(" Bevor ein Spielplan erstellt werden kann müssen Sportart, Stufe und Tag gesetzt werden!");
				System.out.println();
				break;
			case "exit": // Programm komplett beenden
				break endlosschleife;
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
