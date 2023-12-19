import argparse
from collections import defaultdict
import csv
import os

class Graph:
    """
    Class to represent a graph, supporting operations to check if it's a tree, DAG, or complete graph.
    """
    def __init__(self, is_directed):
        self.vertices = 0
        self.vertex_map = {}
        self.adj_list = defaultdict(set) if is_directed else defaultdict(set)
        self.is_directed = is_directed

    def add_vertex(self, vertex):
        if vertex not in self.vertex_map:
            self.vertex_map[vertex] = self.vertices
            self.vertices += 1

    def add_edge(self, u, v):
        if u not in self.vertex_map:
            self.add_vertex(u)
        if v not in self.vertex_map:
            self.add_vertex(v)
        u_index = self.vertex_map[u]
        v_index = self.vertex_map[v]
        self.adj_list[u_index].add(v_index)
        if not self.is_directed:
            self.adj_list[v_index].add(u_index)


    def get_number_of_edges(self):
        edges = sum(len(neighbors) for neighbors in self.adj_list.values())
        return edges if self.is_directed else edges // 2

    def is_tree(self):
        visited = set()

        def dfs(node, parent):
            if node in visited:
                return False
            visited.add(node)
            for neighbor in self.adj_list[node]:
                if neighbor != parent and not dfs(neighbor, node):
                    return False
            return True

        return self.vertices == len(visited) and dfs(next(iter(self.vertex_map.values())), -1)

    def is_dag(self):
        visited = set()
        stack = set()

        def dfs(node):
            visited.add(node)
            stack.add(node)
            for neighbor in self.adj_list[node]:
                if neighbor not in visited:
                    if not dfs(neighbor):
                        return False
                elif neighbor in stack:
                    return False
            stack.remove(node)
            return True

        for node in range(self.vertices):
            if node not in visited:
                if not dfs(node):
                    return False
        return True

    def is_complete(self):
        if any(i in self.adj_list[i] for i in self.adj_list):
        # If any node has a self-loop, it's not a complete graph.
            return False

        if self.is_directed:
        # In a directed graph, check for edges in both directions.
            for i in range(self.vertices):
                for j in range(self.vertices):
                    if i != j and (j not in self.adj_list[i] or i not in self.adj_list[j]):
                        return False
        else:
            # In an undirected graph, just check for the presence of edges.
            for i in range(self.vertices):
                for j in range(i + 1, self.vertices):
                    if j not in self.adj_list[i]:
                        return False

        return True



def load_graph(dot_file):
    """
    Loads a graph from a DOT file, with support for alternative edge notations.
    """
    graph = None
    try:
        with open(dot_file, 'r') as f:
            for line in f:
                stripped_line = line.strip().strip(';')

                if "digraph" in stripped_line:
                    graph = Graph(True)
                elif "graph" in stripped_line:
                    graph = Graph(False)
                else:
                    # Adjust delimiter for different edge types
                    if "->" in stripped_line or "=>" in stripped_line:
                        delimiter = "->" if "->" in stripped_line else "=>"
                    elif "--" in stripped_line or "==" in stripped_line:
                        delimiter = "--" if "--" in stripped_line else "=="
                    else:
                        delimiter = None

                    if delimiter:
                        vertices = stripped_line.split(delimiter)
                        if len(vertices) == 2:
                            u, v = [vertex.strip() for vertex in vertices]
                            graph.add_edge(u, v)  # Add all edges directly, including self-loops
                    else:
                        # Handle standalone nodes
                        parts = stripped_line.split()
                        if parts and all(part not in ('digraph', 'graph', '{', '}') for part in parts):
                            for part in parts:
                                graph.add_vertex(part.strip())
    except IOError as e:
        print(f"Error reading file {dot_file}: {e}")
        return None

    return graph

def generate_report(graph, graph_name, dot_file):
    """
    Generates a report for the given graph and saves it into a separate CSV file in the 'output' folder.
    Each CSV file is named based on the input file name.
    """
    report_data = {
        "Graph Name": graph_name,
        "File": dot_file,
        "Number of Nodes": graph.vertices,
        "Number of Edges": graph.get_number_of_edges(),
        "Is Tree": graph.is_tree(),
        "Is DAG": graph.is_dag(),
        "Is Complete": graph.is_complete()
    }

    # Displaying on console
    print(f"{graph_name} Report for {dot_file}:")
    for key, value in report_data.items():
        print(f"{key}: {value}")
    print()

    # Create 'output' directory if it doesn't exist
    output_dir = 'output'
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    # Extracting file name without extension and appending '_stats.csv'
    base_file_name = os.path.splitext(os.path.basename(dot_file))[0]
    csv_file_name = f"{base_file_name}_stats.csv"
    csv_file_path = os.path.join(output_dir, csv_file_name)

    # Writing to CSV in the 'output' folder
    with open(csv_file_path, 'w', newline='') as file:
        writer = csv.DictWriter(file, fieldnames=report_data.keys())
        writer.writeheader()
        writer.writerow(report_data)

    print(f"Report saved to {csv_file_path}")
    print()

def main():
    parser = argparse.ArgumentParser(description="Graph Analyzer")
    parser.add_argument("dot_file", help="Path to the DOT file")
    args = parser.parse_args()

    graph = load_graph(args.dot_file)

    if graph:
        generate_report(graph, "Graph", args.dot_file)
    else:
        print("Failed to load the graph.")

if __name__ == "__main__":
    main()
