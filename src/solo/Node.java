package solo;

import java.io.ObjectInputStream.GetField;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;

public class Node implements Serializable {
	private static final long serialVersionUID = 2L;
	private String name;
    private List<Node> shortestPath = new LinkedList<>();
     
    private Integer distance = Integer.MAX_VALUE;
     
    Map<Node, Integer> adjacentNodes = new HashMap<>();
    Map<String, Integer> adj = new HashMap<String, Integer>(); 
 
    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
        adj.put(destination.getName(), distance);
    }
    
    public void repopulateAdjacentNodes(Graph graph) {
    	for (String name : adj.keySet()) {
    		System.out.println(adj.get(name));
    		System.out.println(graph.getNode(name).name);
			adjacentNodes.put(graph.getNode(name), adj.get(name));
		}
    }
    
    
    public Node(String named) {
        this.name = named;
    }
    
    public Node() {}
    
    public void setDistance(int distance) {
    	this.distance = distance;
    }
    
    @XmlElement
    public String getName() {
    	return name;
    }
    
    public void setName(String Name) {
    	this.name = Name;
    }
    
    @XmlElement
    public int getDistance() {
    	return distance;
    }
    
    @XmlElement
    public Map<String, Integer> getAdjacentNodesandDistances(){
    	return adj;
    }
    
    public void setAdjacentNodesandDistances(Map<String, Integer> adjacent) {
    	this.adj = adjacent;
    }
    
    public Map<Node, Integer> getAdjacentNodes(){
    	return adjacentNodes;
    }

	public List<Node> getShortestpath() {
		return shortestPath;
	}

	public void setShortestPath(List<Node> shortestPath2) {
		this.shortestPath = shortestPath2;
	}
	
	public void printNeighbours() {
		System.out.println("Node " + name );
		for (Entry <Node, Integer> adjacencyPair : adjacentNodes.entrySet()) {
			System.out.println("Neighbour: " + adjacencyPair.getKey().name + " distance: " + adjacencyPair.getValue() );
		}
	}
	
	public void printShortest() {
		System.out.println("Node " + name );
		for (Node node : shortestPath) {
			System.out.println("Neighbour: " + node.name);
		}
		System.out.println("total  distanc: " + distance);
	}
     
    // getters and setters
}
