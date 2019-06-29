package amata1219.tosochu;

import java.io.File;
import java.util.HashMap;

import org.bukkit.World;

import amata1219.tosochu.config.MapSettings;

public class MapSettingsStorage {

	private final File folder = new File(Tosochu.getPlugin() + File.separator + "MapSettings");
	private final HashMap<String, MapSettings> settings = new HashMap<>();

	public MapSettingsStorage(){
		if(!folder.exists())
			folder.mkdirs();
	}

	public MapSettings get(World world){
		return get(world.getName());
	}

	public MapSettings get(String worldName){
		return settings.get(worldName);
	}

}
