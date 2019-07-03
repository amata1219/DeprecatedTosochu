package amata1219.tosochu.game.displayer.scoreboard;

import org.bukkit.entity.Player;

import amata1219.tosochu.game.GameAPI;

public class AdministratorStatesScoreboard extends StatesScoreboard {

	/*
	 * -Run For Money-
		逃走中		10
					9
		あなたの役職8
		＜役職＞	7
					6
		参加人数	5
		逃走者		4
		生存		3
		牢獄		2
		ハンター	1
		通報部隊	0
	 */

	public AdministratorStatesScoreboard(GameAPI game, Player player) {
		super(game, player);
	}

	@Override
	public void updateStates() {
	}

}
