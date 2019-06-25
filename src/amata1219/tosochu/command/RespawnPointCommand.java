package amata1219.tosochu.command;

import amata1219.tosochu.player.GamePlayer;
import amata1219.tosochu.player.Permission;

public class RespawnPointCommand implements Command {

	@Override
	public String getName() {
		return "rp";
	}

	@Override
	public boolean hasPermission(GamePlayer player) {
		return player.permission == Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(GamePlayer sender, Args args) {
		if(args.hasNextInt()){

		}else{
			switch(args.next()){
			case "count":

			case "close":

			case "open":

			}
		}
	}

}
