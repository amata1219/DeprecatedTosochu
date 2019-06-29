package amata1219.tosochu.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.Game;
import amata1219.tosochu.playerdata.Permission;

public class StartCommand implements Command {

	@Override
	public String getName() {
		return "start";
	}

	@Override
	public Permission getPermission(){
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		if(Game.isInGame()){
			sender.sendMessage(ChatColor.RED + "現在ゲームが行われているため実行出来ません。");
			return;
		}

		MapSettings settings = Tosochu.getPlugin().getMapSettings(sender.getWorld().getName());
		if(settings == null){
			sender.sendMessage(ChatColor.RED + "このワールドに対応した設定ファイルが存在しないため実行出来ません。");
			return;
		}

		Game.loadGame(settings).prepare();
		sender.sendMessage(ChatColor.AQUA + "ゲームの準備を開始しました。");

		Game game = Game.game;
		for(Player player : Bukkit.getOnlinePlayers()){
			game.join(player);
		}
		game.broadcast("ゲームの準備を開始しました。");
	}

}
