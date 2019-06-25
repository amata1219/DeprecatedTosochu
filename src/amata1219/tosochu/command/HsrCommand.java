package amata1219.tosochu.command;

import amata1219.tosochu.GameManager;
import amata1219.tosochu.player.GamePlayer;
import amata1219.tosochu.player.Permission;

public class HsrCommand implements Command {

	@Override
	public String getName() {
		return "hsr";
	}

	@Override
	public boolean hasPermission(GamePlayer player) {
		return player.permission == Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(GamePlayer sender, Args args) {
		if(!args.hasNextInt()){
			sender.warning("ハンターの募集人数を指定して下さい。");
			return;
		}else{
			int number = args.nextInt();
			sender.information("ハンターを" + number + "人募集しました。");
			GameManager.getInstance().setNumberOfHunters(number);
		}
	}

}
