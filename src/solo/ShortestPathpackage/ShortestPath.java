package solo.ShortestPathpackage;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.print.attribute.standard.SheetCollate;

import solo.Graph;
import solo.Node;
import solo.ObjectToFile;

import java.util.Set;

public class ShortestPath {
	
	public ShortestPath() {}
	/*
	 * Change this!
	 */
	private Node getLowestDistanceNode(Set < Node > unsettledNodes) {
	    Node lowestDistanceNode = null;
	    int lowestDistance = Integer.MAX_VALUE;
	    for (Node node: unsettledNodes) {
	        int nodeDistance = node.getDistance();
	        if (nodeDistance < lowestDistance) {
	            lowestDistance = nodeDistance;
	            lowestDistanceNode = node;
	        }
	    }
	    return lowestDistanceNode;
	}
	/*
	 * Change this!
	 */
	private void CalculateMinimumDistance(Node evaluationNode, Integer edgeWeigh, Node sourceNode) {
	    Integer sourceDistance = sourceNode.getDistance();
	    if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
	        evaluationNode.setDistance(sourceDistance + edgeWeigh);
	        LinkedList<Node> shortestPath = new LinkedList<Node>(sourceNode.getShortestpath());
	        shortestPath.add(sourceNode);
	        evaluationNode.setShortestPath(shortestPath);
	    }
	}

	public int calculateShortestPathFromSourceToDestination(Graph graph) {
		String sourceName = graph.getSourceNode();
		String destinationName = graph.getDestinationNode();
		Node source, destination;
		source = graph.getNode(sourceName);
		destination = graph.getNode(destinationName);
		
		System.out.println("Source " +sourceName);
		System.out.println("destination " +destinationName);
		for (Node node : graph.getNodes()) {
			node.setDistance(Integer.MAX_VALUE);
		}
		source.setDistance(0);
		Set<Node> settledNodes = new HashSet<>();
		Set<Node> unsettledNodes = new HashSet<>();
		
		unsettledNodes.add(source);
		/*
		 * Change below here!
		 */
		while (unsettledNodes.size() != 0) {
			Node currentNode = getLowestDistanceNode(unsettledNodes);
			System.out.println("Current Node= "+ currentNode.getName()+ " distance " + currentNode.getDistance());
			unsettledNodes.remove(currentNode);
			for (Entry < Node, Integer > adjacencyPair: currentNode.getAdjacentNodes().entrySet()) {
				Node adjacentNode = adjacencyPair.getKey();
				Integer edgeWeight = adjacencyPair.getValue();
				if (!settledNodes.contains(adjacentNode)) {
					CalculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
					unsettledNodes.add(adjacentNode);
				}
			}
			System.out.println("adding node: " + currentNode.getName());
			settledNodes.add(currentNode);
			if (currentNode.equals(destination)) {
				return currentNode.getDistance();
			}
		}
		System.out.println("No path available");
		return -1;
	}

	/*
	 * Don't change below please !
	 */
	public static void main(String[] args) {//"ObjectFiles/graph.ser";
		String from = args[0];
        ObjectToFile objectToFile = new ObjectToFile();
        try {
        	Graph graph =  (Graph) objectToFile.readIn(from);
        	ShortestPath2 sp = new ShortestPath2();
        	int distance = sp.calculateShortestPathFromSourceToDestination(graph);
        	System.out.println(Integer.toString(distance));
        } catch(ClassNotFoundException e){
        	e.printStackTrace();
        	System.out.println("File object not graph!!");
        } catch (IOException e) {
			e.printStackTrace();
		}
	}

}
