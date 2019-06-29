package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.playerdata.Permission;
import amata1219.tosochu.playerdata.PlayerData;

public interface Command {

	String getName();

	void onCommand(Player sender, Args args);

	boolean hasPermission(Player player);

	default boolean isAdministrator(Player player){
		return getPlayerData(player).hasPermission(Permission.ADMINISTRATOR);
	}

	default boolean isYouTuber(Player player){
		return getPlayerData(player).hasPermission(Permission.YOUTUBER);
	}

	default boolean isNormalPlayer(Player player){
		return getPlayerData(player).hasPermission(Permission.NORMAL_PLAYER);
	}

	default PlayerData getPlayerData(Player player){
		return Tosochu.getPlugin().getPlayerData(player.getUniqueId());
	}

}
