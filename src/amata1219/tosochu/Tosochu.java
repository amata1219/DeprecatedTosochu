package amata1219.tosochu;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import amata1219.tosochu.command.Args;
import amata1219.tosochu.command.Command;
import amata1219.tosochu.command.Sender;

public class Tosochu extends JavaPlugin {

	private static Tosochu plugin;

	private final HashMap<String, Command> commands = new HashMap<>();

	@Override
	public void onEnable(){
		plugin = this;
	}

	@Override
	public void onDisable(){
		HandlerList.unregisterAll((JavaPlugin) this);
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args){
		commands.get(command.getName()).onCommand(new Sender(sender), new Args(args));
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
