package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.Tosochu;

public interface GameCommand extends Command {

	@Override
	default void onCommand(Player sender, Args args){
		if(!Tosochu.getPlugin().isInGame()){
			warn(sender, "ゲームが行われていないため実行出来ません。");
			return;
		}

		execute(sender, args);
	}

	void execute(Player sender, Args args);

}
