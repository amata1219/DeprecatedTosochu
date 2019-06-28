package amata1219.tosochu.game.timer;

import org.bukkit.scheduler.BukkitRunnable;

import amata1219.tosochu.game.Game;

public abstract class Timer extends BukkitRunnable {

	protected final Game game;
	private int timeLimit;
	protected int count;

	protected Timer(Game game, int timeLimit){
		this.game = game;
		this.timeLimit = timeLimit;
		this.count = timeLimit;
	}

	public boolean isZero(){
		return count <= 0;
	}

	public int getElapsedTime(){
		return timeLimit - count;
	}

	public abstract void start();

	public abstract void end();

}
