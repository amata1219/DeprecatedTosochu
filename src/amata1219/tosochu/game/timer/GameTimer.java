package amata1219.tosochu.game.timer;

import org.bukkit.scheduler.BukkitRunnable;

import amata1219.tosochu.game.Game;

public abstract class GameTimer extends BukkitRunnable {

	protected final Game game;
	protected int count;

	protected GameTimer(Game game){
		this.game = game;
	}

	public abstract void initialize();

}
