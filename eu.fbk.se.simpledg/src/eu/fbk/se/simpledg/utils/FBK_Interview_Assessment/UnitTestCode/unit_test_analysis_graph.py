import unittest
import os
from analysis_graph import GraphLoader, GraphAnalyzer, GraphProcessor
import networkx as nx

class TestGraphLoader(unittest.TestCase):
    def test_load_graph_file_not_found(self):
        loader = GraphLoader("nonexistent_file.dot")
        graph = loader.load_graph()
        self.assertIsNone(graph)

    def test_load_graph_valid_file(self):
        loader = GraphLoader("example4.dot")  # Replace with an existing DOT file
        graph = loader.load_graph()
        self.assertIsNotNone(graph)
        self.assertIsInstance(graph, (nx.Graph, nx.DiGraph))

class TestGraphAnalyzer(unittest.TestCase):
    def test_is_complete_empty_graph(self):
        graph = nx.Graph()
        analyzer = GraphAnalyzer()
        result = analyzer.is_complete(graph)
        self.assertTrue(result)

    def test_analyze_graph_valid_graph(self):
        graph = nx.DiGraph()
        graph.add_edges_from([("A", "B"), ("B", "C")])

        analyzer = GraphAnalyzer()
        result = analyzer.analyze_graph(graph)

        self.assertEqual(result["Number of Nodes"], 3)
        self.assertEqual(result["Number of Edges"], 2)
        self.assertFalse(result["Is Tree"])
        self.assertTrue(result["Is Directed Acyclic Graph (DAG)"])
        self.assertFalse(result["Is Complete Graph"])

class TestGraphProcessor(unittest.TestCase):
    def test_main(self):
        # Assuming the example files exist in the specified folder
        folder_path = "ExampleDOTFiles"
        examples = ["example4.dot", "example5.dot", "example6.dot", "example7.dot"]

        for example in examples:
            file_path = os.path.join(os.path.expanduser("~"), "Documents", "runtime-EclipseApplication", folder_path, example)
            processor = GraphProcessor()
            processor.main(file_path)

if __name__ == "__main__":
    unittest.main()
