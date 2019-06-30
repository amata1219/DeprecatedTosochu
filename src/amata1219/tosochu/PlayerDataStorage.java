package amata1219.tosochu;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import amata1219.tosochu.config.Config;
import amata1219.tosochu.playerdata.Permission;
import amata1219.tosochu.playerdata.PlayerData;

public class PlayerDataStorage {

	private final Config config = new Config("player_data.yml");
	private final HashMap<UUID, PlayerData> storage = new HashMap<>();

	public PlayerDataStorage(){
		for(Entry<String, ConfigurationSection> hierarchy : config.getShallowSections().entrySet()){
			ConfigurationSection section = hierarchy.getValue();
			PlayerData data = new PlayerData(
									UUID.fromString(hierarchy.getKey()),
									Permission.valueOf(section.getString("Permission")),
									section.getInt("Money"),
									section.getInt("Number of wins"),
									section.getInt("Number of times became hunter")
								);
			add(data);
		}
	}

	public PlayerData get(Player player){
		return get(player.getUniqueId());
	}

	public PlayerData get(UUID uuid){
		return storage.get(uuid);
	}

	public void add(PlayerData data){
		if(storage.containsKey(data.uuid))
			new IllegalArgumentException("The data already exists");
		else
			storage.put(data.uuid, data);
	}

	public boolean isExist(Player player){
		return isExist(player);
	}

	public boolean isExist(UUID uuid){
		return storage.containsKey(uuid);
	}

	public void saveAll(){
		FileConfiguration file = config.get();
		for(Entry<UUID, PlayerData> entry : storage.entrySet()){
			String uuid = entry.getKey().toString();
			PlayerData data = entry.getValue();
			file.set(uuid + ".Permission", data.getPermission().toString());
			file.set(uuid + ".Money", data.getMoney());
			file.set(uuid + ".Number of wins", data.getNumberOfWins());
			file.set(uuid + ".Number of times became hunter", data.getNumberOfTimesThatBecameHunter());
		}
		config.update();
	}

}
