package solo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@XmlRootElement
public class Graph implements Serializable {
	private static final long serialVersionUID = 1L;
	private Set<Node> nodes = new HashSet<>();
	private Graph graph;
	private String sourceNode, destinationNode;
	
	public Graph() {
		graph = Graph.this;
	}
	
	public void removeNode(Node nodeA) {
		nodes.remove(nodeA);
	}

	public void addNode(Node nodeA) {
		nodes.add(nodeA);
	}

	public void printGraph() {
		for (Node node : nodes) {
			node.printNeighbours();
		}
	}

	public void printShortestGraph() {
		for (Node node : nodes) {
			node.printShortest();
		}
	}
	
	public void setDestinationNode(String destinationNodein) {
		destinationNode = destinationNodein;
	}
	
	public void setSourceNode(String sourceNodein) {
		sourceNode = sourceNodein;
	}
	
	@XmlElement
	public String getDestinationNode() {
		return destinationNode;
	}
	
	@XmlElement
	public String getSourceNode() {
		return sourceNode;
	}
	

	@XmlElement
	public Set<Node> getNodes() {
		return nodes;
	}
	
	public void repopulateNodes() {
		for (Node node : nodes) {
			node.repopulateAdjacentNodes(graph);
		}
	}
	
	public Node getNode(String Name) {
		for (Node node : nodes) {
			System.out.println("Node name= " + node.getName());
			if (node.getName().equals(Name)) {
				return node;
			}
		}
		return null;
	}
}
