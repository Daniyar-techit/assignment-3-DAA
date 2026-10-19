package experiment;

import connectivity.ConnectivityAnalysis;
import dsu.DSU;
import graph.Graph;
import mst.KruskalMST;
import mst.PrimMST;
import shortestpath.DijkstraShortestPath;

import java.util.*;

public class ExperimentRunner {

    public static class GraphConfig {
        String name;
        int vertices;
        int edges;

        public GraphConfig(String name, int vertices, int edges) {
            this.name = name;
            this.vertices = vertices;
            this.edges = edges;
        }
    }

    public static void main(String[] args) {
        List<GraphConfig> configs = Arrays.asList(
                new GraphConfig("Small", 10, 30),
                new GraphConfig("Medium", 100, 500),
                new GraphConfig("Large", 1000, 5000)
        );

        long seed = 42L;
        int runs = 5;

        System.out.println("<<< Starting experimental analysis");

        for (GraphConfig config : configs) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("Configuration: " + config.name + " (V = " + config.vertices + ", E = " + config.edges + ")");
            System.out.println("--------------------------------------------------");

            long kruskalTimeSum = 0, primTimeSum = 0, topoTimeSum = 0, sccTimeSum = 0, dijkstraTimeSum = 0;
            long kruskalOpSum = 0, primOpSum = 0, dijkstraOpSum = 0;

            for (int r = 0; r < runs; r++) {
                Graph undirectedGraph = generateConnectedGraph(config.vertices, config.edges, false, seed + r);
                Graph directedGraph = generateDirectedGraph(config.vertices, config.edges, seed + r);

                long start = System.nanoTime();
                KruskalMST.Result kruskalRes = KruskalMST.findMST(undirectedGraph);
                kruskalTimeSum += (System.nanoTime() - start);
                kruskalOpSum += kruskalRes.operationsCount;

                start = System.nanoTime();
                PrimMST.Result primRes = PrimMST.findMST(undirectedGraph);
                primTimeSum += (System.nanoTime() - start);
                primOpSum += primRes.operationsCount;

                start = System.nanoTime();
                ConnectivityAnalysis.topologicalSort(directedGraph);
                topoTimeSum += (System.nanoTime() - start);

                start = System.nanoTime();
                ConnectivityAnalysis.findSCC(directedGraph);
                sccTimeSum += (System.nanoTime() - start);

                start = System.nanoTime();
                DijkstraShortestPath.Result dijkstraRes = DijkstraShortestPath.findShortestPaths(undirectedGraph, 0);
                dijkstraTimeSum += (System.nanoTime() - start);
                dijkstraOpSum += dijkstraRes.operationsCount;
            }

            System.out.printf("Kruskal's Algorithm:   Avg Time = %.2f ms | Avg Ops = %d\n", (kruskalTimeSum / (double) runs) / 1_000_000.0, kruskalOpSum / runs);
            System.out.printf("Prim's Algorithm:      Avg Time = %.2f ms | Avg Ops = %d\n", (primTimeSum / (double) runs) / 1_000_000.0, primOpSum / runs);
            System.out.printf("Topological Sort:      Avg Time = %.2f ms\n", (topoTimeSum / (double) runs) / 1_000_000.0);
            System.out.printf("SCC (Kosaraju):        Avg Time = %.2f ms\n", (sccTimeSum / (double) runs) / 1_000_000.0);
            System.out.printf("Dijkstra's Algorithm:  Avg Time = %.2f ms | Avg Ops = %d\n", (dijkstraTimeSum / (double) runs) / 1_000_000.0, dijkstraOpSum / runs);
        }

        System.out.println("\n<<<Experiments succsesfully done>>>");
    }


    private static Graph generateConnectedGraph(int vertices, int edges, boolean directed, long seed) {
        Random random = new Random(seed);
        Graph graph = new Graph(vertices, directed);

        for (int i = 1; i < vertices; i++) {
            int parent = random.nextInt(i);
            double weight = 1 + random.nextDouble() * 99;
            graph.addEdge(parent, i, weight);
        }

        int addedEdges = vertices - 1;
        Set<String> existingEdges = new HashSet<>();

        while (addedEdges < edges) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);
            if (u == v) continue;

            String edgeKey = Math.min(u, v) + "-" + Math.max(u, v);
            if (!existingEdges.contains(edgeKey)) {
                existingEdges.add(edgeKey);
                double weight = 1 + random.nextDouble() * 99;
                graph.addEdge(u, v, weight);
                addedEdges++;
            }
        }

        return graph;
    }

    private static Graph generateDirectedGraph(int vertices, int edges, long seed) {
        Random random = new Random(seed);
        Graph graph = new Graph(vertices, true);
        Set<String> existingEdges = new HashSet<>();
        int addedEdges = 0;

        while (addedEdges < edges) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);
            if (u == v) continue;

            String edgeKey = u + "->" + v;
            if (!existingEdges.contains(edgeKey)) {
                existingEdges.add(edgeKey);
                double weight = 1 + random.nextDouble() * 99;
                graph.addEdge(u, v, weight);
                addedEdges++;
            }
        }

        return graph;
    }
}