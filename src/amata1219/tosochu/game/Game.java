package amata1219.tosochu.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.scoreboard.StatesDisplayer;
import amata1219.tosochu.game.timer.GameTimer;
import amata1219.tosochu.game.timer.PreparationTimer;
import amata1219.tosochu.game.timer.Timer;
import amata1219.tosochu.location.ImmutableLocation;
import amata1219.tosochu.location.RandomLocationSelector;

public class Game {

	public final MapSettings settings;

	private Timer preparationTimer;
	private Timer gameTimer;

	//このゲームが行われているワールド
	public final World world;

	//賞金単価
	private int unitPriceOfPrizeMoney;

	//脱落してから復活出来るまでのクールダウンタイム
	private int respawnCooldownTime;

	//最終ログアウトから何秒経過すると強制的に逃走者にされるか
	private int applyRejoinPenalty;
	private int npcTimeToLive;

	private ImmutableLocation firstSpawnPoint;
	private ImmutableLocation hunterSpawnPoint;

	private RandomLocationSelector runawayRespawnLocations;
	private RandomLocationSelector jailLocations;

	//落下時に付与する移動速度低下のエフェクト効果のレベル
	//配列長は256なので落下距離を添え字にするだけで取得出来る
	private int[] fallImpact;

	private final HashMap<Player, Profession> players = new HashMap<>();

	//要求しているハンター数
	private int requiredHunters;

	//ハンターの募集に応募したプレイヤー
	private final ArrayList<Player> applicants = new ArrayList<>();

	//脱落者と確保された時刻(ミリ秒)
	private final HashMap<Player, Long> dropouts = new HashMap<>();

	//ログアウトしたプレイヤーとその時刻(ミリ秒)
	private final HashMap<Player, Long> quittedPlayers = new HashMap<>();

	//ログアウトしたハンター
	private final ArrayList<Player> quittedHunters = new ArrayList<>();

	private final HashMap<Player, StatesDisplayer> displayers = new HashMap<>();
	private final HashMap<Player, Integer> runawayMoney = new HashMap<>();

	private final String prefix = "§7[§4逃走中§7]§r ";

	public Game(MapSettings settings){
		this.settings = settings;
		world = settings.world;
		load(settings);
	}

	public void load(MapSettings settings){
		if(!world.equals(settings.world))
			new IllegalArgumentException("World must be same ");

		unitPriceOfPrizeMoney = settings.getUnitPriceOfPrizeMoney();
		respawnCooldownTime = settings.getRespawnCooldownTime() * 1000;

		applyRejoinPenalty = settings.getWhenApplyRejoinPenalty() * 1000;
		npcTimeToLive = settings.getTimeToLiveOfNPC();

		firstSpawnPoint = settings.getFirstSpawnLocation();
		hunterSpawnPoint = settings.getHunterSpawnLocation();

		runawayRespawnLocations = new RandomLocationSelector(settings.getRunawayRespawnLocations());
		jailLocations = new RandomLocationSelector(settings.getJailSpawnLocations());

		fallImpact = settings.getFallImpact();
	}

	public int getUnitPriceOfPrizeMoney(){
		return unitPriceOfPrizeMoney;
	}

	public void setUnitPriceOfPrizeMoney(int money){
		unitPriceOfPrizeMoney = Math.max(money, 0);
	}

	public boolean isJoined(Player player){
		return players.containsKey(player);
	}

	public Set<Player> getPlayers(){
		return players.keySet();
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

		return isDropout(runaway) && System.currentTimeMillis() - dropouts.get(runaway) >= respawnCooldownTime;
	}

	private boolean matchProfession(Player player, Profession profession){
		return players.get(player) == profession;
	}

	public void setProfession(Player player, Profession profession){
		players.put(player, profession);
	}

	private void apply(Profession profession, Consumer<Player> consumer){
		for(Entry<Player, Profession> entry : players.entrySet())
			if(profession == entry.getValue())
				consumer.accept(entry.getKey());
	}

	public boolean isHunter(Player player){
		return matchProfession(player, Profession.HUNTER);
	}

	public boolean isSpectator(Player player){
		return matchProfession(player, Profession.SPECTATOR);
	}

	public boolean isQuitted(Player player){
		return quittedPlayers.containsKey(player);
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

		players.put(player, Profession.PLAYER);

		displayers.values().forEach(StatesDisplayer::updatePlayerCount);

		displayers.put(player, new StatesDisplayer(this, player));

		broadcast(player.getName() + "が参加しました。");

		//まだ準備状態であれば戻る
		if(preparationTimer.getCount() > 0)
			return;

		if(isQuitted(player)){
			if(quittedHunters.contains(player) && System.currentTimeMillis() - quittedPlayers.get(player) >= applyRejoinPenalty){
				quittedHunters.remove(player);
				becomeHunter(player);
			}else{
				becomeRunaway(player);
				runawayMoney.put(player, 0);
			}
		}else if(gameTimer.getElapsedTime() < settings.getWhenRestrictParticipation()){
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
			quittedHunters.add(player);

		applicants.remove(player);

		//観戦者でなければ最終ログアウト時間を記録する
		if(!isSpectator(player))
			quittedPlayers.put(player, System.currentTimeMillis());

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
		applicants.clear();

		//全体に告知する
		broadcast(ChatColor.AQUA + "ハンターを" + requiredHunters + "人募集しました。");
		broadcast(ChatColor.GRAY + "/hunter, /h で応募出来ます。");
	}

	//応募者の中からハンターを決定する
	public void decideHunters(){
		int count = requiredHunters;
		for(Player applicant : applicants){
			if(count <= 0)
				break;

			becomeHunter(applicant);
		}

		AtomicInteger counter = new AtomicInteger(count);

		apply(Profession.RUNAWAY, (player) -> {
			if(counter.get() > 0)
				becomeHunter(player);

			counter.decrementAndGet();
		});
		applicants.clear();
	}

	//現在のハンターの募集に応募したか
	public boolean isApplied(Player player){
		return applicants.contains(player);
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
		applicants.add(player);
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

		dropouts.put(player, System.currentTimeMillis());

		getDisplayer(player).updateProfession();
	}

	public void respawn(Player dropout){
		if(!isDropout(dropout) || !canRespawn(dropout))
			return;

		becomeRunaway(dropout);

		runawayRespawnLocations.select().teleport(dropout);
	}

	public void becomeHunter(Player player){
		if(!isJoined(player) || !isHunter(player) || isSpectator(player))
			return;

		applicants.remove(player);

		setProfession(player, Profession.HUNTER);
		getDisplayer(player).updateProfession();

		hunterSpawnPoint.teleport(player);

		message(player, "あなたはハンターになりました。");
		broadcast(player.getName() + "がハンターになりました。");
	}

	public void becomeSpectator(Player player){
		if(!isJoined(player) || isSpectator(player))
			return;

		applicants.remove(player);

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

}
