package amata1219.tosochu.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import amata1219.tosochu.Tosochu;
import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.timer.GameTimer;
import amata1219.tosochu.game.timer.PreparationTimer;
import amata1219.tosochu.game.timer.Timer;

public class TosoGame implements GameAPI {

	private final Tosochu plugin = Tosochu.getPlugin();

	private final MapSettings settings;

	private final List<Player> players = new ArrayList<>();

	private final Map<Profession, List<Player>> professions = new HashMap<>();
	private final Map<Player, Long> quittedPlayers = new HashMap<>();

	private Timer timer;

	public TosoGame(MapSettings settings){
		this.settings = settings;

		for(Profession profession : Profession.values())
			professions.put(profession, new ArrayList<>());
	}

	@Override
	public void start() {
	}

	@Override
	public void forcedTermination() {
	}

	@Override
	public boolean isPreparing() {
		return timer instanceof PreparationTimer;
	}

	@Override
	public boolean isStarting() {
		return timer instanceof GameTimer;
	}

	@Override
	public void setTimer(Timer timer){
		this.timer = timer;

		timer.runTaskTimer(plugin, 20, 20);
	}

	@Override
	public MapSettings getLoadedMapSettings() {
		return settings;
	}

	@Override
	public void join(Player player) {
		players.add(player);

		if(isQuitted(player)){

		}
	}

	@Override
	public void quit(Player player) {
		players.remove(player);

		//ログアウト時刻を記録
		quittedPlayers.put(player, System.currentTimeMillis());
	}

	@Override
	public void recruitHunters(int recruitmentNumber) {
	}

	@Override
	public boolean isRecruitingHunters() {
		return false;
	}

	@Override
	public List<Player> getPlayers() {
		return players;
	}

	@Override
	public List<Player> getQuittedPlayers() {
		return new ArrayList<>(quittedPlayers.keySet());
	}

	@Override
	public List<Player> getMatchedPlayers(Profession profession) {
		return professions.get(profession);
	}

	@Override
	public List<Player> getApplicantsForHunterLottery() {
		return null;
	}

}
