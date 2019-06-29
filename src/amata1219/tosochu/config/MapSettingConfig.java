package amata1219.tosochu.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.World;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.game.Difficulty;
import amata1219.tosochu.location.ImmutableLocation;

public class MapSettingConfig extends Config {

	public final World world;

	public MapSettingConfig(String name) {
		super(name, new File(Tosochu.getPlugin().getMapSettingsFolder(), name));
		world = Bukkit.getWorld(getName());
	}

	public int getPreparationTime(){
		return getInt("PreparationTime");
	}

	public void setPreparationTime(int preparationTime){
		set("PreparationTime", preparationTime);
	}

	public int getTimeLimit(){
		return getInt("TimeLimit");
	}

	public void setTimeLimit(int timeLimit){
		set("TimeLimit", timeLimit);
	}

	public Difficulty getDifficulty(){
		return Difficulty.valueOf(getString("Dfficulty").toUpperCase());
	}

	public void setDifficulty(Difficulty difficulty){
		set("Difficulty", difficulty.toString());
	}

	public int getUnitPriceOfPrizeMoney(){
		return getDifficulty().getInteger(getString("UnitPriceOfPrizeMoney"));
	}

	public void setUnitPriceOfPrizeMoney(int unitPriceOfPrizeMoney){
		String[] values = getString("UnitPriceOfPrizeMoney").split(",");
		values[getDifficulty().ordinal()] = String.valueOf(unitPriceOfPrizeMoney);
		set("UnitPriceOfPrizeMoney", String.join(",", values));
	}

	public int getRespawnCooldownTime(){
		return Integer.valueOf(getDifficulty().getString(getString("RespawnCooldownTime")).split("@")[0]);
	}

	public void setRespawnCooldownTime(int respawnCooldownTime){
		String[] values = getString("RespawnCooldownTime").split(",");
		int ordinal = getDifficulty().ordinal();
		String[] times = values[ordinal].split("@");
		values[ordinal] = times.length == 1 ? String.valueOf(respawnCooldownTime) : respawnCooldownTime + "@" + times[1];
		set("RespawnCooldownTime", String.join(",", values));
	}

	public int getSecondaryRespawnCooldownTime(){
		String[] times = getDifficulty().getString(getString("RespawnCooldownTime")).split("@");
		return Integer.valueOf(times.length == 1 ? times[0] : times[1]);
	}

	public void setSecondaryRespawnCooldownTime(int secondaryRespawnCooldownTime){
		String[] values = getString("RespawnCooldownTime").split(",");
		int ordinal = getDifficulty().ordinal();
		String[] times = values[ordinal].split("@");
		values[ordinal] = times[0] + "@" + secondaryRespawnCooldownTime;
		set("RespawnCooldownTime", String.join(",", values));
	}

	public double getCorrectionValueForItemCooldownTime(){
		return getDifficulty().getDouble(getString("CorrectionValueForItemCoolTime"));
	}

	public void setCorrectionValueForItemCooldownTime(int correctionValueForItemCooldownTime){
		String[] values = getString("CorrectionValueForItemCoolTime").split(",");
		values[getDifficulty().ordinal()] = String.valueOf(correctionValueForItemCooldownTime);
		set("CorrectionValueForItemCoolTime", String.join(",", values));
	}

	public int getCorrectionValueForItemStackSize(){
		String value = getDifficulty().getString(getString("CorrectionValueForItemStackSize"));
		return Integer.valueOf(value.indexOf("@") == 1 ? value.substring(1) : value);
	}

	public void setCorrectionValueForItemStackSize(int correctionValueForItemStackSize, boolean specifyItemStackSize){
		String[] values = getString("CorrectionValueForItemStackSize").split(",");
		values[getDifficulty().ordinal()] = (specifyItemStackSize ? "@" : "") + correctionValueForItemStackSize;
		set("CorrectionValueForItemStackSize", String.join(",", values));
	}

	public boolean isCorrectionValueToSpecifyItemStackSize(){
		return getDifficulty().getString(getString("CorrectionValueForItemStackSize")).indexOf("@") == 1;
	}

	public int getForceSpectatorTimeThreshold(){
		return getInt("ForceSpectatorTimeThreshold");
	}

	public void setForceSpectatorTimeThreshold(int forceSpectatorTimeThreshold){
		set("ForceSpectatorTimeThreshold", forceSpectatorTimeThreshold);
	}

	public int getApplyRejoinPenalty(){
		return getInt("ApplyRejoinPenalty");
	}

	public void setApplyRejoinPenalty(int applyRejoinPenalty){
		set("ApplyRejoinPenalty", applyRejoinPenalty);
	}

	public int getNPCTimeToLive(){
		return getInt("NPCTimeToLive");
	}

	public void setNPCTimeToLive(int npcTimeToLive){
		set("NPCTimeToLive", npcTimeToLive);
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
			points.put(Integer.valueOf(key), ImmutableLocation.at(getString("Point.JailSpawn." + key)));

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

	private void set(String key, Object value){
		get().set(key, value);
		update();
	}

}
