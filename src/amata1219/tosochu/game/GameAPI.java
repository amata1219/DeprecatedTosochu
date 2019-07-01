package amata1219.tosochu.game;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.timer.GameTimer;
import amata1219.tosochu.game.timer.PreparationTimer;
import amata1219.tosochu.game.timer.Timer;

public interface GameAPI {

	static final String PREFIX = "§7[§4逃走中§7]§r ";

	//void load(MapSettings settings);

	//void unload();

	void start();

	void forcedTermination();

	default boolean isPreparing(){
		return getTimer() instanceof PreparationTimer;
	}

	default boolean isStarting(){
		return getTimer() instanceof GameTimer;
	}

	Timer getTimer();

	void setTimer(Timer timer);

	MapSettings getLoadedMapSettings();

	default World getWorld(){
		return getLoadedMapSettings().getWorld();
	}

	default Difficulty getDifficulty(){
		return getLoadedMapSettings().getDifficulty();
	}

	void join(Player player);

	void quit(Player player);

	void recruitHunters(int recruitmentNumber);

	boolean isRecruitingHunters();

	List<Player> getPlayers();

	List<Player> getQuittedPlayers();

	List<Player> getMatchedPlayers(Profession profession);

	default List<Player> getRunaways(){
		return getMatchedPlayers(Profession.RUNAWAY);
	}

	default List<Player> getDropouts(){
		return getMatchedPlayers(Profession.DROPOUT);
	}

	default List<Player> getHunters(){
		return getMatchedPlayers(Profession.HUNTER);
	}

	default List<Player> getSpectators(){
		return getMatchedPlayers(Profession.SPECTATOR);
	}

	List<Player> getApplicantsForHunterLottery();

	default boolean isJoined(Player player){
		return getPlayers().contains(player);
	}

	default boolean isQuitted(Player player){
		return getQuittedPlayers().contains(player);
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

	default boolean isSpectator(Player player){
		return getSpectators().contains(player);
	}

	default boolean isApplicantForHunterLottery(Player player){
		return getApplicantsForHunterLottery().contains(player);
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
