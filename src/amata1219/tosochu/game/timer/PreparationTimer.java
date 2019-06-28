package amata1219.tosochu.game.timer;

import amata1219.tosochu.game.Game;

public class PreparationTimer extends Timer {

	public PreparationTimer(Game game){
		super(game, game.settings.getPreparationTime());
	}

	@Override
	public void run() {
		if(isZero()){
			end();
			return;
		}

		count--;
	}

	@Override
	public void start() {
		game.broadcast("ゲームの準備を開始しました。");
		game.recruitHunters(game.settings.getNumberOfFirstRequiredHunters());
	}

	@Override
	public void end(){
		if(isCancelled())
			return;

		game.start();
		cancel();
	}

}
