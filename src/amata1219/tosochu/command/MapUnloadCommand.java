package amata1219.tosochu.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import amata1219.tosochu.playerdata.Permission;

public class MapUnloadCommand implements Command {

	@Override
	public String getName() {
		return "mapunload";
	}

	@Override
	public Permission getPermission() {
		return null;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		World world = Bukkit.getWorld(args.next());
		if(world == null){
			warn(sender, "指定されたマップはロードされていません。");
			return;
		}


		Bukkit.unloadWorld(world, false);

		info(sender, "指定されたマップをアンロードしました。");
	}

}
