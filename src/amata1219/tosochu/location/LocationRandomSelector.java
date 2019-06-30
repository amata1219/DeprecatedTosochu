package amata1219.tosochu.location;

import java.util.List;
import java.util.Random;

public class LocationRandomSelector {

	private final Random random = new Random();
	private final List<ImmutableLocation> locations;

	public LocationRandomSelector(List<ImmutableLocation> locations){
		if(locations.isEmpty())
			new IllegalArgumentException("Locations can not be empty");

		this.locations = locations;
	}

	public ImmutableLocation select(){
		return locations.get(random.nextInt(locations.size()));
	}

}
