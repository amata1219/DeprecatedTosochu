package amata1219.tosochu.game;

public enum Difficulty {

	EASY,
	NORMAL,
	HARD,
	HARDCORE;

	public int getInteger(String text){
		return Integer.valueOf(getString(text));
	}

	public String getString(String text){
		return text.split(",")[ordinal()];
	}

}
