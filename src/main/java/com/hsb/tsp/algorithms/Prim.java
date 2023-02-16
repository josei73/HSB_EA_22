package com.hsb.tsp.algorithms;

import com.hsb.tsp.graph.Edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Prim {
    // Number of nodes in the graph
    private static int N;

    // Adjacency matrix to represent the graph
    private static int[][] adjMatrix;

    // Minimum spanning tree of the graph
    private static int[] parent;

    // Key values used to pick minimum weight edge in cut
    private static int[] key;

    // To represent set of vertices not yet included in MST
    private static boolean[] mstSet;


    public Prim(int adjMatrix[][]) {
        N = adjMatrix.length;
        this.adjMatrix = adjMatrix;
    }

    // Function to find the minimum spanning tree of the given graph
    public static List<Edge>  primMST() {

        mstSet = new boolean[N];


        key = new int[N];


        parent = new int[N];

        Arrays.fill(key, Integer.MAX_VALUE);


        key[0] = 0;
        parent[0] = -1;

        for (int count = 0; count < N - 1; count++) {
            // Pick the minimum key vertex from the set of vertices
            // not yet included in MST
            int u = minKey();


            mstSet[u] = true;
            for (int v = 0; v < N; v++) {
                if (adjMatrix[u][v] != 0 && !mstSet[v] && adjMatrix[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = adjMatrix[u][v];
                }
            }
        }

        return printMST();
    }

    private static int minKey() {
        // Initialize min value
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < N; v++)
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }

        return minIndex;
    }

    private static List<Edge>  printMST() {
        List<Edge> nodes = new ArrayList<>();
        for (int i = 1; i < N; i++) {
            nodes.add(new Edge(parent[i],i, adjMatrix[i][parent[i]]));
        }
        nodes.sort(Comparator.comparing(Edge::getDist));

        return nodes;
    }


}
