package Sine;
import java.lang.Math;	
public class SineExample {
	public SineExample(int x, int y){
		// Perform source test case
		double result = performSine(Math.toRadians(x));
		double expected = y;
		assert(result ==  expected);
		System.out.printf("Source test case: \n");
		System.out.printf("Expected= %f\n", expected);
		System.out.printf("Result= %f\n", result);
		
		
		metaTest(x);
		metaTest(x+52);
		metaTest(x-5352);
		metaTest(x+2562);
		metaTest(x+354*3);
		metaTest(x-347*17);
	}
	
	private void metaTest (int x) {
		double Ain, Aout, Bin, Bout;
		Ain = Math.toRadians(x);
		Bin = Ain + (2*Math.PI);
		
		Aout = performSine(Ain);
		Bout = performSine(Bin);
		
		assert(Aout == Bout);
		System.out.println();
		System.out.printf("Ain=  %f\n", Ain);
		System.out.printf("Bin=  %f\n", Bin);
		System.out.printf("Aout= %f\n", Aout);
		System.out.printf("Bout= %f\n", Bout);
	}
	
	private double performSine(double x) {
		return Math.sin(x);
	}
	
	public static void main(String[] args) {
		SineExample se = new SineExample(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}
}
