import os
import networkx as nx
import csv

#input folder
INPUT_DOT_FILES = 'resources'

#output folder
OUTPUT_CSV_FILES = 'output'

def parser(text):
    """
    Description: Parses the input string to create a graph
    Returns: nx.Graph or nx.DiGraph - NetworkX graph object
    """

    if "=>" in text: #directed
        G = nx.DiGraph()
    else:
        G = nx.Graph() #undirected  
    
    for line in text.splitlines(): #each line of text
        if "=>" in line:
            nodes = line.split("=>")
            #add new edge
            G.add_edge(nodes[0].strip(), nodes[1].strip()) 

        elif "==" in line:
            nodes = line.split("==")
            #add new edge
            G.add_edge(nodes[0].strip(), nodes[1].strip())
    
    return G

def report(G):
    """
    Description: Analyzes the graph and returns report
    Returns: dictionary containing report values
    """

    nodes = G.number_of_nodes()
    edges = G.number_of_edges()

    #is_tree
    if not G.is_directed():
        is_tree = nx.is_tree(G)
    else:
        is_tree = False

    #is_dag
    if G.is_directed():
        is_dag = nx.is_directed_acyclic_graph(G)
    else:
        is_dag = False

    #is_connected
    if G.is_directed():
        is_connected = nx.is_weakly_connected(G) #nodes reach each other
    else:
        is_connected = nx.is_connected(G) #nodes connected to each other

    #is_directed
    if G.is_directed():
        is_complete = False
    else:
        is_complete = is_connected and all(len(neighbors) == len(G) - 1 for node, neighbors in G.adjacency()) #check node connection with others


    results = {
        "nodes": nodes,
        "edges": edges,
        "is_tree": is_tree,
        "is_dag": is_dag,
        "is_complete": is_complete
    }

    return results

def main(): 
    #output folder
    os.makedirs(OUTPUT_CSV_FILES, exist_ok=True)
    
    #statistics creation
    for filename in os.listdir(INPUT_DOT_FILES): #list dot files in /resources
        if filename.endswith(".dot"):
            dot_path = os.path.join(INPUT_DOT_FILES, filename)

            with open(dot_path, 'r') as file:
                text = file.read()
                graph = parser(text)
                results = report(graph)

                #building path 
                csv_name = os.path.splitext(filename)[0] + '_stats.csv' 
                csv_path = os.path.join(OUTPUT_CSV_FILES, csv_name)

                #write file w/ results
                with open(csv_path, 'w', newline='') as csv_file:
                    columns_name = results.keys()
                    writer = csv.DictWriter(csv_file, fieldnames=columns_name)
                    writer.writeheader()
                    writer.writerow(results)

if __name__ == "__main__":
    main()
