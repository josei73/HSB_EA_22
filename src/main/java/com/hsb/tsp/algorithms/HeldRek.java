package com.hsb.tsp.algorithms;

import java.util.ArrayList;

public class HeldRek extends Algorithm {


    private final int N;
    private final int START_NODE;
    private final int FINISHED_STATE;

    private int[][] distance;
    private double minTourCost = Double.POSITIVE_INFINITY;

    private ArrayList<Integer> tour = new ArrayList<>();
    private boolean ranSolver = false;

    public HeldRek(int[][] distance) {
        this(0, distance);
    }

    public HeldRek(int startNode, int[][] distance) {
        super("Held-Karp");
        this.distance = distance;
        N = distance.length;
        START_NODE = startNode;

        // Validate inputs.
        if (N <= 2) throw new IllegalStateException("TSP on 0, 1 or 2 nodes doesn't make sense.");
        if (N != distance[0].length)
            throw new IllegalArgumentException("Matrix must be square (N x N)");
        if (START_NODE < 0 || START_NODE >= N)
            throw new IllegalArgumentException("Starting node must be: 0 <= startNode < N");
        if (N > 32)
            throw new IllegalArgumentException(
                    "Matrix too large! A matrix that size for the DP TSP problem with a time complexity of"
                            + "O(n^2*2^n) requires way too much computation for any modern home computer to handle");


        FINISHED_STATE = (1 << N) - 1;
    }


    public ArrayList<Integer> getTour() {
        if (!ranSolver) solve();
        return tour;
    }


    public double getTourCost() {
        if (!ranSolver) solve();
        return minTourCost;
    }

    @Override
    public void solve() {

        // Run the solver
        int state = 1 << START_NODE;
        Integer[][] memo = new Integer[N][1 << N];
        Integer[][] prev = new Integer[N][1 << N];
        minTourCost = tsp(START_NODE, state, memo, prev);

        // Regenerate path
        int index = START_NODE;
        while (true) {
            tour.add(index + 1);
            Integer nextIndex = prev[index][state];
            if (nextIndex == null) break;
            int nextState = state | (1 << nextIndex);
            state = nextState;
            index = nextIndex;
        }
        tour.add(START_NODE + 1);


        ranSolver = true;
    }


    private int tsp(int i, int state, Integer[][] memo, Integer[][] prev) {


        if (state == FINISHED_STATE) return distance[i][START_NODE];


        if (memo[i][state] != null) return memo[i][state];

        int minCost = Integer.MAX_VALUE;
        int index = -1;
        for (int next = 0; next < N; next++) {


            if ((state & (1 << next)) != 0) continue;

            int nextState = state | (1 << next);
            int newCost = distance[i][next] + tsp(next, nextState, memo, prev);
            if (newCost < minCost) {
                minCost = newCost;
                index = next;
            }
        }

        prev[i][state] = index;
        return memo[i][state] = minCost;
    }
}



