package amata1219.tosochu;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import amata1219.tosochu.player.GamePlayer;

public class PlayerManager {

	private final static PlayerManager manager = new PlayerManager();

	private final HashMap<UUID, GamePlayer> players = new HashMap<>();

	private PlayerManager(){

	}

	public static PlayerManager getManager(){
		return manager;
	}

	public void registerPlayer(Player player){
		players.put(player.getUniqueId(), new GamePlayer(player.getUniqueId()));
	}

	public void unregisterPlayer(Player player){
		players.remove(player.getUniqueId());
	}

	public GamePlayer toGamePlayer(UUID uuid){
		return players.get(uuid);
	}

}
