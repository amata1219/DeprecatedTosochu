package amata1219.tosochu.game.displayer.scoreboard;

import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.game.GamePlayer;
import amata1219.tosochu.text.MoneyFormatter;

public class NormalPlayerStatesScoreboard extends StatesScoreboard {

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
		参加人数	2
		<人数>		1
	 */

	public NormalPlayerStatesScoreboard(GameAPI game, GamePlayer gamePlayer) {
		super(game, gamePlayer);

		//スコアボードにテキストをセットする
		initialize(
			"逃走中",
			"",
			"あなたの役職",
			"[役職]",
			"",
			"現在の賞金",
			"[賞金]",
			"([賞金単価] / 秒)",
			"",
			"難易度",
			"[難易度]",
			"",
			"参加人数",
			"[参加人数]"
		);
	}

	@Override
	public void updateProfession() {
		updateText(11, game.getProfession(player).getDisplayName());
	}

	@Override
	public void updateStates() {
		//賞金の更新
		updateText(8, MoneyFormatter.format(gamePlayer.getMoney()));

		//賞金単価の更新
		updateText(7, "(" + MoneyFormatter.format(game.getLoadedMapSettings().getUnitPriceOfPrizeMoney(gamePlayer.getDifficulty())) + " / 秒)");

		//参加人数の更新
		updateText(1, String.valueOf(game.getOnlinePlayers().size()));
	}

}
