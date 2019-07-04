package amata1219.tosochu.game;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.text.FirstLetterCapitalizer;

public enum Profession {

	RUNAWAY,
	DROPOUT,
	HUNTER,
	REPORTER,
	SPECTATOR;

	public String getDisplayName(){
		return Tosochu.getPlugin()
				.getMessagesConfig()
				.get()
				.getString("ProfessionNames." + FirstLetterCapitalizer.capitalizeFirstLetter(toString()));
	}

}
