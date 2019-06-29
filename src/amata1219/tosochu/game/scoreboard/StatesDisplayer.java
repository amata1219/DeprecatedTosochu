package amata1219.tosochu.game.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import amata1219.tosochu.game.Game;

public class StatesDisplayer {

	//スコアボードのラッパークラス

	private final Game game;
	private final Player player;

	private final Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
	private final Objective objective = board.registerNewObjective(ChatColor.AQUA + "- Run for Money -", "dummy");

	private String playerCount, profession, money;

	public StatesDisplayer(Game game, Player player){
		this.game = game;
		this.player = player;

		set(0, "難易度: " + game.difficulty.toString());
		set(1, "");
		set(2, playerCount = "参加人数: " + game.getPlayers().size());
		set(3, "");
		set(4, profession = "あなたの役職: " + getProfession(player));
		set(5, "");
		set(6, money = "現在の獲得賞金: " + game.getMoney(player));
	}

	public void setDisplay(boolean display){
		player.setScoreboard(display ? board : null);
	}

	public void updatePlayerCount(){
		remove(playerCount);
		set(2, playerCount = "参加人数: " + game.getPlayers().size());
	}

	public void updateProfession(){
		remove(profession);
		set(4, profession = getProfession(player));
	}

	public void updateMoney(int money){
		remove(this.money);
		set(6, this.money = "現在の獲得賞金: " + money);
	}

	private void remove(String text){
		board.resetScores(text);
	}

	private void set(int score, String text){
		objective.getScore(text).setScore(score);
	}

	private String getProfession(Player player){
		if(game.isRunaway(player))
			return "逃走者";
		else if(game.isDropout(player))
			return "脱落者";
		else if(game.isHunter(player))
			return "ハンター";
		else if(game.isSpectator(player))
			return "観戦者";
		else
			return "参加者";
	}

}
