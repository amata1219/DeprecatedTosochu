package amata1219.tosochu.game;

import java.util.List;
import java.util.stream.Collectors;

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
		for(Player player : getOnlinePlayers()){

			//スコアボードやスペクテイター状態を外す

			getRandomRespawnLocation().teleport(getWorld(), player);
		}
	}

	default boolean isBeforeStart(){
		return !isPreparing() && !isNowPlaying();
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

	ImmutableLocation getRandomJailSpawnLocation();

	List<GamePlayer> getGamePlayers();

	default List<Player> getOnlinePlayers(){
		return getGamePlayers()
				.stream()
				.filter(GamePlayer::isOnline)
				.map(GamePlayer::getPlayer)
				.collect(Collectors.toList());
	}

	List<GamePlayer> getQuittedPlayers();

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

	boolean isJoined(Player player);

	boolean isQuitted(Player player);

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

	void join(Player player);

	void quit(Player player);

	void fall(Player runaway);

	void touchedByHunter(Player runaway);

	void tryRespawn(Player dropout);

	void recruitHunters(int recruitmentNumberOfHunters, int waitTime);

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
		getOnlinePlayers().forEach(player -> player.sendMessage(text));
	}

	default void broadcastTitle(String title, int fadeIn, int stay, int fadeOut){
		broadcastTitle(title, "", fadeIn, stay, fadeOut);
	}

	default void broadcastTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut){
		getOnlinePlayers().forEach(player -> player.sendTitle(title, subTitle, fadeIn, stay, fadeOut));
	}

	default void broadcastSound(Sound sound, float volume, float pitch){
		getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
	}

}
