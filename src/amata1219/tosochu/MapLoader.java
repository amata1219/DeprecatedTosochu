package amata1219.tosochu;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.game.TosoGame;
import amata1219.tosochu.storage.MapSettingsStorage;

public class MapLoader {

	private static MapLoader loader;

	private final Tosochu plugin = Tosochu.getPlugin();
	private final MapSettingsStorage storage = plugin.getMapSettingsStorage();
	private final List<String> defaultWorlds = plugin.getServer().getWorlds().stream().map(World::getName).collect(Collectors.toList());

	private World world;
	private MapSettings settings;
	private GameAPI game;

	public static MapLoader newInstance(){
		return loader = new MapLoader();
	}

	public static MapLoader getLoader(){
		return loader;
	}

	private MapLoader(){
		/*
		 * ログインログアウトリスナー
		 * →ゲームが生成されていれば機能する
		 *
		 * ゲームリスナー
		 * →ゲームが開始されていれば機能する
		 */
	}

	public boolean isLoadedMap(){
		return world != null;
	}

	public World getWorld(){
		return world;
	}

	public MapSettings getSettings(){
		return settings;
	}

	public GameAPI getGame(){
		return game;
	}

	public void nextGame(){
		//ゲームが終了していなければ強制終了する
		if(!game.isEnded())
			game.forcedTermination();

		game = new TosoGame(settings);
	}

	public boolean canLoad(String worldName){
		//デフォルトで読み込まれているワールドであればfalse
		if(defaultWorlds.contains(worldName))
			return false;

		//設定ファイルが存在しなければfalse
		if(!storage.hasMapSettings(worldName))
			return false;

		//ワールドフォルダが存在するかどうか
		return new File(plugin.getServer().getWorldContainer().getParent(), worldName).exists();
	}

	public void move(World from, String to){
		//移動先のマップをロードする
		load(to);

		//移動前のワールドにいるプレイヤーを移動先のワールドにテレポートさせる
		for(Player player : from.getPlayers())
			game.join(player);

		//移動前のワールドをアンロードする
		unload(from);
	}

	public void load(String mapName){
		//ロード出来なければ戻る
		if(!canLoad(mapName))
			return;

		//ワールドが既にロードされていればエラー
		if(Bukkit.getWorld(mapName) != null)
			new IllegalArgumentException("Already loaded world");

		//新しい値を代入する
		world = WorldCreator.name(mapName).createWorld();
		settings = storage.getMapSettings(mapName);
		game = new TosoGame(settings);
	}

	public void unload(){
		unload(world);
	}

	public void unload(World world){
		if(defaultWorlds.contains(world.getName()))
			return;

		Bukkit.unloadWorld(world, false);
	}

}
