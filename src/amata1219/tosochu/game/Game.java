package amata1219.tosochu.game;

import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import amata1219.tosochu.collection.LockableArrayList;
import amata1219.tosochu.collection.LockableArrayList.LockableArrayListLocker;
import amata1219.tosochu.config.MapSettingConfig;
import amata1219.tosochu.location.ImmutableLocation;

public class Game {

	private final String prefix = "";

	private final MapSettingConfig settings;

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
		spectators = LockableArrayList.of();

	private int requiredHunters;
	private final LockableArrayListLocker<Player> hunterApplicants = LockableArrayList.of();

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

	public void join(Player player){
		players.bypass((list) -> list.add(player));
		runaways.bypass((list) -> list.add(player));
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

	//ハンターを募集しているか
	public boolean isRecruiting(){
		return requiredHunters > 0;
	}

	//ハンターの募集を開始する
	public void recruitHunters(int requiredHunters){
		//既に募集をしていたらエラー
		if(isRecruiting())
			throw new IllegalArgumentException("Already recruiting hunters");

		//募集人数が0以下であればエラー
		if(requiredHunters <= 0)
			throw new IllegalArgumentException("Required hunters can not be less than or qual to 0");

		this.requiredHunters = requiredHunters;

		//念の為、応募者リストをクリアする
		hunterApplicants.bypass((list) -> list.clear());

		//全体に告知する
		broadcast(ChatColor.AQUA + "ハンターを" + requiredHunters + "人募集しました。");
		broadcast(ChatColor.GRAY + "/hunter, /h で応募出来ます。");
	}

	//応募者の中からハンターを決定する
	public void decideHunters(){

	}

	//現在のハンターの募集に応募したか
	public boolean isApplied(Player player){
		return getHunterApplicants().contains(player);
	}

	//ハンターの募集に応募する
	public void applyForHunter(Player player){
		hunterApplicants.bypass((list) -> list.add(player));
	}

	//参加者全員にメッセージを送信する
	public void broadcast(String message){
		message = prefix + message;
		for(Player player : getPlayers())
			player.sendMessage(message);
	}

}
