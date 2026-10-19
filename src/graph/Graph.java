package graph;

import java.util.*;

public class Graph {
    public static class Edge implements Comparable<Edge> {
        public int src, dest;
        public double weight;

        public Edge(int src, int dest, double weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }
    }

    private final int vertices;
    private final List<Edge> allEdges;
    private final Map<Integer, List<Edge>> adjacencyList;
    private final boolean directed;

    public Graph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        this.allEdges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }

    public void addEdge(int src, int dest, double weight) {
        Edge edge = new Edge(src, dest, weight);
        allEdges.add(edge);
        adjacencyList.get(src).add(edge);

        if (!directed) {
            Edge reverseEdge = new Edge(dest, src, weight);
            adjacencyList.get(dest).add(reverseEdge);
        }
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public List<Edge> getNeighbors(int vertex) {
        return adjacencyList.getOrDefault(vertex, Collections.emptyList());
    }

    public boolean isDirected() {
        return directed;
    }
}
