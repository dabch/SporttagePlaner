package danielbuecheler.sporttage;

public class Stufe {
	private String stufeKurz;
	private String stufeLang;
	
	public Stufe(String stufe) throws IllegalArgumentException {
		switch(stufe) {
		case "US":
		case "Unterstufe":
			stufeKurz = "US";
			stufeLang = "Unterstufe";
		case "MS":
		case "Mittelstufe":
			stufeKurz = "MS";
			stufeLang = "Mittelstufe";
			break;
		case "OS":
		case "Oberstufe":
			stufeKurz = "OS";
			stufeLang = "Oberstufe";
			break;
		default:
			throw new IllegalArgumentException("Keine gültige Stufe angegeben!");
		}
	}

	public String getStufeKurz() {
		return stufeKurz;
	}

	public String getStufeLang() {
		return stufeLang;
	}
}
