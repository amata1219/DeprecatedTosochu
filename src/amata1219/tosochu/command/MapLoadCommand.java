package amata1219.tosochu.command;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import amata1219.tosochu.MapSettingsStorage;
import amata1219.tosochu.Tosochu;
import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.playerdata.Permission;

public class MapLoadCommand implements Command {

	@Override
	public String getName() {
		return "mapload";
	}

	@Override
	public Permission getPermission() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	public void onCommand(Player sender, Args args) {
		String worldName = args.next();
		if(Bukkit.getWorld(worldName) != null){
			warn(sender, "指定されたマップは既にロードされています。");
			return;
		}

		MapSettingsStorage storage = Tosochu.getPlugin().getMapSettingsStorage();
		if(!storage.isSettingsExist(worldName)){
			warn(sender, "指定されたマップ(" + worldName + ")は設定ファイルが存在しないためロード出来ません。");
			return;
		}

		//ワールドをロードする
		Bukkit.createWorld(new WorldCreator(worldName));

		//全プレイヤーを初期スポーン地点にテレポートさせる
		MapSettings settings = storage.get(worldName);
		for(Player player : Bukkit.getOnlinePlayers())
			settings.getFirstSpawnPoint().teleport(player);

		info(sender, "指定されたマップ(" + worldName + ")をロードしました。");
	}

}
