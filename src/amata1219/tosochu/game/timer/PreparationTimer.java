package amata1219.tosochu.game.timer;

import amata1219.tosochu.game.OldGame;

public class PreparationTimer extends Timer {

	public PreparationTimer(OldGame game){
		super(game, game.settings.getPreparationTime());
	}

	@Override
	public void execute() {
	}

	@Override
	public void end(){
		super.end();
		game.start();
	}

}
