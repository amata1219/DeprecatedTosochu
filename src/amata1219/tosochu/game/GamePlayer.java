package amata1219.tosochu.game;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import amata1219.tosochu.game.displayer.StatesDisplayer;

public class GamePlayer {

	private final UUID uuid;

	public Difficulty difficulty;
	public int money;
	public StatesDisplayer displayer;

	public boolean administrator, spectator;

	private long dropout, quit;

	public GamePlayer(Player player){
		uuid = player.getUniqueId();
	}

	public Player getPlayer(){
		return Bukkit.getPlayer(uuid);
	}

	public boolean isDropout(){
		return dropout > 0;
	}

	public long getTimeOfDropout(){
		return dropout;
	}

	public boolean isQuitted(){
		return quit > 0;
	}

	public long getTimeOfQuit(){
		return quit;
	}

	public void message(String message){
		getPlayer().sendMessage(TosoGame.PREFIX + message);
	}

}
