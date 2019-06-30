package amata1219.tosochu;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
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
									section.getInt("NumberOfWins"),
									section.getInt("NumberOfTimesThatBecameHunter")
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

}
