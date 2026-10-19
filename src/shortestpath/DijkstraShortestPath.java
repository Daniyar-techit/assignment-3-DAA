package shortestpath;

import graph.Graph;
import java.util.*;

public class DijkstraShortestPath {

    public static class Result {
        public double[] distances;
        public int[] parent;
        public int operationsCount;

        public Result(double[] distances, int[] parent, int operationsCount) {
            this.distances = distances;
            this.parent = parent;
            this.operationsCount = operationsCount;
        }

        public List<Integer> getPath(int target) {
            List<Integer> path = new ArrayList<>();
            if (distances[target] == Double.POSITIVE_INFINITY) {
                return path;
            }
            for (int at = target; at != -1; at = parent[at]) {
                path.add(at);
            }
            Collections.reverse(path);
            return path;
        }
    }

    public static Result findShortestPaths(Graph graph, int source) {
        int vertices = graph.getVertices();
        double[] distances = new double[vertices];
        int[] parent = new int[vertices];
        Arrays.fill(distances, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);

        distances[source] = 0;
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a.distance));
        pq.add(new NodeDistance(source, 0));

        int operationsCount = 0;

        while (!pq.isEmpty()) {
            operationsCount++;
            NodeDistance current = pq.poll();
            int u = current.node;

            if (current.distance > distances[u]) {
                continue;
            }

            for (Graph.Edge edge : graph.getNeighbors(u)) {
                operationsCount++;
                int v = edge.dest;
                double weight = edge.weight;

                if (distances[u] + weight < distances[v]) {
                    distances[v] = distances[u] + weight;
                    parent[v] = u;
                    pq.add(new NodeDistance(v, distances[v]));
                }
            }
        }

        return new Result(distances, parent, operationsCount);
    }

    private static class NodeDistance {
        int node;
        double distance;

        public NodeDistance(int node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}