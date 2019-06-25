package amata1219.tosochu;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import amata1219.tosochu.command.Args;
import amata1219.tosochu.command.Command;
import amata1219.tosochu.command.FirstSpawnCommand;
import amata1219.tosochu.command.GameTimerCommand;
import amata1219.tosochu.command.HsrCommand;
import amata1219.tosochu.command.JailPointCommand;
import amata1219.tosochu.command.RespawnPointCommand;
import amata1219.tosochu.command.StartCommand;
import amata1219.tosochu.player.GamePlayer;

public class Tosochu extends JavaPlugin {

	private static Tosochu plugin;

	private final HashMap<String, Command> commands = new HashMap<>();

	@Override
	public void onEnable(){
		plugin = this;

		registerCommands(
			new FirstSpawnCommand(),
			new GameTimerCommand(),
			new HsrCommand(),
			new JailPointCommand(),
			new RespawnPointCommand(),
			new StartCommand()
		);

		for(Player player : getServer().getOnlinePlayers())
			PlayerManager.getManager().registerPlayer(player);

		registerListeners(new EventListener());


	}

	@Override
	public void onDisable(){
		HandlerList.unregisterAll((JavaPlugin) this);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command input, String label, String[] args){
		if(!(sender instanceof Player)){
			sender.sendMessage(ChatColor.RED + "ゲーム内から実行して下さい。");
			return true;
		}
		sender.sendMessage(ChatColor.GRAY + "(Debug: v.1.0)");
		Command command = commands.get(input.getName());
		GamePlayer player = PlayerManager.getManager().toGamePlayer(((Player) sender).getUniqueId());
		if(command.hasPermission(player))
			command.onCommand(player, new Args(args));
		return true;
	}

	public static Tosochu getPlugin(){
		return plugin;
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
