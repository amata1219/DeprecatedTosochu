package amata1219.tosochu.command;

import org.bukkit.entity.Player;

public interface GameCommand extends Command {

	@Override
	default void onCommand(Player sender, Args args){
		execute(sender, args);
	}

	void execute(Player sender, Args args);

}
