package amata1219.tosochu.game.displayer;

import org.bukkit.entity.Player;

import amata1219.tosochu.game.GameAPI;

public class StatesDisplayerForNormalPlayer extends StatesDisplayer {

	/*
	 * -Run For Money- Title
		逃走中		14
					13
		あなたの役職12
		＜役職＞	11
					10
		現在の賞金	9
		賞金		8
		（０円/秒）	7
					6
		難易度		5
		ノーマル	4
					3
		参加人数	1
		<人数>		0
	 */

	public StatesDisplayerForNormalPlayer(GameAPI game, Player player) {
		super(game, player);

		//スコアボードにテキストをセットする
		initialize(
			"15行目",
			"14行目",
			"13行目"
		);
	}

	public void updateProfession(){
		update(0, "");
	}

	public void updatePrizeMoney(){

	}

	public void updatePrizeMoneyPerSecond(){

	}

	public void updateNumberOfPlayers(){

	}

}
