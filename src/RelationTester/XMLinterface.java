package RelationTester;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import RelationTester.FunctionTester.Comparator;

public class XMLinterface {
	private Document XMLDoc;
	
	public Document blankXML() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			StringBuilder xmlStringBuilder = new StringBuilder();
			xmlStringBuilder.append("<?xml version=\"1.0\"?> <class> </class>");
			ByteArrayInputStream input = new ByteArrayInputStream(
			xmlStringBuilder.toString().getBytes("UTF-8"));
			Document doc = dBuilder.parse(input);
			
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void outputXML(Document doc) {
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("student");
        System.out.println("----------------------------");
        
        for (int i = 0; i < nList.getLength(); i++) {
           Node nNode = nList.item(i);
           System.out.println("\nCurrent Element :" + nNode.getNodeName());
           
           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
              Element eElement = (Element) nNode;
              System.out.println(eElement);
              System.out.println("Student roll no : " 
                 + eElement.getAttribute("rollno"));
              System.out.println("First Name : " 
                 + eElement
                 .getElementsByTagName("firstname")
                 .item(0)
                 .getTextContent());
              System.out.println("Last Name : " 
                 + eElement
                 .getElementsByTagName("lastname")
                 .item(0)
                 .getTextContent());
              System.out.println("Marks : " 
                 + eElement
                 .getElementsByTagName("marks")
                 .item(0)
                 .getTextContent());
           }
        }
	}
	
	public void outputXML() {
		Document doc;
		doc = this.XMLDoc;
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("student");
        System.out.println("----------------------------");
        
        for (int i = 0; i < nList.getLength(); i++) {
           Node nNode = nList.item(i);
           System.out.println("\nCurrent Element :" + nNode.getNodeName());
           
           if (nNode.getNodeType() == Node.ELEMENT_NODE) {
              Element eElement = (Element) nNode;
              System.out.println("Student roll no : " 
                 + eElement.getAttribute("rollno"));
              System.out.println("First Name : " 
                 + eElement
                 .getElementsByTagName("firstname")
                 .item(0)
                 .getTextContent());
              System.out.println("Last Name : " 
                 + eElement
                 .getElementsByTagName("lastname")
                 .item(0)
                 .getTextContent());
              System.out.println("Marks : " 
                 + eElement
                 .getElementsByTagName("marks")
                 .item(0)
                 .getTextContent());
           }
        }
	}
	
	public void readXML(String url) {
		try {
			Document doc;// = blankXML();
			 
	        File inputFile = new File(url);
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			
			dBuilder = dbFactory.newDocumentBuilder();
	        doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	        this.XMLDoc = doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		 
}
