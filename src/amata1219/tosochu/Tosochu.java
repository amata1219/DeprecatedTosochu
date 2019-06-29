package amata1219.tosochu;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import amata1219.tosochu.command.Command;
import amata1219.tosochu.config.Config;
import amata1219.tosochu.config.MapSettingConfig;
import amata1219.tosochu.playerdata.PlayerData;

public class Tosochu extends JavaPlugin implements Listener {

	private static Tosochu plugin;

	private final HashMap<String, Command> commands = new HashMap<>();

	private final HashMap<UUID, PlayerData> players = new HashMap<>();

	private File mapSettingsFolder;
	private final HashMap<String, MapSettingConfig> mapSettings = new HashMap<>();

	private Config config;
	private Config messages;

	@Override
	public void onEnable(){
		plugin = this;

		mapSettingsFolder = new File(getDataFolder() + File.separator + "MapSettings");

		config = new Config("config.yml").create();
		messages = new Config("messages.yml").create();

		registerCommands();

		registerListeners(
			this,
			new GameListener()
		);

		reloadMapSettings();

		//for(Player player : getServer().getOnlinePlayers())

	}

	@Override
	public void onDisable(){
		HandlerList.unregisterAll((JavaPlugin) this);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command input, String label, String[] args){
		if(!(sender instanceof Player))
			return true;

		return true;
	}

	public static Tosochu getPlugin(){
		return plugin;
	}

	public File getMapSettingsFolder(){
		return mapSettingsFolder;
	}

	public void reloadMapSettings(){
		mapSettings.clear();

		for(File file : mapSettingsFolder.listFiles()){
			MapSettingConfig settings = new MapSettingConfig(file.getName());
			mapSettings.put(settings.getName(), settings);
		}
	}

	public void registerCommands(Command... commands){
		for(Command command : commands)
			this.commands.put(command.getName(), command);
	}

	public void registerListeners(Listener... listeners){
		for(Listener listener : listeners)
			getServer().getPluginManager().registerEvents(listener, this);
	}

	public void loadWorld(String name){
		Bukkit.getServer().createWorld(new WorldCreator(name));
	}

	public void unloadWorld(String name){
		Bukkit.getServer().unloadWorld(name, false);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event){
		UUID uuid = event.getPlayer().getUniqueId();
		if(players.containsKey(uuid))
			players.put(uuid, new PlayerData(uuid));
	}

	public PlayerData getPlayerData(UUID uuid){
		return players.get(uuid);
	}

}
