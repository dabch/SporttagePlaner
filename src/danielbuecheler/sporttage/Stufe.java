package danielbuecheler.sporttage;

public class Stufe {
	private String stufeKurz;
	private String stufeLang;
	
	public Stufe(String stufe) throws IllegalArgumentException {
		switch(stufe.toLowerCase()) {
		case "us":
		case "unterstufe":
			stufeKurz = "US";
			stufeLang = "Unterstufe";
			break;
		case "ms":
		case "mittelstufe":
			stufeKurz = "MS";
			stufeLang = "Mittelstufe";
			break;
		case "os":
		case "oberstufe":
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
