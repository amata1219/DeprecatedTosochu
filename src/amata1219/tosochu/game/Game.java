package amata1219.tosochu.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import amata1219.tosochu.Config;
import amata1219.tosochu.collection.LockableArrayList;
import amata1219.tosochu.collection.LockableArrayList.LockableArrayListLocker;
import amata1219.tosochu.location.ImmutableLocation;

public class Game {

	public final String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "逃走中" + ChatColor.GRAY + "] " + ChatColor.RESET;

	private final Config config;

	public final World world;

	public final Difficulty difficulty;

	public final int preparationTime;
	public final int timeLimit;
	private int unitPriceOfPrizeMoney;
	private int respawnCooldownTime;
	private int correctionValueOfItemCoolTime;
	public final int correctionValueOfNumberOfItems;
	private int alreadyRejoinPenalty;
	private int npcTimeToLive;

	private ImmutableLocation firstSpawnPoint;
	private ImmutableLocation hunterSpawnPoint;
	public final Map<Integer, ImmutableLocation> runawayRespawnPoints = new HashMap<>();
	public final Map<Integer, ImmutableLocation> jailPoints = new HashMap<>();

	private final int[] fallInpact = new int[256];

	private final LockableArrayListLocker<Player> players = LockableArrayList.of();

	private final LockableArrayListLocker<Player> runaways = LockableArrayList.of();
	private final LockableArrayListLocker<Player> dropouts = LockableArrayList.of();
	private final LockableArrayListLocker<Player> hunters = LockableArrayList.of();
	private final LockableArrayListLocker<Player> spectators = LockableArrayList.of();

	private int requiredHunters;
	private final LockableArrayListLocker<Player> hunterApplicants = LockableArrayList.of();

	private Game(Config config){
		this.config = config;

		FileConfiguration file = config.get();

		//設定ファイル名に対応したワールドを代入する
		world = Bukkit.getWorld(config.name.replace(".yml", ""));

		difficulty = Difficulty.valueOf(file.getString("Difficulty").toUpperCase());
		unitPriceOfPrizeMoney = difficulty.getInteger(file.getString("UnitPriceOfPrizeMoney"));
	}

	public Difficulty getDifficulty(){
		return difficulty;
	}

	public World getWorld(){
		return world;
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
