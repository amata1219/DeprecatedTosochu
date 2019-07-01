package amata1219.tosochu.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import amata1219.tosochu.config.MapSettings;

public class TosoGame implements GameAPI {

	private final MapSettings settings;

	private final List<Player> players = new ArrayList<>();

	private final Map<Profession, List<Player>> professions = new HashMap<>();
	private final Map<Player, Long> quittedPlayers = new HashMap<>();

	public TosoGame(MapSettings settings){
		this.settings = settings;

		for(Profession profession : Profession.values())
			professions.put(profession, new ArrayList<>());
	}

	@Override
	public void start() {
	}

	@Override
	public void end() {
	}

	@Override
	public boolean isPreparing() {
		return false;
	}

	@Override
	public boolean isStarting() {
		return false;
	}

	@Override
	public boolean isEnding() {
		return false;
	}

	@Override
	public MapSettings getLoadedMapSettings() {
		return settings;
	}

	@Override
	public void join(Player player) {
	}

	@Override
	public void quit(Player player) {
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
