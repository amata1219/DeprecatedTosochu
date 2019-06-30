package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.playerdata.Permission;

public class MapSettingsReloadCommand implements Command {

	@Override
	public String getName() {
		return "msr";
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		Tosochu.getPlugin().getMapSettingsStorage().reload();
		info(sender, "マップの設定ファイルをリロードしました。");
	}

}
