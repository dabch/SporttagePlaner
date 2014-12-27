package danielbuecheler.sporttage;

public class Sportart {
	private String type;
	private String typeLang;
	int anzahlFelder;
	
	public Sportart(String typ) throws IllegalArgumentException {
		switch(typ.toLowerCase()) {
		case "fb":
		case "fussball":
		case "fußball":
			type = "FB";
			typeLang = "Fussball";
			anzahlFelder = 3;
			break;
		case "bb":
		case "basketball":
			type = "BB";
			typeLang = "Basketball";
			anzahlFelder = 3;
			break;
		case "vb":
		case "volleyball":
			type = "VB";
			typeLang = "Volleyball";
			anzahlFelder = 2;
			break;
		case "st":
		case "staffellauf":
			type = "ST";
			typeLang = "Staffellauf";
			anzahlFelder = 1;
			break;
		case "tt":
		case "tischtennis":
			type = "TT";
			typeLang = "Tischtennis";
			anzahlFelder = 4;
			break;
		case "bm":
		case "badminton":
			type = "BM";
			typeLang = "Badminton";
			anzahlFelder = 6;
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
