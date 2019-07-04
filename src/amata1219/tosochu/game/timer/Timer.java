package amata1219.tosochu.game.timer;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import amata1219.tosochu.game.GameAPI;

public abstract class Timer extends BukkitRunnable {

	protected final GameAPI game;
	private final int timeLimit;
	private int remainingTime;

	protected Timer(GameAPI game, int timeLimit){
		this.game = game;
		this.timeLimit = this.remainingTime = timeLimit;
	}

	@Override
	public final void run(){
		if(remainingTime > 0){
			for(Player player : game.getOnlinePlayers())
				player.setLevel(remainingTime);

			//残り時間が10の倍数又は5秒以下であれば全体に告知する
			if(remainingTime % 10 == 0 || remainingTime <= 5)
				game.broadcastMessage("ゲーム開始まで残り" + remainingTime + "秒");

			execute();

			remainingTime--;
		}else{
			end();
		}
	}

	public abstract void execute();

	public abstract void end();

	public int getRemainingTime(){
		return remainingTime;
	}

	public int getElapsedTime(){
		return timeLimit - remainingTime;
	}

}
