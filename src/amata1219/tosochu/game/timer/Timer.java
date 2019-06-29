package amata1219.tosochu.game.timer;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import amata1219.tosochu.game.Game;

public abstract class Timer extends BukkitRunnable {

	protected final Game game;
	private int limit, count;

	protected Timer(Game game, int limit){
		this.game = game;
		this.count = this.limit = limit;
	}

	@Override
	public final void run(){
		if(count <= 0){
			end();
			return;
		}

		//残り時間の表示
		for(Player player : game.getPlayers())
			player.setLevel(count);

		execute();

		count--;
	}

	public abstract void execute();

	public void end(){
		cancel();
	}

	public int getLimit(){
		return limit;
	}

	public int getCount(){
		return count;
	}

	public int getElapsedTime(){
		return limit - count;
	}

}
