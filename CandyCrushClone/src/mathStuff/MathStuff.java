package mathStuff;

public class MathStuff {
	public static int randomRange(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
}
