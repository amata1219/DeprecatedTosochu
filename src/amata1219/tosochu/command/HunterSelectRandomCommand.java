package amata1219.tosochu.command;

import org.bukkit.entity.Player;

import amata1219.tosochu.GameLoader;
import amata1219.tosochu.Tosochu;
import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.playerdata.Permission;

public class HunterSelectRandomCommand implements Command {

	private final GameLoader loader = Tosochu.getPlugin().gameLoader;

	@Override
	public String getName() {
		return "hsr";
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		GameAPI game = loader.getGame();
		if(game == null){
			warn(sender, "このワールドではゲームは行なえないため実行出来ません。");
			return;
		}

		if(!args.hasNextInt()){
			warn(sender, "募集人数を指定して下さい。");
			return;
		}

		int recruitmentNumberOfHunters = args.nextInt();
		game.recruitHunters(recruitmentNumberOfHunters, 0);
		game.broadcastMessage("ハンターを" + recruitmentNumberOfHunters + "人選ばれました。");
	}

}
