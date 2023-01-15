package com.hsb.tsp.algorithms;

import java.util.ArrayList;


public class GreedyTSP extends Algorithm {


    public GreedyTSP(int[][] matrix) {
        this(0, matrix);
    }

    public GreedyTSP(int startNode, int[][] matrix) {
        super("Greedy");
        distance = matrix;
        start = startNode;
        visitedNodes = new boolean[matrix.length];
    }

    private int[][] distance;
    private ArrayList<Integer> visited = new ArrayList<>();
    private boolean[] visitedNodes;
    private double sum = 0;

    private final int start;

    private boolean ranSolver = false;

    @Override
    public void solve() {

        int count = 0;
        int j = 0, i = 0;
        double min = Double.MAX_VALUE;


        visited.add(start);
        int[] route = new int[distance.length];
        visitedNodes[0]=true;

        while (i < distance.length && j < distance[i].length) {

            if (count >= distance[i].length - 1) {
                break;
            }

            if (j != i && !(visitedNodes[j])) {
                if (distance[i][j] < min) {
                    min = distance[i][j];
                    route[count] = j + 1;
                }
            }
            j++;

            if (j == distance[i].length) {
                sum += min;
                min = Integer.MAX_VALUE;
                visited.add(route[count] - 1);
                visitedNodes[route[count]-1]=true;

                j = 0;
                i = route[count] - 1;
                count++;
            }
        }

        i = route[count - 1] - 1;

        sum += min;

        visited.add(start);
        ranSolver = true;


    }

    public ArrayList<Integer> getTour() {
        if (!ranSolver) solve();
        ArrayList<Integer> solver = new ArrayList<>();
        sum=0;
        for (int i = 0; i < visited.size()-1; i++)
            sum+=distance[visited.get(i)][visited.get(i+1)];


        for (int i = 0; i < visited.size(); i++)
            solver.add(visited.get(i) + 1);


        return solver;
    }

    public double getTourCost() {
        if (!ranSolver) solve();

        return sum;
    }

}
