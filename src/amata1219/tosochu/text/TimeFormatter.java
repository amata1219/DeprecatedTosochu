package amata1219.tosochu.text;

public class TimeFormatter {

	public static String format(int time){
		return (time / 60) + ":" + time % 60;
	}

	public static int toSeconds(String time){
		String[] values = time.split(":");
		return values.length == 1 ? Integer.valueOf(values[0]) : Integer.valueOf(values[0]) * 60 + Integer.valueOf(values[1]);
	}

}
