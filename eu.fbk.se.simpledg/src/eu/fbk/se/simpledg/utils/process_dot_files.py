import os
import argparse
from graph_analyzer import load_graph, generate_report

def process_dot_files(directory):
    for filename in os.listdir(directory):
        if filename.endswith(".dot"):
            dot_file_path = os.path.join(directory, filename)
            print(f"Processing file: {dot_file_path}")
            graph = load_graph(dot_file_path)
            if graph:
                generate_report(graph, "Graph", dot_file_path)
            else:
                print(f"Failed to load graph from {dot_file_path}")

def main():
    parser = argparse.ArgumentParser(description="Process all .dot files in a directory")
    parser.add_argument("directory", help="Directory containing DOT files")
    args = parser.parse_args()

    process_dot_files(args.directory)

if __name__ == "__main__":
    main()
