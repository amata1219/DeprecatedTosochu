package amata1219.tosochu.game;

import java.util.List;

import org.bukkit.entity.Player;

import amata1219.tosochu.collection.LockableArrayList;
import amata1219.tosochu.collection.LockableArrayList.LockableArrayListLocker;

public class Game {

	public static final Game INSTANCE = new Game();

	private final LockableArrayListLocker<Player> runaways = LockableArrayList.of();
	private final LockableArrayListLocker<Player> dropouts = LockableArrayList.of();
	private final LockableArrayListLocker<Player> hunters = LockableArrayList.of();
	private final LockableArrayListLocker<Player> spectators = LockableArrayList.of();

	private Game(){

	}

	public List<Player> getRunaways(){
		return runaways.list;
	}

	public List<Player> getDropouts(){
		return dropouts.list;
	}

	public List<Player> getHunters(){
		return hunters.list;
	}

	public List<Player> getSpectators(){
		return spectators.list;
	}

}
