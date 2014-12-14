package danielbuecheler.sporttage;

public class Sportart {
	private String type;
	private String typeLang;
	
	public Sportart(String typ) throws IllegalArgumentException {
		switch(typ.toLowerCase()) {
		case "fb":
		case "fussball":
		case "fußball":
			type = "FB";
			typeLang = "Fussball";
			break;
		case "bb":
		case "basketball":
			break;
		case "vb":
		case "volleyball":
			type = "VB";
			typeLang = "Volleyball"; 
			break;
		case "st":
		case "staffellauf":
			type = "ST";
			typeLang = "Staffellauf";
			break;
		case "tt":
		case "tischtennis":
			type = "TT";
			typeLang = "Tischtennis";
			break;
		case "bm":
		case "badminton":
			type = "BM";
			typeLang = "Badminton";
			break;
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
