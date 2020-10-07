package command;

import java.util.ArrayList;
import java.util.HashMap;
import workers.CSVWorker;
import workers.CommandOperation;
import workers.FileInterface;
import workers.GraphWorker;
import workers.NumberWorker;
import xml.XMLinterface;


import org.w3c.dom.Document;


public class MetaTestOrchestrator {

	/*
	 * This carries out the orchestration of the test case generator. It populates the hashmaps with the workers or file interfaces
	 * the test cases are read in from the xml interface
	 * The correct file worker is instantiated as fileInterface, This will pass the source test case to the worker
	 * The test cases are looped through with the all of transformation
	 * The correct operation for the interface is called to transform the input or output
	 * When all transformations are done addCurrTestCase() is called to add the current state of the worker to the test cases.
	 * this will be repeated for the amount of times specified in the test descriptor.
	 * This is repeated for each branch/ relation
	 * Finally the worker is told to send the test cases to the xml interface for saving
	 */
	private static HashMap<String, CommandFileInterface> interfaces = new HashMap<String, CommandFileInterface>();
	private static HashMap<String , CommandOperation> operations = new HashMap<String, CommandOperation>();

	private String getTempFile(String pre, String name, String post, int fileNumber){
		return pre + name + Integer.toString(fileNumber) + post;
	}
	
	public FileInterface getFileInterface(String fileInterface) {
//		System.out.println("getting fileinterface= " + fileInterface);
//		System.out.println();
		return (FileInterface) interfaces.get(fileInterface).create();
	}

	private void doOperation(String operation, FileInterface fileInterface, boolean input){
		try{
			operations.get(operation).operation(fileInterface, input);
		} catch (Exception e){
			System.out.println(e);
		}
	}
	
	/*
	 * input true if operations are on input
	 */
	private void branchTransform(FileInterface fileInterface, ArrayList<String> operations, String expression, boolean input){
		if (operations != null) {
			for (String op : operations) {
				System.out.println("Input operation: " + op);
				//System.out.println("To file : " + getTempFile(tempIn, fileNumber));
				if (op.equals("expr")) {
					try {
						fileInterface.applyFunction(input, expression);
					} catch (Exception e) {
						System.out.println(e);
					}
				} else if(!op.equals("repeat")) {
					doOperation(op, fileInterface, input);
				}
			}
		}
	}
	
	private void populateInterfaces() {
		interfaces.put("csv", new CommandFileInterface() {
			@Override
			public FileInterface create(String sourceIn, String soureOut) {
				return new CSVWorker(sourceIn, soureOut);
			}

			@Override
			public FileInterface create() {
				return new CSVWorker();
			}
		});
		interfaces.put("num", new CommandFileInterface() {
			@Override
			public FileInterface create(String sourceIn, String soureOut) {
				return new NumberWorker(sourceIn, soureOut);
			}

			@Override
			public FileInterface create() {
				return new NumberWorker();
			}
		});
		interfaces.put("graph", new CommandFileInterface() {
			@Override
			public FileInterface create(String sourceIn, String soureOut) {
				return new GraphWorker(sourceIn, soureOut);
			}

			@Override
			public FileInterface create() {
				return new GraphWorker();
			}
		});
	}

	private void populateOperations() {
		operations.put("permute", new CommandOperation() {
			@Override
			public void operation(FileInterface fileInterface, boolean input) {
				try {
					fileInterface.permute(input);
				} catch (Exception e){
					System.out.println(e);
				}
			}
		});
		operations.put("random", new CommandOperation() {
			@Override
			public void operation(FileInterface fileInterface, boolean input) {
				try {
					fileInterface.random(input);
				} catch (Exception e){
					System.out.println(e);
				}
			}
		});
		operations.put("mathTransform", new CommandOperation() {
			@Override
			public void operation(FileInterface fileInterface, boolean input) {
				try {
					fileInterface.randomMultiplication(input);
				} catch (Exception e){
					System.out.println(e);
				}
			}
		});
		operations.put("dupMin", new CommandOperation() {
			@Override
			public void operation(FileInterface fileInterface, boolean input) {
				try{
					fileInterface.dupMin(input);					
				} catch (Exception e){
					System.out.println(e);			
				}
			}
		});
		operations.put("dupMax", new CommandOperation() {
			@Override
			public void operation(FileInterface fileInterface, boolean input) {
				try{
					fileInterface.dupMax(input);
				} catch (Exception e){
					System.out.println(e);
				}
			}
		});
		operations.put("reverse", new CommandOperation() {
			@Override
			public void operation(FileInterface fileInterface, boolean input) {
				try{
					fileInterface.reverse(input);
				} catch (Exception e){
					System.out.println(e);
				}
			}
		});
		operations.put("duplicate", new CommandOperation() {
			@Override
			public void operation(FileInterface fileInterface, boolean input) {
				try{
					fileInterface.duplicate(input);
				} catch (Exception e){
					System.out.println(e);
				}
			}
		});
		operations.put("expr", new CommandOperation() {
			public void operation(FileInterface fileInterface, boolean input) throws Exception {
				throw new Exception("Operation not Supported with expr");
			};
//			public String applyFunction(FileInterface fileInterface, String valueIn, String expression, boolean input) {
//				try{
//					return fileInterface.applyFunction(input, valueIn, expression);
//				} catch (Exception e){
//					System.out.println(e);
//					return null;
//				}
//			}
			public void applyFunction(FileInterface fileInterface, String expression, boolean input) {
				try{
					fileInterface.applyFunction(input, expression);
				} catch (Exception e){
					System.out.println(e);
				}
			}
		});
	}
	
	private void generateTests(String testDescrptorFileName) {
		// Change line below to use a different test descriptor"src/xml/sortTestDescriptor.xml"
		Document xml = XMLinterface.getXMLinterface().readXML(testDescrptorFileName);
		//xmlInterface.outputXML(xml);
		
		XMLinterface.getXMLinterface().readTestDescriptor(xml);
		
		String exe = XMLinterface.getXMLinterface().getDescriptorExecutable();
		SourceTest test;

		String startINFile = XMLinterface.getXMLinterface().getSourceIn();
		String startOUTFile = XMLinterface.getXMLinterface().getSourceOut();	
		
		ArrayList<Branch> branches = XMLinterface.getXMLinterface().getTransformations();
		String inputType = XMLinterface.getXMLinterface().getTypeIn();
		System.out.println("INPUT TYPE : " + inputType);
		FileInterface fileInterface = interfaces.get(inputType).create(startINFile, startOUTFile);
		for (Branch branch : branches) {
			

			if (branch.getInReps() > 1 && branch.getOutReps() > 1) {
				throw new ArithmeticException("Both input and Output have repition");
			}
			// Cannot have multiple of both input and output
			System.out.println("Repeating:  " + branch.getInReps() + " times");
			for (int i = 0; i < branch.getInReps(); i++) {
//				
				branchTransform(fileInterface, branch.getInOperations(), branch.getInputExpression(), true);
				branchTransform(fileInterface, branch.getOutOperations(), branch.getOutputExpression(), false);

				fileInterface.addCurrTestCase();
			}

			

			
		}
		fileInterface.writeTestCases("src/xml/TestCases.xml");
	}
	
	public MetaTestOrchestrator() {
		populateInterfaces();
		populateOperations();
	}
	

	public static void main(String[] args) throws Exception {
		MetaTestOrchestrator mtcg= new MetaTestOrchestrator();
		mtcg.generateTests(args[0]);
	}
}