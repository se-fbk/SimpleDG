# Graph Analyzer

This Python script serves as a simple graph analyzer, capable of analyzing properties of a graph specified in the DOT (Graphviz) file format. The tool checks whether the graph is a tree, a directed acyclic graph (DAG), or a complete graph. Additionally, it generates and saves a report in CSV format for further analysis.

## Features
### Graph Class:

Represents a graph and supports operations to check if it's a tree, DAG, or complete graph.
Includes methods to add vertices, add edges, get the number of edges, and perform graph property checks.

### Load Graph Function:

Reads a graph from a DOT file with support for alternative edge notations.
Creates a Graph object and populates it based on the contents of the DOT file.

### Generate Report Function:

Generates a detailed report for the given graph, including graph name, file name, number of nodes, number of edges, and whether the graph is a tree, DAG, or complete.
Saves the report into a separate CSV file in the 'output' folder, with the file named based on the input file name.

### Main Function:

Uses argparse to parse command-line arguments, specifically the path to the DOT file.
Calls the load_graph function to load the graph from the specified DOT file.
Generates and displays a report for the loaded graph.


## Usage

To Run the script from the command line by providing the directory containing the .dot files, for Example:

	python process_dot_files.py "path/to/directory"

this approach is for specific .dot file's report

To run the script for all .dot files in any specific directory run the script from the command line by providing the directory containing the .dot files. For Example:

	python graph_analyzer.py "path/to/directory"

## Report Output

The script generates a report containing valuable information about the analyzed graph, displaying it on the console and saving it as a CSV file in the 'output' folder.

## Dependices

The script relies on the standard Python libraries, including argparse, collections, csv, and os.


