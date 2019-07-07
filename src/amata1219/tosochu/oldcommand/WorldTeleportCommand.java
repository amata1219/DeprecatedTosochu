package amata1219.tosochu.oldcommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import amata1219.tosochu.playerdata.Permission;

public class WorldTeleportCommand implements Command {

	@Override
	public String getName() {
		return "worldtp";
	}

	@Override
	public Permission getPermission(){
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		World world = Bukkit.getWorld(args.next());
		if(world == null){
			sender.sendMessage(ChatColor.RED + "指定されたワールドは存在しません。");
			return;
		}

		Location location = sender.getLocation();
		location.setWorld(world);
		sender.teleport(location);
		sender.sendMessage(ChatColor.AQUA + world.getName() + "にテレポートしました。");
	}

}
