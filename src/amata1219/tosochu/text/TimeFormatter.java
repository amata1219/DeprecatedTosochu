package amata1219.tosochu.text;

public class TimeFormatter {

	public static String format(int time){
		return (time / 60) + ":" + time % 60;
	}

}
