package amata1219.tosochu.game.timer;

import org.bukkit.entity.Player;

import amata1219.tosochu.game.GameAPI;

public class GameTimer extends Timer {

	public GameTimer(GameAPI game) {
		super(game, game.getLoadedMapSettings().getTimeLimit());
	}

	@Override
	public void execute(){
		//賞金を与える
		for(Player runaway : game.getRunaways())
			
			game.depositMoney(runaway, game.getUnitPriceOfPrizeMoney(game.getDifficulty(runaway)));

		for(Player player : game.getOnlinePlayers()){
			game.getStatesDisplayer(player).update(true);
		}
	}

	@Override
	public void end(){
		cancel();
	}

}
