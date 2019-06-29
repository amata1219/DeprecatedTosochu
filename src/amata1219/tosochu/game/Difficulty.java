package amata1219.tosochu.game;

public enum Difficulty {

	EASY,
	NORMAL,
	HARD,
	HARDCORE;

	public String split(String text){
		return text.split(",")[ordinal()];
	}

	public int getInteger(String text){
		return Integer.valueOf(split(text));
	}

	public double getDouble(String text){
		return Double.valueOf(split(text));
	}

}
