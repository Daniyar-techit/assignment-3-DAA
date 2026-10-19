package mst;

import graph.Graph;
import java.util.*;

public class PrimMST {

    public static class Result {
        public List<Graph.Edge> mstEdges;
        public double totalWeight;
        public boolean isConnected;
        public int operationsCount;

        public Result(List<Graph.Edge> mstEdges, double totalWeight, boolean isConnected, int operationsCount) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
            this.isConnected = isConnected;
            this.operationsCount = operationsCount;
        }
    }

    public static Result findMST(Graph graph) {
        int vertices = graph.getVertices();
        if (vertices <= 0) {
            return new Result(new ArrayList<>(), 0, true, 0);
        }

        boolean[] visited = new boolean[vertices];
        PriorityQueue<Graph.Edge> pq = new PriorityQueue<>();
        List<Graph.Edge> mstEdges = new ArrayList<>();
        double totalWeight = 0;
        int operationsCount = 0;

        visited[0] = true;
        for (Graph.Edge edge : graph.getNeighbors(0)) {
            pq.add(edge);
            operationsCount++;
        }

        while (!pq.isEmpty() && mstEdges.size() < vertices - 1) {
            Graph.Edge edge = pq.poll();
            operationsCount++;

            int dest = edge.dest;
            if (visited[dest]) {
                continue;
            }

            visited[dest] = true;
            mstEdges.add(edge);
            totalWeight += edge.weight;

            for (Graph.Edge nextEdge : graph.getNeighbors(dest)) {
                operationsCount++;
                if (!visited[nextEdge.dest]) {
                    pq.add(nextEdge);
                }
            }
        }

        boolean isConnected = (mstEdges.size() == vertices - 1);
        return new Result(mstEdges, totalWeight, isConnected, operationsCount);
    }
}