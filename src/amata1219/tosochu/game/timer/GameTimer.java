package amata1219.tosochu.game.timer;

import amata1219.tosochu.game.GameAPI;

public class GameTimer extends Timer {

	public GameTimer(GameAPI game) {
		super(game, game.getLoadedMapSettings().getTimeLimit());
	}

	@Override
	public void execute(){
		//賞金を与える
	}

	@Override
	public void end(){
		cancel();
	}

}
