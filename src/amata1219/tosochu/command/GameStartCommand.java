package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.GameLoader;
import amata1219.tosochu.Tosochu;
import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.playerdata.Permission;

public class GameStartCommand implements Command {

	@Override
	public String getName() {
		return "start";
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		GameLoader loader = Tosochu.getPlugin().gameLoader;
		GameAPI game = loader.getGame();
		if(game != null){
			if(game.getHunters().size() <= 0){
				warn(sender, "ハンターが選ばれていません。");
				return;
			}

			if(!game.isBeforeStart()){
				warn(sender, "既にゲームは始まっています。");
				return;
			}

			game.start();
		}else{
			warn(sender, "このワールドでは実行出来ません。");
		}
	}

}
