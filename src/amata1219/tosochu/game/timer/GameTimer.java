package amata1219.tosochu.game.timer;

import amata1219.tosochu.game.OldGame;

public class GameTimer extends Timer {

	public GameTimer(OldGame game) {
		super(game, game.settings.getTimeLimit());
	}

	@Override
	public void execute(){
		//賞金を与える
	}

	@Override
	public void end(){
		super.end();
	}

}
