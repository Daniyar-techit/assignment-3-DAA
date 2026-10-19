package connectivity;

import graph.Graph;
import java.util.*;

public class ConnectivityAnalysis {
    public static class TopoResult {
        public List<Integer> order;
        public boolean hasCycle;

        public TopoResult(List<Integer> order, boolean hasCycle) {
            this.order = order;
            this.hasCycle = hasCycle;
        }
    }

    public static TopoResult topologicalSort(Graph graph) {
        int vertices = graph.getVertices();
        int[] visited = new int[vertices];
        List<Integer> order = new ArrayList<>();
        boolean[] cycleDetected = {false};

        for (int i = 0; i < vertices; i++) {
            if (visited[i] == 0) {
                if (dfsTopo(i, graph, visited, order, cycleDetected)) {
                    return new TopoResult(new ArrayList<>(), true);
                }
            }
        }

        Collections.reverse(order);
        return new TopoResult(order, false);
    }

    private static boolean dfsTopo(int v, Graph graph, int[] visited, List<Integer> order, boolean[] cycleDetected) {
        visited[v] = 1;

        for (Graph.Edge edge : graph.getNeighbors(v)) {
            int neighbor = edge.dest;
            if (visited[neighbor] == 1) {
                cycleDetected[0] = true;
                return true;
            }
            if (visited[neighbor] == 0) {
                if (dfsTopo(neighbor, graph, visited, order, cycleDetected)) {
                    return true;
                }
            }
        }

        visited[v] = 2;
        order.add(v);
        return false;
    }

    public static List<List<Integer>> findSCC(Graph graph) {
        int vertices = graph.getVertices();
        boolean[] visited = new boolean[vertices];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                fillOrder(i, graph, visited, stack);
            }
        }

        Graph transposedGraph = transpose(graph);

        Arrays.fill(visited, false);
        List<List<Integer>> sccList = new ArrayList<>();

        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                dfsSCC(v, transposedGraph, visited, component);
                sccList.add(component);
            }
        }

        return sccList;
    }

    private static void fillOrder(int v, Graph graph, boolean[] visited, Stack<Integer> stack) {
        visited[v] = true;
        for (Graph.Edge edge : graph.getNeighbors(v)) {
            if (!visited[edge.dest]) {
                fillOrder(edge.dest, graph, visited, stack);
            }
        }
        stack.push(v);
    }

    private static Graph transpose(Graph graph) {
        Graph transposed = new Graph(graph.getVertices(), true);
        for (Graph.Edge edge : graph.getAllEdges()) {
            transposed.addEdge(edge.dest, edge.src, edge.weight);
        }
        return transposed;
    }

    private static void dfsSCC(int v, Graph graph, boolean[] visited, List<Integer> component) {
        visited[v] = true;
        component.add(v);
        for (Graph.Edge edge : graph.getNeighbors(v)) {
            if (!visited[edge.dest]) {
                dfsSCC(edge.dest, graph, visited, component);
            }
        }
    }
}