package mst;

import dsu.DSU;
import graph.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalMST {

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
        List<Graph.Edge> allEdges = new ArrayList<>(graph.getAllEdges());
        Collections.sort(allEdges);

        DSU dsu = new DSU(graph.getVertices());
        List<Graph.Edge> mstEdges = new ArrayList<>();
        double totalWeight = 0;
        int operationsCount = 0;

        for (Graph.Edge edge : allEdges) {
            operationsCount++;
            if (dsu.union(edge.src, edge.dest)) {
                mstEdges.add(edge);
                totalWeight += edge.weight;
                if (mstEdges.size() == graph.getVertices() - 1) {
                    break;
                }
            }
        }

        operationsCount += dsu.getOperationsCount();
        boolean isConnected = (graph.getVertices() <= 1) || (mstEdges.size() == graph.getVertices() - 1);
        return new Result(mstEdges, totalWeight, isConnected, operationsCount);
    }
}
