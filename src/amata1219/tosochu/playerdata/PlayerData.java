package amata1219.tosochu.playerdata;

import java.util.UUID;

import org.apache.commons.lang.Validate;

public class PlayerData {

	//ハンターのなった人の回数情報・生存回数・賞金・アイテム買ったデータ

	public final UUID uuid;

	private Permission permission = Permission.ADMINISTRATOR;

	private int money;
	private int countOfWins;
	private int countOfBecameHunter;

	public PlayerData(UUID uuid){
		this.uuid = uuid;
	}

	public Permission getPermission(){
		return permission;
	}

	public boolean hasPermission(Permission permission){
		switch(this.permission){
		case ADMINISTRATOR:
			return true;
		case YOUTUBER:
			return permission != Permission.ADMINISTRATOR;
		case NORMAL_PLAYER:
			return permission == Permission.NORMAL_PLAYER;
		default:
			return false;
		}
	}

	public void setPermission(Permission permission){
		Validate.notNull(permission, "Permission can not be null");
		this.permission = permission;
	}

	public int getMoney(){
		return money;
	}

	public void depositMoney(int value){
		money += value;
	}

	public void withdrawMoney(int value){
		money = Math.max(money - value, 0);
	}

	public int getCountOfWins(){
		return countOfWins;
	}

	public void incrementCountOfWins(){
		countOfWins++;
	}

	public int getCountOfBecameHunter(){
		return countOfBecameHunter;
	}

	public void incrementCountOfBecameHunter(){
		countOfBecameHunter++;
	}

}
