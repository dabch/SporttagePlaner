package danielbuecheler.sporttage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;


public class SpielplanerApp {

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		System.out.println("SporttagePlaner Beta by Daniel Bücheler");
		System.out.println();
		System.out.println("Was möchten Sie tun? (h, help oder hilfe für Hilfe)");
		schleife:
		while(true) {
			System.out.print("> ");
			String input = scn.nextLine();
			System.out.println(input);
			int[] leerzeichen = new int[7];
			for(int i = 0; i < leerzeichen.length; i++) {
				if(i  == 0) {
					leerzeichen[0] = input.indexOf(" ");
					continue;
				}
				leerzeichen[i] = input.indexOf(" ", leerzeichen[i-1] + 1);
			}
			
			String cmd = leerzeichen[0] >= 0 ? input.substring(0, leerzeichen[0]) : input;
			String[] argumente = new String[6];
			for(int i = 0; i < argumente.length; i++) {
				if(leerzeichen[i] < 0){
					break;
				}
				argumente[i] = leerzeichen[i+1] >= 0 ? input.substring(leerzeichen[i] + 1, leerzeichen[i+1]) : input.substring(leerzeichen[i] + 1, input.length());
			}
			
			switch (cmd.toLowerCase()) {
			case "einlesen":
				if(argumente[0] == null) {
					System.out.println("Bitte den Dateinamen angeben!");
					break;
				}
				System.out.println("Einlesen von " + argumente[0]);
				Einleser einleser = null;
				try {
					einleser = new Einleser(argumente[0]);
					einleser.readAll();
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
			case "spielplanerstellen":
			case "plane":
			case "planen":
				try {
					Spielplanmaker sm = new Spielplanmaker();
					for(String mannschaft : argumente) {
						if(mannschaft == null || mannschaft.isEmpty())
							continue;
						sm.addMannschaft(mannschaft);
					}
					sm.plane(13, 00, 5, 2);
					System.out.println("Spielplan erstellt und hochgeladen");
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					System.out.println("FEHLER: Minimal vier gültige Mannschaften angeben!");
					System.out.println("Kein Spielplan erstellt");
				}
				break;
			case "help":
			case "hilfe":
			case "h":
				System.out.println("Mögliche Kommandos: ([..]: Pflichtargument; <...>: Optionales Argument)");
				System.out.println(" einlesen [Dateiname] - Liest Mannschaften von der Excel-Tabelle ein und trägt sie in die Datenbank ein");
				System.out.println(" planen [team1] [team2] [team3] [team4] <team5> <team6> - erstellt einen Spielplan für vier bis sechs Teams in einer Gruppe");
				System.out.println(" h - Diese Hilfe anzeigen (auch help oder hilfe)");
				System.out.println(" exit - Programm beenden");
				break;
			case "exit":
				break schleife;
			default:
				System.out.println("Befehl nicht erkannt");
				break;
			}
			System.out.println();
		}
		System.out.println("Programm wird beendet.");
		scn.close();
	}

}
