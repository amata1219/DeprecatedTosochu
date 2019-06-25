package amata1219.tosochu.command;

import amata1219.tosochu.player.GamePlayer;

public interface Command {

	String getName();

	boolean hasPermission(GamePlayer player);

	void onCommand(GamePlayer sender, Args args);

}
