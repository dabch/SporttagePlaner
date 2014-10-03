import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;


public class SpielplanerApp {

	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		Einleser e = null;
		try {
			e = new Einleser("7a.xls", "Mittelstufe");
			e.readAll();
		} catch (IOException | SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				e.close();
			} catch (IOException | SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

}
