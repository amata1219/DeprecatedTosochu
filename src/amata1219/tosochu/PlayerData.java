package amata1219.tosochu;

import java.util.UUID;

public class PlayerData {

	//プレイヤーのUUID
	public final UUID uuid;

	//逃走成功回数
	private int runawaySuccessCount;

	//ハンターになった回数
	private int becameHunterCount;

	//所持金
	private int money;

	public PlayerData(UUID uuid){
		this.uuid = uuid;
	}

	//逃走成功回数をインクリメントする
	public void incrementRunawaySuccessCount(){
		runawaySuccessCount++;
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
		this.money = money;
	}

	//指定値分だけ所持金を増やす
	public void depositMoney(int value){
		money += value;
	}

	//指定値分だけ所持金を減らす
	public void withdrawMoney(int value){
		money -= value;
	}

}
