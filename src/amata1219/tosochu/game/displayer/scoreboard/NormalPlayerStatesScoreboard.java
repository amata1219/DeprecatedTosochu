package amata1219.tosochu.game.displayer.scoreboard;

import org.bukkit.entity.Player;

import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.text.MoneyTextFormatter;

public class NormalPlayerStatesScoreboard extends StatesScoreboard {

	/*
	 * -Run For Money- Title
		逃走中		13
					12
		あなたの役職11
		＜役職＞	10
					9
		現在の賞金	8
		賞金		7
		（０円/秒）	6
					5
		難易度		4
		ノーマル	3
					2
		参加人数	1
		<人数>		0
	 */

	public NormalPlayerStatesScoreboard(GameAPI game, Player player) {
		super(game, player);

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
	public void updateStates() {
		updateText(10, game.gerProfession(player).toDisplayName());
		updateText(7, MoneyTextFormatter.format(game.getMoney(player)));
		updateText(6, "(" + MoneyTextFormatter.format(game.getLoadedMapSettings().getUnitPriceOfPrizeMoney(game.getDifficulty(player))) + " / 秒)");
		updateText(0, String.valueOf(game.getPlayers().size()));
	}

}
