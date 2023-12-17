import pygraphviz as pgv
import networkx as nx
import csv
import os

#initialize variables
chk_is_tree = False
chk_is_DAG = False
chk_is_complete = False
graph_type = ""
data = []

# A tree is a connected graph has no cycle
def checkIsTree(file_path):
    graph = pgv.AGraph(file_path)
    nx_graph = nx.Graph(graph)
    chk_is_tree = nx.is_tree(nx_graph)
    return chk_is_tree

# A (DAG) is a graph with no cycles or it is impossible to traverse the entire graph
def checkIsDAG(file_path):
    graph = pgv.AGraph(file_path)
    nx_graph = nx.DiGraph(graph)
    chk_is_DAG = nx.is_directed_acyclic_graph(nx_graph)
    return chk_is_DAG

# For n number of nodes ,There are n*(n-1)/2 edges
def checkIsComplete(file_path):
    graph = pgv.AGraph(file_path)
    num_of_nodes = len(graph.nodes())
    num_of_edges = len(graph.edges())
    edge_chk = (num_of_nodes*(num_of_nodes-1))/2
    
    if num_of_edges == edge_chk:
        chk_is_complete = True
    else:
        chk_is_complete = False

    return chk_is_complete    

def graphClassification(file_path):
    result_tree = checkIsTree(file_path)
    result_DAG = checkIsDAG(file_path)
    result_complete = checkIsComplete(file_path)

    if result_tree == True:
        graph_type = "Tree"
    elif result_complete == True:
        graph_type = "Complete"
    elif result_DAG == True:
        graph_type = "DAG"
    else:
        graph_type = "Others"

    return graph_type

def numberOfNodesAndEdges(file_path):
    graph = pgv.AGraph(file_path)
    num_of_nodes = len(graph.nodes())
    num_of_edges = len(graph.edges())

    return num_of_nodes, num_of_edges


def main():
    try: 
        output_file_name = "examples_stats.csv"
        directory_path = 'resources/'
        files = os.listdir(directory_path)
        for file_name in files:
            file_path = os.path.join(directory_path, file_name)
            if os.path.isfile(file_path):           
                graph_type = graphClassification(file_path)
                num_nodes, num_edges = numberOfNodesAndEdges(file_path)
                header = ['File Name', 'Graph Type', 'Number of Nodes', 'Number of Edges']
                data.append([file_name.replace('.dot', ''), graph_type, num_nodes, num_edges])     
       
    
        if not os.path.exists("output2"): 
            os.makedirs("output2") 
        
        with open(os.path.join('output2', output_file_name), 'w', newline="") as file:
            csvwriter = csv.writer(file)
            csvwriter.writerow(header)
            csvwriter.writerows(data)
            print("The CSV file is generated successfuly!")

    except FileNotFoundError:
        print('File Not Found')
    except ValueError:
        print('Invalid Input')


if __name__ == "__main__":
    main()
