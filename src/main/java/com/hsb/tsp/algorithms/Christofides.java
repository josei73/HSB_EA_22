package com.hsb.tsp.algorithms;



import com.hsb.tsp.graph.Edge;

import java.util.*;

public class Christofides extends Algorithm {
    public Christofides() {

    }

    private List<Edge> computeMST(int[][] adj) {
        Prim primAlgo = new Prim(adj);
        return primAlgo.primMST();

    }


    private List<Edge> computePerfectMatching(int[][] matrix, List<Edge> mst) {

        List<Integer> mstNodesId = new ArrayList<>();


        mst.forEach(edge -> {
            mstNodesId.add(edge.getNode1());
            mstNodesId.add(edge.getNode2());
        });
        Map<Integer, Boolean> visit = new HashMap<>();


        Set<Integer> vertices = new HashSet<>();
        for (int i = 0; i < mstNodesId.size(); ++i) {
            var value = mstNodesId.get(i);
            int oddCounter = 1;
            for (int j = 0; j < mstNodesId.size(); ++j) {
                if (j != i) {

                    if (value == mstNodesId.get(j))
                        ++oddCounter;

                }
            }

            if (oddCounter % 2 != 0 && visit.get(value) == null)
                vertices.add(value);
            visit.put(value, true);
        }
        while (!vertices.isEmpty()) {
            Integer integer = vertices.iterator().next();
            int length = Integer.MAX_VALUE;
            int node2 = 1;
            vertices.remove(integer);
            for (Integer node : vertices) {
                if (matrix[integer][node] < length) {
                    length = matrix[integer][node];
                    node2 = node;
                }
            }
            mst.add(new Edge(integer, node2, length));
            vertices.remove(node2);
        }


        return mst;

    }

    // Computes the eulerian tour on the given graph.
    private static List<Integer> computeEulerianTour(List<Edge> mst) {
        List<List<Integer>> adj = new ArrayList<>(mst.size());
        for (int i = 0; i < mst.size(); i++) {
            adj.add(new ArrayList<>());
        }
        mst.forEach(edge -> {
            adj.get(edge.getNode1()).add(edge.getNode2());
        });

        Hierholzer h = new Hierholzer(adj);
        List<Integer> path = h.findEulerianPath();
        return path;
    }


    private static List<Integer> computeHamiltonianCycle(List<Integer> et) {
        List<Integer> tour = new ArrayList<>();
        for (Integer integer : et) {
            if (!tour.contains(integer))
                tour.add(integer);
        }
        tour.add(et.get(0));

        List<Integer> solver = new ArrayList<>();
        for (Integer integer : tour) solver.add(integer + 1);
        return solver;
    }


    public List<Integer> applyChristofides(int[][] adj) {
        // Compute the minimum spanning tree.
        List<Edge> mst = computeMST(adj);
        // Compute the perfect matching.
        List<Edge> pm = computePerfectMatching(adj,mst);
        // Compute the eulerian tour.
        List<Integer> et = computeEulerianTour(pm);
        // Compute the Hamiltonian cycle.
        List<Integer> hc = computeHamiltonianCycle(et);
        return hc;
    }

    @Override
    public ArrayList<Integer> getTour() {
        return null;
    }

    @Override
    public void solve() {

    }
}

