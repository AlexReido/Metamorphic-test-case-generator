package workers;
import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ExpressionEvaluator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


import xml.TestCase;
import xml.XMLinterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CSVWorker extends InterfaceAdapter {
	ArrayList<String> workingInput, workingOutput;
	final ArrayList<String> SOURCEINPUT;
	final ArrayList<String> SOURCEOUTPUT;
	//boolean modeIn; // true when working with input false when working with output
	Document doc;
	
	ArrayList<TestCase<ArrayList<String>>> testCases;
	
	private void addTotestCases (ArrayList<String> in, ArrayList<String> out) {
		testCases.add(new TestCase<ArrayList<String>>(in, out));
	}
	

	
	public Class<?> getType(){
		ArrayList<String> list = new ArrayList<String>();
		return list.getClass();
	}
	
	private ArrayList<String> getWorkingCSV(boolean input) {
		if (input){
			return workingInput;
		} else {
			return workingOutput;
		}
	}
	
	private void setWorkingCSV(ArrayList<String> csv, boolean input) {
		if (input){
			workingInput = csv;
		} else {
			workingOutput = csv;
		}
	}
	
//	private Element ConvertToXml(ArrayList<String> csvList) {
////		Object e;
////		ObjectOutputStream outStream = new ObjectOutputStream();
////        XMLEncoder encoder = new XMLEncoder(outStream);
////        encoder.writeObject(csvList);
////        encoder.close();
////        //fos.close();
//		
//		XMLinterface xmLinterface = new XMLinterface();
//		doc = xmLinterface.blankXML();
//		Element e = xmLinterface.blankElement();
//		Node n = new Node(null);
//		for (int j = 0; j < csvList.size(); j++) {
//			e.appendChild(doc.createTextNode(csvList.get(j)));
//		}
//		
//		return e;
//	}
	public CSVWorker() {
		SOURCEINPUT = null;
		SOURCEOUTPUT = null;
	}
	
	public CSVWorker(String SourceIn, String SourceOut) {
		ArrayList<String> inputCSV = readFile(SourceIn);
		ArrayList<String> outputCSV = readFile(SourceOut);
		SOURCEINPUT = (ArrayList<String>) inputCSV.clone();
		SOURCEOUTPUT = (ArrayList<String>) outputCSV.clone();
		workingInput = (ArrayList<String>) inputCSV.clone();
		workingOutput = (ArrayList<String>) outputCSV.clone();
		System.out.println();
		System.out.println();
		System.out.println("SOURCE IN in:");
		printCSV(SOURCEINPUT);
		System.out.println();
		System.out.println("TEST Case Out:");
		printCSV(SOURCEOUTPUT);
		System.out.println();
		System.out.println();
		testCases = new ArrayList<TestCase<ArrayList<String>>>();
		testCases.add(new TestCase<ArrayList<String>>(inputCSV, outputCSV));
		
	}
	
//	public void saveCurrinput() {
//		tempIn = tempCsv;
//		tempCsv = sourceOutputCSV;
//		modeIn = false;
//	}
	
	public void addCurrTestCase() {
		addTotestCases(workingInput, workingOutput);
		workingInput = (ArrayList<String>) SOURCEINPUT.clone();
		workingOutput = (ArrayList<String>) SOURCEOUTPUT.clone();
	}
	
//	private ArrayList<TestCase<Element>> csvToTestcases(ArrayList<TestCase<ArrayList<String>>> testcaseList){
//		for (TestCase<ArrayList<String>> testcase : testcaseList) {
//			for (String line : testcase.getInput()) {
//				
//			}
//		}
//		return null;
//	}
	
	public void writeTestCases(String filename) {
		Document doc  = XMLinterface.getXMLinterface().blankXML();
		
		Element inElement, outElement, testElement, root, lineElement;
		root = doc.createElement("Testcases");
    	doc.appendChild(root);
		for (TestCase<ArrayList<String>> testCase : testCases) {

			assert(testCase != null);
			testElement = doc.createElement("TestCase");
			root.appendChild(testElement);
			inElement = doc.createElement("Input");
			assert(testCase.getInput() != null);
			String csv = "";
			for (String line : testCase.getInput()) {
				csv = csv + line + ",\n"; 
			}
			assert(csv != null);
			inElement.setTextContent(csv);
			
	        testElement.appendChild(inElement);
	
	        outElement =doc.createElement("Output");
	        csv = "";
	        assert(testCase.getOutput() != null);
	        for (String line : testCase.getOutput()) {
				csv = csv + line + ",\n"; 
			}
	        assert(csv != null);
			outElement.setTextContent(csv);
//	        for (String line : testCase.getOutput()) {
//	        	assert(line != null);
//	        	lineElement = doc.createElement("line");
//				lineElement.setTextContent(line);
//	        	outElement.appendChild(lineElement);
//	        }
	        testElement.appendChild(outElement);
		}
		XMLinterface.getXMLinterface().saveMetaTests(doc, root, filename);
    }


	public ArrayList<String> readFile(String filename){
        String line = "";
        String cvsSplitBy = ",";
        ArrayList<String> lines = new ArrayList<>();
        //System.out.println("Printing csv: ");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
            	
            	if (line.endsWith(",")) {
            		line = line.substring(0, (line.length()-1));
            	}
            	///System.out.println(line);
                lines.add(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
	
	public String rawToType(String raw, boolean input){
		//System.out.println("HERE");
		String[] array;
		array  = raw.split(",\n");
		List<String> list = Arrays.asList(array); 
		ArrayList<String> al = new ArrayList<String>(list);
		return writeToCSV(al, input);
	}

    private String writeToCSV(ArrayList<String> lines, boolean input){
    	String fileName;
    	if (input) {
    		fileName = "src/execution/in.csv";
    	} else {
    		fileName = "src/execution/out.csv";
    	}
    	try {
    	      File myObj = new File(fileName);
    	      if (myObj.createNewFile()) {
    	        System.out.println("File created: " + myObj.getName());
    	      } //else {
    	        //System.out.println("File already exists.");
    	      //}
    	    } catch (IOException e) {
    	      System.out.println("An error occurred.");
    	      e.printStackTrace();
    	    }
        try {
            FileWriter csvWriter = new FileWriter(fileName);
            for (String l : lines) {
                csvWriter.append(l);
                csvWriter.append(",\n");
            }
            csvWriter.flush();
            csvWriter.close();
            if (input) {
            	return (fileName + ",src/execution/temp.csv");
            }else {
            	return fileName;
            }
           
        } catch (IOException e) {
        System.out.println(e);
        }
		return null;
    }

    public boolean checkEqual (String path1, String path2){
        String line1, line2 = "";
        boolean equal = true;
        try {
            BufferedReader br1 = new BufferedReader(new FileReader(path1));
            BufferedReader br2 = new BufferedReader(new FileReader(path2));
            while ((line1 = br1.readLine()) != null) {
                line2 = br2.readLine();
                //System.out.println("A: "+ line1 + "\t B: " + line2);
                if (!line1.equals(line2)){
                    equal = false;
                }
            }
            if ((line2 = br2.readLine()) != null) {
                return false; // path2 csv contains extra lines
            }
            br1.close();
            br2.close();
            return equal;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void permute(boolean input){
//    	System.out.println();
//    	System.out.println("permuting: ");
//    	printCSV(getWorkingCSV(input));
    	Collections.shuffle(getWorkingCSV(input));
//    	System.out.println("permuted: ");
//    	printCSV(getWorkingCSV(input));
    }

    public void applyFunction(boolean input, String function){
        try {
        	ArrayList<String> list = getWorkingCSV(input);
        	ArrayList<String> newList = new ArrayList<String>();
            String cvsSplitBy = ",";
	        ExpressionEvaluator ee = new ExpressionEvaluator();
	        ee.setParameters(new String[]{"x"}, new Class[]{int.class});
	        ee.setExpressionType(int.class);
	        System.out.println("COOKING: " +  function);
	        ee.cook(function);
	        int out;
	        String newline;
	        for (String line : list) {
	        	out = (int) ee.evaluate(new Object[] { Integer.parseInt(line) });
	        	newline = Integer.toString(out);
	        	newList.add(newline);
			}
	        setWorkingCSV(newList, input);
        } catch (CompileException e) {
            e.printStackTrace();
        }catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
            //while ((line = br.readLine()) != null && line != "") {
                // use comma as separator
                //System.out.println("Evaluating: " +  Integer.parseInt(line.substring(0, line.length()-1)));
//                out = (int) ee.evaluate(new Object[] { Integer.parseInt(line.substring(0, line.length()-1)) });
//                newline = Integer.toString(out);
//                lines.add(newline + ",");
            	
 //           }
            
        
        
    

    public void randomMultiplication(boolean input){
    	ArrayList<String> list = getWorkingCSV(input);
    	ArrayList<String> newList = new ArrayList<String>();
        Random rand = new Random();
        int rVal;
        rVal = rand.nextInt(15);
        for (int i = 0; i < list.size(); i++) {
        	newList.add(Integer.toString(Integer.parseInt(list.get(i))* rVal));
		}
        setWorkingCSV(newList, input);
    }
//                // use comma as separator
//                //System.out.println("LINE : " + line);
//                num = Integer.parseInt(line.substring(0, line.length()-1));
//                add.add(Integer.toString(num + rand.nextInt(30)));
//                sub.add(Integer.toString(num - rand.nextInt(30)));
//                mul.add(Integer.toString(num * rand.nextInt(12)));
//
//        writeToCSV(add, filePre + fileNum + filePost);
//        fileNum++;
//        writeToCSV(sub, filePre + fileNum + filePost);
//        fileNum++;
//        writeToCSV(mul, filePre + fileNum + filePost);
//        
//    }

    public void dupMin(boolean input){
    	ArrayList<String> list = getWorkingCSV(input);
        int num, min = Integer.MAX_VALUE;
        for (String line : list) {
        	//TODO error handling not int
        	num = Integer.parseInt(line);
            if (num < min){
                min = num;
            }
		}   
        list.add(0, Integer.toString(min));
        setWorkingCSV(list, input);
    }

    public void dupMax(boolean input){
    	ArrayList<String> list = getWorkingCSV(input);
//    	if ( input) {
//    		list= SOURCEINPUT;
//    	} else {
//    		list = SOURCEOUTPUT;
//    	}
    	
//    	System.out.println();
//    	System.out.println("SOUCE IN ");
//    	printCSV(SOURCEINPUT);
        int num, max = Integer.MIN_VALUE;
        for (String line : list) {
        	num = Integer.parseInt(line);
            if (num > max){
                max = num;
            }
		}       
        list.add(list.size(), Integer.toString(max));
        setWorkingCSV(list, input);
//    	System.out.println("cSOURCE OUT ");
//    	printCSV(SOURCEOUTPUT);
//    	System.out.println();
    }
    
    public void printCSV(ArrayList<String> csv) {
    	for (String string : csv) {
			System.out.println("	"  + string);
		}
    }
    
    public void reverse(boolean input){	
//    	System.out.println();
//    	System.out.println("Reversing: ");
//    	printCSV(getWorkingCSV(input));
    	Collections.reverse(getWorkingCSV(input));
//    	System.out.println("csv now reversed: ");
//    	printCSV(getWorkingCSV(input));
//    	System.out.println();
    	
    }

//    public void duplicate(boolean input){
//        ArrayList<String> lines = new ArrayList<>();
//        lines = readCSV(from);
//        writeToCSV(lines, to);
//    }
}

