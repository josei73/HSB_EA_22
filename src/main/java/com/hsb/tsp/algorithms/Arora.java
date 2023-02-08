package com.hsb.tsp.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Arora extends Algorithm {
    private static final double EPSILON = 0.01; // approximation factor
    ArrayList<Integer> result = new ArrayList<>();

    private int[][] distances;
    private boolean ranSolver = false;

    public Arora(int[][] distances) {
        this.distances = distances;
    }

    public void solve() {


        int n = distances.length;
        double L = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                L += distances[i][j];
            }
        }
        L = L / (n - 1);


        GreedyTSP greedyTSP = new GreedyTSP(distances);
        List<Integer> greedy = greedyTSP.getTour();
        List<Integer> tour = new ArrayList<>();
        for (int i = 0; i < greedy.size(); i++) {
            tour.add(greedy.get(i) - 1);
        }




        while (true) {
            boolean improved = false;
            for (int i = 1; i < n - 2; i++) {
                for (int j = i + 1; j < n - 1; j++) {
                    double oldDistance = distances[tour.get(i - 1)][tour.get(i)]
                            + distances[tour.get(j)][tour.get(j + 1)];
                    double newDistance = distances[tour.get(i - 1)][tour.get(j)]
                            + distances[tour.get(i)][tour.get(j + 1)];
                    if (newDistance < oldDistance) {
                        tour = getNewTour(n, tour, i, j);
                        improved = true;
                    }
                }
            }


            double currentOptLength = calculateSolutionLength(tour, distances, L);
            if (currentOptLength <= 1 + EPSILON || !improved) {
                List<Integer> optSolution = new ArrayList<>();
                tour.forEach(integer -> {
                    optSolution.add(++integer);

                });
                result = new ArrayList<>(optSolution);
                ranSolver = true;
                return;


            }
        }
    }



    private  List<Integer> getNewTour(int n, List<Integer> tour, int i, int j) {
        List<Integer> newTour = new ArrayList<>();
        for (int k = 0; k < i; k++) {
            newTour.add(tour.get(k));
        }
        for (int k = j; k >= i; k--) {
            newTour.add(tour.get(k));
        }
        for (int k = j + 1; k <= n; k++) {
            newTour.add(tour.get(k));
        }
        return newTour;
    }


    private double calculateSolutionLength(List<Integer> solution, int[][] distances, double lowerBound) {
        double cost = 0.0;
        for (int i = 0; i < solution.size() - 1; i++) {
            cost += distances[solution.get(i)][solution.get(i + 1)];
        }
        return cost / lowerBound;
    }


    @Override
    public ArrayList<Integer> getTour() {
        if (!ranSolver) solve();
        return result;
    }


}
