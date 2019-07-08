package amata1219.tosochu.game.displayer.scoreboard;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.game.GamePlayer;

public abstract class StatesScoreboard {

	//スコアは0～14の間を使用、重複無し

	protected final GameAPI game;
	protected final GamePlayer gamePlayer;
	protected final Player player;

	private final ScoreboardManager manager = Bukkit.getScoreboardManager();

	protected final Scoreboard board = manager.getNewScoreboard();
	protected final Objective objective = board.registerNewObjective(ChatColor.AQUA + "Run for Money", "dummy");

	//スコアの更新を円滑に行う為のボードのテキスト複製マップ
	protected HashMap<Integer, String> texts = new HashMap<>(15);

	protected StatesScoreboard(GameAPI game, GamePlayer gamePlayer){
		this.game = game;
		this.gamePlayer = gamePlayer;
		this.player = gamePlayer.getPlayer();

		//スコアボードの表示位置はサイドバーで固定する
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}

	//現在このスコアボードが表示されているか
	public boolean isDisplayingNow(){
		return board.equals(player.getScoreboard());
	}

	//スコアボードの表示するかどうか設定する
	public void setDisplay(boolean display){
		//常にスコアボードを表示する設定であれば戻る
		if(game.getSettings().isAlwaysDisplayScoreboard())
			return;

		//現在の状態と指定された状態が違えば状態を更新する
		if(display != isDisplayingNow())
			player.setScoreboard(display ? board : manager.getNewScoreboard());
	}

	public abstract void updateProfession();

	public abstract void updateStates();

	//指定されたスコアのテキストを書き換える
	protected void updateText(int score, String text){
		//スコアが範囲外であればエラーを投げる
		if(score < 0 || 14 < score)
			throw new IllegalArgumentException("Score must be in the range 0 to 15");

		//指定されたスコアに現在表示されているテキストを削除する
		board.resetScores(texts.get(score));

		//テキストをセットする
		set(score, text);
	}

	protected void initialize(String... texts){
		if(texts.length > 14)
			throw new IllegalArgumentException("Texts length be 15 or less");

		//配列の値を前から順にボードの上から下にセットしていく
		for(int i = 0; i < texts.length; i++)
			set(14 - i, texts[i]);
	}

	protected void set(int score, String text){
		//複製マップに新しいテキストを記録し、指定されたスコアに新しいテキストをセットする
		objective.getScore(texts.put(score, text)).setScore(score);
	}

}
