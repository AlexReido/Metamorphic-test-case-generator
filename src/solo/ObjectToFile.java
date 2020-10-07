package solo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public class ObjectToFile {
	public ObjectToFile() {}
	
	public Object readIn(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(filename)));
        return ois.readObject();
    }
      
    public void writeOut(java.io.Serializable obj, String filename) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));
        oos.writeObject(obj);
        oos.close();
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
		
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		ObjectToFile objectToFile = new ObjectToFile();
		for (int i = 0; i < 10; i++) {
			System.out.println("Hello");
			Graph graph = objectToFile.generateRandomGraph();
			System.out.println(graph.getNodes().size());
			graph.printGraph();
		}
//		Graph graph = new Graph();
//		Node nodeA = new Node("A");
//		Node nodeB = new Node("B");
//		Node nodeC = new Node("C");
//		Node nodeD = new Node("D"); 
//		Node nodeE = new Node("E");
//		Node nodeF = new Node("F");
//		nodeA.addDestination(nodeB, 10);
//		nodeB.addDestination(nodeA, 10);
//		
//		nodeA.addDestination(nodeC, 15);
//		nodeC.addDestination(nodeA, 15);
//		
//		nodeB.addDestination(nodeD, 12);
//		nodeD.addDestination(nodeB, 12);
//		nodeB.addDestination(nodeF, 15);
//		nodeF.addDestination(nodeB, 15);
//		
//		nodeC.addDestination(nodeE, 10);
//		nodeE.addDestination(nodeC, 10);
//		
//		nodeD.addDestination(nodeE, 2);
//		nodeE.addDestination(nodeD, 2);
//		
//		nodeD.addDestination(nodeF, 1);
//		nodeF.addDestination(nodeD, 1);
//		
//		nodeF.addDestination(nodeE, 5);
//		nodeE.addDestination(nodeF, 5);
//		
//		graph.setSourceNode("A");
//		graph.setDestinationNode("D");
//		 
//		graph.addNode(nodeA);
//		graph.addNode(nodeB);
//		graph.addNode(nodeC);
//		graph.addNode(nodeD);
//		graph.addNode(nodeE);
//		graph.addNode(nodeF);
//		//graph.printGraph();
//		ShortestPath sp = new ShortestPath();
//		//graph = sp.calculateShortestPathFromSource(graph, nodeA);
//		//graph.printGraph();
//		System.out.println("#######################################################################");
//		ObjectToFile objectToFile = new ObjectToFile();
//		objectToFile.writeOut(graph, "ObjectFiles/graph.ser");
//		Graph newGraph = (Graph) objectToFile.readIn("ObjectFiles/graph.ser");
//		//newGraph.printGraph();
//		newGraph.printShortestGraph();
//		System.out.println(newGraph.getDestinationNode());
//		System.out.println(newGraph.getSourceNode());
	}

}
