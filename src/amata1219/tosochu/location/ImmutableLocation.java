package amata1219.tosochu.location;

import org.bukkit.Location;
import org.bukkit.World;

public class ImmutableLocation {

	public final int x, y, z;

	public static ImmutableLocation at(int x, int y, int z){
		return new ImmutableLocation(x, y, z);
	}

	public static ImmutableLocation at(String xyz){
		String[] values = xyz.split(",");
		return at(Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
	}

	public ImmutableLocation(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location toLocation(World world){
		return new Location(world, x, y, z);
	}

	@Override
	public String toString(){
		return x + "," + y + "," + z;
	}

}
