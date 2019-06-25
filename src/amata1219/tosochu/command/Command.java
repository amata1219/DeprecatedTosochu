package amata1219.tosochu.command;

import amata1219.tosochu.deprecated.OldGamePlayer;

public interface Command {

	String getName();

	boolean hasPermission(OldGamePlayer player);

	void onCommand(OldGamePlayer sender, Args args);

}
