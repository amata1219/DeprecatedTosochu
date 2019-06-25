package amata1219.tosochu.deprecated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.collection.LockableHashSet;
import amata1219.tosochu.collection.LockableHashSet.LockableHashSetLocker;
import amata1219.tosochu.game.ImmutableLocation;

public class OldGameManager implements OldGame {

	private final static OldGameManager GAME = new OldGameManager();

	private BukkitTask task;

	private LockableHashSetLocker<OldGamePlayer>
		players = LockableHashSet.of(),
		runaways = LockableHashSet.of(),
		dropouts = LockableHashSet.of(),
		hunterApplicants = LockableHashSet.of(),
		hunters = LockableHashSet.of(),
		spectators = LockableHashSet.of();

	private int
		preparationTime = 20,
		elapsedTime,
		timeLimit,
		moneyPerSecond = 100,
		numberOfHunters;

	private World world = Bukkit.getWorld("yourname");

	private ImmutableLocation
		firstSpawnLocation;

	private Map<Integer, ImmutableLocation>
		respawnLocations = new HashMap<>(),
		jailLocations = new HashMap<>();

	private OldGameManager(){

	}

	public static OldGameManager getInstance(){
		return GAME;
	}

	@Override
	public Set<OldGamePlayer> getPlayers() {
		return players.set;
	}

	@Override
	public void addPlayer(OldGamePlayer player) {
		if(isPlayer(player))
			return;

		players.bypass((set) -> set.add(player));
	}

	@Override
	public void removePlayer(OldGamePlayer player) {
		if(!isPlayer(player))
			return;

		players.bypass((set) -> set.remove(player));
	}

	@Override
	public Set<OldGamePlayer> getRunaways() {
		return runaways.set;
	}

	@Override
	public Set<OldGamePlayer> getDropouts() {
		return dropouts.set;
	}

	@Override
	public Set<OldGamePlayer> getHunterApplicants() {
		return hunterApplicants.set;
	}

	@Override
	public void applyForHunter(OldGamePlayer player) {
		if(isHunterApplicant(player)){
			player.warning("既にハンターの募集に応募しています。");
			return;
		}else{
			hunterApplicants.bypass((set) -> set.add(player));
			player.information("ハンターの募集に応募しました。");
		}
	}

	@Override
	public Set<OldGamePlayer> getHunters() {
		return hunters.set;
	}

	@Override
	public Set<OldGamePlayer> getSpectators() {
		return spectators.set;
	}

	@Override
	public boolean isStarted() {
		return task != null;
	}

	@Override
	public String canStart(){
		if(timeLimit == 0)
			return "制限時間を設定して下さい(/gametimer [秒数])";
		else if(numberOfHunters == 0)
			return "ハンターの初期人数を設定して下さい(/hsr [人数])";
		else if(firstSpawnLocation == null)
			return "初期スポーン地点を設定して下さい(/fistsp)";
		//else if(respawnLocations.size() <= 0)
			//return "リスポーン地点を設定して下さい";
		else if(jailLocations.size() <= 0)
			return "牢獄の座標を設定して下さい(/jailp)";

		return null;
	}

	@Override
	public void start() {
		startRecruitingHunters(numberOfHunters);

		Location spawnPoint = firstSpawnLocation.toLocation(world);
		getPlayers().forEach(player -> player.getPlayer().teleport(spawnPoint));

		task = new BukkitRunnable(){

			@Override
			public void run(){
				if(preparationTime > 0){
					preparationTime--;
					if(preparationTime < 10){
						broadcast(ChatColor.GRAY + "ゲーム開始まで後" + preparationTime + "秒");
					}else if(preparationTime % 10 == 0){
						broadcast(ChatColor.GRAY + "ゲーム開始まで後" + preparationTime + "秒");
					}
					return;
				}else if(preparationTime == 0){
					getPlayers().forEach(player -> player.sendTitle(ChatColor.RED + "逃走中スタート！"));

				}

				elapsedTime++;
				if(timeLimit <= elapsedTime){
					stop();
				}
			}

		}.runTaskTimer(Tosochu.getPlugin(), 20, 20);
	}

	@Override
	public boolean isStopped() {
		return task == null;
	}

	@Override
	public void stop() {
		for(OldGamePlayer player : getPlayers()){
			if(isRunaway(player)){
				player.sendTitle(ChatColor.YELLOW + "逃げ切り成功！");
				player.depositMoney(player.getPrizeMoney());
				player.setPrizeMoney(0);
			}else{
				player.sendTitle(ChatColor.RED + "逃走中終了！");
			}
		}
	}

	@Override
	public ImmutableLocation getFirstSpawnLocation() {
		return firstSpawnLocation;
	}

	@Override
	public void setFirstSpawnLocation(ImmutableLocation location) {
		firstSpawnLocation = location;
	}

	@Override
	public Map<Integer, ImmutableLocation> getRespawnLocations() {
		return respawnLocations;
	}

	@Override
	public ImmutableLocation getHunterSpawnLocation() {
		return null;
	}

	@Override
	public void setHunterSpawnLocation(ImmutableLocation location){
		//hunterSpawnLocation = location;
	}

	@Override
	public Map<Integer, ImmutableLocation> getJailLocations() {
		return jailLocations;
	}

	@Override
	public int getElapsedTime() {
		return elapsedTime;
	}

	@Override
	public int getMoneyPerSecond() {
		return moneyPerSecond;
	}

	@Override
	public void setMoneyPerSecond(int money) {
		moneyPerSecond = money;
	}

	@Override
	public void touch(OldGamePlayer hunter, OldGamePlayer runaway) {
		broadcast(ChatColor.RED + runaway.getPlayer().getName() + "が確保された。");
		runaways.bypass((set) -> set.remove(runaway));
		dropouts.bypass((set) -> set.add(runaway));
		jail(runaway);
	}

	@Override
	public void jail(OldGamePlayer runaway) {
		ImmutableLocation location = new ArrayList<>(jailLocations.values()).get(new Random().nextInt(jailLocations.size()));
		runaway.getPlayer().teleport(location.toLocation(world));
	}

	@Override
	public void respawn(OldGamePlayer dropout) {
		ImmutableLocation location = new ArrayList<>(respawnLocations.values()).get(new Random().nextInt(respawnLocations.size()));
		dropout.getPlayer().teleport(location.toLocation(world));
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void setWorld(World world) {
		this.world = world;
	}

	@Override
	public int getTimeLimit() {
		return timeLimit;
	}

	@Override
	public void setTimeLimit(int time) {
		timeLimit = time;
	}

	@Override
	public int getNumberOfHunters() {
		return numberOfHunters;
	}

	@Override
	public void setNumberOfHunters(int number) {
		numberOfHunters = Math.max(number, 0);
	}

	@Override
	public void startRecruitingHunters(int number) {
		broadcast(ChatColor.RED + "ハンターの募集を開始しました(/hunterで応募)。");
		new BukkitRunnable(){

			@Override
			public void run() {
				int num = number;
				boolean complete = false;
				for(OldGamePlayer applicant : getHunterApplicants()){
					if(num > 0){
						num--;
						runaways.bypass((set) -> set.remove(applicant));
						hunters.bypass((set) -> set.add(applicant));
					}else{
						complete = true;
						break;
					}
				}

				hunterApplicants.bypass((set) -> set.clear());
				if(!complete){
					for(OldGamePlayer runaway : getRunaways()){
						if(num > 0){
							num--;
							runaways.bypass((set) -> set.remove(runaway));
							hunters.bypass((set) -> set.add(runaway));
						}else{
							break;
						}
					}
				}
			}

		}.runTaskLater(Tosochu.getPlugin(), preparationTime <= 0 ? 400 : preparationTime);
	}

	@Override
	public void setSpectator(OldGamePlayer player) {
		player.information("スペクテイターモードになりました。");
		player.getPlayer().setGameMode(GameMode.SPECTATOR);
		runaways.bypass((set) -> set.remove(player));
		hunterApplicants.bypass((set) -> set.remove(player));
		dropouts.bypass((set) -> set.remove(player));
		hunters.bypass((set) -> set.remove(player));
	}

}
