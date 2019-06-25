package amata1219.tosochu.command;

import amata1219.tosochu.GameManager;
import amata1219.tosochu.ImmutableLocation;
import amata1219.tosochu.player.GamePlayer;
import amata1219.tosochu.player.Permission;

public class FirstSpawnCommand implements Command {

	@Override
	public String getName() {
		return "firstsp";
	}

	@Override
	public boolean hasPermission(GamePlayer player) {
		return player.permission == Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(GamePlayer sender, Args args) {
		GameManager.getInstance().setFirstSpawnLocation(ImmutableLocation.at(sender.getPlayer().getLocation()));
		sender.information("現在地点を初期スポーン地点に設定しました。");
	}

}
