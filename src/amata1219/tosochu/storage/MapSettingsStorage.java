package amata1219.tosochu.storage;

import java.io.File;
import java.util.HashMap;

import org.bukkit.World;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.config.MapSettings;

public class MapSettingsStorage {

	private static MapSettingsStorage storage;
	private static File folder;

	private final HashMap<String, MapSettings> settings = new HashMap<>();

	public static void mkdir(){
		folder = new File(Tosochu.getPlugin().getDataFolder() + File.separator + "MapSettings");

		if(!folder.exists())
			folder.mkdirs();
	}

	public static MapSettingsStorage load(){
		(storage = new MapSettingsStorage()).reload();
		return storage;
	}

	public static MapSettingsStorage getStorage(){
		return storage;
	}

	private MapSettingsStorage(){

	}

	public MapSettings getMapSettings(World world){
		return getMapSettings(world.getName());
	}

	public MapSettings getMapSettings(String worldName){
		return settings.get(worldName);
	}

	public boolean hasMapSettings(World world){
		return hasMapSettings(world.getName());
	}

	public boolean hasMapSettings(String worldName){
		return settings.containsKey(worldName);
	}

	public void reload(){
		//マップをクリアする
		settings.clear();

		//ファイルを読み込む
		for(File file : folder.listFiles()){
			MapSettings settings = new MapSettings(file);
			this.settings.put(settings.pureName, settings);
			System.out.println("Loaded map settings - " + file.getName());
		}
	}

}
