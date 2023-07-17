package eu.fbk.se.simpledg.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

// While the methods are both covered 100% the entire GraphUtils class is covered only at 95.9%. The reason
// for this is that both getNextNodes and areNodesDirectlyConnected are static methods. This means that the
// GraphUtils class is never created hence the not 100% coverage.

public class GraphUtilsTest {

	private EdgeDefinition constructEdge(SimpleDGFactory factory, String nameA, String nameB) {
		Edge edgeAB = factory.createEdge();
		edgeAB.setSource(nameA);
		edgeAB.setTarget(nameB);

		EdgeDefinition edgeDefAB = factory.createEdgeDefinition();
		edgeDefAB.setEdge(edgeAB);
		return edgeDefAB;
	}

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

		simpleDG.getEdgesDefinition().add(constructEdge(factory, "A", "B"));

		List<NodeDefinition> nextNodes = GraphUtils.getNextNodes(simpleDG, nodeA);

		assertEquals(nextNodes.get(0).getNodeName(), nodeB.getNodeName());

	}

	@Test
	public void areNodesDirectlyConnectedTest1() {

		SimpleDGFactory factory = new SimpleDGFactoryImpl();
		DirectedGraph simpleDG = factory.createDirectedGraph();

		NodeDefinition nodeA = factory.createNodeDefinition();
		nodeA.setNodeName("A");
		simpleDG.getNodesDefinition().add(nodeA);
		NodeDefinition nodeB = factory.createNodeDefinition();
		nodeB.setNodeName("B");
		simpleDG.getNodesDefinition().add(nodeB);
		NodeDefinition nodeC = factory.createNodeDefinition();
		nodeC.setNodeName("C");
		simpleDG.getNodesDefinition().add(nodeC);

		simpleDG.getEdgesDefinition().add(constructEdge(factory, "A", "B"));
		simpleDG.getEdgesDefinition().add(constructEdge(factory, "B", "C"));

		boolean connectedAB = GraphUtils.areNodesDirectlyConnected(simpleDG, nodeA, nodeB);
		boolean connectedAC = GraphUtils.areNodesDirectlyConnected(simpleDG, nodeA, nodeC);

		assertTrue(connectedAB);
		assertFalse(connectedAC);

	}

}
