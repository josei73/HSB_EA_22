package com.hsb.tsp.utils;

import java.util.ArrayList;
import java.util.List;

public class GreedyTSP {


    public GreedyTSP(double[][] matrix) {
        this(0, matrix);
    }

    public GreedyTSP(int startNode, double[][] matrix) {
        distance = matrix;
        start = startNode;
    }

    static double[][] distance;
    static List<Integer> visited = new ArrayList<>();
    static double sum = 0;

    private final int start;

    private boolean ranSolver = false;

    public void solve() {

        int count = 0;
        int j = 0, i = 0;
        double min = Double.MAX_VALUE;


        visited.add(start);
        int[] route = new int[distance.length];

        while (i < distance.length && j < distance[i].length) {

            if (count >= distance[i].length - 1) {
                break;
            }

            if (j != i && !(visited.contains(j))) {
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
                j = 0;
                i = route[count] - 1;
                count++;
            }
        }

        i = route[count - 1] - 1;

        for (j = 0; j < distance.length; j++) {

            if ((i != j) && distance[i][j] < min) {
                min = distance[i][j];
                route[count] = j + 1;
            }
        }
        sum += min;

        visited.add(start);
        ranSolver = true;

        // System.out.print(" Cost is : ");
        // System.out.println(sum);
        // System.out.println(visited.toString());


    }

    public List<Integer> getTour() {
        if (!ranSolver) solve();
        System.out.println("greedy");
        List<Integer> solver = new ArrayList<>();


        for (int i = 0; i < visited.size(); i++)
            solver.add(visited.get(i) + 1);


        return solver;
    }

    public double getTourCost() {
        if (!ranSolver) solve();
        System.out.println("greedy");
        return sum;
    }

    public static void main(String[] args) {

        int n = 6;
        double[][] distanceMatrix = new double[n][n];
        for (double[] row : distanceMatrix) java.util.Arrays.fill(row, 10000);
        distanceMatrix[1][4] = distanceMatrix[4][1] = 2;
        distanceMatrix[4][2] = distanceMatrix[2][4] = 4;
        distanceMatrix[2][3] = distanceMatrix[3][2] = 6;
        distanceMatrix[3][0] = distanceMatrix[0][3] = 8;
        distanceMatrix[0][5] = distanceMatrix[5][0] = 10;
        distanceMatrix[5][1] = distanceMatrix[1][5] = 12;

        long startTime = System.currentTimeMillis();

        int startNode = 0;
        GreedyTSP solver = new GreedyTSP(distanceMatrix);
        solver.solve();
        System.out.println(solver.getTour());
        long endTime = System.currentTimeMillis();

        System.out.println("\nRuntime for GreedyTSP   : " + (endTime - startTime) + " milliseconds");
    }
}
