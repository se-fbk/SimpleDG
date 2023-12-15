# Graph Analysis Script

## Introduction

This is a Python script for analyzing graphs represented in .dot files. The analysis includes determining some key properties of the graph:
- number of nodes
- number of edges
- if the graph is a tree
- if the graph is a directed acyclic graph (DAG)
- if the graph is complete

## Requirements

- Python 3.x
- [NetworkX library](https://networkx.github.io/)

You can install it using the following command:
```bash
   pip install -r requirements.txt
```

The project has been built and tested on the following systems
- macOS Ventura 13.6, Python 3.10.0, networkx 3.2.1

## Usage
1. Ensure you meet the requirements mentioned above
2. Place the .dot files you want to analyze in the `resources` folder
3. Run `graph_analysis.py` script using Python:

```bash
   python3 graph_analysis.py
```
4. The analysis results will be available in the `output` folder in separate CSV files for each .dot file


> During the development of this script, I sought guidance and ideas on how to structure and implement it efficiently. I used online resources, including Stack Overflow, to solve specific coding problems and questions. In addition, I consulted ChatGPT for an initial idea of the tools to be used within the time constraints.