package amata1219.tosochu;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import amata1219.tosochu.command.Arguments;
import amata1219.tosochu.command.Command;
import amata1219.tosochu.command.Sender;
import amata1219.tosochu.config.Config;
import amata1219.tosochu.storage.MapSettingsStorage;
import amata1219.tosochu.storage.PlayerDataStorage;

public class Tosochu extends JavaPlugin {

	/*
	 * マップロード時にGameインスタンスを生成する
	 * GameLoaderは/mapload, /mapmove時に使用される
	 */

	private static Tosochu plugin;

	private final HashMap<String, Command> commands = new HashMap<>();

	private MapSettingsStorage settingStorage;
	private PlayerDataStorage playerStorage;

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
		settingStorage = MapSettingsStorage.load();

		//プレイヤーデータのストレージをロードする
		playerStorage = PlayerDataStorage.load();

		//コマンドを登録する
		registerCommands(
			//new WorldTeleportCommand()
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
			playerStorage
		);
	}

	@Override
	public void onDisable(){
		HandlerList.unregisterAll((JavaPlugin) this);

		playerStorage.saveAll();
	}

	@Override
	public boolean onCommand(CommandSender commander, org.bukkit.command.Command input, String label, String[] args){
		Sender sender = new Sender(commander);
		Command command = commands.get(input.getName());
		if(command.blockNonPlayer(sender))
			return true;

		if(playerStorage.get((Player) commander).hasPermission(command.getPermission()))
			command.onCommand(sender, new Arguments(args));

		return true;
	}

	public static Tosochu getPlugin(){
		return plugin;
	}

	public MapSettingsStorage getMapSettingsStorage(){
		return settingStorage;
	}

	public PlayerDataStorage getPlayerDataStorage(){
		return playerStorage;
	}

	public Config getMainConfig(){
		return mainConfig;
	}

	public Config getMessagesConfig(){
		return messagesConfig;
	}

	public void registerCommands(Command... commands){
		for(Command command : commands){
			String className = command.getClass().getSimpleName();
			String commandName = className.substring(0, className.length() - 6).toLowerCase();
			this.commands.put(commandName, command);
		}
	}

	public void registerListeners(Listener... listeners){
		for(Listener listener : listeners)
			getServer().getPluginManager().registerEvents(listener, this);
	}

}
