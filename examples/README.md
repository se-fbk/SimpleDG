### Examples of graphs
This folder contains some examples of graphs that can be implemented with SimpleDG.

#### Example 1
File [example1](example1.dot) 
```
digraph g{
	A => A
	A => B
}
```
corresponds to graph
```mermaid
graph TD
	id1((A)) --> id1((A))
	id1((A)) --> id2((B))
```


#### Example 2
File [example2](example2.dot) 
```
graph{
	A == A
	A == B
	A == C
	C == D
}
```
corresponds to graph
```mermaid
graph TD
	id1((A)) --- id1((A))
	id1((A)) --- id2((B))
	id1((A)) --- id3((C))
	id3((C)) --- id4((D))
```


#### Example 3
File [example3](example3.dot) 
```
digraph {
	a => b
	b => c
	c => c
}
```
corresponds to graph
```mermaid
graph TD
	id1((a)) --> id2((b))
	id2((b)) --> id3((c))
	id3((c)) --> id3((c))
```

#### Example 4
```mermaid
graph TD
	id1((1)) --> id4((4))
	id2((2)) --> id1((1))
	id2((2)) --> id4((4))
	id3((3)) --> id2((2))
	id3((3)) --> id4((4))
	id4((4)) --> id5((5))
	id4((4)) --> id6((6))
	id6((6)) --> id5((5))
```

#### Example 5
```mermaid
graph TD
	id1((a)) --- id2((b))
	id2((b)) --- id3((c))
	id2((b)) --- id4((d))
	id4((d)) --- id5((e))
```

#### Example 6
```mermaid
graph TD
	id1((N1)) --> id2((N2))
	id1((N1)) --> id3((N3))
	id2((N2)) --> id4((N4))
	id2((N2)) --> id5((N5))
	id3((N3)) --> id5((N5))
	id3((N3)) --> id6((N6))
	id3((N3)) --> id7((N7))
```

#### Example 7
```mermaid
graph TD
	id1((A)) --- id2((B))
	id1((A)) --- id4((D))
	id1((A)) --- id5((E))
	id2((B)) --- id3((C))
	id2((B)) --- id4((D))
	id2((B)) --- id5((E))
	id2((B)) --- id6((F))
	id3((C)) --- id5((E))
	id3((C)) --- id6((F))
	id4((D)) --- id5((E))
	id5((E)) --- id6((F))
```
