package amata1219.tosochu.game;

public enum Profession {

	PLAYER("参加者"),
	RUNAWAY("逃走者"),
	DROPOUT("脱落者"),
	HUNTER("ハンター"),
	SPECTATOR("観戦者");

	public final String name;

	private Profession(String name){
		this.name = name;
	}

}
