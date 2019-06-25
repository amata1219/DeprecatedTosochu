package amata1219.tosochu.game;

import org.bukkit.World;

public class ImmutableLocation {

	public final World world;
	public final int x, y, z;

	public static ImmutableLocation at(World world, int x, int y, int z){
		return new ImmutableLocation(world, x, y, z);
	}

	public ImmutableLocation(World world, int x, int y, int z){
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
