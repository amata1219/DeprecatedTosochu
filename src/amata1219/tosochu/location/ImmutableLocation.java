package amata1219.tosochu.location;

public class ImmutableLocation {

	public final int x, y, z;

	public static ImmutableLocation at(int x, int y, int z){
		return new ImmutableLocation(x, y, z);
	}

	public ImmutableLocation(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
