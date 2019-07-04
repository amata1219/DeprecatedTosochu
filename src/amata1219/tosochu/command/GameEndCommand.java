package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.GameLoader;
import amata1219.tosochu.Tosochu;
import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.playerdata.Permission;

public class GameEndCommand implements Command {

	private final GameLoader gameLoader = Tosochu.getPlugin().gameLoader;

	@Override
	public String getName() {
		return "end";
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		gameLoader.unload();
		MapSettings settings = gameLoader.getGame().getLoadedMapSettings();
		gameLoader.load(settings);
		info(sender, "ゲームを強制終了しました。");
	}

}
