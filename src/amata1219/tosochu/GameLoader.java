package amata1219.tosochu;

import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.GameAPI;
import amata1219.tosochu.game.TosoGame;

public class GameLoader {

	private MapSettings loadedMapSettings;
	private GameAPI game;

	public GameLoader(){

	}

	public void load(MapSettings settings){
		loadedMapSettings = settings;
		reload();
	}

	public void reload(){
		unload();

		game = new TosoGame(loadedMapSettings);
	}

	public void unload(){
		//試合が終了していなければ強制終了する
		if(!game.isEnded())
			game.forcedTermination();
	}

	public GameAPI getGame(){
		return game;
	}

}
