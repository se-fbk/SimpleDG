package eu.fbk.se.simpledg.utils;

import java.util.ArrayList;
import java.util.List;

import eu.fbk.se.simpledg.simpleDG.DotGraphs;
import eu.fbk.se.simpledg.simpleDG.NodeDefinition;

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
	public static List<NodeDefinition> getNextNodes(DotGraphs dg, NodeDefinition node ){
		
		// Output node list
		List<NodeDefinition> outList = new ArrayList<>();			
		
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
	public static boolean areNodesDirectlyConnected(DotGraphs dg, NodeDefinition nodeA, NodeDefinition nodeB  ){
		return false;		
	}
	
}
