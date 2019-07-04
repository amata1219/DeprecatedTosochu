package amata1219.tosochu.game.displayer.scoreboard;

import org.bukkit.entity.Player;

import amata1219.tosochu.game.GameAPI;

public class AdministratorStatesScoreboard extends StatesScoreboard {

	/*
	 * -Run For Money-
		逃走中		14
					13
		あなたの役職12
		＜役職＞	11
					10
		参加人数	9
		逃走者		8
		生存		7
		牢獄		6
		ハンター	5
		通報部隊	4
	 */

	public AdministratorStatesScoreboard(GameAPI game, Player player) {
		super(game, player);

		initialize(
			"逃走中",
			"",
			"あなたの役職",
			"[役職]",
			"",
			"参加人数: [人数]",
			"逃走者: [人数]",
			"生存者: [人数]",
			"脱落者: [人数]",
			"ハンター: [人数]",
			"通報部隊: [人数]"
		);
	}

	@Override
	public void updateProfession() {
		updateText(11, game.getProfession(player).getDisplayName());
	}

	@Override
	public void updateStates() {
		updateText(9, "参加人数: " + (game.getPlayers().size() - game.getSpectators().size()));

		int numberOfRunaways = game.getRunaways().size();
		int numberOfDropouts = game.getDropouts().size();
		updateText(8, "逃走者: " + numberOfRunaways + numberOfDropouts);
		updateText(7, "生存者: " + numberOfRunaways);
		updateText(6, "脱落者: " + numberOfDropouts);
		updateText(5, "ハンター: " + game.getHunters().size());
		updateText(4, "通報部隊: " + game.getReporters().size());
	}

}
