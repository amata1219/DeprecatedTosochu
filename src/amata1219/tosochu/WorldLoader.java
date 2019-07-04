package amata1219.tosochu;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.storage.MapSettingsStorage;

public class WorldLoader {

	private final MapSettingsStorage storage = Tosochu.getPlugin().getMapSettingsStorage();
	private World loadedWorld;

	public WorldLoader(){

	}

	public World getLoadedWorld(){
		return loadedWorld;
	}

	public MapSettings getMapSettings(){
		return storage.getMapSettings(loadedWorld);
	}

	public boolean canLoad(String worldName){
		for(World world : Bukkit.getWorlds())
			if(world.getName().equals(worldName))
				return false;

		if(!storage.hasMapSettings(worldName))
			return false;

		return new File(Tosochu.getPlugin().getServer().getWorldContainer().getParent(), worldName).exists();
	}

	public void move(String from, String to){
		loadedWorld = load(to);
		MapSettings toSettings = storage.getMapSettings(loadedWorld);
		World fromWorld = Bukkit.getWorld(from);
		for(Player player : fromWorld.getPlayers()){
			toSettings.getFirstSpawnLocation().teleport(loadedWorld, player);
		}
		if(storage.hasMapSettings(fromWorld))
			unload(from);
	}

	public World load(String worldName){
		if(Bukkit.getWorld(worldName) != null)
			new IllegalArgumentException("Already loaded world");

		return WorldCreator.name(worldName).createWorld();
	}

	public void unload(){
		if(loadedWorld == null)
			return;

		unload(loadedWorld.getName());
		loadedWorld = null;
	}

	private void unload(String worldName){
		Bukkit.unloadWorld(worldName, false);
	}

}
