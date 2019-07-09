package amata1219.tosochu.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.timer.PreparationTimer;
import amata1219.tosochu.game.timer.Timer;
import amata1219.tosochu.location.ImmutableLocation;
import amata1219.tosochu.storage.PlayerDataStorage;

public class TosoGame implements GameAPI {

	private final Tosochu plugin = Tosochu.getPlugin();

	private final MapSettings settings;

	private Timer timer;

	private final Map<UUID, GamePlayer> participants = new HashMap<>();
	private final Map<Profession, List<GamePlayer>> players = new HashMap<>();

	private int recruitmentNumberOfHunters;
	private final List<GamePlayer> applicantsForHunterLottery = new ArrayList<>();

	public TosoGame(MapSettings settings){
		this.settings = settings;

		for(Profession profession : Profession.values())
			players.put(profession, new ArrayList<>());
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
	public MapSettings getSettings() {
		return settings;
	}

	@Override
	public ImmutableLocation getRandomRespawnLocation() {
		return settings.getSelectorOfRunawayRespawnLocation().select();
	}

	@Override
	public ImmutableLocation getRandomJailSpawnLocation() {
		return settings.getSelectorOfJailSpawnLocation().select();
	}

	@Override
	public List<GamePlayer> getGamePlayers() {
		return new ArrayList<>(participants.values());
	}

	@Override
	public List<GamePlayer> getPlayersByProfession(Profession profession) {
		return players.get(profession);
	}

	@Override
	public List<GamePlayer> getApplicantsForHunterLottery() {
		return applicantsForHunterLottery;
	}

	@Override
	public GamePlayer getGamePlayer(Player player) {
		return participants.get(player.getUniqueId());
	}

	@Override
	public boolean isJoined(Player player) {
		return participants.containsKey(player.getUniqueId());
	}

	@Override
	public boolean isQuitted(Player player) {
		UUID uuid = player.getUniqueId();
		return participants.containsKey(uuid) ? participants.get(uuid).isQuitted() : false;
	}

	@Override
	public void join(Player player) {
	}

	@Override
	public void quit(Player player) {
	}

	@Override
	public void recruitHunters(int recruitmentNumberOfHunters, int waitTime) {
		if(isRecruitingHunters())
			return;

		this.recruitmentNumberOfHunters = recruitmentNumberOfHunters;

		new BukkitRunnable(){

			@Override
			public void run() {
				int count = recruitmentNumberOfHunters;
				if(!applicantsForHunterLottery.isEmpty()){
					PlayerDataStorage storage = PlayerDataStorage.getStorage();
					Collections.sort(applicantsForHunterLottery, (p1, p2) -> storage.get(p1.getPlayer()).getNumberOfTimesThatBecameHunter() - storage.get(p2.getPlayer()).getNumberOfTimesThatBecameHunter());
					int[] cumulativeSummary = new int[applicantsForHunterLottery.size()];
				}

			}

		}.runTaskLater(Tosochu.getPlugin(), waitTime * 20);
	}

	private List<Player> drawHunterLottery(){

	}

	@Override
	public boolean isRecruitingHunters() {
		return recruitmentNumberOfHunters > 0;
	}

}
