package amata1219.tosochu;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import amata1219.tosochu.command.Args;
import amata1219.tosochu.command.Command;
import amata1219.tosochu.command.GameEndCommand;
import amata1219.tosochu.command.GameStartCommand;
import amata1219.tosochu.command.HunterRandomSelectCommand;
import amata1219.tosochu.command.MapLoadCommand;
import amata1219.tosochu.command.MapUnloadCommand;
import amata1219.tosochu.command.WorldTeleportCommand;
import amata1219.tosochu.config.Config;
import amata1219.tosochu.game.Game;
import amata1219.tosochu.playerdata.PlayerData;

public class Tosochu extends JavaPlugin {

	private static Tosochu plugin;

	public Game game;

	private final HashMap<String, Command> commands = new HashMap<>();

	private MapSettingsStorage mapSettingsStorage;
	private PlayerDataStorage playerDataStorage;

	private Config config;
	private Config messages;
	private Config playerData;

	@Override
	public void onEnable(){
		plugin = this;

		(config = new Config("config.yml")).create();

		(messages = new Config("messages.yml")).create();

		(playerData = new Config("player_data.yml")).create();

		//テンプレートファイルを作成する
		new Config("template.yml").create();

		mapSettingsStorage = new MapSettingsStorage();
		playerDataStorage = new PlayerDataStorage();

		registerCommands(
			new GameStartCommand(),
			new GameEndCommand(),
			new WorldTeleportCommand(),
			new MapLoadCommand(),
			new MapUnloadCommand(),
			new HunterRandomSelectCommand()
		);

		registerListeners(
			new GameListener()
		);

		for(Player player : getServer().getOnlinePlayers()){
			if(playerDataStorage.isExist(player))
				playerDataStorage.add(new PlayerData(player.getUniqueId()));
		}
	}

	@Override
	public void onDisable(){
		HandlerList.unregisterAll((JavaPlugin) this);

		playerDataStorage.saveAll();

		if(isInGame())
			Bukkit.unloadWorld(game.world, false);
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

	public boolean isInGame(){
		return game != null;
	}

	public MapSettingsStorage getMapSettingsStorage(){
		return mapSettingsStorage;
	}

	public PlayerDataStorage getPlayerDataStorage(){
		return playerDataStorage;
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
