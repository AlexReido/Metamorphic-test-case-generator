package solo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import solo.ShortestPathpackage.ShortestPath;
import xml.XMLinterface; 

public class ObjectToString {
	
	public ObjectToString() {}
	
	public Object fromString( String s ) throws IOException ,ClassNotFoundException {
		byte [] data = Base64.getDecoder().decode(s);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		Object o  = ois.readObject();
		ois.close();
		return o;
	}

	/** Write the object to a Base64 string. */
	public String objToString( Serializable o ) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream( baos );
		oos.writeObject( o );
		oos.close();
		return Base64.getEncoder().encodeToString(baos.toByteArray()); 
	}
	
	private Graph readIn(String filename) throws IOException, ClassNotFoundException, JAXBException {
		File file = new File(filename);
		JAXBContext contextObj = JAXBContext.newInstance(Graph.class);
		Unmarshaller jaxbUnmarshaller = contextObj.createUnmarshaller();  
        Graph graph= (Graph) jaxbUnmarshaller.unmarshal(file);  
        return graph;
    }
      
    private void writeOut(Graph graph, String filename) throws JAXBException, FileNotFoundException {
    	XMLinterface xmlInterface = XMLinterface.getXMLinterface();
    	JAXBContext contextObj = JAXBContext.newInstance(Graph.class);  
  	  	
	    Marshaller marshallerObj = contextObj.createMarshaller();  
	    marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
	    marshallerObj.marshal(graph, new FileOutputStream(new File(filename)));
	    Document doc  = XMLinterface.getXMLinterface().blankXML();
	    Element inElement, outElement, testElement, root;
	    root = doc.createElement("Testcases");
	    doc.appendChild(root);
	    testElement = doc.createElement("TestCase");
		root.appendChild(testElement);
	
		inElement = doc.createElement("Input");
//		assert(testCase.getInput() != null);
		DOMResult res = new DOMResult();
		marshallerObj.marshal(graph, res) ;
		System.out.println("=====================" + res.getNode().getChildNodes().toString());
//		inElement.setTextContent(testCase.getInput().toString());
        testElement.appendChild(res.getNode());
        outElement =doc.createElement("Output");
//        assert(testCase.getOutput() != null);
        marshallerObj.marshal(graph, outElement) ;
//		outElement.setTextContent(testCase.getOutput().toString());
        testElement.appendChild(outElement);
	    xmlInterface.saveMetaTests(doc, root, "src/xml/GraphTestcase.xml");
    }
	
	
	// https://www.javatpoint.com/jaxb-marshalling-example
	public static void main(String[] args) throws Exception{  
//	    JAXBContext contextObj = JAXBContext.newInstance(Graph.class);  
//	  
//	    Marshaller marshallerObj = contextObj.createMarshaller();  
//	    marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
	    
		Graph graph = new Graph();
		Node nodeA = new Node("A");
		Node nodeB = new Node("B");
		Node nodeC = new Node("C");
		Node nodeD = new Node("D"); 
		Node nodeE = new Node("E");
		Node nodeF = new Node("F");
		 
		nodeA.addDestination(nodeB, 10);
		nodeA.addDestination(nodeC, 15);
		 
		nodeB.addDestination(nodeD, 12);
		nodeB.addDestination(nodeF, 15);
		 
		nodeC.addDestination(nodeE, 10);
		 
		nodeD.addDestination(nodeE, 2);
		nodeD.addDestination(nodeF, 1);
		 
		nodeF.addDestination(nodeE, 5);
		 
		 
		graph.addNode(nodeA);
		graph.addNode(nodeB);
		graph.addNode(nodeC);
		graph.addNode(nodeD);
		graph.addNode(nodeE);
		graph.addNode(nodeF);
		//graph.printGraph();
		ShortestPath sp = new ShortestPath();
		//graph = sp.calculateShortestPathFromSource(graph, nodeA);
		
	    graph.printGraph();
	    ObjectToString gtXml = new ObjectToString();
	    String filename = "src/xml/Graph.xml";
	    gtXml.writeOut(graph, filename);
	    //marshallerObj.marshal(graph, new FileOutputStream(new File("src/xml/Graph.xml")));
	    System.out.println("Done");
	    Graph readinGrapg  = gtXml.readIn(filename);
	    readinGrapg.repopulateNodes();
	    //readinGrapg.printGraph();
	    
	}
       
}  