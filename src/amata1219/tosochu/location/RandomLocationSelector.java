package amata1219.tosochu.location;

import java.util.ArrayList;
import java.util.Random;

public class RandomLocationSelector {

	private final Random random = new Random();
	private final ArrayList<ImmutableLocation> locations;

	public RandomLocationSelector(ArrayList<ImmutableLocation> locations){
		if(locations.isEmpty())
			new IllegalArgumentException("Locations can not be empty");

		this.locations = locations;
	}

	public ImmutableLocation select(){
		return locations.get(random.nextInt(locations.size()));
	}

}
