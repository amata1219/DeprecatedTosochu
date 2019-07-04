package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.GameLoader;
import amata1219.tosochu.Tosochu;
import amata1219.tosochu.WorldLoader;
import amata1219.tosochu.playerdata.Permission;

public class MapMoveCommand implements Command {

	private final GameLoader gameLoader = Tosochu.getPlugin().gameLoader;
	private final WorldLoader worldLoader = Tosochu.getPlugin().worldLoader;

	@Override
	public String getName() {
		return "mapmove";
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		//mapmove mapname
		if(!args.hasNext()){
			warn(sender, "移動先のマップを指定して下さい。");
			return;
		}

		String worldName = args.next();
		if(!worldLoader.canLoad(worldName)){
			warn(sender, "指定されたマップは存在しません。");
			return;
		}

		worldLoader.move(sender.getWorld().getName(), worldName);
		info(sender, "元いたマップはアンロードし、指定されたマップをロードしました。");

		gameLoader.load(Tosochu.getPlugin().getMapSettingsStorage().getMapSettings(worldLoader.getLoadedWorld()));
	}

}
