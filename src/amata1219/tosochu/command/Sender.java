package amata1219.tosochu.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Sender {

	public final CommandSender sender;

	public Sender(CommandSender sender){
		this.sender = sender;
	}

	public void information(String message){
		sendMessage(ChatColor.AQUA + message);
	}

	public void addition(String message){
		sendMessage(ChatColor.GRAY + message);
	}

	public void warning(String message){
		sendMessage(ChatColor.RED + message);
	}

	public void sendMessage(String message){
		sender.sendMessage(message);
	}

}
