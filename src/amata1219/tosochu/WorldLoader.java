package amata1219.tosochu;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

public class WorldLoader {

	public static void load(String worldName){
		if(Bukkit.getWorld(worldName) != null)
			new IllegalArgumentException("Already exist world");

		WorldCreator.name(worldName).createWorld();
	}

	public static void unload(String worldName){
		Bukkit.unloadWorld(worldName, false);
	}

}
