package amata1219.tosochu.command;

import amata1219.tosochu.Game;
import amata1219.tosochu.GameManager;
import amata1219.tosochu.player.GamePlayer;
import amata1219.tosochu.player.Permission;

public class StartCommand implements Command {

	@Override
	public String getName() {
		return "start";
	}

	@Override
	public boolean hasPermission(GamePlayer player) {
		return player.permission == Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(GamePlayer sender, Args args) {
		Game game = GameManager.getInstance();
		String text = game.canStart();
		if(text != null){
			sender.warning(text);
			return;
		}

		if(!game.isStarted())
			sender.information("ゲームを開始しました。");
		else
			sender.warning("既にゲームを開始しています。");
	}

}
