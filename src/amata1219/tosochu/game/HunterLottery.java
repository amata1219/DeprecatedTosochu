package amata1219.tosochu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import amata1219.tosochu.playerdata.PlayerData;

public class HunterLottery {

	public static GamePlayer drawLottery(List<GamePlayer> players){
		if(players.isEmpty())
			return null;

		int size = players.size();

		List<PlayerData> dataList = new ArrayList<>(size);
		for(GamePlayer player : players)
			dataList.add(player.getData());

		int totalProbability = 0;
		for(PlayerData data : dataList){
			int number = data.getNumberOfTimesThatBecameHunter();
			totalProbability += number > 0 ? number : 1;
		}

		List<Pair> pairs = new ArrayList<>(size);
		for(int i = 0; i < size; i++){
			int number = dataList.get(size - 1 - i).getNumberOfTimesThatBecameHunter();
			pairs.add(new Pair(players.get(i), number > 0 ? number : 1));
		}

		int probability = new Random().nextInt(totalProbability) + 1;
		int cumulativeProbability = 0;
		for(Pair pair : pairs){
			cumulativeProbability += pair.probability;
			if(probability < cumulativeProbability)
				return pair.player;
		}

		return null;
	}

	private static class Pair {

		public final GamePlayer player;
		public final int probability;

		public Pair(GamePlayer player, int probability){
			this.player = player;
			this.probability = probability;
		}

	}

}
