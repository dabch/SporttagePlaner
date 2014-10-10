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
			int pos1 = input.indexOf(" ");
			int pos2 = input.indexOf(" ", pos1);
			String cmd = pos1 >= 0 ? input.substring(0, pos1) : input;
			String arg0 = pos2 >= 0 ? input.substring(pos1 + 1) : null;
			switch (cmd.toLowerCase()) {
			case "einlesen":
				if(arg0 == null) {
					System.out.println("Bitte den Dateinamen angeben!");
					break;
				}
				System.out.println("Einlesen von " + arg0);
				Einleser einleser = null;
				try {
					einleser = new Einleser(arg0);
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
					Spielplanmaker sm = new Spielplanmaker("9a1", "9a2", "9a3", "9a4", "9a5", "9a6");
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
