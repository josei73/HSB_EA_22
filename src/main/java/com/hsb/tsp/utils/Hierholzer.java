package com.hsb.tsp.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hierholzer {
    private static final int UNVISITED = 0;
    private static final int VISITED = 1;

    // adjacency list representation of the graph
    private List<List<Integer>> adj;
    // array to store the state of each vertex (visited or unvisited)
    private boolean[] visited;

    public Hierholzer(List<List<Integer>> adj) {
        this.adj = adj;
        this.visited = new boolean[adj.size()];
    }

    public List<Integer> findEulerianPath() {
        // list to store the Eulerian path
        List<Integer> path = new ArrayList<>();
        // start the algorithm at vertex 0
        dfs(0, path);
        Collections.reverse(path);
        return path;
    }

    private void dfs(int u, List<Integer> path) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                dfs(v, path);
            }
        }
        path.add(u);
    }

}


