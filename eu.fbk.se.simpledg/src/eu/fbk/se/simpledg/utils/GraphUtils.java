package eu.fbk.se.simpledg.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;

import eu.fbk.se.simpledg.simpleDG.DirectedGraph;
import eu.fbk.se.simpledg.simpleDG.EdgeDefinition;
import eu.fbk.se.simpledg.simpleDG.NodeDefinition;
import eu.fbk.se.simpledg.simpleDG.SimpleDGFactory;
import eu.fbk.se.simpledg.simpleDG.impl.SimpleDGFactoryImpl;

/**
 * This class implements some utils to navigate a SimpleDG 
 * 
 * @author FBK
 *
 */
public class GraphUtils {

	/**
	 * Given a node of a directed graph, the method returns the list of nodes reachable with one transition
	 * @param dg a DirectedGraph
	 * @param node a NodeDefinition
	 * @return a list of NodeDefinition
	 */
	public static List<NodeDefinition> getNextNodes(DirectedGraph dg, NodeDefinition node ){
		
		// Output node list
		List<NodeDefinition> outList = new ArrayList<>();			
		
		// Factory to create output nodes
		SimpleDGFactory factory = new SimpleDGFactoryImpl();
		
		// Iterate over the list of edges of dg
		EList<EdgeDefinition> edgesDefinition = dg.getEdgesDefinition();
		for(EdgeDefinition edge : edgesDefinition) {
			// check if the source of the edge corresponds to the input node
			if (edge.getEdge().getSource().equals(node.getNodeName())) {
				// create a new node definition corresponding to the edge target
				NodeDefinition newNode = factory.createNodeDefinition();
				newNode.setNodeName(edge.getEdge().getTarget());
				outList.add(newNode);
			}
		}
		
		return outList;		
	}
	
	/**
	 * Given two NodeDefinition, nodaA and nodeB, and a DirectGraph dg, 
	 * the method returns true if and only if there is and edge in dg connecting nodaA and nodeB
	 * @param dg
	 * @param nodeA
	 * @param nodeB
	 * @return
	 */
	public static boolean areNodesDirectlyConnected(DirectedGraph dg, NodeDefinition nodeA, NodeDefinition nodeB  ){
		return false;		
	}
	
}
