package amata1219.tosochu.game;

import org.bukkit.scheduler.BukkitRunnable;

public class PreparationTimer extends BukkitRunnable {

	int preparationTime;

	public PreparationTimer(int preparationTime){
		this.preparationTime = preparationTime;
	}

	@Override
	public void run() {
		preparationTime--;
	}

}
