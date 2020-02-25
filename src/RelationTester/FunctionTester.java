package RelationTester;
import java.lang.Math;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.*;
import org.w3c.dom.Document;
public class FunctionTester {
	
	enum Function{
		Sine,
		/*
		 * Sine Metamorphic relations: 
		 * 	A) Sin(x) 	=  sin(x + 2π)
		 * 	B) Sin(x) 	= -sin(x + π)
		 * 	C) Sin(x)   = -sin(-x)  
		 * 	D) Sin(x) 	=  sin(π - x)
		 * 	E) Sin(x) 	= -sin(2π - x)
		 */
		Cos,
		/*
		 * Sine Metamorphic relations: 
		 * 	A) Cos(x) 	=  cos(x + 2π)
		 * 	B) Cos(x) 	= -cos(x + π)
		 * 	C) Cos(x)   =  cos(2π - x)  
		 * 	D) Cos(x) + cos(y) + cos(z) - cos(x+y+z)  =  2*cos((x+y)/2) * cos((x-z)/2) + cos(π/2- (x+y+2z)/2) * cos(π/2 - (x+y)/2)
		 */
		Tan
	}
	
	enum Comparator {
		Equal,
		LessThan,
		LessThanOEqual,
		MoreThan,
		MoreThanOEqual
	}
	
	public FunctionTester(int x, int y, Function f, Comparator c){
		// Perform source test case
		double result = 0;
		switch(f) {
			case Sine:
				result = performSine(Math.toRadians(x));
				break;
			case Cos:
				result = performCos(Math.toRadians(x));
				break;
			case Tan:
				result = performTan(Math.toRadians(x));
				break;
		}
		
		double expected = y;
		try {
			assert(result ==  expected): "Source test case failed!!";
		} catch (AssertionError e) {System.out.println(e);}
		
		
		System.out.printf("Source test case: \n");
		System.out.printf("Expected= %f\n", expected);
		System.out.printf("Result= %f\n", result);
		
		metaTest(x, c, f);
		metaTest(x+52, c, f);
		metaTest(x-5352, c, f);
		metaTest(x+2562, c, f);
		metaTest(x+354*3, c, f);
		metaTest(x-347*17, c, f);
	}
	
	private void metaTest (int x, Comparator c, Function f) {
		double Ain, Bin, Aout = 0, Bout = 0;
		
		Ain = Math.toRadians(x);
		
		Bin = Ain + (2*Math.PI);
		
		
		switch (f) {
			case Sine:
				Aout = performSine(Ain);
				Bout = performSine(Bin);
				break;
			case Cos:
				Aout = performCos(Ain);
				Bout = performCos(Bin);
				break;
			case Tan:
				Aout = performTan(Ain);
				Bout = performTan(Bin);
				break;
		}
		
		try {
			switch (c) {
				case Equal:
					assert(Bout == Aout): "Meta test case failed, not Equal!!";
					break;
				case LessThan:
					assert(Bout < Aout): "Meta test case failed!!";
					break;
				case MoreThan:
					assert(Bout > Aout): "Meta test case failed!!";				
					break; 
			}
		} catch (AssertionError e) {System.out.println(e);}
		showOutIn (Ain, Aout, Bin, Bout);
		
	}
	
	private void showOutIn (double Ain, double Aout, double Bin, double Bout) {
		System.out.println();
		System.out.printf("Ain=  %f\n", Ain);
		System.out.printf("Bin=  %f\n", Bin);
		System.out.printf("Aout= %f\n", Aout);
		System.out.printf("Bout= %f\n", Bout);
	}
	
	private double performSineBad(double x) {
		int n = 10;
        int i,j,fac;
        double sine = 0;
        for(i=0; i<=n; i++){
            fac = 1;
            for(j=2; j<=2*i+1; j++){
                fac*=j;
            }
            sine+=Math.pow(-1.0,i)*Math.pow(x,2*i+1)/fac;
        }
        return sine;
	}
	
	private double performSine(double x) {
		return Math.sin(x);
	}
	
	private double performCos(double x) {
		return Math.cos(x);
	}
	
	private double performTan(double x) {
		return Math.tan(x);
	}
	
	public static void main(String[] args) throws CompileException, InvocationTargetException {
		/*try {
			XMLinterface xmlInterface = new XMLinterface();
			Document doc = null;
			xmlInterface.readXML("C:/Users/areid/OneDrive/Documents/COMPSCI/YEAR 3/MT/Java project/src/RelationTester/Students.xml");
			xmlInterface.outputXML();
		} catch (Exception e) {
			e.printStackTrace();
	    }
		*/
		String relationExpression = "Math.sin(x)";
		ExpressionEvaluator ee = new ExpressionEvaluator();
		ee.setParameters(new String[]{"x"}, new Class[]{Double.class});	
		ee.setExpressionType(Double.class);
		ee.cook(relationExpression);
		
		Double out = (Double) ee.evaluate(new Object[] { Math.toRadians(30) });
		System.out.println(out.toString());
		ee.cook("Math.sin(x+(2*Math.PI))");
		
		System.out.println(ee.evaluate(new Object[] { Math.toRadians(30) }).toString());
		out = (Double) ee.evaluate(new Object[] { Math.toRadians(30) });
		System.out.println(out.toString());
		//FunctionTester fe = new FunctionTester(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Function.Cos, Comparator.Equal);
	}
}