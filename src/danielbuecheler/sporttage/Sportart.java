package danielbuecheler.sporttage;

public class Sportart {
	private String type;
	private String typeLang;
	
	public Sportart(String typ) throws IllegalArgumentException {
		switch(typ) {
		case "FB":
		case "Fussball":
		case "Fußball":
			type = "FB";
			typeLang = "Fussball";
			break;
		case "BB":
		case "Basketball":
			break;
		case "VB":
		case "Volleyball":
			type = "VB";
			typeLang = "Volleyball"; 
			break;
		case "ST":
		case "Staffellauf":
			type = "ST";
			typeLang = "Staffellauf";
			break;
		case "TT":
		case "Tischtennis":
			type = "TT";
			typeLang = "Tischtennis";
			break;
		case "BM":
		case "Badminton":
			type = "BM";
			typeLang = "Badminton";
		default:
			throw new IllegalArgumentException("Keine gültige Sportart angegeben!");
		}
	}

	public String getSportartKurz() {
		return type;
	}

	public String getSportartLang() {
		return typeLang;
	}
	
	
}
