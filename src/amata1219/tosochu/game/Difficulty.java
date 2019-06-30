package amata1219.tosochu.game;

public enum Difficulty {

	EASY,
	NORMAL,
	HARD,
	HARDCORE;

	public String split(String text){
		return text.split(",")[ordinal()];
	}

}
