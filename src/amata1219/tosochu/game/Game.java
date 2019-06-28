package amata1219.tosochu.game;

import java.util.List;
import java.util.Map;

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
import amata1219.tosochu.config.MapSettingConfig;
import amata1219.tosochu.game.timer.GameTimer;
import amata1219.tosochu.game.timer.PreparationTimer;
import amata1219.tosochu.game.timer.Timer;
import amata1219.tosochu.location.ImmutableLocation;

public class Game {

	private final String prefix = "§7[§4逃走中§7]§r";

	public final MapSettingConfig settings;

	public final World world;
	public final Difficulty difficulty;

	private int
		unitPriceOfPrizeMoney,
		respawnCooldownTime;

	private final int
		correctionValueForItemCoolTime,
		correctionValueForItemStackSize,
		applyRejoinPenalty,
		npcTimeToLive;

	private ImmutableLocation
		firstSpawnPoint,
		hunterSpawnPoint;

	public final Map<Integer, ImmutableLocation>
		runawayRespawnPoints,
		jailPoints;

	private final int[] fallImpact;

	private final LockableArrayListLocker<Player>
		players = LockableArrayList.of(),
		runaways = LockableArrayList.of(),
		dropouts = LockableArrayList.of(),
		hunters = LockableArrayList.of(),
		spectators = LockableArrayList.of(),
		hunterApplicants = LockableArrayList.of();

	private final LockableHashMapLocker<Player, Long> quittedPlayers = LockableHashMap.of();

	private int requiredHunters;

	private Timer
		preparationTimer,
		gameTimer;

	private Game(MapSettingConfig settings){
		this.settings = settings;

		world = settings.getMap();
		difficulty = settings.getDifficulty();

		unitPriceOfPrizeMoney = settings.getUnitPriceOfPrizeMoney();
		respawnCooldownTime = settings.getRespawnCooldownTime();

		correctionValueForItemCoolTime = settings.getCorrectionValueForItemCooldownTime();
		correctionValueForItemStackSize = settings.getCorrectionValueForItemStackSize();
		applyRejoinPenalty = settings.getApplyRejoinPenalty();
		npcTimeToLive = settings.getNPCTimeToLive();

		firstSpawnPoint = settings.getFirstSpawnPoint();
		hunterSpawnPoint = settings.getHunterSpawnPoint();

		runawayRespawnPoints = settings.getRunawayRespawnPoints();
		jailPoints = settings.getJailSpawnPoints();

		fallImpact = settings.getFallImpact();
	}

	public List<Player> getPlayers(){
		return players.list;
	}

	public boolean isJoined(Player player){
		return getPlayers().contains(player);
	}

	public List<Player> getRunaways(){
		return runaways.list;
	}

	public boolean isRunaway(Player player){
		return getRunaways().contains(player);
	}

	public List<Player> getDropouts(){
		return dropouts.list;
	}

	public boolean isDropout(Player player){
		return getDropouts().contains(player);
	}

	public List<Player> getHunters(){
		return hunters.list;
	}

	public boolean isHunter(Player player){
		return getHunters().contains(player);
	}

	public List<Player> getSpectators(){
		return spectators.list;
	}

	public boolean isSpectator(Player player){
		return getSpectators().contains(player);
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

		(gameTimer = new GameTimer(this)).runTaskTimer(Tosochu.getPlugin(), 20, 20);
	}

	//ゲームを強制終了する
	public void end(){
		preparationTimer.end();
		gameTimer.end();
	}

	public void join(Player player){
		if(isJoined(player))
			return;

		players.bypass((list) -> list.add(player));
		if(!preparationTimer.isZero())
			return;

		if(isQuitted(player)){
			players.bypass((list) -> list.add(player));
			becomeRunaway(player);
		}else if(gameTimer.getElapsedTime() < settings.getForceSpectatorTimeThreshold()){
			becomeRunaway(player);
			message(player, "あなたは逃走者になりました。");
		}else{
			becomeSpectator(player);
			message(player, "あなたは観戦者になりました。");
		}
	}

	public void quit(Player player){
		if(!isJoined(player))
			return;

		removePlayer(player);
		removeRunaway(player);
		removeDropout(player);
		removeHunter(player);
		removeSpectator(player);
		removeHunterApplicant(player);

		quittedPlayers.bypass((map) -> map.put(player, System.currentTimeMillis()));
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

	//ハンターの募集に応募する
	public void applyForHunter(Player player){
		hunterApplicants.bypass((list) -> list.add(player));
		message(player, "ハンターの募集に応募しました。");
	}

	public void becomeRunaway(Player player){
		if(isJoined(player) || isHunter(player) || isRunaway(player) || isSpectator(player))
			return;

		removeDropout(player);

		runaways.bypass((list) -> list.add(player));
	}

	public void becomeDropout(Player player){
		if(!isJoined(player) || isHunter(player) || !isRunaway(player) || isSpectator(player))
			return;

		removeRunaway(player);

		dropouts.bypass((list) -> list.add(player));
	}

	public void becomeHunter(Player player){
		if(!isJoined(player) || !isHunter(player) || isSpectator(player))
			return;

		removeRunaway(player);
		removeDropout(player);
		removeHunterApplicant(player);

		hunters.bypass((list) -> list.add(player));
	}

	public void becomeSpectator(Player player){
		if(!isJoined(player) || isSpectator(player))
			return;

		removeRunaway(player);
		removeDropout(player);
		removeHunter(player);
		removeHunterApplicant(player);

		spectators.bypass((list) -> list.add(player));
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

	private void removePlayer(Player player){
		players.bypass((list) -> list.remove(player));
	}

	private void removeRunaway(Player runaway){
		runaways.bypass((list) -> list.remove(runaway));
	}

	private void removeDropout(Player dropout){
		dropouts.bypass((list) -> list.remove(dropout));
	}

	private void removeHunter(Player hunter){
		hunters.bypass((list) -> list.remove(hunter));
	}

	private void removeSpectator(Player spectator){
		spectators.bypass((list) -> list.remove(spectator));
	}

	private void removeHunterApplicant(Player applicant){
		hunterApplicants.bypass((list) -> list.remove(applicant));
	}

}
