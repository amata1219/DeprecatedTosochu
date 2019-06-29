package amata1219.tosochu.game.timer;

import amata1219.tosochu.game.Game;

public class GameTimer extends Timer {

	public GameTimer(Game game) {
		super(game, game.settings.getTimeLimit());
	}

	@Override
	public void execute(){
		game.giveMoneyToRunaways();
	}

	@Override
	public void end(){
		super.end();
	}

}
