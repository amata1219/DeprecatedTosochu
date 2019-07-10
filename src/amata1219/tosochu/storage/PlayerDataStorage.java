package amata1219.tosochu.storage;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import amata1219.tosochu.config.Config;
import amata1219.tosochu.game.GamePlayer;
import amata1219.tosochu.playerdata.Permission;
import amata1219.tosochu.playerdata.PlayerData;

public class PlayerDataStorage implements Listener {

	private static PlayerDataStorage storage;

	private final Config config = new Config("player_data.yml");
	private final HashMap<UUID, PlayerData> dataMap = new HashMap<>();

	public static PlayerDataStorage load(){
		return storage = new PlayerDataStorage();
	}

	public static PlayerDataStorage getStorage(){
		return storage;
	}

	private PlayerDataStorage(){
		config.create();

		for(Entry<String, ConfigurationSection> hierarchy : config.getShallowSections().entrySet()){
			ConfigurationSection section = hierarchy.getValue();
			PlayerData data = new PlayerData(
									UUID.fromString(hierarchy.getKey()),
									Permission.valueOf(section.getString("Permission")),
									section.getInt("Money"),
									section.getInt("Number of wins"),
									section.getInt("Number of times became hunter")
								);
			dataMap.put(data.uuid, data);
		}

		for(Player player : Bukkit.getOnlinePlayers()){
			UUID uuid = player.getUniqueId();
			if(!dataMap.containsKey(uuid))
				dataMap.put(uuid, new PlayerData(uuid));
		}
	}

	public PlayerData get(GamePlayer player){
		return get(player.uuid);
	}

	public PlayerData get(Player player){
		return get(player.getUniqueId());
	}

	public PlayerData get(UUID uuid){
		return dataMap.get(uuid);
	}

	public boolean isExist(GamePlayer player){
		return isExist(player.uuid);
	}

	public boolean isExist(Player player){
		return isExist(player.getUniqueId());
	}

	public boolean isExist(UUID uuid){
		return dataMap.containsKey(uuid);
	}

	public void saveAll(){
		FileConfiguration file = config.get();
		for(Entry<UUID, PlayerData> entry : dataMap.entrySet()){
			String uuid = entry.getKey().toString();
			PlayerData data = entry.getValue();
			file.set(uuid + ".Permission", data.getPermission().toString());
			file.set(uuid + ".Money", data.getMoney());
			file.set(uuid + ".Number of wins", data.getNumberOfWins());
			file.set(uuid + ".Number of times became hunter", data.getNumberOfTimesThatBecameHunter());
		}
		config.update();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onFirstJoin(PlayerJoinEvent event){
		UUID uuid = event.getPlayer().getUniqueId();
		if(!dataMap.containsKey(uuid))
			dataMap.put(uuid, new PlayerData(uuid));
	}

}
