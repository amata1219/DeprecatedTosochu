package amata1219.tosochu.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.collection.LockableArrayList;
import amata1219.tosochu.collection.LockableArrayList.LockableArrayListLocker;
import amata1219.tosochu.collection.LockableHashMap;
import amata1219.tosochu.collection.LockableHashMap.LockableHashMapLocker;
import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.scoreboard.StatesDisplayer;
import amata1219.tosochu.game.timer.GameTimer;
import amata1219.tosochu.game.timer.PreparationTimer;
import amata1219.tosochu.game.timer.Timer;
import amata1219.tosochu.location.ImmutableLocation;

public class Game {

	public static Game game;

	public static boolean isInGame(){
		return game != null && game.preparationTimer != null;
	}

	public static Game loadGame(MapSettings settings){
		if(isInGame())
			new IllegalStateException("The game is currently being played");

		return game = new Game(settings);
	}

	public static void unloadGame(){
		game = null;
	}

	private final String prefix = "§7[§4逃走中§7]§r";

	public MapSettings settings;

	public final World world;
	public final Difficulty difficulty;

	private int unitPriceOfPrizeMoney;
	private int respawnCooldownTime;
	private double correctionValueForItemCoolTime;
	private int correctionValueForItemStackSize;
	private int applyRejoinPenalty;
	private int npcTimeToLive;

	private ImmutableLocation firstSpawnPoint;
	private ImmutableLocation hunterSpawnPoint;

	private Map<Integer, ImmutableLocation> runawayRespawnPoints;
	private Map<Integer, ImmutableLocation> jailPoints;

	private final int[] fallImpact;

	private final HashMap<Player, Profession> professions = new HashMap<>();

	private final LockableArrayListLocker<Player> hunterApplicants = LockableArrayList.of();

	private final LockableHashMapLocker<Player, Long> dropouts = LockableHashMap.of();
	private final LockableHashMapLocker<Player, Long> quittedPlayers = LockableHashMap.of();
	private final LockableHashMapLocker<Player, Long> quittedHunters = LockableHashMap.of();

	private int requiredHunters;

	private Timer preparationTimer;
	private Timer gameTimer;

	private final HashMap<Player, StatesDisplayer> displayers = new HashMap<>();
	private final HashMap<Player, Integer> runawayMoney = new HashMap<>();

	private Game(MapSettings settings){
		this.settings = settings;

		world = settings.world;
		difficulty = settings.getDifficulty();

		unitPriceOfPrizeMoney = settings.getUnitPriceOfPrizeMoney();
		respawnCooldownTime = settings.getRespawnCooldownTime() * 1000;

		correctionValueForItemCoolTime = settings.getCorrectionValueForItemCooldownTime();
		correctionValueForItemStackSize = settings.getCorrectionValueForItemStackSize();
		applyRejoinPenalty = settings.getApplyRejoinPenalty() * 1000;
		npcTimeToLive = settings.getNPCTimeToLive();

		firstSpawnPoint = settings.getFirstSpawnPoint();
		hunterSpawnPoint = settings.getHunterSpawnPoint();

		runawayRespawnPoints = settings.getRunawayRespawnPoints();
		jailPoints = settings.getJailSpawnPoints();

		fallImpact = settings.getFallImpact();
	}

	public void load(MapSettings settings){
		if(!world.equals(settings.world))
			new IllegalArgumentException("World must be same ");

		unitPriceOfPrizeMoney = settings.getUnitPriceOfPrizeMoney();
		respawnCooldownTime = settings.getRespawnCooldownTime() * 1000;

		correctionValueForItemCoolTime = settings.getCorrectionValueForItemCooldownTime();
		correctionValueForItemStackSize = settings.getCorrectionValueForItemStackSize();
		applyRejoinPenalty = settings.getApplyRejoinPenalty() * 1000;
		npcTimeToLive = settings.getNPCTimeToLive();

		firstSpawnPoint = settings.getFirstSpawnPoint();
		hunterSpawnPoint = settings.getHunterSpawnPoint();

		runawayRespawnPoints = settings.getRunawayRespawnPoints();
		jailPoints = settings.getJailSpawnPoints();

		fallImpact = settings.getFallImpact();
	}

	public int getUnitPriceOfPrizeMoney(){
		return unitPriceOfPrizeMoney;
	}

	public void setUnitPriceOfPrizeMoney(int money){
		unitPriceOfPrizeMoney = Math.max(money, 0);
	}

	public boolean isJoined(Player player){
		return professions.containsKey(player);
	}

	public Set<Player> getPlayers(){
		return professions.keySet();
	}

	public boolean isRunaway(Player player){
		return matchProfession(player, Profession.RUNAWAY);
	}

	public boolean isDropout(Player player){
		return matchProfession(player, Profession.DROPOUT);
	}

	public boolean canRespawn(Player runaway){
		if(!isDropout(runaway))
			return false;

		return isDropout(runaway) && System.currentTimeMillis() - dropouts.map.get(runaway) >= respawnCooldownTime;
	}

	public List<Player> getHunters(){
		return getPlayers(Profession.HUNTER);
	}

	private boolean matchProfession(Player player, Profession profession){
		return professions.get(player) == profession;
	}

	public void setProfession(Player player, Profession profession){
		professions.put(player, profession);
	}

	private List<Player> getPlayers(Profession profession){
		List<Player> list = new ArrayList<>();
		for(Entry<Player, Profession> entry : professions.entrySet())
			if(profession == entry.getValue())
				list.add(entry.getKey());
		return list;
	}

	public boolean isHunter(Player player){
		return getHunters().contains(player);
	}

	public boolean isSpectator(Player player){
		return matchProfession(player, Profession.SPECTATOR);
	}

	public List<Player> getHunterApplicants(){
		return hunterApplicants.list;
	}

	public boolean isQuitted(Player player){
		return quittedPlayers.map.containsKey(player);
	}

	//ゲームの準備を開始する
	public void prepare(){
		if(preparationTimer != null)
			throw new IllegalStateException("Game preparation has already been started");

		(preparationTimer = new PreparationTimer(this)).runTaskTimer(Tosochu.getPlugin(), 20, 20);
	}

	//ゲームを開始する
	public void start(){
		if(gameTimer != null)
			throw new IllegalStateException("The game has already been started");

		broadcastTitle(ChatColor.DARK_RED + "逃走中スタート！", "");

		decideHunters();
		for(Player player : getPlayers()){
			if(!isHunter(player))
				runawayMoney.put(player, 0);
		}
		(gameTimer = new GameTimer(this)).runTaskTimer(Tosochu.getPlugin(), 20, 20);
	}

	//ゲームを強制終了する
	public void end(){
		broadcast("ゲームが強制終了されました。");
		preparationTimer.end();
		gameTimer.end();
	}

	public void join(Player player){
		if(isJoined(player))
			return;

		professions.put(player, Profession.PLAYER);

		displayers.values().forEach(StatesDisplayer::updatePlayerCount);

		displayers.put(player, new StatesDisplayer(this, player));

		broadcast(player.getName() + "が参加しました。");

		//まだ準備状態であれば戻る
		if(preparationTimer.getCount() > 0)
			return;

		if(isQuitted(player)){
			if(System.currentTimeMillis() - quittedHunters.map.getOrDefault(player, 0L) >= applyRejoinPenalty){
				quittedHunters.bypass((map) -> map.remove(player));
				becomeHunter(player);
			}else{
				becomeRunaway(player);
				runawayMoney.put(player, 0);
			}
		}else if(gameTimer.getElapsedTime() < settings.getForceSpectatorTimeThreshold()){
			becomeRunaway(player);
			runawayMoney.put(player, 0);
			message(player, "あなたは逃走者になりました。");
		}else{
			becomeSpectator(player);
			message(player, "あなたは観戦者になりました。");
		}
	}

	public void quit(Player player){
		if(!isJoined(player))
			return;

		if(isHunter(player))
			quittedHunters.bypass((map) -> map.put(player, System.currentTimeMillis()));

		removePlayer(player);
		removeRunaway(player);
		removeDropout(player);
		removeHunter(player);
		removeSpectator(player);
		removeHunterApplicant(player);

		//観戦者でなければ最終ログアウト時間を記録する
		if(!isSpectator(player))
			quittedPlayers.bypass((map) -> map.put(player, System.currentTimeMillis()));

		displayers.remove(player);

		broadcast(player.getName() + "が退出しました。");
	}

	public void fall(Player player){
		if(isRunaway(player))
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, fallImpact[Math.max((int) player.getFallDistance(), 255)]));
	}

	//ハンターを募集しているか
	public boolean isRecruiting(){
		return requiredHunters > 0;
	}

	//ハンターの募集を開始する
	public void recruitHunters(int requiredHunters){
		//既に募集をしていたらエラー
		if(isRecruiting())
			throw new IllegalStateException("Recruitment of hunters has already started");

		//募集人数が0以下であればエラー
		if(requiredHunters <= 0)
			throw new IllegalArgumentException("Required hunters can not be less than or equal to 0");

		this.requiredHunters = requiredHunters;

		//念の為、応募者リストをクリアする
		hunterApplicants.bypass((list) -> list.clear());

		//全体に告知する
		broadcast(ChatColor.AQUA + "ハンターを" + requiredHunters + "人募集しました。");
		broadcast(ChatColor.GRAY + "/hunter, /h で応募出来ます。");
	}

	//応募者の中からハンターを決定する
	public void decideHunters(){
		int count = requiredHunters;
		for(Player applicant : getHunterApplicants()){
			if(count <= 0)
				break;

			becomeHunter(applicant);
			message(applicant, "あなたはハンターになりました。");
		}

		if(count > 0){
			for(Player runaway : getRunaways()){
				if(count <= 0)
					break;

				becomeHunter(runaway);
				message(runaway, "あなたはハンターになりました。");
			}
		}

		hunterApplicants.bypass((list) -> list.clear());
	}

	//現在のハンターの募集に応募したか
	public boolean isApplied(Player player){
		return getHunterApplicants().contains(player);
	}

	public void giveMoneyToRunaways(){
		for(Player runaway : getPlayers()){
			int money = runawayMoney.get(runaway) + unitPriceOfPrizeMoney;
			runawayMoney.put(runaway, money);
			getDisplayer(runaway).updateMoney(money);
		}
	}

	public int getMoney(Player player){
		return runawayMoney.getOrDefault(player, 0);
	}

	public StatesDisplayer getDisplayer(Player player){
		return displayers.get(player);
	}

	//ハンターの募集に応募する
	public void applyForHunter(Player player){
		hunterApplicants.bypass((list) -> list.add(player));
		message(player, "ハンターの募集に応募しました。");
	}

	public void becomeRunaway(Player player){
		if(isJoined(player) || isHunter(player) || isRunaway(player) || isSpectator(player))
			return;

		setProfession(player, Profession.RUNAWAY);
	}

	public void becomeDropout(Player player){
		if(!isJoined(player) || !isRunaway(player))
			return;

		setProfession(player, Profession.DROPOUT);
		dropouts.bypass((map) -> map.put(player, System.currentTimeMillis()));

		getDisplayer(player).updateProfession();
	}

	public void respawn(Player dropout){
		if(!isDropout(dropout) || !canRespawn(dropout))
			return;

		becomeRunaway(dropout);

		ArrayList<ImmutableLocation> points = new ArrayList<>(runawayRespawnPoints.values());
		points.get(points.size()).teleport(dropout);
	}

	public void becomeHunter(Player player){
		if(!isJoined(player) || !isHunter(player) || isSpectator(player))
			return;

		removeHunterApplicant(player);

		setProfession(player, Profession.HUNTER);

		getDisplayer(player).updateProfession();

		hunterSpawnPoint.teleport(player);
	}

	public void becomeSpectator(Player player){
		if(!isJoined(player) || isSpectator(player))
			return;

		removeHunterApplicant(player);

		setProfession(player, Profession.SPECTATOR);

		getDisplayer(player).updateProfession();
	}

	public void message(Player receiver, String message){
		receiver.sendMessage(prefix + message);
	}

	//参加者全員にメッセージを送信する
	public void broadcast(String message){
		message = prefix + message;
		for(Player player : getPlayers())
			player.sendMessage(message);
	}

	public void broadcastTitle(String title, String subTitle){
		for(Player player : getPlayers())
			player.sendTitle(title, subTitle, 0, 50, 10);
	}

	private void removeHunterApplicant(Player applicant){
		hunterApplicants.bypass((list) -> list.remove(applicant));
	}

}
