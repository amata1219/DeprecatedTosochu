package amata1219.tosochu.location;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ImmutableLocation {

	public final World world;
	public final int x, y, z;

	public static ImmutableLocation at(World world, int x, int y, int z){
		return new ImmutableLocation(world, x, y, z);
	}

	public static ImmutableLocation at(World world, String xyz){
		String[] values = xyz.split(",");
		return at(world, Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
	}

	public ImmutableLocation(World world, int x, int y, int z){
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void teleport(Player player){
		player.teleport(new Location(world, x, y, z));
	}

	@Override
	public String toString(){
		return x + "," + y + "," + z;
	}

}
