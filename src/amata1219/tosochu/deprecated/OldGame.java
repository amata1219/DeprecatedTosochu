package amata1219.tosochu.deprecated;

import java.util.Map;
import java.util.Set;

import org.bukkit.World;

import amata1219.tosochu.game.ImmutableLocation;

public interface OldGame {

	Set<OldGamePlayer> getPlayers();

	default boolean isPlayer(OldGamePlayer player){
		return getPlayers().contains(player);
	}

	void addPlayer(OldGamePlayer player);

	void removePlayer(OldGamePlayer player);

	Set<OldGamePlayer> getRunaways();

	default boolean isRunaway(OldGamePlayer player){
		return getRunaways().contains(player);
	}

	Set<OldGamePlayer> getDropouts();

	default boolean isDropout(OldGamePlayer player){
		return getDropouts().contains(player);
	}

	Set<OldGamePlayer> getHunterApplicants();

	default boolean isHunterApplicant(OldGamePlayer player){
		return getHunterApplicants().contains(player);
	}

	void applyForHunter(OldGamePlayer player);

	Set<OldGamePlayer> getHunters();

	default boolean isHunter(OldGamePlayer player){
		return getHunters().contains(player);
	}

	Set<OldGamePlayer> getSpectators();

	default boolean isSpectator(OldGamePlayer player){
		return getSpectators().contains(player);
	}

	void setSpectator(OldGamePlayer player);

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

	void touch(OldGamePlayer hunter, OldGamePlayer runaway);

	void jail(OldGamePlayer runaway);

	void respawn(OldGamePlayer dropout);

	void startRecruitingHunters(int number);

	default void broadcast(String message){
		broadcast(message, getPlayers());
	}

	default void broadcast(String message, OldProfession profession){
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

	default void broadcast(String message, Set<OldGamePlayer> receivers){
		for(OldGamePlayer receiver : receivers)
			receiver.sendMessage(message);
	}

}
