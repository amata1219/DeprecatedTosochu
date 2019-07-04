package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.GameLoader;
import amata1219.tosochu.Tosochu;
import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.playerdata.Permission;

public class HunterCommand implements Command {

	private final GameLoader loader = Tosochu.getPlugin().gameLoader;

	@Override
	public String getName() {
		return "hunter";
	}

	@Override
	public Permission getPermission() {
		return Permission.NORMAL_PLAYER;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		GameAPI game = loader.getGame();
		if(game == null){
			warn(sender, "このワールドではゲームは行なえないため実行出来ません。");
			return;
		}

	}

}
