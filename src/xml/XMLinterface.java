package xml;

import org.w3c.dom.*;

import command.MetaTestOrchestrator;
import command.Branch;
import workers.FileInterface;

import javax.lang.model.type.ArrayType;
import javax.sound.midi.MetaEventListener;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
/*
 * Xml interface implements the singleton pattern, this is because the same instance needs 
 * to be called from different parts of the program.  
 */
public class XMLinterface {
    private Document XMLDoc;
    private String descriptorExecutable, testsExecutable;
    private ArrayList<Branch> transformations;
    private String sourceIn, sourceOut, typeIn, typeOut;
    static private XMLinterface instance;
    private XMLinterface() {}
    public static XMLinterface getXMLinterface() {
    	if (instance != null) {
    		return instance;
    	} else {
    		instance = new XMLinterface();
    		return instance;
    	}
    	
    }

    public Document blankXML() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            StringBuilder xmlStringBuilder = new StringBuilder();
            xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");
            ByteArrayInputStream input = new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8"));
            Document doc = dBuilder.parse(input);
            doc.removeChild(doc.getFirstChild());
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Element blankElement() {
    	Document doc = blankXML();
    	Element root = doc.createElement("Testcase");
    	doc.appendChild(root);
    	return root;
    }
    
    // TODO add source case to xml
    public void saveMetaTests(Document doc, Element root, String filename) {
       // Document doc = blankXML();

//        Element root = doc.createElement("TestCases");
//        doc.appendChild(root);

        Element program = doc.createElement("Executable");
        System.out.println("EXE  = " + getDescriptorExecutable());
        if (descriptorExecutable != null) {
            program.appendChild(doc.createTextNode(descriptorExecutable));
            root.appendChild(program);
        } else {
        	System.out.println("MISSING EXECUTABLE");
        }
        
        assert(sourceIn != null && typeIn != null);
        Element sourceInE = doc.createElement("SourceIn");
        sourceInE.appendChild(doc.createTextNode(sourceIn));
        sourceInE.setAttribute("Type", typeIn);
        root.appendChild(sourceInE);
        assert(sourceOut != null && typeOut != null);
        Element sourceOutE = doc.createElement("SourceOut");
        sourceOutE.appendChild(doc.createTextNode(sourceOut));
        sourceOutE.setAttribute("Type", typeOut);
        root.appendChild(sourceOutE);

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);    //new File(filename))
            StreamResult streamResult = new StreamResult("src/xml/TestCases.xml");

            System.out.println();
            transformer.transform(domSource, streamResult);
        } catch (TransformerException te){
            te.printStackTrace();
        }
    }

    public ArrayList<TestCase<String>> getTestCases(Document doc){
        Element element;
        NodeList nList = doc.getElementsByTagName("Executable");
        System.out.println("----------------------------");
        element = (Element) nList.item(0);
        testsExecutable = element.getTextContent();
        System.out.println("Executable: " + testsExecutable);
        
        nList = doc.getElementsByTagName("SourceIn");
        element = (Element) nList.item(0);
        typeIn = element.getAttribute("Type");
        //System.out.println("type In " + typeIn);

        nList = doc.getElementsByTagName("SourceOut");
        element = (Element) nList.item(0);
        typeOut = element.getAttribute("Type");
        
         
        
        nList = doc.getElementsByTagName("TestCase");
        TestCase<String> t;
        String inT, outT;
        ArrayList<TestCase<String>> tests = new ArrayList<>();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                //System.out.println("FIRST CHEOLE= " + eElement.getChildNodes().item(0).getNodeName());
                assert (eElement.getChildNodes().item(0).getNodeName() == "Input");
                assert (eElement.getChildNodes().item(1).getNodeName() == "Output");
                inT = eElement.getChildNodes().item(0).getTextContent();
                outT = eElement.getChildNodes().item(1).getTextContent();
//                System.out.println("\nTest: "
//                        + eElement.getAttribute("Name")
//                        + "\nInput: "
//                        + inT
//                        + "\nOutput: "
//                        + outT);
                t = new TestCase<String>(inT, outT);
                tests.add(t);
            }
        }
        return tests;
    }

    private Element getFirstChild(String tag, Node parent) {
        if (parent.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) parent;
            NodeList tempNodeList = ((Element) parent).getElementsByTagName(tag);
            Node tnode = tempNodeList.item(0);
            Element childElement = (Element) tnode;
            return childElement;
        } else {
            throw new InputMismatchException("Node not valid");
        }
    }

    private ArrayList<String> getOperations(Element element){
        try {
            NodeList operations = element.getElementsByTagName("Operation");
            ArrayList<String> ops = new ArrayList<>();
            for (int j = 0; j < operations.getLength(); j++) {
                Node operation = operations.item(j);
                if ((operation.getNodeType() == Node.ELEMENT_NODE)) {
                    ops.add(operation.getTextContent());
                }
            }
            return ops;
        } catch (NullPointerException npe){
            return null;
        }
    }

    public void readTestDescriptor(Document doc) {
        transformations = new ArrayList<Branch>();
        System.out.println("Root element xcvxc:" + doc.getDocumentElement().getNodeName());
        Element element;
        NodeList nList = doc.getElementsByTagName("Execution");
        System.out.println("----------------------------");
        element = (Element) nList.item(0);
        descriptorExecutable = element.getTextContent();
        System.out.println("Executable: " + descriptorExecutable);

        nList = doc.getElementsByTagName("Input");
        element = (Element) nList.item(0);
        typeIn = element.getAttribute("Type");
        System.out.println("SDKF " + typeIn);

        nList = doc.getElementsByTagName("Output");
        element = (Element) nList.item(0);
        typeOut = element.getAttribute("Type");

        nList = doc.getElementsByTagName("Source_Test");
        element = (Element) nList.item(0);
        sourceIn = element.getAttribute("Input");
        sourceOut = element.getAttribute("Output");
        

        nList = doc.getElementsByTagName("Branch");
        Branch t;
        Element inElement, outElement;
        ArrayList<String> inOps, outOps;
        String inT, outT;
        for (int i = 0; i < nList.getLength(); i++) {
            Node branchNode = nList.item(i);
            Element branchElemnt = (Element) branchNode;
            System.out.println("BRANCH : " + branchElemnt.getAttribute("Name"));
            inElement = getFirstChild("In", branchNode);
            outElement = getFirstChild("Out", branchNode);
            inOps = getOperations(inElement);
            outOps = getOperations(outElement);
            
            t = new Branch(branchElemnt.getAttribute("Name"), inOps, outOps, typeIn, typeOut);
            System.out.println("repittition val " + inElement.getAttribute("repition"));
            if (inOps != null) {
            	if (inOps.contains("expr")) {
            		t.setInputExpression(inElement.getAttribute("expr"));
            	}
            	if (inOps.contains("repeat")) {
            		System.out.println("PAASNDK");
            		t.setInReps(Integer.parseInt(inElement.getAttribute("repition")));
            	}
            }
            if (outOps != null) {
            	if (outOps.contains("expr")) {        
            		t.setOutputExpression(outElement.getAttribute("expr"));
            	}
            	if (outOps.contains("repeat")) {
            		t.setOutReps(Integer.parseInt(outElement.getAttribute("repition")));
            	}
            }
            System.out.println("repition in T: " + t.getInReps());
            transformations.add(t);
        }
    }

    public String getSourceIn() {
    	return sourceIn;
	}
    
    public String getSourceOut() {
    	return sourceOut;
	}
    
    public String getTypeIn() {
    	return typeIn;
	}
    
    public String getTypeOut() {
    	return typeOut;
	}
    
    public String getDescriptorExecutable() {
        return descriptorExecutable;
    }

    public String getTestsExecutable() { return testsExecutable; }

    public ArrayList<Branch> getTransformations() {
    	if (transformations == null) {
			System.out.println("Transformation null");
		}
        return transformations;
    }

    public void outputXML() {
        Document doc;
        doc = this.XMLDoc;
        printxml(doc);    
    }
    		
    public void outputXML(Document doc) {
        printxml(doc);    
    }
    
    private void printxml(Document doc) {
    	//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("TestCase");
        //System.out.println("----------------------------");
        System.out.println("ksjdfb lsdkgh");
        System.out.println(" length  " + nList.getLength());
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                NodeList testList  = eElement.getChildNodes();
                for (int j = 0; j < testList.getLength(); j++) {
                    Node tNode = testList.item(j);
                    System.out.println("     Current Element :" + tNode.getNodeName());
                    
                    
                    Element zElement = (Element) tNode;
                    NodeList zlist  = zElement.getChildNodes();
                    for (int z = 0; z < zlist.getLength(); z++) {
                        Node zNode = zlist.item(z);
                        System.out.println("	  " + zNode.getNodeName() + ": " + zNode.getTextContent());
    				}
				}
            }
        }
    }

    public Document readXML(String url) {
        try {
            Document doc;
            File inputFile = new File(url);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            this.XMLDoc = doc;
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
