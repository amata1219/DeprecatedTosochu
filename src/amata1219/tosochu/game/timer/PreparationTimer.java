package amata1219.tosochu.game.timer;

import amata1219.tosochu.game.GameAPI;

public class PreparationTimer extends Timer {

	public PreparationTimer(GameAPI game){
		super(game, game.getLoadedMapSettings().getPreparationTime());
	}

	@Override
	public void execute() {
	}

	@Override
	public void end(){
		cancel();

		//ゲームを開始する
		game.setTimer(new GameTimer(game));
	}

}
