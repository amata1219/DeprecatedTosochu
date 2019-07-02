package amata1219.tosochu;

import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.GameAPI;

public class GameLoader {

	private GameAPI nowPlayingGame;

	public GameLoader(){

	}

	public void load(MapSettings settings){
		//ゲームが行われていたら強制終了する
		if(nowPlayingGame != null)
			nowPlayingGame.forcedTermination();
	}

	public void unload(){

	}

	public GameAPI getNowPlayingGame(){
		return nowPlayingGame;
	}

}
