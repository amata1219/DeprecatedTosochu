package amata1219.tosochu;

import java.io.File;
import java.util.HashMap;

import org.bukkit.World;

import amata1219.tosochu.config.MapSettings;

public class MapSettingsStorage {

	public final File folder = new File(Tosochu.getPlugin() + File.separator + "MapSettings");
	private final HashMap<String, MapSettings> settings = new HashMap<>();

	public MapSettingsStorage(){
		if(!folder.exists())
			folder.mkdirs();
		reload();
	}

	public MapSettings get(World world){
		return get(world.getName());
	}

	public MapSettings get(String worldName){
		return settings.get(worldName);
	}

	public boolean isSettingsExist(World world){
		return isSettingsExist(world.getName());
	}

	public boolean isSettingsExist(String worldName){
		return settings.containsKey(worldName);
	}

	public void reload(){
		//マップをクリアする
		settings.clear();

		//ファイルを読み込む
		for(File file : folder.listFiles()){
			MapSettings settings = new MapSettings(file);
			this.settings.put(settings.getName(), settings);
		}
	}

}
