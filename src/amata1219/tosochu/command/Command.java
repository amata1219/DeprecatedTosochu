package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.playerdata.Permission;

public interface Command {

	String getName();

	Permission getPermission();

	void onCommand(Player sender, Args args);

}
