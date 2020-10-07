package workers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.janino.ExpressionEvaluator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import xml.TestCase;
import xml.XMLinterface;

public class NumberWorker extends InterfaceAdapter{
	double workingInput, workingOutput;
	final double SOURCEINPUT, SOURCEOUTPUT;
	ArrayList<TestCase<Double>> testCases;
	Document doc;
	
	public NumberWorker() {
		this.SOURCEINPUT = 0;
		this.SOURCEOUTPUT = 0;
	}
	
	public NumberWorker(String valueIn, String valueOut) {
		this.SOURCEINPUT = Double.parseDouble(valueIn);
//		System.out.println("Value out is: " + valueOut);
		this.SOURCEOUTPUT = Double.parseDouble(valueOut);
		testCases = new ArrayList<TestCase<Double>>();
		addTotestCases(SOURCEINPUT, SOURCEOUTPUT);
	}
	
	private void addTotestCases (double in, double out) {
		testCases.add(new TestCase<Double>(in, out));
	}
	
	private void setWorkingNum(double num, boolean input) {
		if (input){
			workingInput = num;
		} else {
			workingOutput = num;
		}
	}
	
	private double getWorkingNum(boolean input) {
		if (input){
			return workingInput;
		} else {
			return workingOutput;
		}
	}
	
	public void addCurrTestCase() {
		addTotestCases(workingInput, workingOutput);
		workingInput = SOURCEINPUT;
		workingOutput = SOURCEOUTPUT;
	}
	
	public void writeTestCases(String filename) {
		Document doc  = XMLinterface.getXMLinterface().blankXML();
		
		Element inElement, outElement, testElement, root;
		root = doc.createElement("Testcases");
    	doc.appendChild(root);
		for (TestCase<Double> testCase : testCases) {
			assert(testCase != null);
			testElement = doc.createElement("TestCase");
			root.appendChild(testElement);
			inElement = doc.createElement("Input");
			assert(testCase.getInput() != null);
			inElement.setTextContent(testCase.getInput().toString());
	        testElement.appendChild(inElement);
	        outElement =doc.createElement("Output");
	        assert(testCase.getOutput() != null);
			outElement.setTextContent(testCase.getOutput().toString());
	        testElement.appendChild(outElement);
		}
		XMLinterface.getXMLinterface().saveMetaTests(doc, root, filename);
    }
	
	public String rawToType(String raw, boolean input){
		return raw;
	}
	
	public boolean checkEqual (String str1, String str2){
		double num1 = Double.parseDouble(str1);
		double num2 = Double.parseDouble(str2);
		if (num1 == num2) {
			return true;
		} else {
			return false;
		}
    }
	
//    @Override
//    public boolean checkEqual(String path1, String path2) {
//        return false;
//    }

    @Override
    public void applyFunction(boolean input, String function) throws Exception {
        double num = getWorkingNum(input);
        ExpressionEvaluator ee = new ExpressionEvaluator();
        ee.setParameters(new String[]{"x"}, new Class[]{double.class});
        ee.setExpressionType(double.class);
        System.out.println("COOKING: " +  function);
        ee.cook(function);
        double out;
        out = (double) ee.evaluate(new Object[] { num });
        setWorkingNum(out, input);
    }
}
