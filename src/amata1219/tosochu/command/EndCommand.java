package amata1219.tosochu.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import amata1219.tosochu.game.Game;
import amata1219.tosochu.playerdata.Permission;

public class EndCommand implements Command {

	@Override
	public String getName() {
		return "g_end";
	}

	@Override
	public Permission getPermission(){
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		if(!Game.isInGame()){
			sender.sendMessage(ChatColor.RED + "現在ゲームが行われていないため実行出来ません。");
			return;
		}

		Game.game.end();
		sender.sendMessage(ChatColor.AQUA + "ゲームを強制終了しました。");
	}

}
