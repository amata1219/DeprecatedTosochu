package amata1219.tosochu.config;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import amata1219.tosochu.game.Difficulty;
import amata1219.tosochu.location.ImmutableLocation;

public class MapSettings extends Config {

	public MapSettings(File file){
		super(file);
	}

	public String getWorldName(){
		return getName();
	}

	public World getWorld(){
		return Bukkit.getWorld(getWorldName());
	}

	public int getPreparationTime(){
		return getInt("Preparation time");
	}

	public void setPreparationTime(int time){
		set("Preparation time", time);
	}

	public int getTimeLimit(){
		return getInt("Time limit");
	}

	public void setTimeLimit(int limit){
		set("Time limit", limit);
	}

	public Difficulty getDifficulty(){
		return Difficulty.valueOf(getString("Dfficulty").toUpperCase());
	}

	public void setDifficulty(Difficulty difficulty){
		Validate.notNull(difficulty, "Difficulty can not be null");
		set("Difficulty", getDifficulty().toString());
	}

	public int getUnitPriceOfPrizeMoney(){
		return Integer.valueOf(getPart("Unit price of prize money"));
	}

	public void setUnitPriceOfPrizeMoney(int money){
		String[] values = getString("Unit price of prize money").split(",");
		values[getDifficulty().ordinal()] = String.valueOf(money);
		set("Unit price of prize money", String.join(",", values));
	}

	public int getRespawnCooldownTime(){
		return Integer.valueOf(getPart("Respawn cooldown time"));
	}

	public void setRespawnCooldownTime(int time){
		String[] values = getString("Respawn cooldown time").split(",");
		values[getDifficulty().ordinal()] = String.valueOf(time);
		set("Respawn cooldown time", String.join(",", values));
	}

	public double getCorrectionValueForCooldownTimeOfItem(){
		return Double.valueOf(getPart("Correction value for cooldown time of item"));
	}

	public void setCorrectionValueForCooldownTimeOfItem(int value){
		String[] values = getString("Correction value for cooldown time of item").split(",");
		values[getDifficulty().ordinal()] = String.valueOf(value);
		set("Correction value for cooldown time of item", String.join(",", values));
	}

	public int getCorrectionValueForAmountOfItems(){
		return Integer.valueOf(getPart("Correction value for amount of items"));
	}

	public void setCorrectionValueForAmountOfItems(int value){
		String[] values = getString("Correction value for amount of items").split(",");
		values[getDifficulty().ordinal()] = String.valueOf(value);
		set("Correction value for amount of items", String.join(",", values));
	}

	public int getWhenRestrictParticipation(){
		return getInt("When restrict participation");
	}

	public void setWhenRestrictParticipation(int when){
		set("When restrict participation", when);
	}

	public int getWhenApplyRejoinPenalty(){
		return getInt("When apply rejoin penalty");
	}

	public void setWhenApplyRejoinPenalty(int when){
		set("When apply rejoin penalty", when);
	}

	public int getTimeToLiveOfNPC(){
		return getInt("Time to live of NPC");
	}

	public void setTimeToLiveOfNPC(int time){
		set("Time to live of NPC", time);
	}

	public ImmutableLocation getFirstSpawnLocation(){
		return ImmutableLocation.at(getString("First spawn location"));
	}

	public void setFirstSpawnLocation(int x, int y, int z){
		set("First spawn location", x + "," + y + "," + z);
	}

	public ImmutableLocation getHunterSpawnLocation(){
		return ImmutableLocation.at(getString("Hunter spawn location"));
	}

	public void setHunterSpawnLocation(int x, int y, int z){
		set("Hunter spawn location", x + "," + y + "," + z);
	}

	public List<ImmutableLocation> getRunawayRespawnLocations(){
		ConfigurationSection section = get().getConfigurationSection("Runaway respawn locations");
		return section.getKeys(false)
				.stream()
				.map(key -> section.getString(key))
				.map(xyz -> ImmutableLocation.at(xyz))
				.collect(Collectors.toList());
	}

	public void addRunawayRespawnLocation(int key, ImmutableLocation location){
		set("Runaway respawn locations." + key, location.toString());
	}

	public void removeRunawayRespawnLocation(int key){
		remove("Runaway respawn locations." + key);
	}

	public void setRunawayRespawnLocations(Map<Integer, ImmutableLocation> locations){
		for(Entry<Integer, ImmutableLocation> entry : locations.entrySet())
			set("Runaway respawn locations." + entry.getKey(), entry.getValue().toString());
	}

	public List<ImmutableLocation> getJailSpawnLocations(){
		ConfigurationSection section = get().getConfigurationSection("Jail spawn locations");
		return section.getKeys(false)
				.stream()
				.map(key -> section.getString(key))
				.map(xyz -> ImmutableLocation.at(xyz))
				.collect(Collectors.toList());
	}

	public void addJailSpawnLocation(int key, ImmutableLocation location){
		set("Jail spawn locations." + key, location.toString());
	}

	public void removeJailSpawnLocation(int key){
		remove("Jail spawn locations." + key);
	}

	public void setJailSpawnLocations(Map<Integer, ImmutableLocation> locations){
		for(Entry<Integer, ImmutableLocation> entry : locations.entrySet())
			set("Jail spawn locations." + entry.getKey(), entry.getValue().toString());
	}

	public int[] getFallImpact(){
		int[] levels = new int[256];
		ConfigurationSection section = get().getConfigurationSection("Fall impact");
		for(String key : section.getKeys(false))
			levels[Integer.valueOf(key) - 1] = section.getInt(key);

		int tmp = levels[0];
		for(int i = 1; i < levels.length; i++){
			int level = levels[i];
			if(level != tmp)
				tmp = level;

			levels[i] = tmp;
		}

		return levels;
	}

	public int getNumberOfFirstRequiredHunters(){
		return getInt("Number of first required hunters");
	}

	public void setNumberOfFirstRequiredHunters(int hunters){
		set("Number of first required hunters", hunters);
	}

	public int getHunterSpeedLevel(){
		return getInt("Level of speed effect that gave to hunter");
	}

	public void setHunterSpeedLevel(int level){
		set("Level of speed effect that gave to hunter", level);
	}

	public boolean isAlwaysDisplayScoreboard(){
		return !getBoolean("Always display scoreboard");
	}

	public void setAlwaysDisplayScoreboard(boolean always){
		set("Always display scoreboard", always);
	}

	private String getString(String key){
		return get().getString(key);
	}

	private String getPart(String key){
		return getDifficulty().split(getString(key));
	}

	private boolean getBoolean(String key){
		return get().getBoolean(key);
	}

	private int getInt(String key){
		return get().getInt(key);
	}

	private void set(String key, Object value){
		get().set(key, value);
		update();
	}

	private void remove(String key){
		set(key, null);
	}

}
