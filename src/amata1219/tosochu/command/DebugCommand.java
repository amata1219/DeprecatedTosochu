package amata1219.tosochu.command;

import amata1219.tosochu.GameManager;
import amata1219.tosochu.player.GamePlayer;
import amata1219.tosochu.player.Permission;

public class DebugCommand implements Command {

	@Override
	public String getName() {
		return "debugcmd";
	}

	@Override
	public boolean hasPermission(GamePlayer player) {
		return player.permission == Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(GamePlayer sender, Args args) {
		sender.sendMessage(GameManager.getInstance().isHunter(sender) ? "あなたはハンターです" : "あなたは逃走者です");
	}

}
