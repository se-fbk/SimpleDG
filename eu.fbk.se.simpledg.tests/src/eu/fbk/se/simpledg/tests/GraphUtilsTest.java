package eu.fbk.se.simpledg.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import eu.fbk.se.simpledg.simpleDG.DirectedGraph;
import eu.fbk.se.simpledg.simpleDG.Edge;
import eu.fbk.se.simpledg.simpleDG.EdgeDefinition;
import eu.fbk.se.simpledg.simpleDG.NodeDefinition;
import eu.fbk.se.simpledg.simpleDG.SimpleDGFactory;
import eu.fbk.se.simpledg.simpleDG.impl.SimpleDGFactoryImpl;
import eu.fbk.se.simpledg.utils.GraphUtils;

public class GraphUtilsTest {

	@Test
	public void getNextNodesTest1() {
		
		SimpleDGFactory factory = new SimpleDGFactoryImpl();		
		DirectedGraph simpleDG = factory.createDirectedGraph();
		
		NodeDefinition nodeA = factory.createNodeDefinition();
		nodeA.setNodeName("A");
		simpleDG.getNodesDefinition().add(nodeA);
		NodeDefinition nodeB = factory.createNodeDefinition();
		nodeB.setNodeName("B");
		simpleDG.getNodesDefinition().add(nodeB);
		
		Edge edgeAB = factory.createEdge();
		edgeAB.setSource("A");
		edgeAB.setTarget("B");
				EdgeDefinition edgeDefAB = factory.createEdgeDefinition();
		edgeDefAB.setEdge(edgeAB);
		simpleDG.getEdgesDefinition().add(edgeDefAB);
		
		List<NodeDefinition> nextNodes = GraphUtils.getNextNodes(simpleDG, nodeA);
		
		assertEquals(nextNodes.get(0).getNodeName(), nodeB.getNodeName());
		
	}
	
}
