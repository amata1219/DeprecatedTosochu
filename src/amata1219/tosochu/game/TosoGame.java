package amata1219.tosochu.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.displayer.StatesDisplayer;
import amata1219.tosochu.game.timer.PreparationTimer;
import amata1219.tosochu.game.timer.Timer;
import amata1219.tosochu.location.ImmutableLocation;
import amata1219.tosochu.location.LocationRandomSelector;

public class TosoGame implements GameAPI {

	private final Tosochu plugin = Tosochu.getPlugin();

	private final MapSettings settings;

	private Timer timer;

	private final Map<UUID, GamePlayer> players = new HashMap<>();
	private final Map<Profession, List<Player>> gamePlayers = new HashMap<>();

	private int recruitmentNumberOfHunters;
	private final List<Player> applicantsForHunterLottery = new ArrayList<>();

	public TosoGame(MapSettings settings){
		this.settings = settings;

		for(Profession profession : Profession.values())
			gamePlayers.put(profession, new ArrayList<>());
	}

	@Override
	public void start() {
		//startコマンドですべきか？

		if(!isBeforeStart())
			return;

		setTimer(new PreparationTimer(this));

		for(Player player : settings.getWorld().getPlayers())
			join(player);

		/*
		 * ハンターが指定されていないと準備を開始出来ない
		 * 準備開始時に逃走者を確定する
		 */
	}

	@Override
	public boolean isEnded() {
		return false;
	}

	@Override
	public Timer getTimer() {
		return timer;
	}

	@Override
	public void setTimer(Timer timer) {
		if(this.timer != null && this.timer.isCancelled())
			this.timer.cancel();

		(this.timer = timer).runTaskTimer(Tosochu.getPlugin(), 20, 20);
	}

	@Override
	public MapSettings getLoadedMapSettings() {
		return settings;
	}

	@Override
	public ImmutableLocation getRandomRespawnLocation() {
		return settings.getRunawayRespawnLocationSelector().select();
	}

	@Override
	public ImmutableLocation getRandomJailSpawnLocation() {
		return settings.getJailSpawnLocations().select();
	}

	@Override
	public List<GamePlayer> getGamePlayers() {
		return new ArrayList<>(players.values());
	}

	@Override
	public List<GamePlayer> getQuittedPlayers() {
		return getGamePlayers()
				.stream()
				.filter(GamePlayer::isQuitted)
				.collect(Collectors.toList());
	}

	@Override
	public List<Player> getPlayersByProfession(Profession profession) {
		return gamePlayers.get(profession);
	}

	@Override
	public List<Player> getApplicantsForHunterLottery() {
		return applicantsForHunterLottery;
	}

	@Override
	public boolean isJoined(Player player) {
		return false;
	}

	@Override
	public boolean isQuitted(Player player) {
		return false;
	}

	@Override
	public void join(Player player) {
	}

	@Override
	public void quit(Player player) {
	}

	@Override
	public void fall(Player runaway) {
	}

	@Override
	public void touchedByHunter(Player runaway) {
	}

	@Override
	public void tryRespawn(Player dropout) {
	}

	@Override
	public void recruitHunters(int recruitmentNumberOfHunters, int waitTime) {
	}

	@Override
	public boolean isRecruitingHunters() {
		return false;
	}

}
