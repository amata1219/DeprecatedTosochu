package amata1219.tosochu;

import org.bukkit.Location;
import org.bukkit.World;

public class ImmutableLocation {

	public final int x, y, z;

	public static ImmutableLocation at(int x, int y, int z){
		return new ImmutableLocation(x, y, z);
	}

	public static ImmutableLocation at(Location location){
		return at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	public ImmutableLocation(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Location toLocation(World world){
		return new Location(world, x, y, z);
	}

}
