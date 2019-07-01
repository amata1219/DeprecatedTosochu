package amata1219.tosochu;

import amata1219.tosochu.config.MapSettings;
import amata1219.tosochu.game.OldGame;

public class GameLoader {

	private OldGame playing;

	public GameLoader(){

	}

	public boolean isPlaying(){
		return playing != null;
	}

	public OldGame getPlaying(){
		return playing;
	}

	public void load(MapSettings settings){

	}

	public void unload(){

	}

}
