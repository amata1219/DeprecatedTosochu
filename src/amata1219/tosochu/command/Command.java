package amata1219.tosochu.command;

import org.bukkit.entity.Player;

public interface Command {

	String getName();

	boolean hasPermission(Player player);

	void onCommand(Player sender, Args args);

}
