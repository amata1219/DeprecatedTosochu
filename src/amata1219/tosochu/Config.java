package amata1219.tosochu;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private final Tosochu plugin = Tosochu.getPlugin();

	public final String name;
	private final File file;
	private FileConfiguration config;

	public Config(String name){
		this.name = name;
		this.file = new File(plugin.getDataFolder(), name);
	}

	public Config create(){
		return create(name);
	}

	public Config create(String resourceFileName){
		if(!file.exists())
			plugin.saveResource(resourceFileName, false);

		return this;
	}

	public FileConfiguration get(){
		return config == null ? reload() : config;
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
		config  = YamlConfiguration.loadConfiguration(file);

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
