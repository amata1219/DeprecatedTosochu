package amata1219.tosochu.text;

public class TimeToSeconds {

	public static int toSeconds(String text){
		String[] values = text.split(":");
		return values.length == 1 ? Integer.valueOf(values[0]) : Integer.valueOf(values[0]) * 60 + Integer.valueOf(values[1]);
	}

}
