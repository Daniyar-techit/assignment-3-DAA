# Assignment 3: Smart City Infrastructure and Route Planning

## Overview
In this assignment, I created a Java application that simulates a city infrastructure using graph algorithms. The city is represented as a graph:
-**Vertices:** Districts or locations in the city
- **Edges:** Roads or connections between locations
- **Weights:** Costs of building roads or distance between locations

The application handles three main tasks:
1. Building a minimum cost network using Kruskal and Prim MST algorithms.
2. Checking graph connectivity, finding topological sorting, and discovering Strongly Connected Components (SCC).
3. Finding the shortest path between city districts using Dijkstra's algorithm.

---

## Project Structure
I organized the code into separate packages inside src:

-  src/graph/ - contains Graph.java class with adjacency list representation.
-  src/dsu/ - Disjoint Set Union implementation with path compression and union by rank.
-  src/mst/ - Kruskal and Prim algorithms implementation.
-  src/connectivity/ - Topological sort and Kosaraju algorithm for SCC.
-  src/shortestpath/ - Dijkstra algorithm implementation.
-  src/experiment/ - Main class to run benchmarks and print results.

---

## Complexity Analysis

Here is a summary of the time and space complexity for each implemented algorithm:

| Algorithm | Time Complexity | Space Complexity | Explanation |
| :--- | :---: | :---: | :--- |
| **DSU (find / union)** | $O(\alpha(V))$ | $O(V)$ | Thanks to path compression and union by rank, operations are almost $O(1)$. |
| **Kruskal's MST** | $O(E \log E)$ | $O(V + E)$ | Main time is spent on sorting all edges of the graph. |
| **Prim's MST** | $O((V + E) \log V)$ | $O(V + E)$ | Uses Java PriorityQueue to extract minimum weight edge. |
| **Topological Sort** | $O(V + E)$ | $O(V + E)$ | Simple DFS traversal. Works only on DAGs (detects cycles). |
| **SCC (Kosaraju)** | $O(V + E)$ | $O(V + E)$ | Requires two DFS passes (one on normal graph, one on transposed). |
| **Dijkstra** | $O((V + E) \log V)$ | $O(V)$ | Finds shortest paths using PriorityQueue. Needs positive weights. |

### Why Dijkstra fails with negative weights
Dijkstra is a greedy algorithm. It assumes that once a vertex distance is finalized, it can never get smaller. If there are negative weights in the graph, adding a negative edge later could actually produce a shorter total distance to an already processed node, which Dijkstra misses.

---

## Experimental Setup & Results

I tested all algorithms on three different graph sizes generated randomly with a fixed seed (42) so results can be reproduced:
1. **Small:** 10 vertices, 30 edges
2. **Medium:** 100 vertices, 500 edges
3. **Large:** 1000 vertices, 5000 edges

Each experiment was executed 5 times, and I recorded the average execution time in milliseconds using System.nanoTime().

### Benchmark Results Table

| Graph Size | Vertices | Edges | Kruskal (ms) | Prim (ms) | Topo Sort (ms) | SCC (ms) | Dijkstra (ms) |
| :--- | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **Small** | 10 | 30 | 0.41 | 0.36 | 0.18 | 0.21 | 2.66 |
| **Medium** | 100 | 500 | 0.68 | 0.58 | 0.02 | 0.49 | 0.47 |
| **Large** | 1,000 | 5,000 | 7.04 | 4.66 | 0.05 | 5.79 | 5.46 |

---

## Comparison and Discussion

1. **Kruskal vs Prim:**
    - Kruskal works really fast on smaller and sparse graphs because it sorts edges first.
    - Prim algorithm performs slightly better when the graph gets larger and denser because heap operations scale with $\log V$ instead of sorting all $E$ edges.
2. **Importance of DSU Optimizations:**
    - Without path compression and union by rank, DSU operations would slow down Kruskal's algorithm to $O(E \cdot V)$ because the trees inside DSU would become too deep.
3. **Connectivity Algorithms:**
    - Topological sort and Kosaraju algorithm run in linear $O(V + E)$ time, so they scale very smoothly even on the Large graph configuration.

---

## Testing & Verification

I wrote manual tests to verify correctness:
- Checked that Kruskal and Prim return the exact same MST total weight on connected undirected graphs.
- Tested Topological Sort on a graph with a cycle to make sure it properly detects the cycle and reports it.
- Verified that Kosaraju algorithm correctly assigns every vertex into exactly one Strongly Connected Component.
- Tested Dijkstra on graphs with disconnected/unreachable nodes to ensure distances remain set to infinity without causing exceptions.

---

## Conclusion
This assignment helped me understand how graph algorithms behave in practice. Kruskal is great for sparse networks, Prim works well for dense networks, Kosaraju and Topological sort are quick linear tools for flow analysis, and Dijkstra is effective for finding shortest distances as long as edge weights are non-negative.