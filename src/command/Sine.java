package command;
import java.lang.Math;
public class Sine {
	public static void main(String[] args) {
		Double x = Double.parseDouble(args[0]);
		x = Math.toRadians(x);
		Double result = Math.sin(x);
		System.out.print(result);
	}
}
