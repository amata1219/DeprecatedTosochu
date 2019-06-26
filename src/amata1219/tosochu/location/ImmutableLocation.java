package amata1219.tosochu.location;

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

}
