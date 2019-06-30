package amata1219.tosochu.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.game.Difficulty;
import amata1219.tosochu.location.ImmutableLocation;

public class MapSettings extends Config {

	//このマップに対応するワールド
	public final World world;
	public final Difficulty difficulty;

	public MapSettings(File file){
		super(file);

		world = Bukkit.getWorld(getName());
		difficulty = Difficulty.valueOf(getString("Dfficulty").toUpperCase());
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

	public void setDifficulty(Difficulty difficulty){
		set("Difficulty", difficulty.toString());
	}

	public int getUnitPriceOfPrizeMoney(){
		return Integer.valueOf(getPart("Unit price of prize money"));
	}

	public void setUnitPriceOfPrizeMoney(int money){
		String[] values = getString("Unit price of prize money").split(",");
		values[difficulty.ordinal()] = String.valueOf(money);
		set("Unit price of prize money", String.join(",", values));
	}

	public int getRespawnCooldownTime(){
		return Integer.valueOf(getPart("Respawn cooldown time"));
	}

	public void setRespawnCooldownTime(int time){
		String[] values = getString("Respawn cooldown time").split(",");
		values[difficulty.ordinal()] = String.valueOf(time);
		set("Respawn cooldown time", String.join(",", values));
	}

	public double getCorrectionValueForCooldownTimeOfItem(){
		return Double.valueOf(getPart("Correction value for cooldown time of item"));
	}

	public void setCorrectionValueForCooldownTimeOfItem(int value){
		String[] values = getString("Correction value for cooldown time of item").split(",");
		values[difficulty.ordinal()] = String.valueOf(value);
		set("Correction value for cooldown time of item", String.join(",", values));
	}

	public int getCorrectionValueForAmountOfItems(){
		return Integer.valueOf(getPart("Correction value for amount of items"));
	}

	public void setCorrectionValueForAmountOfItems(int value){
		String[] values = getString("Correction value for amount of items").split(",");
		values[difficulty.ordinal()] = String.valueOf(value);
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

	public ImmutableLocation getFirstSpawnPoint(){
		return ImmutableLocation.at(getString("Point.FirstSpawn"));
	}

	public void setFirstSpawnPoint(int x, int y, int z){
		set("Point.FirstSpawn", x + "," + y + "," + z);
	}

	public ImmutableLocation getHunterSpawnPoint(){
		return ImmutableLocation.at(getString("Point.HunterSpawn"));
	}

	public void setHunterSpawnPoint(int x, int y, int z){
		set("Point.HunterSpawn", x + "," + y + "," + z);
	}

	public HashMap<Integer, ImmutableLocation> getRunawayRespawnPoints(){
		HashMap<Integer, ImmutableLocation> points = new HashMap<>();
		if(get().isConfigurationSection("Point.RunawayRespawn"))
			return points;

		for(String key : get().getConfigurationSection("Point.RunawayRespawn").getKeys(false))
			points.put(Integer.valueOf(key), ImmutableLocation.at(getString("Point.RunawayRespawn." + key)));

		return points;
	}

	public void setRunawayRespawnPoints(HashMap<Integer, ImmutableLocation> points){
		set("Point.RunawayRespawn", null);

		for(Entry<Integer, ImmutableLocation> entry : points.entrySet())
			set("Point.RunawayRespawn." + entry.getKey(), entry.getValue().toString());
	}

	public HashMap<Integer, ImmutableLocation> getJailSpawnPoints(){
		HashMap<Integer, ImmutableLocation> points = new HashMap<>();
		if(get().isConfigurationSection("Point.JailSpawn"))
			return points;

		for(String key : get().getConfigurationSection("Point.JailSpawn").getKeys(false))
			points.put(Integer.valueOf(key), ImmutableLocation.at(world, getString("Point.JailSpawn." + key)));

		return points;
	}

	public void setJailSpawnPoints(HashMap<Integer, ImmutableLocation> points){
		set("Point.JailSpawn", null);

		for(Entry<Integer, ImmutableLocation> entry : points.entrySet())
			set("Point.JailSpawn." + entry.getKey(), entry.getValue().toString());
	}

	public int[] getFallImpact(){
		int[] levels = new int[256];
		if(get().isConfigurationSection("Runaway.FallImpact"))
			return levels;

		for(String key : get().getConfigurationSection("Runaway.FallImpact").getKeys(false))
			levels[Integer.valueOf(key) - 1] = getInt("Runaway.FallImpact." + key);

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
		return getInt("Hunter.NumberOfFirstRequiredHunters");
	}

	public void setNumberOfFirstRequiredHunters(int hunters){
		set("NumberOfFirstRequiredHunters", hunters);
	}

	public int getHunterSpeedLevel(){
		return getInt("Hunter.SpeedLevel");
	}

	public void setHunterSpeedLevel(int level){
		set("Hunter.SpeedLevel", level);
	}

	private String getString(String key){
		return get().getString(key);
	}

	private String getPart(String key){
		return difficulty.split(getString(key));
	}

	private int getInt(String key){
		return get().getInt(key);
	}

	private double getDouble(String key){
		return get().getDouble(key);
	}

	private void set(String key, Object value){
		get().set(key, value);
		update();
	}

}
