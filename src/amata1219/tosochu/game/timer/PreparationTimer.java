package amata1219.tosochu.game.timer;

import org.bukkit.ChatColor;

import amata1219.tosochu.game.GameAPI;

public class PreparationTimer extends Timer {

	public PreparationTimer(GameAPI game){
		super(game, game.getSettings().getPreparationTime());
	}

	@Override
	public void execute() {
	}

	@Override
	public void end(){
		cancel();

		//ゲームを開始する
		game.setTimer(new GameTimer(game));

		game.broadcastTitle(ChatColor.DARK_RED + "" + ChatColor.BOLD + "逃走中スタート！", 0, 50, 0);
	}

}
