package amata1219.tosochu.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import amata1219.tosochu.game.Difficulty;
import amata1219.tosochu.location.ImmutableLocation;
import amata1219.tosochu.text.TimeFormatter;

public class MapSettings extends Config {

	private int preparationTime;
	private int timeLimit;
	private Difficulty difficulty;
	private int[] unitPriceOfPrizeMoney;
	private int[] respawnCooldownTime;
	private double[] correctionValueForCooldownTimeOfItem;
	private double[] correctionValueForAmountOfItems;
	private int whenRestrictParticipation;
	private int whenApplyRejoinPenalty;
	private int timeToLiveOfNPC;
	private ImmutableLocation firstSpawnLocation;
	private ImmutableLocation hunterSpawnLocation;
	private Map<Integer, ImmutableLocation> runawayRespawnLocations = new HashMap<>();
	private Map<Integer, ImmutableLocation> jailSpawnLocations = new HashMap<>();
	private int[] levelsOfSlownessEffectAppliedWhenPlayerLands;
	private boolean alwaysDisplayScoreboard;


	public MapSettings(File file){
		super(file);
	}

	public void reloadValues(){
		preparationTime = TimeFormatter.toSeconds(getString("Preparation time"));
		timeLimit = TimeFormatter.toSeconds(getString("Time limit"));
		difficulty = Difficulty.valueOf(getString("Dfficulty").toUpperCase());
		unitPriceOfPrizeMoney = Difficulty.splitAndToInt(getString("Unit price of prize money"));
		respawnCooldownTime = Difficulty.splitAndToInt(getString("Respawn cooldown time"));
		correctionValueForCooldownTimeOfItem = Difficulty.splitAndToDouble(getString("Correction value for cooldown time of item"));
		correctionValueForAmountOfItems = Difficulty.splitAndToDouble(getString("Correction value for amount of items"));
		whenRestrictParticipation = getInt("When restrict participation");
		whenApplyRejoinPenalty = getInt("When apply rejoin penalty");
		timeToLiveOfNPC = getInt("Time to live of NPC");
		firstSpawnLocation = ImmutableLocation.at(getString("First spawn location"));
		hunterSpawnLocation = ImmutableLocation.at(getString("Hunter spawn location"));

		runawayRespawnLocations.clear();
		ConfigurationSection sectionOfRunawayRespawnLocations = get().getConfigurationSection("Runaway respawn locations");
		for(String key : sectionOfRunawayRespawnLocations.getKeys(false))
			runawayRespawnLocations.put(Integer.valueOf(key), ImmutableLocation.at(sectionOfRunawayRespawnLocations.getString(key)));

		jailSpawnLocations.clear();
		ConfigurationSection sectionOfJailSpawnLocations = get().getConfigurationSection("Jail spawn locations");
		for(String key : sectionOfJailSpawnLocations.getKeys(false))
			jailSpawnLocations.put(Integer.valueOf(key), ImmutableLocation.at(sectionOfJailSpawnLocations.getString(key)));

		levelsOfSlownessEffectAppliedWhenPlayerLands = new int[256];
		ConfigurationSection sectionOfLevels = get().getConfigurationSection("Levels of slowness effect applied when player lands");
		for(String key : sectionOfLevels.getKeys(false))
			levelsOfSlownessEffectAppliedWhenPlayerLands[Integer.valueOf(key) - 1] = sectionOfLevels.getInt(key);

		int tmpLevel = levelsOfSlownessEffectAppliedWhenPlayerLands[0];
		for(int i = 1; i < levelsOfSlownessEffectAppliedWhenPlayerLands.length; i++){
			int level = levelsOfSlownessEffectAppliedWhenPlayerLands[i];
			if(level != tmpLevel)
				tmpLevel = level;

			levelsOfSlownessEffectAppliedWhenPlayerLands[i] = tmpLevel;
		}

		alwaysDisplayScoreboard = getBoolean("Always display scoreboard");
	}

	public World getWorld(){
		return Bukkit.getWorld(pureName);
	}

	public int getPreparationTime(){
		return preparationTime;
	}

	public void setPreparationTime(int time){
		set("Preparation time", TimeFormatter.format(preparationTime = time));
	}

	public int getTimeLimit(){
		return timeLimit;
	}

	public void setTimeLimit(int limit){
		set("Time limit", TimeFormatter.format(timeLimit = limit));
	}

	public Difficulty getDifficulty(){
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty){
		set("Difficulty", (this.difficulty = difficulty).toString());
	}

	public int getUnitPriceOfPrizeMoney(Difficulty difficulty){
		return unitPriceOfPrizeMoney[difficulty.ordinal()];
	}

	public void setUnitPriceOfPrizeMoney(Difficulty difficulty, int money){
		unitPriceOfPrizeMoney[difficulty.ordinal()] = money;
		set("Unit price of prize money", Difficulty.join(unitPriceOfPrizeMoney));
	}

	public int getRespawnCooldownTime(Difficulty difficulty){
		return respawnCooldownTime[difficulty.ordinal()];
	}

	public void setRespawnCooldownTime(Difficulty difficulty, int time){
		respawnCooldownTime[difficulty.ordinal()] = time;
		set("Respawn cooldown time", Difficulty.join(respawnCooldownTime));
	}

	public double getCorrectionValueForCooldownTimeOfItem(Difficulty difficulty){
		return correctionValueForCooldownTimeOfItem[difficulty.ordinal()];
	}

	public void setCorrectionValueForCooldownTimeOfItem(Difficulty difficulty, int value){
		correctionValueForCooldownTimeOfItem[difficulty.ordinal()] = value;
		set("Correction value for cooldown time of item", Difficulty.join(correctionValueForCooldownTimeOfItem));
	}

	public double getCorrectionValueForAmountOfItems(Difficulty difficulty){
		return correctionValueForAmountOfItems[difficulty.ordinal()];
	}

	public void setCorrectionValueForAmountOfItems(Difficulty difficulty, int value){
		correctionValueForAmountOfItems[difficulty.ordinal()] = value;
		set("Correction value for amount of items", Difficulty.join(correctionValueForAmountOfItems));
	}

	public int getWhenRestrictParticipation(){
		return whenRestrictParticipation;
	}

	public void setWhenRestrictParticipation(int when){
		set("When restrict participation", whenRestrictParticipation = when);
	}

	public int getWhenApplyRejoinPenalty(){
		return whenApplyRejoinPenalty;
	}

	public void setWhenApplyRejoinPenalty(int when){
		set("When apply rejoin penalty", whenApplyRejoinPenalty = when);
	}

	public int getTimeToLiveOfNPC(){
		return timeToLiveOfNPC;
	}

	public void setTimeToLiveOfNPC(int time){
		set("Time to live of NPC", timeToLiveOfNPC = time);
	}

	public ImmutableLocation getFirstSpawnLocation(){
		return firstSpawnLocation;
	}

	public void setFirstSpawnLocation(int x, int y, int z){
		set("First spawn location", (firstSpawnLocation = ImmutableLocation.at(x, y, z)).toString());
	}

	public ImmutableLocation getHunterSpawnLocation(){
		return hunterSpawnLocation;
	}

	public void setHunterSpawnLocation(int x, int y, int z){
		set("Hunter spawn location", (hunterSpawnLocation = ImmutableLocation.at(x, y, z)).toString());
	}

	public List<ImmutableLocation> getRunawayRespawnLocations(){
		return new ArrayList<>(runawayRespawnLocations.values());
	}

	public Map<Integer, ImmutableLocation> getRunawayRespawnLocationsMap(){
		return runawayRespawnLocations;
	}

	public void setRunawayRespawnLocations(Map<Integer, ImmutableLocation> locations){
		runawayRespawnLocations = locations;
		for(Entry<Integer, ImmutableLocation> entry : locations.entrySet())
			set("Runaway respawn locations." + entry.getKey(), entry.getValue().toString());
	}

	public List<ImmutableLocation> getJailSpawnLocations(){
		return new ArrayList<>(jailSpawnLocations.values());
	}

	public void setJailSpawnLocations(Map<Integer, ImmutableLocation> locations){
		jailSpawnLocations = locations;
		for(Entry<Integer, ImmutableLocation> entry : locations.entrySet())
			set("Jail spawn locations." + entry.getKey(), entry.getValue().toString());
	}

	public int[] getLevelsOfSlownessEffectAppliedWhenPlayerLands(){
		return levelsOfSlownessEffectAppliedWhenPlayerLands;
	}

	public boolean isAlwaysDisplayScoreboard(){
		return alwaysDisplayScoreboard;
	}

	public void setAlwaysDisplayScoreboard(boolean always){
		set("Always display scoreboard", alwaysDisplayScoreboard = always);
	}

	private String getString(String key){
		return get().getString(key);
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

}
