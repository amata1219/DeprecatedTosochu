package amata1219.tosochu.game.timer;

import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.game.GamePlayer;

public class GameTimer extends Timer {

	public GameTimer(GameAPI game) {
		super(game, game.getSettings().getTimeLimit());
	}

	@Override
	public void execute(){
		for(GamePlayer player : game.getOnlinePlayers()){
			if(game.isRunaway(player))
				player.depositMoney(game.getSettings().getUnitPriceOfPrizeMoney(player.getDifficulty()));

			player.getDisplayer().update(true);
		}
	}

	@Override
	public void end(){
		cancel();
	}

}
