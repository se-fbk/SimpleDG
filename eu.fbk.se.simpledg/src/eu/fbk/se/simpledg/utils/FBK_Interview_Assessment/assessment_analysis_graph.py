#Import all the packages here
import os
import networkx as nx
import re

class GraphLoader: # Class will return the loaded DOT files
    def __init__(self, file_path):
        self.file_path = file_path

    def load_graph(self):
        try:
            with open(self.file_path, 'r') as file:
                lines = file.readlines()
                graph_type = nx.DiGraph if 'digraph' in lines[0] else nx.Graph()
                graph = graph_type()

                for line in lines:
                    if '=>' not in line and '==' not in line:
                        continue

                    if '=>' in line:
                        match = re.match(r'\s*(\S+)\s*=>\s*(\S+)', line)
                    elif '==' in line:
                        match = re.match(r'\s*(\S+)\s*==\s*(\S+)', line)

                    if match:
                        source_node, target_node = match.groups()
                        graph.add_edge(source_node, target_node)

                return graph
        except FileNotFoundError as e:
            print(f"Error: {e}")
            return None
        except Exception as e:
            print(f"Error loading the graph: {e}")
            return None

class GraphAnalyzer: #Class will analyze the graphs and will return the results 
    @staticmethod
    def is_complete(graph):
        if graph is None:
            return False

        complement_graph = nx.complement(graph)
        return complement_graph.number_of_edges() == 0

    @staticmethod
    def analyze_graph(graph):
        if graph is None:
            return {}

        num_nodes = graph.number_of_nodes()
        num_edges = graph.number_of_edges()
        is_tree = nx.is_tree(graph)
        is_dag = nx.is_directed_acyclic_graph(graph)
        is_complete_graph = GraphAnalyzer.is_complete(graph)

        return {
            "Number of Nodes": num_nodes,
            "Number of Edges": num_edges,
            "Is Tree": is_tree,
            "Is Directed Acyclic Graph (DAG)": is_dag,
            "Is Complete Graph": is_complete_graph,
        }

class GraphReporter: #Class will generate the reports for all the provided graphs
    @staticmethod
    def print_report(file_name, report):
        print(f"\nGraph Statistics for {file_name}:")
        for stat, value in report.items():
            print(f"{stat}: {value}")

class GraphProcessor: # Class will process all the data
    @staticmethod
    def main(example):
        folder_path = "ExampleDOTFiles"        
        file_path = os.path.join(os.path.expanduser("~"), "Documents", "runtime-EclipseApplication", folder_path, example)

        graph_loader = GraphLoader(file_path)
        graph = graph_loader.load_graph()

        graph_analyzer = GraphAnalyzer()
        report = graph_analyzer.analyze_graph(graph)

        graph_reporter = GraphReporter()
        graph_reporter.print_report(example, report)

if __name__ == "__main__":
    examples = ["example4.dot", "example5.dot", "example6.dot", "example7.dot"]

    for example in examples:
        GraphProcessor.main(example)
