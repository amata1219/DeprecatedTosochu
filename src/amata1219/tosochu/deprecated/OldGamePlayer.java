package amata1219.tosochu.deprecated;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class OldGamePlayer {

	//プレイヤーのUUID
	public final UUID uuid;

	//権限
	public final OldPerm permission = OldPerm.ADMINISTRATOR;

	//逃走成功回数
	private int runawaySuccessCount;

	//ハンターになった回数
	private int becameHunterCount;

	//所持金
	private int money;

	//現在の賞金
	private int prizeMoney;

	public OldGamePlayer(UUID uuid){
		this.uuid = uuid;
	}

	//Playerインスタンスを取得する
	public Player getPlayer(){
		return Bukkit.getPlayer(uuid);
	}

	//逃走成功回数を取得する
	public int getRunawaySuccessCount(){
		return runawaySuccessCount;
	}

	//逃走成功回数をインクリメントする
	public void incrementRunawaySuccessCount(){
		runawaySuccessCount++;
	}

	//ハンターになった回数を取得する
	public int getBecameHunterCount(){
		return becameHunterCount;
	}

	//ハンターになった回数をインクリメントする
	public void incrementBecameHunterCount(){
		becameHunterCount++;
	}

	//所持金を取得する
	public int getMoney(){
		return money;
	}

	//所持金を設定する
	public void setMoney(int money){
		this.money = Math.max(money, 0);
	}

	//指定値分だけ所持金を増やす
	public void depositMoney(int value){
		money += value;
	}

	//指定値分だけ所持金を減らす
	public void withdrawMoney(int value){
		money = Math.max(money - value, 0);
	}

	public int getPrizeMoney(){
		return prizeMoney;
	}

	public void setPrizeMoney(int money){
		prizeMoney = money;
	}

	public void depositPrizeMoney(int value){
		prizeMoney += value;
	}

	public void withdrawPrizeMoney(int value){
		prizeMoney = Math.max(prizeMoney - value, 0);
	}

	public void information(String message){
		sendMessage(ChatColor.AQUA + message);
	}

	public void addition(String message){
		sendMessage(ChatColor.GRAY + message);
	}

	public void warning(String message){
		sendMessage(ChatColor.RED + message);
	}

	public void sendMessage(String message){
		getPlayer().sendMessage(message);
	}

	public void sendTitle(String message){
		getPlayer().sendTitle(message, "", 0, 50, 10);
	}

}
