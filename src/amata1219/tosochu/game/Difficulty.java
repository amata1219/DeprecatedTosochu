package amata1219.tosochu.game;

public enum Difficulty {

	EASY,
	NORMAL,
	HARD,
	HARDCORE;

	public String get(String text){
		return split(text)[ordinal()];
	}

	public int getAsInt(String text){
		return Integer.valueOf(get(text));
	}

	public double getAsDouble(String text){
		return Double.valueOf(get(text));
	}

	public static String[] split(String text){
		return text.split(",");
	}

	public static int[] splitAndToInt(String text){
		String[] values = split(text);
		int length = values.length;
		int[] args = new int[length];
		for(int i = 0; i < length; i ++)
			args[i] = Integer.valueOf(values[i]);
		return args;
	}

	public static double[] splitAndToDouble(String text){
		String[] values = split(text);
		int length = values.length;
		double[] args = new double[length];
		for(int i = 0; i < length; i ++)
			args[i] = Integer.valueOf(values[i]);
		return args;
	}

	public static String join(int[] args){
		StringBuilder builder = new StringBuilder(String.valueOf(args[0]));
		for(int i = 1; i < args.length; i++)
			builder.append(",").append(args[i]);
		return builder.toString();
	}

	public static String join(double[] args){
		StringBuilder builder = new StringBuilder(String.valueOf(args[0]));
		for(int i = 1; i < args.length; i++)
			builder.append(",").append(args[i]);
		return builder.toString();
	}

}
