package amata1219.tosochu;

import java.util.Map;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import amata1219.tosochu.player.GamePlayer;
import amata1219.tosochu.player.Profession;

public interface Game {

	Set<GamePlayer> getPlayers();

	default boolean isPlayer(GamePlayer player){
		return getPlayers().contains(player);
	}

	void addPlayer(GamePlayer player);

	void removePlayer(GamePlayer player);

	Set<GamePlayer> getRunaways();

	default boolean isRunaway(GamePlayer player){
		return getRunaways().contains(player);
	}

	Set<GamePlayer> getDropouts();

	default boolean isDropout(GamePlayer player){
		return getDropouts().contains(player);
	}

	Set<GamePlayer> getHunterApplicants();

	default boolean isHunterApplicant(GamePlayer player){
		return getHunterApplicants().contains(player);
	}

	void applyForHunter(GamePlayer player);

	Set<GamePlayer> getHunters();

	default boolean isHunter(GamePlayer player){
		return getHunters().contains(player);
	}

	default void setHunter(GamePlayer player){
		if(isHunter(player) || isSpectator(player))
			return;

		Player hunter = player.getPlayer();
		hunter.setFoodLevel(2);
		hunter.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (getTimeLimit() - getElapsedTime()) * 20, 1));
	}

	Set<GamePlayer> getSpectators();

	default boolean isSpectator(GamePlayer player){
		return getSpectators().contains(player);
	}

	void setSpectator(GamePlayer player);

	boolean isStarted();

	String canStart();

	void start();

	boolean isStopped();

	void stop();

	World getWorld();

	void setWorld(World world);

	ImmutableLocation getFirstSpawnLocation();

	void setFirstSpawnLocation(ImmutableLocation location);

	Map<Integer, ImmutableLocation> getRespawnLocations();

	ImmutableLocation getHunterSpawnLocation();

	void setHunterSpawnLocation(ImmutableLocation location);

	Map<Integer, ImmutableLocation> getJailLocations();

	int getTimeLimit();

	void setTimeLimit(int time);

	int getElapsedTime();

	int getMoneyPerSecond();

	void setMoneyPerSecond(int money);

	int getNumberOfHunters();

	void setNumberOfHunters(int number);

	void touch(GamePlayer hunter, GamePlayer runaway);

	void jail(GamePlayer runaway);

	void respawn(GamePlayer dropout);

	void startRecruitingHunters(int number);

	default void broadcast(String message){
		broadcast(message, getPlayers());
	}

	default void broadcast(String message, Profession profession){
		switch(profession){
		case RUNAWAYS:
			broadcast(message, getRunaways());
			break;
		case DROPOUTS:
			broadcast(message, getDropouts());
			break;
		case HUNTERS:
			broadcast(message, getHunters());
			break;
		case SPECTATORS:
			broadcast(message, getSpectators());
			break;
		default:
			break;
		}
	}

	default void broadcast(String message, Set<GamePlayer> receivers){
		for(GamePlayer receiver : receivers)
			receiver.sendMessage(message);
	}

}
