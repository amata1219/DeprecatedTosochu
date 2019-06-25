package amata1219.tosochu.command;

import amata1219.tosochu.GameManager;
import amata1219.tosochu.player.GamePlayer;
import amata1219.tosochu.player.Permission;

public class GameTimerCommand implements Command {

	@Override
	public String getName() {
		return "gametimer";
	}

	@Override
	public boolean hasPermission(GamePlayer player) {
		return player.permission == Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(GamePlayer sender, Args args) {
		if(!args.hasNextInt()){
			sender.warning("ゲーム時間を設定して下さい。");
		}else{
			int time = args.nextInt();
			GameManager.getInstance().setTimeLimit(time);
			sender.information("ゲーム時間を" + time + "に設定しました。");
		}
	}

}
