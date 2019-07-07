package amata1219.tosochu.oldcommand;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import amata1219.tosochu.playerdata.Permission;

public interface Command {

	String getName();

	Permission getPermission();

	void onCommand(Player sender, Args args);

	default void info(Player sender, String message){
		sender.sendMessage(ChatColor.GRAY + message);
	}

	default void warn(Player sender, String message){
		sender.sendMessage(ChatColor.RED + message);
	}

}
