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
			String arg1 = pos2 >= 0 ? input.substring(pos1 + 1) : null;
			switch (cmd.toLowerCase()) {
			case "einlesen":
				if(arg1 == null) {
					System.err.println("Bitte den Dateinamen angeben!");
					break;
				}
				System.out.println("Einlesen von " + arg1);
				Einleser e = null;
				try {
					e = new Einleser(arg1);
					e.readAll();
				} catch (IOException | SQLException e1) {
					e1.printStackTrace();
				} finally {
					try {
						e.close();
					} catch (IOException | SQLException e2) {
						e2.printStackTrace();
					}
				}
				break;
			case "help":
			case "hilfe":
			case "h":
				System.out.println("Mögliche Kommandos:");
				System.out.println(" einlesen [Dateiname] - Liest Mannschaften von der Excel-Tabelle ein und trägt sie in die Datenbank ein");
				System.out.println(" h - Diese Hilfe anzeigen (auch help oder hilfe)");
				System.out.println(" exit - Programm beenden");
				break;
			case "exit":
				break schleife;
			default:
				System.out.println("Befehl nicht erkannt");
				break;
			}
		}
		System.out.println("Programm wird beendet.");
		scn.close();
	}

}
