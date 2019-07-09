package amata1219.tosochu.game;

import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import amata1219.tosochu.game.displayer.StatesDisplayer;
import amata1219.tosochu.location.ImmutableLocation;

public class GamePlayer {

	private final GameAPI game;
	public final UUID uuid;
	private Difficulty difficulty;
	private Profession profession = Profession.NOTHING;
	private int money;
	private StatesDisplayer displayer;
	private boolean administratorMode;
	private long timeOfDrop, timeOfQuit;

	public GamePlayer(GameAPI game, Player player){
		this.game = game;
		difficulty = game.getDifficulty();
		uuid = player.getUniqueId();
	}

	public Player getPlayer(){
		return Bukkit.getPlayer(uuid);
	}

	public boolean isOnline(){
		return getPlayer() != null;
	}

	public Difficulty getDifficulty(){
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty){
		Validate.notNull(difficulty, "Difficulty can not be null");
		this.difficulty = difficulty;
	}

	public Profession getProfession(){
		return profession;
	}

	public void setProfession(Profession profession){
		Validate.notNull(profession, "Profession can not be null");
		this.profession = profession;
	}

	public int getMoney(){
		return money;
	}

	public void depositMoney(int value){
		setMoney(money + value);
	}

	public void withdrawMoney(int value){
		setMoney(money - value);
	}

	private void setMoney(int money){
		this.money = Math.max(money, 0);
	}

	public StatesDisplayer getDisplayer(){
		return displayer;
	}

	public void updateDisplayer(){
		if(!isOnline())
			return;

		boolean actionBarMode = displayer.isActionBarMode();

		displayer = new StatesDisplayer(this);
		displayer.setAdiministratorMode(administratorMode);
		displayer.setActionBarMode(actionBarMode);
	}

	public boolean isAdministratorMode(){
		return administratorMode;
	}

	public void setAdministratorMode(boolean administratorMode){
		if(this.administratorMode == administratorMode)
			return;

		this.administratorMode = administratorMode;
		displayer.setAdiministratorMode(administratorMode);
	}

	public long getTimeOfDrop(){
		return timeOfDrop;
	}

	public boolean isDroped(){
		return timeOfDrop > 0;
	}

	public void recordTimeOfDrop(){
		timeOfDrop = System.currentTimeMillis();
	}

	public void removeTimeOfDrop(){
		timeOfDrop = 0;
	}

	public long getTimeOfQuit(){
		return timeOfQuit;
	}

	public boolean isQuitted(){
		return timeOfQuit > 0;
	}

	public void recordTimeOfQuit(){
		timeOfQuit = System.currentTimeMillis();
	}

	public void removeTimeOfQuit(){
		timeOfQuit = 0;
	}

	public void teleport(ImmutableLocation location){
		if(isOnline())
			getPlayer().teleport(location.toLocation(game.getWorld()));
	}

	public void sendMessage(String message){
		if(isOnline())
			getPlayer().sendMessage(TosoGame.PREFIX + message);
	}

}
