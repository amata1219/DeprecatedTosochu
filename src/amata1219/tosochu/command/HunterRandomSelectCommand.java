package amata1219.tosochu.command;

import java.util.List;

import org.bukkit.entity.Player;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.game.Game;
import amata1219.tosochu.game.Profession;
import amata1219.tosochu.playerdata.Permission;

public class HunterRandomSelectCommand implements GameCommand {

	@Override
	public String getName() {
		return "hrs";
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void execute(Player sender, Args args) {
		if(!args.hasNextInt()){
			warn(sender, "必要なハンターの人数を指定して下さい。");
			return;
		}

		int numberOfHunters = args.nextInt();
		Game game = Tosochu.getPlugin().game;
		List<Player> runaways = game.getPlayers(Profession.RUNAWAY);
		if(runaways.size() <= numberOfHunters){
			warn(sender, "指定された数は大きすぎます。");
			return;
		}

		int count = numberOfHunters;
		for(Player player : runaways){
			if(count <= 0)
				break;

			game.becomeHunter(player);
			count--;
		}

		info(sender, "ハンターを" + numberOfHunters + "追加しました。");
	}

}
