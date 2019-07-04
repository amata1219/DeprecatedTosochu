package amata1219.tosochu.game;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.timer.GameTimer;
import amata1219.tosochu.game.timer.PreparationTimer;
import amata1219.tosochu.game.timer.Timer;
import amata1219.tosochu.location.ImmutableLocation;

public interface GameAPI {

	static final String PREFIX = "§7[§4逃走中§7]§r ";

	//void load(MapSettings settings);

	//void unload();

	void start();

	default void forcedTermination(){
		for(Player player : getPlayers()){

			//スコアボードやスペクテイター状態を外す

			getRandomRespawnLocation().teleport(getWorld(), player);
		}
	}

	default boolean isPreparing(){
		return getTimer() instanceof PreparationTimer;
	}

	default boolean isNowPlaying(){
		return getTimer() instanceof GameTimer;
	}

	boolean isEnded();

	Timer getTimer();

	void setTimer(Timer timer);

	default int getElapsedTime(){
		return getTimer() == null ? 0 : getTimer().getElapsedTime();
	}

	MapSettings getLoadedMapSettings();

	default World getWorld(){
		return getLoadedMapSettings().getWorld();
	}

	default Difficulty getDifficulty(){
		return getLoadedMapSettings().getDifficulty();
	}

	ImmutableLocation getRandomRespawnLocation();

	ImmutableLocation getRandomJailLocation();

	int getUnitPriceOfPrizeMoney();

	int setUnitPriceOfPrizeMoney(int money);

	List<Player> getPlayers();

	List<Player> getQuittedPlayers();

	List<Player> getAdiministrators();

	List<Player> getPlayersByProfession(Profession profession);

	default List<Player> getRunaways(){
		return getPlayersByProfession(Profession.RUNAWAY);
	}

	default List<Player> getDropouts(){
		return getPlayersByProfession(Profession.DROPOUT);
	}

	default List<Player> getHunters(){
		return getPlayersByProfession(Profession.HUNTER);
	}

	default List<Player> getReporters(){
		return getPlayersByProfession(Profession.REPORTER);
	}

	default List<Player> getSpectators(){
		return getPlayersByProfession(Profession.SPECTATOR);
	}

	List<Player> getApplicantsForHunterLottery();

	default boolean isJoined(Player player){
		return getPlayers().contains(player);
	}

	default boolean isQuitted(Player player){
		return getQuittedPlayers().contains(player);
	}

	default boolean isAdiministrator(Player player){
		return getAdiministrators().contains(player);
	}

	default Profession getProfession(Player player){
		if(isRunaway(player))
			return Profession.RUNAWAY;
		else if(isDropout(player))
			return Profession.DROPOUT;
		else if(isHunter(player))
			return Profession.HUNTER;
		else if(isReporter(player))
			return Profession.REPORTER;
		else
			return Profession.SPECTATOR;
	}

	default boolean isRunaway(Player player){
		return getRunaways().contains(player);
	}

	default boolean isDropout(Player player){
		return getDropouts().contains(player);
	}

	default boolean isHunter(Player player){
		return getHunters().contains(player);
	}

	default boolean isReporter(Player player){
		return getReporters().contains(player);
	}

	default boolean isSpectator(Player player){
		return getSpectators().contains(player);
	}

	default boolean isApplicantForHunterLottery(Player player){
		return getApplicantsForHunterLottery().contains(player);
	}

	int getMoney(Player player);

	int setMoney(Player player, int money);

	default void depositMoney(Player player, int money){
		setMoney(player, getMoney(player) + money);
	}

	default void withdrawMoney(Player player, int money){
		setMoney(player, Math.min(getMoney(player) - money, 0));
	}

	Difficulty getDifficulty(Player player);

	void setDifficulty(Player player, Difficulty difficulty);

	void join(Player player);

	void quit(Player player);

	int fall(Player runaway);

	void recruitHunters(int recruitmentNumberOfHunters);

	boolean isRecruitingHunters();

	default boolean isFindRunaway(Player hunter){
		for(Entity entity : hunter.getNearbyEntities(25, 25, 25))
			if(entity instanceof Player)
				if(isRunaway((Player) entity))
					return true;

		return false;
	}

	default boolean isLockOnRunaway(Player hunter){
		return hunter.getFoodLevel() > 6;
	}

	default void setLockOnRunaway(Player hunter, boolean lockOn){
		hunter.setFoodLevel(lockOn ? 20 : 4);
	}

	default void broadcastMessage(String message){
		String text = PREFIX + message;
		for(Player player : getPlayers())
			player.sendMessage(text);
	}

	default void broadcastTitle(String title, int fadeIn, int stay, int fadeOut){
		broadcastTitle(title, "", fadeIn, stay, fadeOut);
	}

	default void broadcastTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut){
		for(Player player : getPlayers())
			player.sendTitle(title, subTitle, fadeIn, stay, fadeOut);
	}

	default void broadcastSound(Sound sound, float volume, float pitch){
		for(Player player : getPlayers())
			player.playSound(player.getLocation(), sound, volume, pitch);
	}

}
