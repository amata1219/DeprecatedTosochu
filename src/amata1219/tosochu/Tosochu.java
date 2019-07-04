package amata1219.tosochu;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import amata1219.tosochu.command.Args;
import amata1219.tosochu.command.Command;
import amata1219.tosochu.command.GameEndCommand;
import amata1219.tosochu.command.GameStartCommand;
import amata1219.tosochu.command.HunterSelectRandomCommand;
import amata1219.tosochu.command.MapMoveCommand;
import amata1219.tosochu.command.WorldTeleportCommand;
import amata1219.tosochu.config.Config;
import amata1219.tosochu.game.TosoGameListener;
import amata1219.tosochu.storage.MapSettingsStorage;
import amata1219.tosochu.storage.PlayerDataStorage;

public class Tosochu extends JavaPlugin {

	/*
	 * マップロード時にGameインスタンスを生成する
	 * GameLoaderは/mapload, /mapmove時に使用される
	 */

	private static Tosochu plugin;

	private final HashMap<String, Command> commands = new HashMap<>();

	private MapSettingsStorage mapSettingsStorage;
	private PlayerDataStorage playerDataStorage;

	public WorldLoader worldLoader;
	public GameLoader gameLoader;

	private Config mainConfig;
	private Config messagesConfig;

	@Override
	public void onEnable(){
		plugin = this;

		//config.ymlを生成する
		(mainConfig = new Config("config.yml")).create();

		//messages.ymlを生成する
		(messagesConfig = new Config("messages.yml")).create();

		//テンプレートファイルを作成する
		new Config("template.yml").create();

		//マップ設定を格納するフォルダーを作成
		MapSettingsStorage.mkdir();

		//マップ設定のストレージをロードする
		mapSettingsStorage = MapSettingsStorage.load();

		worldLoader = new WorldLoader();
		gameLoader = new GameLoader();

		//プレイヤーデータのストレージをロードする
		playerDataStorage = PlayerDataStorage.load();

		//コマンドを登録する
		registerCommands(
			new WorldTeleportCommand(),
			new GameStartCommand(),
			new GameEndCommand(),
			new HunterSelectRandomCommand(),
			new MapMoveCommand()
		);

		/*
		 * start
		 * end
		 * hsr
		 * join
		 * leave
		 * opgame
		 * mode
		 *
		 * jail
		 * rp
		 * szn
		 */

		//イベントリスナを登録する
		registerListeners(
			playerDataStorage,
			new TosoGameListener()
		);
	}

	@Override
	public void onDisable(){
		HandlerList.unregisterAll((JavaPlugin) this);

		playerDataStorage.saveAll();
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command input, String label, String[] args){
		if(!(sender instanceof Player))
			return true;

		Player player = (Player) sender;
		Command command = commands.get(input.getName());

		//コマンドに設定されたパーミッションを持っていればコマンドを実行
		if(playerDataStorage.get(player).hasPermission(command.getPermission()))
			command.onCommand(player, new Args(args));
		return true;
	}

	public static Tosochu getPlugin(){
		return plugin;
	}

	public MapSettingsStorage getMapSettingsStorage(){
		return mapSettingsStorage;
	}

	public PlayerDataStorage getPlayerDataStorage(){
		return playerDataStorage;
	}

	public Config getMainConfig(){
		return mainConfig;
	}

	public Config getMessagesConfig(){
		return messagesConfig;
	}

	public void registerCommands(Command... commands){
		for(Command command : commands)
			this.commands.put(command.getName(), command);
	}

	public void registerListeners(Listener... listeners){
		for(Listener listener : listeners)
			getServer().getPluginManager().registerEvents(listener, this);
	}

}
