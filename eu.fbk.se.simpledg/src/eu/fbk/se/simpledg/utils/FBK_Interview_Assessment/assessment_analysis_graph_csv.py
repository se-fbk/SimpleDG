import os
import networkx as nx
import re
import csv

class GraphLoader:
    def __init__(self, file_path):
        self.file_path = file_path
        self.graph = self.load_graph()

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
                        node1, node2 = match.groups()
                        graph.add_edge(node1, node2)

                return graph

        except FileNotFoundError:
            print(f"Error: File '{self.file_path}' not found.")
            exit(1)
        except Exception as e:
            print(f"Error loading the graph: {e}")
            exit(1)

    def analyze_graph(self):
        num_nodes = self.graph.number_of_nodes()
        num_edges = self.graph.number_of_edges()
        is_tree = nx.is_tree(self.graph)
        is_dag = nx.is_directed_acyclic_graph(self.graph)

        # Check if the graph is complete
        max_edges = num_nodes * (num_nodes - 1) // 2
        is_complete_graph = num_edges == max_edges

        return num_nodes, num_edges, is_tree, is_dag, is_complete_graph


class CSVWriter:
    def __init__(self, file_path, report):
        self.file_path = file_path
        self.report = report

    def write_to_csv(self):
        with open(self.file_path, mode='w', newline='') as csv_file:
            csv_writer = csv.writer(csv_file)
            csv_writer.writerow(["File Name", "Number of Nodes", "Number of Edges", "Is Tree", "Is DAG", "Is Complete Graph"])
            self.write_report(csv_writer)

    def write_report(self, csv_writer):
        row = [os.path.splitext(os.path.basename(self.file_path))[0]]
        row.extend(self.report)
        csv_writer.writerow(row)


def main():
    folder_path = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'resources')
    examples = ["example4.dot", "example5.dot", "example6.dot", "example7.dot"]

    # Create the 'output' folder if it doesn't exist
    output_folder = os.path.join(os.path.dirname(os.path.realpath(__file__)), 'output')
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)

    for example in examples:
        file_path = os.path.join(folder_path, example)
        print(f"Processing: {file_path}")

        graph_loader = GraphLoader(file_path)
        report = graph_loader.analyze_graph()

        csv_file_name = f"{os.path.splitext(example)[0]}_stats.csv"
        csv_file_path = os.path.join(output_folder, csv_file_name)

        csv_writer = CSVWriter(csv_file_path, report)
        csv_writer.write_to_csv()


if __name__ == "__main__":
    main()
