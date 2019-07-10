package amata1219.tosochu.game;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
		for(GamePlayer player : getOnlinePlayers()){

			//スコアボードやスペクテイター状態を外す

			player.teleport(getRandomRespawnLocation());
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

	MapSettings getSettings();

	default World getWorld(){
		return getSettings().getWorld();
	}

	default Difficulty getDifficulty(){
		return getSettings().getDifficulty();
	}

	ImmutableLocation getRandomRespawnLocation();

	ImmutableLocation getRandomJailSpawnLocation();

	List<GamePlayer> getGamePlayers();

	GamePlayer getGamePlayer(Player player);

	default List<GamePlayer> getOnlinePlayers(){
		return getGamePlayers()
				.stream()
				.filter(GamePlayer::isOnline)
				.collect(Collectors.toList());
	}

	default List<GamePlayer> getQuittedPlayers(){
		return getGamePlayers()
				.stream()
				.filter(player -> !player.isOnline())
				.collect(Collectors.toList());
	}

	List<GamePlayer> getPlayersByProfession(Profession profession);

	default List<GamePlayer> getRunaways(){
		return getPlayersByProfession(Profession.RUNAWAY);
	}

	default List<GamePlayer> getDropouts(){
		return getPlayersByProfession(Profession.DROPOUT);
	}

	default List<GamePlayer> getHunters(){
		return getPlayersByProfession(Profession.HUNTER);
	}

	default List<GamePlayer> getReporters(){
		return getPlayersByProfession(Profession.REPORTER);
	}

	default List<GamePlayer> getSpectators(){
		return getPlayersByProfession(Profession.SPECTATOR);
	}

	default List<GamePlayer> getNothings(){
		return getPlayersByProfession(Profession.NOTHING);
	}

	List<GamePlayer> getApplicantsForHunterLottery();

	boolean isJoined(Player player);

	boolean isQuitted(Player player);

	default Profession getProfession(GamePlayer player){
		if(isRunaway(player))
			return Profession.RUNAWAY;
		else if(isDropout(player))
			return Profession.DROPOUT;
		else if(isHunter(player))
			return Profession.HUNTER;
		else if(isReporter(player))
			return Profession.REPORTER;
		else if(isSpectator(player))
			return Profession.SPECTATOR;
		else
			return Profession.NOTHING;
	}

	default boolean isRunaway(GamePlayer player){
		return getRunaways().contains(player);
	}

	default void setRunaway(GamePlayer player){
		if(!isNothing(player))
			return;

		player.setProfession(Profession.RUNAWAY);

		getDropouts().remove(player);
		getRunaways().add(player);

		player.sendMessage("逃走者になりました。");

		player.teleport(getSettings().getFirstSpawnLocation());
	}

	default boolean isDropout(GamePlayer player){
		return getDropouts().contains(player);
	}

	default void setDropout(GamePlayer player){
		if(!isRunaway(player))
			return;

		player.setProfession(Profession.DROPOUT);

		getRunaways().remove(player);
		getDropouts().add(player);

		player.sendMessage("確保されました。");

		player.teleport(getRandomRespawnLocation());
	}

	default boolean isHunter(GamePlayer player){
		return getHunters().contains(player);
	}

	default void setHunter(GamePlayer player){
		if(!isRunaway(player) && !isNothing(player))
			return;

		player.setProfession(Profession.HUNTER);

		getRunaways().remove(player);
		getHunters().add(player);

		player.sendMessage("ハンターになりました。");

		Player bukkitPlayer = player.getPlayer();

		bukkitPlayer.setFoodLevel(4);
		bukkitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 2));

		player.teleport(getSettings().getHunterSpawnLocation());
	}

	default boolean isReporter(GamePlayer player){
		return getReporters().contains(player);
	}

	default void setReporter(GamePlayer player){
		if(!isRunaway(player) && !isNothing(player))
			return;

		player.setProfession(Profession.REPORTER);

		getRunaways().remove(player);

		player.sendMessage("通報部隊になりました。");

	}

	default boolean isSpectator(GamePlayer player){
		return getSpectators().contains(player);
	}

	default boolean isNothing(GamePlayer player){
		return getProfession(player) == Profession.NOTHING;
	}

	default boolean isApplicantForHunterLottery(GamePlayer player){
		return getApplicantsForHunterLottery().contains(player);
	}

	void join(Player player);

	void quit(Player player);

	default void fall(Player runaway){
		runaway.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, getSettings().getLevelsOfSlownessEffectAppliedWhenPlayerLands()[Math.min((int) runaway.getFallDistance(), 255)]));
	}

	default void touchedByHunter(GamePlayer runaway){
		if(!isRunaway(runaway))
			return;

		getRunaways().remove(runaway);
		getDropouts().add(runaway);

		runaway.teleport(getSettings().getSelectorOfJailSpawnLocation().select());
	}

	default void tryRespawn(GamePlayer dropout){
		if(!isDropout(dropout) || !dropout.isDroped())
			return;

		if(System.currentTimeMillis() - dropout.getTimeOfDrop() < getSettings().getRespawnCooldownTime(dropout.getDifficulty()))
			return;

		dropout.removeTimeOfDrop();
		dropout.teleport(getSettings().getSelectorOfRunawayRespawnLocation().select());
	}

	void recruitHunters(int recruitmentNumberOfHunters, int waitTime);

	boolean isRecruitingHunters();

	default boolean isFindRunaway(GamePlayer hunter){
		for(Entity entity : hunter.getPlayer().getNearbyEntities(25, 25, 25))
			if(entity instanceof Player)
				if(isRunaway(getGamePlayer((Player) entity)))
					return true;

		return false;
	}

	default boolean isLockOnRunaway(Player hunter){
		return hunter.getFoodLevel() > 6;
	}

	default void setLockOnRunaway(Player hunter, boolean lockOn){
		hunter.setFoodLevel(lockOn ? 20 : 4);
	}

	default void updateStatesScoreboards(){
		for(GamePlayer player : getOnlinePlayers())
			player.getDisplayer().update(false);
	}

	default void broadcastMessage(String message){
		String text = PREFIX + message;
		getOnlinePlayers().forEach(player -> player.sendMessage(text));
	}

	default void broadcastTitle(String title, int fadeIn, int stay, int fadeOut){
		broadcastTitle(title, "", fadeIn, stay, fadeOut);
	}

	default void broadcastTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut){
		getOnlinePlayers().forEach(player -> player.getPlayer().sendTitle(title, subTitle, fadeIn, stay, fadeOut));
	}

	default void broadcastSound(Sound sound, float volume, float pitch){
		getOnlinePlayers()
		.stream()
		.map(GamePlayer::getPlayer)
		.forEach(player -> player.playSound(player.getLocation(), sound, volume, pitch));
	}

}
