### SimpleDG
SimpleDG is a text editor to specify [directed graphs](https://en.wikipedia.org/wiki/Directed_graph).
The tool is developed with Java and [Xtext](https://www.eclipse.org/Xtext/). 

#### Specify a directed graph
A directed graph is represented as a list of nodes and a list of edges. For instances, [example1](examples/example1.sdg) below
```
Nodes:
	A 
	B
;
Edges:
	A -> A
	A -> B
;
```
corresponds to the following directed graph
```mermaid
graph TD;
	A-->A;
	A-->B;
```

#### Syntactic checkers
Creating a file with the extension _.sdg_ activates Xtext native syntax highlighting and syntactic checker. Moreover, as an example, the editor implements a custom syntactic checker that returns an error if the nodes used in the Edges section are not defined in the Nodes section. For instance, [example2](examples/example2.sdg) below triggers an error, as node C is not defined in section Nodes.
```
Nodes:
	A 
	B
;
Edges:
	A -> A
	A -> B
	A -> C
	C -> D
;
```

#### Requirements
The project has been built and tested on the following systems
- Windows 11 Pro, Eclipse 2023-06, Java 17 (included with Eclipse), Xtext 2.31
- macOS Ventura 13.4.1, Eclipse 2022-12, Java 11, Xtext 2.29
- Ubuntu 22.04.2, Eclipse 2022-09, Java 11, Xtext 2.31 

