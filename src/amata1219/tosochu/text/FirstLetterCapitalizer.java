package amata1219.tosochu.text;

public class FirstLetterCapitalizer {

	public static String capitalizeFirstLetter(String text){
		if(text.isEmpty())
			return "";

		return Character.toUpperCase(text.charAt(0)) + text.substring(1).toLowerCase();
	}

}
