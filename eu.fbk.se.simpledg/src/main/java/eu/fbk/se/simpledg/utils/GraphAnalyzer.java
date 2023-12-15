package eu.fbk.se.simpledg.utils;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceDOT;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GraphAnalyzer {

    private static boolean isDirected;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java GraphStatisticsAnalyzer <path-to-dot-file>");
            System.exit(1);
        }

        String dotFileName = args[0];

        try {
            Graph graph = loadGraph(dotFileName);

            // Print statistics report
            printStatisticsReport(graph);

            // Write results to CSV
            writeResultsToCSV(dotFileName, graph);

        } catch (IOException e) {
            System.err.println("Error loading the graph from the DOT file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Graph loadGraph(String dotFileName) throws IOException {
        Graph graph = new SingleGraph("LoadedGraph");


        File file = new File("src/main/resources/" + dotFileName);
        String absolutePath = file.getAbsolutePath();

        FileSource fileSource = new FileSourceDOT();

        try {
            fileSource.addSink(graph);

            // Read the file content
            String fileContent = readFileContent(absolutePath);

            // Perform transformations on the file content
            String transformedContent = transformFileContent(fileContent);

            // Load the modified content into the graph
            fileSource.readAll(new StringReader(transformedContent));
            // fileSource.readAll(absolutePath);
        } catch (IOException e) {
            System.err.println("Error reading DOT file: " + e.getMessage());
            throw e; // Rethrow the exception to propagate it up
        } finally {
            fileSource.removeSink(graph);
        }

        return graph;
    }

    private static String readFileContent(String filePath) throws IOException {
        // Read the content of the file
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }

    private static String transformFileContent(String originalContent) {
        // Replace "=>" with "->" and add a semicolon at the end of each statement
        originalContent = originalContent.replaceAll("=>", "->");

        // Replace "==" with "--" and add a semicolon at the end of each statement
        originalContent = originalContent.replaceAll("==", "--");

        // Add a semicolon at the end of each statement
        originalContent = originalContent.replaceAll("(\\s*\\w+\\s*->\\s*\\w+\\s*)(?!;)", "$1;");


        // Add a semicolon at the end of each statement
        originalContent = originalContent.replaceAll("(\\s*\\w+\\s*--\\s*\\w+\\s*)(?!;)", "$1;");

        isDirected = originalContent.contains("->");

        return originalContent;
    }


    private static void printStatisticsReport(Graph graph) {
        // Calculate and print statistics
        System.out.println("Number of nodes: " + graph.getNodeCount());
        System.out.println("Number of edges: " + graph.getEdgeCount());

        try {
            System.out.println("Is a tree: " + isTree(graph));
        } catch (RuntimeException e) {
            System.err.println("Error checking if the graph is a tree: " + e.getMessage());
        }

        try {
            if (isDirected) {
                System.out.println("Is Directed Graph: Yes");
                System.out.println("Is a DAG: " + isDAG(graph));
            } else {
                System.out.println("Is Directed Graph: No");
                System.out.println("Graph is not directed. Skipping DAG check.");
            }
        } catch (RuntimeException e) {
            System.err.println("Error checking if the graph is a DAG: " + e.getMessage());
        }

        try {
            System.out.println("Is complete: " + isComplete(graph));
        } catch (RuntimeException e) {
            System.err.println("Error checking if the graph is complete: " + e.getMessage());
        }
    }

    private static void writeResultsToCSV(String dotFileName, Graph graph) {
        try {
            // Create the output folder if it doesn't exist
            File outputFolder = new File("output");
            if (!outputFolder.exists()) {
                outputFolder.mkdir();
            }

            // Get the file name without extension
            String fileNameWithoutExtension = getFileNameWithoutExtension(dotFileName);

            // Delete existing CSV file if it exists
            Path existingCsvPath = Paths.get("output/" + fileNameWithoutExtension + "_stats.csv");
            Files.deleteIfExists(existingCsvPath);

            // Write results to CSV
            try (FileWriter writer = new FileWriter(existingCsvPath.toString())) {
                writer.write("Number of nodes: " + graph.getNodeCount() + "\n");
                writer.write("Number of edges: " + graph.getEdgeCount() + "\n");
                writer.write("Is a tree: " + isTree(graph) + "\n");
                writer.write("Is a DAG: " + isDAG(graph) + "\n");
                writer.write("Is complete: " + isComplete(graph) + "\n");

                System.out.println("Results written to CSV file.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getFileNameWithoutExtension(String filePath) {
        Path path = Paths.get(filePath);
        String fileNameWithExtension = path.getFileName().toString();
        int dotIndex = fileNameWithExtension.lastIndexOf('.');
        return (dotIndex == -1) ? fileNameWithExtension : fileNameWithExtension.substring(0, dotIndex);
    }


    private static boolean isTree(Graph graph) {
        int numNodes = graph.getNodeCount();
        int numEdges = graph.getEdgeCount();

        // Property 1: Number of edges should be exactly one less than the number of nodes for a connected graph
        if (numEdges != numNodes - 1) {
            return false;
        }

        // Property 2: Check if the graph is acyclic using Depth-First Search (DFS)
        Set<Node> visitedNodes = new HashSet<>();
        Node startNode = graph.getNode(0); // Assuming the graph is connected
        return isAcyclicDFS(startNode, null, visitedNodes);
    }

    private static boolean isAcyclicDFS(Node currentNode, Node parent, Set<Node> visitedNodes) {
        if (visitedNodes.contains(currentNode)) {
            return false; // Cycle detected
        }

        visitedNodes.add(currentNode);

        Iterator<? extends Node> neighborIterator = currentNode.getNeighborNodeIterator();
        while (neighborIterator.hasNext()) {
            Node neighbor = neighborIterator.next();
            if (neighbor != parent && !isAcyclicDFS(neighbor, currentNode, visitedNodes)) {
                // Early return if a cycle is detected
                return false;
            }
        }

        return true;
    }

    private static boolean isDAG(Graph graph) {
        Set<Node> visitedNodes = new HashSet<>();
        Set<Node> currentlyVisitedNodes = new HashSet<>();

        for (Node node : graph.getNodeSet()) {
            if (!visitedNodes.contains(node)) {
                if (!isDAGDFS(node, visitedNodes, currentlyVisitedNodes)) {
                    return false; // Graph is not a DAG
                }
            }
        }

        return true; // Graph is a DAG
    }


    private static boolean isDAGDFS(Node currentNode, Set<Node> visitedNodes, Set<Node> currentlyVisitedNodes) {
        visitedNodes.add(currentNode);
        currentlyVisitedNodes.add(currentNode);

        for (Edge edge : currentNode.getLeavingEdgeSet()) {
            Node neighbor = edge.getOpposite(currentNode);

            if (!visitedNodes.contains(neighbor)) {
                if (!isDAGDFS(neighbor, visitedNodes, currentlyVisitedNodes)) {
                    return false; // Graph is not a DAG
                }
            } else if (currentlyVisitedNodes.contains(neighbor)) {
                // Detected a back edge, indicating a cycle
                return false; // Graph is not a DAG
            }
        }

        currentlyVisitedNodes.remove(currentNode);
        return true; // No cycles detected in this DFS traversal
    }

    private static boolean isComplete(Graph graph) {
        for (Node node1 : graph.getNodeSet()) {
            for (Node node2 : graph.getNodeSet()) {
                if (node1 != node2 && !hasEdgeBetween(node1, node2)) {
                    // If there is no edge from node1 to node2, the graph is not complete
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean hasEdgeBetween(Node source, Node target) {
        for (Edge edge : source.getEdgeSet()) {
            if (edge.getOpposite(source) == target) {
                return true; // There is an edge from source to target
            }
        }
        return false; // No edge found from source to target
    }


}
