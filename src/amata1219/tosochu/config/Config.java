package amata1219.tosochu.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import amata1219.tosochu.Tosochu;

public class Config {

	private final Tosochu plugin = Tosochu.getPlugin();

	private final File file;
	private final String name;
	public final String pureName;
	private FileConfiguration config;

	public Config(String name){
		this(new File(Tosochu.getPlugin().getDataFolder(), name));
		//UTF-8のファイルにすること
	}

	public Config(File file){
		this.file = file;
		this.name = file.getName();
		pureName = name.substring(0, name.length() - 4);
	}

	public void create(){
		create(name);
	}

	public void create(String resourceFileName){
		if(!file.exists())
			plugin.saveResource(resourceFileName, false);
	}

	public FileConfiguration get(){
		return config == null ? reload() : config;
	}

	public Map<String, ConfigurationSection> getShallowSections(){
		Map<String, ConfigurationSection> hierarchies = new HashMap<>();
		FileConfiguration config = get();
		for(String key : config.getKeys(false))
			hierarchies.put(key, config.getConfigurationSection(key));
		return hierarchies;
	}

	public void save(){
		if(config == null)
			return;

		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public FileConfiguration reload(){
		config = YamlConfiguration.loadConfiguration(file);

		InputStream in = plugin.getResource(name);
		if(in == null)
			return config;

		config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(in, StandardCharsets.UTF_8)));

		return config;
	}

	public void update(){
		save();
		reload();
	}

}
