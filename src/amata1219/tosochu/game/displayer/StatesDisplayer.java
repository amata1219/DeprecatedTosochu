package amata1219.tosochu.game.displayer;

import org.bukkit.entity.Player;

import amata1219.tosochu.MapLoader;
import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.game.GamePlayer;
import amata1219.tosochu.game.Profession;
import amata1219.tosochu.game.displayer.scoreboard.AdministratorStatesScoreboard;
import amata1219.tosochu.game.displayer.scoreboard.NormalPlayerStatesScoreboard;
import amata1219.tosochu.game.displayer.scoreboard.StatesScoreboard;
import amata1219.tosochu.text.MoneyFormatter;
import amata1219.tosochu.text.TimeFormatter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class StatesDisplayer {

	private final GameAPI game;
	private final GamePlayer gamePlayer;
	private final Player player;

	private StatesScoreboard board;

	private boolean actionBarMode;

	//アクションバーのテキスト(messages.ymlで設定可能)
	//経過時間:15:00　金額:210,000円　逃走者の数:30人　（←自分の役職に対する人数）

	public StatesDisplayer(GamePlayer gamePlayer){
		this.game = MapLoader.getLoader().getGame();
		this.gamePlayer = gamePlayer;
		this.player = gamePlayer.getPlayer();

		board = new NormalPlayerStatesScoreboard(game, gamePlayer);

		board.setDisplay(true);
	}

	public StatesScoreboard getStatesScoreoard(){
		return board;
	}

	public boolean isAdministratorMode(){
		return board instanceof AdministratorStatesScoreboard;
	}

	public void setAdiministratorMode(boolean administratorMode){
		if(administratorMode == isAdministratorMode())
			return;

		board = administratorMode ? new AdministratorStatesScoreboard(game, gamePlayer) : new NormalPlayerStatesScoreboard(game, gamePlayer);

		//表示を更新する
		board.setDisplay(true);
	}

	public boolean isActionBarMode(){
		return actionBarMode;
	}

	public void setActionBarMode(boolean actionBarMode){
		if(this.actionBarMode == actionBarMode)
			return;

		this.actionBarMode = actionBarMode;

		board.setDisplay(!actionBarMode);
	}

	public void update(boolean byTimer){
		if(byTimer){
			if(actionBarMode){
				Profession profession = game.getProfession(gamePlayer);
				player.spigot().sendMessage(
					ChatMessageType.ACTION_BAR,
					new TextComponent(
						"経過時間: " + TimeFormatter.format(game.getElapsedTime())
						+ "金額: " + MoneyFormatter.format(gamePlayer.getMoney())
						+ profession.getDisplayName() + "の人数: " + game.getPlayersByProfession(profession).size()
					)
				);
			}
		}else{
			board.updateStates();
		}
	}

}
