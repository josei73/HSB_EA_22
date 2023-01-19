package com.hsb.tsp.algorithms;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Branch And Bound
public class BAndB extends Algorithm {


    public BAndB(int[][] matrix) {
        distance = matrix;
        N = matrix.length;
        final_path = new int[N + 1];
        visited = new boolean[N];
    }

    static int N;
    static int[][] distance;
    static int final_path[];

    static boolean visited[];

    static int final_res = Integer.MAX_VALUE;

    private boolean ranSolver = false;

    static void copyToFinal(int curr_path[]) {
        for (int i = 0; i < N; i++)
            final_path[i] = curr_path[i];
        final_path[N] = curr_path[0];
    }

    static int firstMin(int adj[][], int i) {
        int min = Integer.MAX_VALUE;
        for (int k = 0; k < N; k++)
            if (adj[i][k] < min && i != k)
                min = adj[i][k];
        return min;
    }

    static int secondMin(int adj[][], int i) {
        int first = Integer.MAX_VALUE, second = Integer.MAX_VALUE;
        for (int j = 0; j < N; j++) {
            if (i == j)
                continue;

            if (adj[i][j] <= first) {
                second = first;
                first = adj[i][j];
            } else if (adj[i][j] <= second &&
                    adj[i][j] != first)
                second = adj[i][j];
        }
        return second;
    }

    static void TSPRec(int adj[][], int curr_bound, int curr_weight,
                       int level, int curr_path[]) {
        if (level == N) {
            if (adj[curr_path[level - 1]][curr_path[0]] != 0) {
                int curr_res = curr_weight +
                        adj[curr_path[level - 1]][curr_path[0]];
                if (curr_res < final_res) {
                    copyToFinal(curr_path);
                    final_res = curr_res;
                }
            }
            return;
        }
        for (int i = 0; i < N; i++) {
            if (adj[curr_path[level - 1]][i] != 0 &&
                    visited[i] == false) {
                int temp = curr_bound;
                curr_weight += adj[curr_path[level - 1]][i];

                if (level == 1)
                    curr_bound -= ((firstMin(adj, curr_path[level - 1]) +
                            firstMin(adj, i)) / 2);
                else
                    curr_bound -= ((secondMin(adj, curr_path[level - 1]) +
                            firstMin(adj, i)) / 2);

                if (curr_bound + curr_weight < final_res) {
                    curr_path[level] = i;
                    visited[i] = true;

                    TSPRec(adj, curr_bound, curr_weight, level + 1,
                            curr_path);
                }

                curr_weight -= adj[curr_path[level - 1]][i];
                curr_bound = temp;

                Arrays.fill(visited, false);
                for (int j = 0; j <= level - 1; j++)
                    visited[curr_path[j]] = true;
            }
        }
    }

    public void solve() {
        int curr_path[] = new int[N + 1];

        int curr_bound = 0;
        Arrays.fill(curr_path, -1);
        Arrays.fill(visited, false);


        for (int i = 0; i < N; i++)
            curr_bound += (firstMin(distance, i) +
                    secondMin(distance, i));

        curr_bound = (curr_bound == 1) ? curr_bound / 2 + 1 :
                curr_bound / 2;

        visited[0] = true;
        curr_path[0] = 0;

        TSPRec(distance, curr_bound, 0, 1, curr_path);
        ranSolver = true;
    }

    public ArrayList<Integer> getTour() {
        if (!ranSolver) solve();
        ArrayList<Integer> solver = IntStream.of(final_path)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        return solver;
    }

    public double getTourCost() {
        if (!ranSolver) solve();

        return final_res;
    }

     /*   public static void main(String[] args)
        {
            //Adjacency matrix for the given graph
            int adj[][] = {{0, 10, 15, 20},
                    {10, 0, 35, 25},
                    {15, 35, 0, 30},
                    {20, 25, 30, 0} };

            BAndB B = new BAndB(adj);
            B.solve();
            System.out.printf("Minimum cost : %d\n", final_res);
            System.out.printf("Path Taken : ");
            for (int i = 0; i <= N; i++)
            {
                System.out.printf("%d ", final_path[i]);
            }
        }*/
}

