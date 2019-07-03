package amata1219.tosochu.game;

public enum Difficulty {

	EASY,
	NORMAL,
	HARD,
	HARDCORE;

	public String split(String text){
		return text.split(",")[ordinal()];
	}

	public int splitAndToInt(String text){
		return Integer.valueOf(split(text));
	}

	public double splitAndToDouble(String text){
		return Double.valueOf(split(text));
	}

}
