package com.hsb.tsp.utils;

import java.util.ArrayList;
import java.util.List;

public class GreedyTSP extends Algorithm {


    public GreedyTSP(double[][] matrix) {
        this(0, matrix);
    }

    public GreedyTSP(int startNode, double[][] matrix) {
        distance = matrix;
        start = startNode;
    }


    private double[][] distance;
    private List<Integer> visited = new ArrayList<>();
    private double sum = 0;

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
    
}
