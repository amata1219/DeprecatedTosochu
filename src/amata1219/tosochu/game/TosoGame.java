package amata1219.tosochu.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

	private int[] unitPriceOfPrizeMoney = new int[Difficulty.values().length];
	private int[] levelsOfSlownessEffect;
	private LocationRandomSelector respawnLocationSelector, jailLocationSelector;

	private final List<UUID> players = new ArrayList<>();

	private final Map<Profession, List<Player>> playersByProfession = new HashMap<>();

	private final List<UUID> administrators = new ArrayList<>();
	private final List<UUID> spectators = new ArrayList<>();
	private final Map<UUID, Long> dropouts = new HashMap<>();
	private final Map<UUID, Long> quitted = new HashMap<>();

	private int recruitmentNumberOfHunters;
	private final List<Player> applicantsForHunterLottery = new ArrayList<>();

	private final Map<Player, Difficulty> difficulties = new HashMap<>();
	private final Map<Player, Integer> moneyMap = new HashMap<>();
	private final Map<Player, StatesDisplayer> statesDisplayers = new HashMap<>();

	public TosoGame(MapSettings settings){
		this.settings = settings;

		for(Profession profession : Profession.values())
			playersByProfession.put(profession, new ArrayList<>());

		for(Difficulty difficulty : Difficulty.values())
			unitPriceOfPrizeMoney[difficulty.ordinal()] = settings.getUnitPriceOfPrizeMoney(difficulty);

		levelsOfSlownessEffect = settings.getLevelsOfSlownessEffectAppliedWhenPlayerLands();

		respawnLocationSelector = new LocationRandomSelector(settings.getRunawayRespawnLocations());
		jailLocationSelector = new LocationRandomSelector(settings.getJailSpawnLocations());
	}

	@Override
	public void start() {
		//startコマンドですべきか？

		if(!isBeforeStart())
			return;

		setTimer(new PreparationTimer(this));

		for(Player player : getOnlinePlayers())
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
	public void reloadMapSettings() {
	}

	@Override
	public ImmutableLocation getRandomRespawnLocation() {
		return respawnLocationSelector.select();
	}

	@Override
	public ImmutableLocation getRandomJailLocation() {
		return jailLocationSelector.select();
	}

	@Override
	public int getUnitPriceOfPrizeMoney(Difficulty difficulty) {
		return unitPriceOfPrizeMoney[difficulty.ordinal()];
	}

	@Override
	public void setUnitPriceOfPrizeMoney(Difficulty difficulty, int money) {
		unitPriceOfPrizeMoney[difficulty.ordinal()] = money;
	}

	@Override
	public List<UUID> getPlayers() {
		return players;
	}

	@Override
	public List<UUID> getQuittedPlayers() {
		return new ArrayList<>(quitted.keySet());
	}

	@Override
	public List<UUID> getAdiministrators() {
		return administrators;
	}

	@Override
	public List<Player> getPlayersByProfession(Profession profession) {
		return playersByProfession.get(profession);
	}

	@Override
	public List<Player> getApplicantsForHunterLottery() {
		return applicantsForHunterLottery;
	}

	@Override
	public Difficulty getDifficulty(Player player) {
		return difficulties.get(player);
	}

	@Override
	public void setDifficulty(Player player, Difficulty difficulty) {
		difficulties.put(player, difficulty);
	}

	@Override
	public int getMoney(Player player) {
		return moneyMap.get(player);
	}

	@Override
	public void setMoney(Player player, int money) {
		moneyMap.put(player, getMoney(player) + money);
	}

	@Override
	public StatesDisplayer getStatesDisplayer(Player player) {
		return statesDisplayers.get(player);
	}

	@Override
	public void join(Player player) {
		/*
		 * players
		 * moneymap
		 * displayer
		 */
		final UUID uuid = player.getUniqueId();

		if(!isJoined(player)){
			players.add(uuid);
			difficulties.put(player, getDifficulty());
		}

		statesDisplayers.put(player, new StatesDisplayer(this, player));
		if(isQuitted(player)){
			if(!spectators.contains(player.getUniqueId()) && System.currentTimeMillis() - quitted.get(player) >= 30000){
				quitted.remove(player);
			}else{
				getSpectators().add(player);
				player.setGameMode(GameMode.SPECTATOR);
			}
		}else{
			if(isHunter(player)){
				settings.getHunterSpawnLocation().teleport(getWorld(), player);
				player.setFoodLevel(4);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
			}else{
				getRunaways().add(player);
				settings.getFirstSpawnLocation().teleport(getWorld(), player);
			}
		}

	}

	@Override
	public void quit(Player player) {
		final UUID uuid = player.getUniqueId();
		getPlayersByProfession(getProfession(player)).remove(uuid);
		quitted.put(player.getUniqueId(), System.currentTimeMillis());
	}

	@Override
	public void fall(Player runaway) {
		runaway.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 7, levelsOfSlownessEffect[Math.min((int) runaway.getFallDistance(), 255)]));
	}

	@Override
	public void touchedByHunter(Player runaway) {
		getRunaways().remove(runaway);
		getDropouts().add(runaway);
		getRandomJailLocation().teleport(getWorld(), runaway);
		runaway.sendMessage(PREFIX + "確保されました。");
	}

	@Override
	public void tryRespawn(Player dropout){
		if(System.currentTimeMillis() - dropouts.get(dropout) < settings.getRespawnCooldownTime(getDifficulty(dropout)))
			return;

		dropouts.remove(dropout);
		getRandomRespawnLocation().teleport(getWorld(), dropout);
	}

	@Override
	public void recruitHunters(int recruitmentNumberOfHunters, int waitTime) {
		this.recruitmentNumberOfHunters = recruitmentNumberOfHunters;
		new BukkitRunnable(){

			@Override
			public void run() {
				//ハンターの抽選
				int count = recruitmentNumberOfHunters;
				for(Player player : getApplicantsForHunterLottery()){
					if(count <= 0)
						break;

					count--;
					getHunters().add(player);
					broadcastMessage(player.getName() + "がハンターになりました。");
					player.sendMessage(PREFIX + "あなたはハンターになりました。");
				}

				for(Player player : getOnlinePlayers()){
					if(count <= 0)
						break;

					if(isHunter(player))
						continue;

					count--;
					getHunters().add(player);
					broadcastMessage(player.getName() + "がハンターになりました。");
					player.sendMessage(PREFIX + "あなたはハンターになりました。");
				}
			}

		}.runTaskLater(plugin, waitTime * 20);
	}

	@Override
	public boolean isRecruitingHunters() {
		return recruitmentNumberOfHunters > 0;
	}

}
