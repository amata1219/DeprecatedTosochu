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

	private int recruitmentNumberOfHunters;
	private final List<Player> applicantsForHunterLottery = new ArrayList<>();

	private final Map<Player, Difficulty> difficulties = new HashMap<>();
	private final Map<Player, Integer> money = new HashMap<>();

	public TosoGame(MapSettings settings){
		this.settings = settings;

		for(Profession profession : Profession.values())
			professions.put(profession, new ArrayList<>());
	}

}
