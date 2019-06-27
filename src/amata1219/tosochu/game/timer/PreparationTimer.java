package amata1219.tosochu.game.timer;

import amata1219.tosochu.game.Game;

public class PreparationTimer extends GameTimer {

	public PreparationTimer(Game game){
		super(game);
		this.count = game.settings.getPreparationTime();
	}

	@Override
	public void run() {
		if(count >= 0){
			game.decideHunters();
		}

		count--;
	}

	@Override
	public void initialize() {
		game.broadcast("ゲームの準備を開始しました。");
		game.recruitHunters(game.settings.getNumberOfFirstRequiredHunters());
	}

}
