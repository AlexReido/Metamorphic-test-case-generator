package workers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import execution.Execution;
import solo.Graph;
import solo.Node;
import solo.ObjectToString;
import solo.ObjectToFile;
import xml.TestCase;
import xml.XMLinterface;

public class GraphWorker extends InterfaceAdapter {
	private Graph workingInput; 
	private int workingOutput;
	private final Graph SOURCEINPUT; 
	private final int SOURCEOUTPUT;
	private ArrayList<TestCase<String>> testCases;
	private Document doc;
	
	public GraphWorker() {
		this.SOURCEINPUT = null;
		this.SOURCEOUTPUT = 0;
	}
	
	public GraphWorker(String valueIn, String valueOut) {
		this.SOURCEINPUT = readFile(valueIn);
		this.SOURCEOUTPUT = Integer.parseInt(valueOut);
		workingInput = SOURCEINPUT;
		workingOutput = SOURCEOUTPUT;
		testCases = new ArrayList<TestCase<String>>();
		addTotestCases(SOURCEINPUT, SOURCEOUTPUT);
	}
	
	private void addTotestCases (Graph in, int out) {
		ObjectToString gtXml = new ObjectToString();
		String graphString = null;
		try {
			graphString = gtXml.objToString(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		testCases.add(new TestCase<String>(graphString, Integer.toString(out)));
	}
	
	public Graph readFile(String filename) {
		ObjectToFile oToF = new ObjectToFile();
		try {
			return (Graph) oToF.readIn(filename);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	private void setWorkingdistance(int x) {
		workingOutput = x;
	}
	
	private void setWorkingGraph(Graph graph) {
		workingInput = graph;
	}
	
	private Graph getWorkingGraph() {
		return workingInput;
	}
	
	public void addCurrTestCase() {
		addTotestCases(workingInput, workingOutput);
		workingInput = SOURCEINPUT;
		workingOutput = SOURCEOUTPUT;
	}
	
	
	//TODO THIs
	public void writeTestCases(String filename) {
		Document doc  = XMLinterface.getXMLinterface().blankXML();
		XMLinterface xmlInterface = XMLinterface.getXMLinterface();
		ObjectToString objectToString = new ObjectToString();
		
		Element inElement, outElement, testElement, root;
		root = doc.createElement("Testcases");
    	doc.appendChild(root);
		for (TestCase<String> testCase : testCases) {
			assert(testCase != null);
			testElement = doc.createElement("TestCase");
			root.appendChild(testElement);
			inElement = doc.createElement("Input");
			assert(testCase.getInput() != null);
			try {
				inElement.setTextContent(testCase.getInput());
				testElement.appendChild(inElement);
		        outElement =doc.createElement("Output");
		        assert(testCase.getOutput() != null);
				outElement.setTextContent(testCase.getOutput());
				testElement.appendChild(outElement);
			} catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		XMLinterface.getXMLinterface().saveMetaTests(doc, root, filename);
    }
	
	public String rawToType(String raw, boolean input){
		if(input) {	
			ObjectToString gtXml = new ObjectToString();
			Graph graph = null;
			try {
				graph = (Graph) gtXml.fromString(raw);
				return writeGraphToFile(graph, input);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else{
			return raw;
		}
		return null;
		
	}

    private String writeGraphToFile(Graph graph, boolean input){
    	ObjectToFile objectToFile = new ObjectToFile();
    	String fileName;
    	fileName = "src/execution/in.ser";
    	try {
    		objectToFile.writeOut(graph, fileName);
    		return fileName;
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
		return null;
    }
    
    private Graph generateRandomGraph() {
    	Graph graph = new Graph();
    	Random random = new Random();
        int nodeCount = random.nextInt(30);
    	for (int i = 0; i < (nodeCount+8); i++) {
    		char letter = (char)(i+65);
    		Node node = new Node(String.valueOf(letter));
    		graph.addNode(node);
		}
    	double p = (Math.log(nodeCount)/nodeCount); // not multiplying by two because will be done from both node   	
    	for (Node node : graph.getNodes()) {
    		for (Node node2 : graph.getNodes()) {
    			if (!node.equals(node2)) {
    				double x = Math.random();
    				if (x < p) {
    					int distance = random.nextInt(30);
    					node.addDestination(node2, distance);
    					node2.addDestination(node, distance);
    				}
    			}
    		}
		}
    	graph.setSourceNode("A");
    	int d = random.nextInt(7);
    	char letter = (char)(d+66);
		graph.setDestinationNode(String.valueOf(letter));
    	return graph;
    }
    
    public void random(boolean input) {
    	Graph graph = generateRandomGraph();
    	setWorkingGraph(graph);
    	Execution execution = new Execution();
    	String filename = writeGraphToFile(graph, input); 
    	String[] args = new String[1];
    	args[0] = filename;

    	String distance = execution.runProgram("programsUnderTest/shortestPath.jar", args);
    	setWorkingdistance(Integer.parseInt(distance));
    }
    
    public void reverse(boolean input) {
    	
    	String sourceNode, destinationNode;
    	Graph graph  = getWorkingGraph();
    	System.out.println("HERE");
    	sourceNode = graph.getSourceNode(); 
    	destinationNode = graph.getDestinationNode(); 
    	System.out.println("HERE");
    	graph.setDestinationNode(sourceNode);
    	graph.setSourceNode(destinationNode);
    	graph.printGraph();
    	
    	setWorkingGraph(graph);
    }
    
    public boolean checkEqual (String str1, String str2){
    	System.out.println("result = " +str2);
    	System.out.println("out = "+ str1);
    	int num1 = Integer.parseInt(str1);
		int num2 = Integer.parseInt(str2);
		if (num1 == num2) {
			return true;
		} else {
			return false;
		}
//		Graph graph1 = readFile(path1);
//		Graph graph2 = readFile(path2);
//		for (Node node : graph1.getNodes()) {
//			Node node2 = graph2.getNode(node.getName());
//			if (node2 == null) {
//				return false;
//			}else {
//				if (!node.getAdjacentNodes().equals(node2.getAdjacentNodes())){
//					return false;
//				}
//				graph2.removeNode(node2);
//			}
//		}
//		if (!graph2.getNodes().isEmpty()) {
//			return false;
//		}
//		return true;
    }
	
}
