#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <stdbool.h> 

const int MAX_NODES = 10;

typedef struct Graph{
    int numNodes;
    bool **edges;
}Graph;

Graph *createGraph(int numNodes){
    Graph *g = malloc(sizeof(*g));
    g->numNodes=numNodes;

    g->edges =  calloc(sizeof(bool*), g->numNodes);

    for(int i = 0; i<g->numNodes; i++){
        g->edges[i] = calloc(sizeof(bool), g->numNodes);
    }
    return g;
}

void freeGraph(Graph* g){
    for(int i = 0; i < g->numNodes; i++){
        free(g->edges[i]);
    }
    free(g->edges);
}

bool hasEdge(Graph* g, int fromNode, int toNode){
    return g->edges[fromNode,toNode];
}

bool addEdge(Graph* g, int fromNode, int toNode){
    if(hasEdge(g,fromNode,toNode)){
        return false;
    }

    g->edges[fromNode][toNode] = true;
    return true;
}

int arrayMin(int array[]){
    int length = sizeof(array)/sizeof(array[0]);
    int min = array[0];
    for(int i = 0; i < length; i++){
        if(array[i]<min){
            min=array[i];
        }
    }
    return min;
}

bool isCyclic(Graph* g, int v, bool visited[], bool* stack) {
    if (!visited[v]) {
        visited[v] = true;
        stack[v] = true;

        for (int i = 0; i < g->numNodes; ++i) {
            if (g->edges[v][i]) {
                if (!visited[i] && isCyclic(g, i, visited, stack)) {
                    return true;
                } else if (stack[i]) {
                    return true;
                }
            }
        }
    }
    stack[v] = false;
    return false;
}

bool hasCycle(Graph* g) {
    bool* visited = calloc(g->numNodes, sizeof(bool));
    bool* stack = calloc(g->numNodes, sizeof(bool));

    for (int i = 0; i < g->numNodes; ++i) {
        if (isCyclic(g, i, visited, stack)) {
            free(visited);
            free(stack);
            return true;
        }
    }

    free(visited);
    free(stack);
    return false;
}


bool tree(Graph* g, int numNodes, int numEdges){
    if(!hasCycle(g) && numEdges==numNodes-1){
        return true;
    }else{
        return false;
    }
}

bool complete(Graph* g) {
    for (int i = 0; i < g->numNodes; ++i) {
        for (int j = 0; j < g->numNodes; ++j) {
            if (i != j && !g->edges[i][j]) {
                return false;
            }
        }
    }
    return true;
}


int main(int argc, char *argv[]) {

    if (argc != 2) {
        printf("Usage: %s <path_to_dot_file>\n", argv[0]);
        return 1;
    }

    FILE *fp = fopen(argv[1], "r");
    if (fp == NULL) {
        printf("Error opening file %s\n", argv[1]);
        return 1;   
    }

    int numNodes = 0;
    int numEdges = 0;
    bool isDAG = false;
    bool isTree = false;
    bool isComplete = false;

    char line[100];
    char c;
    int nodes[20];
    char nodeNames[100];
    char name[3];
    int length;
    bool edgeFound = false;
    int n = 0;  

    char type[100];
    fgets(type, sizeof(type), fp);

    // Count the number of nodes and edges
    while (fgets(line, sizeof(line), fp) != NULL) {

        length = strlen(line);

        for (int i = 0; i < length; i++) {

            c = line[i];

            if(c==61 && edgeFound==0){                      //edges count
                numEdges++;
                edgeFound=true;
            }

            if(isalnum(c)){                                 //nodes count
                
                name[0]=c;
                if(isalnum(line[i+1])){
                    name[1]=line[i+1];   
                }
                
                if(strstr(nodeNames,name)==NULL){           //check if the node has already been counted
                    strcat(nodeNames, name);
                    strcat(nodeNames, " ");
                    numNodes++;
                }

                nodes[n]=name[0]+name[1];                         //saving the order of the nodes to create the graph later
                n++;

                memset(name,0,sizeof(name));                //clearing name string

            }
        }

        edgeFound=false;
    }
    
    Graph *g = createGraph(numNodes);

    int min = arrayMin(nodes);
    for(int i = 0; i < numEdges*2; i++){
        nodes[i]=nodes[i]-min;
        if(i%2!=0){
            addEdge(g,nodes[i-1],nodes[i]);
        }
    }
    
    if(complete(g)){
        isComplete=true;
    }else{
        if(!hasCycle(g) && type[0]=='d'){
            isDAG = true;
        }if(tree(g,numNodes,numEdges)){
            isTree = true;
        }
    }

    freeGraph(g);

    fclose(fp);

    system("mkdir output");
    char fileName[18] = "output/";
    fileName[7] = argv[1][0];
    strcat(fileName, "_stats.csv");

    fp = fopen(fileName, "w");
    if (fp == NULL) {
        printf("Error opening results file\n");
        return 1;   
    }

    fprintf(fp, "Nodes, Edges, Tree, DAG, Complete\n %d, %d, ", numNodes, numEdges);
    if(isTree){
        fprintf(fp, "TRUE, ");
    }else{
        fprintf(fp, "FALSE, ");
    }
    if(isDAG){
        fprintf(fp, "TRUE, ");
    }else{
        fprintf(fp, "FALSE, ");
    }
    if(isComplete){
        fprintf(fp, "TRUE");
    }else{
        fprintf(fp, "FALSE");
    }

    fclose(fp);

    return 0;
}