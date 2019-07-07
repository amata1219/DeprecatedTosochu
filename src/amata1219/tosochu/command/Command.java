package amata1219.tosochu.command;

import amata1219.tosochu.playerdata.Permission;

public interface Command {

	default Permission getPermission(){
		return Permission.ADMINISTRATOR;
	}

	void onCommand(Sender sender, Arguments args);

	default boolean blockNonPlayer(Sender sender){
		if(sender.isPlayerCommandSender())
			return false;

		sender.warn("ゲーム内から実行して下さい。");
		return true;
	}

}
