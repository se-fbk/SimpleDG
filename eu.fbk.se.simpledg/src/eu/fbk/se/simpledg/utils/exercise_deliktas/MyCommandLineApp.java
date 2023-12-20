import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;


public class MyCommandLineApp {
	
	
	public static void main(String[] args) {
        // Folder name
        String dotFilesDirectory = "resources";
        
        String folderName = "output";

        // Find the working directory of the running Java application
        String projectDirectory = System.getProperty("user.dir");

        // Create the full path using the folder name
        Path directoryPath = Paths.get(projectDirectory, dotFilesDirectory);
        Path folderPath = Paths.get(projectDirectory, folderName);

        // CSV file name
        String csvFileName = "A_stats.csv";
        Path csvFilePath = folderPath.resolve(csvFileName);
        

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath.toFile()))) {
            // Write header to the CSV file
            writeLine(writer, "File Name, Node Count, Edge Count, Is Tree, Is DAG, Is Complete");

            // Process all files in the folder and its subdirectories
            Files.walk(directoryPath, FileVisitOption.FOLLOW_LINKS)
                    .filter(filePath -> Files.isRegularFile(filePath) && filePath.toString().endsWith(".dot"))
                    .forEach(filePath -> {
                        try {
                            // Process each DOT file
                            processDotFile(filePath, writer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private static void processDotFile(Path dotFilePath, BufferedWriter writer) throws IOException {
        // Process the DOT file
        System.out.println("Processing file: " + dotFilePath.toString());
        Set<String> vertices = new HashSet<>();
        Set<String> edges = new HashSet<>();
        
        boolean isDirected = false;
        boolean isGraph = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(dotFilePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Process each line of the DOT file
                processDotLine(line, vertices, edges);
                
                if (line.contains("digraph")) {
                    isDirected = true;
                } else if (line.contains("graph")) {
                    isGraph = true;
                }
            }
        }
        
        // Calculate vertex and edge counts
        int[] counts = new int[2];
        calculateVertexAndEdgeCounts(edges, counts);

        // Write statistics to the CSV file
        int vertexCount = counts[0];
        int edgeCount = counts[1];
        
        System.out.println("Vertex Count: " + vertexCount);
        System.out.println("Edge Count: " + edgeCount);
        
        writeLine(writer, dotFilePath.getFileName().toString() + "," + vertexCount + "," + edgeCount + ","
                + isTree(vertexCount, edgeCount, edges, isGraph ) + "," + isDAG(vertexCount, edgeCount, edges, isDirected) + ","
                + isComplete(vertexCount, edgeCount, edges, isGraph));
    }
    
	private static void calculateVertexAndEdgeCounts(Set<String> edges, int[] counts) {
        Set<String> uniqueVertices = new HashSet<>();
        int totalEdges = edges.size();

        for (String edge : edges) {
            String[] parts = edge.split("\\s*=>\\s*|\\s*==\\s*");
            if (parts.length == 2 ) {
                uniqueVertices.add(parts[0]);
                uniqueVertices.add(parts[1]);
                
            }
        }

        counts[0] = uniqueVertices.size(); // Vertex count
        counts[1] = totalEdges;    // Edge count
        
    }

	private static void processDotLine(String line, Set<String> vertices, Set<String> edges) {
        // Process each line of the DOT file
        System.out.println("Processing line: " + line);
        String[] parts = line.split("\\s*=>\\s*|\\s*==\\s*");

        if (parts.length == 2) {
            String vertex1 = parts[0].trim();
            String vertex2 = parts[1].trim();

            // Add vertices to the set
            vertices.add(vertex1);
            vertices.add(vertex2);

            // Add edges to the set
            edges.add(vertex1 + " => " + vertex2);
           
        }
    }

	private static void writeLine(BufferedWriter writer, String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }

    private static boolean isTree(int vertexCount, int edgeCount, Set<String> edges, boolean isGraph) {
        return isGraph && !hasSelfLoop(edges) && !hasCycle(edges) && edgeCount == vertexCount - 1;
    }

    private static boolean isDAG(int vertexCount, int edgeCount, Set<String> edges, boolean isDirected) {
        return isDirected && !hasSelfLoop(edges) && !hasCycle(edges);
    }
    
    private static boolean hasCycle(Set<String> edges) {
        Set<String> visited = new HashSet<>();
        Set<String> parent = new HashSet<>();

        for (String edge : edges) {
            String[] parts = edge.split("\\s*=>\\s*");
            for (String part : parts) {
                if (!visited.contains(part)) {
                    if (hasCycleDFS(part, visited, parent, edges)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean hasCycleDFS(String vertex, Set<String> visited, Set<String> parent, Set<String> edges) {
        visited.add(vertex);
        parent.add(vertex);

        Set<String> neighbors = getNeighbors(vertex, edges);

        for (String neighbor : neighbors) {
            if (!visited.contains(neighbor)) {
                if (hasCycleDFS(neighbor, visited, parent, edges)) {
                    return true;
                }
            } else if (parent.contains(neighbor)) {
                return true; 
            }
        }

        parent.remove(vertex);
        return false;
    }

    private static Set<String> getNeighbors(String vertex, Set<String> edges) {
        Set<String> neighbors = new HashSet<>();
        for (String edge : edges) {
            String[] parts = edge.split("\\s*=>\\s*");
            if (parts.length == 2 && parts[0].equals(vertex)) {
                neighbors.add(parts[1]);
            }
        }
        return neighbors;
    }
    
    

    private static boolean isComplete(int vertexCount, int edgeCount, Set<String> edges, boolean isGraph) {
        
        int expectedEdgeCount = vertexCount * (vertexCount - 1) / 2 ;
        return isGraph && !hasSelfLoop(edges) && edgeCount == expectedEdgeCount;
    }
    
    private static boolean hasSelfLoop(Set<String> edges) {
        for (String edge : edges) {
            String[] parts = edge.split("\\s*=>\\s*");
            if (parts.length == 2 && parts[0].equals(parts[1])) {
                return true;  
            }
        }
        return false;  
    }

    
}
	    
	   
	        
	        
	   



