package amata1219.tosochu.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.playerdata.Permission;

public class GameEndCommand implements Command {

	private final Tosochu plugin = Tosochu.getPlugin();

	@Override
	public String getName() {
		return "end";
	}

	@Override
	public Permission getPermission(){
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		if(!plugin.isInGame()){
			sender.sendMessage(ChatColor.RED + "現在ゲームが行われていないため実行出来ません。");
			return;
		}

		plugin.game.end();
		sender.sendMessage(ChatColor.AQUA + "ゲームを強制終了しました。");
	}

}
