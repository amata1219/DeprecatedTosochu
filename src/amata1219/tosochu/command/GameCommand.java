package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.MapLoader;
import amata1219.tosochu.game.GameAPI;

public interface GameCommand extends Command {

	@Override
	default void onCommand(Player sender, Args args){
		MapLoader loader = MapLoader.getLoader();
		if(loader.isLoadedMap())
			onCommand(sender, args, loader.getGame());
		else
			warn(sender, "このワールドでは実行出来ません。");
	}

	void onCommand(Player sender, Args args, GameAPI game);

}
