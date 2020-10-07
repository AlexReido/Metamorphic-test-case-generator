package execution;

import org.w3c.dom.Document;

import command.MetaTestOrchestrator;
import workers.CSVWorker;
import workers.FileInterface;
import xml.TestCase;
import xml.XMLinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.io.*;
import java.lang.*;
import java.util.*;

public class Execution {

    private int testsPassed = 0;
    private static String[] addS(int n, String arr[], String x)
    {
        int i;
        List<String> arrlist = new ArrayList<String>(Arrays.asList(arr));
        arrlist.add(n,x);
        arr = arrlist.toArray(arr);
        return arr;
    }
    /* 
     * Will run either a jar file or an exe with the arguments args, returning the last line of output
     */
    public String runProgram(String exe, String[] args){
        try {
            Process process;
            String[] params = addS(0, args, exe);;
            if (exe.substring(exe.lastIndexOf(".")).equals(".exe")){
                process = new ProcessBuilder(params).start();
            } else if((exe.substring(exe.lastIndexOf(".")).equals(".jar"))){
                params = addS(0, params, "-jar");
                params = addS(0, params, "java");
                process = new ProcessBuilder(params).start();
            } else{
                throw new IOException("FILE NOT RUNNABLE");
            }

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line, result = "";
            //The result is always the last line
            while ((line = br.readLine()) != null) {
            	System.out.println("PROGRAM OUTPUT= "+line);
                result = line;
            }
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
    
    public void ExecuteTestCases(){
        Document xml = XMLinterface.getXMLinterface().readXML("src/xml/TestCases.xml");
        ArrayList<TestCase<String>> rawStringTestCases = XMLinterface.getXMLinterface().getTestCases(xml);
        String exe = XMLinterface.getXMLinterface().getTestsExecutable();
        String typeIn = XMLinterface.getXMLinterface().getTypeIn();
        String typeOut = XMLinterface.getXMLinterface().getTypeOut();
        
        MetaTestOrchestrator mtcg = new MetaTestOrchestrator();
        System.out.println("type In " + typeIn);
        FileInterface fi = mtcg.getFileInterface(typeIn);
        assert(!fi.equals(null));
        ArrayList<TestCase> testCases = new ArrayList<TestCase>();
        int testsRun = 0;
        try {
	        for (TestCase<String> test : rawStringTestCases) {
	        	System.out.println("T:" + Integer.toString(testsRun));
	        	String in = (String) fi.rawToType(test.getInput(), true);
	        	String out = (String) fi.rawToType(test.getOutput(), false);
	        	String[] args = in.split(",");
        		String result  = runProgram(exe, args);
        		//The result is always the last line printed by the program under test 
        		try {
        			if(in.contains(",")) {
        				assert(fi.checkEqual(out, args[1])): "Meta test case failed!!";
        			} else {
        				assert(fi.checkEqual(out, result)): "Meta test case failed!!";
        			}
        			testsPassed++;
        		}catch (AssertionError ae) {
                	System.out.println("T:" + Integer.toString(testsRun) + " failed!!");
        			//ae.printStackTrace();
        		}
	            testsRun++;
	        }
        }catch (Exception e) {
			e.printStackTrace();
		}
        System.out.println("Tests Passed: " + testsPassed + "/" + testsRun);
    }

    public static void main(String[] args) {
        Execution execution= new Execution();
        execution.ExecuteTestCases();
    }
}
