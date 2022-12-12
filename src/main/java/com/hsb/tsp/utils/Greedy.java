package com.hsb.tsp.utils;

import java.util.ArrayList;
import java.util.List;

public class Greedy {


    public Greedy(double[][] matrix) {
        adjMatrix = matrix;
        edges = generateEdges(matrix);
    }

    static ArrayList<Edge> edges = new ArrayList<Edge>();
    static ArrayList<Edge> tourEdges = new ArrayList<Edge>();
    static double[][] adjMatrix;

    static double greedyDistance = 0.0;

    public List<Integer> solve() {
        // Begin Greedy TSP
        ArrayList<Integer> visited = new ArrayList<Integer>();
        visited = greedyTSP(edges, visited, 0);
        visited.add(0);

        // Create greedy graph based on results of TSP
        double[][] greedyMatrix = generateGreedyMatrix(visited, adjMatrix);

        // Gather edges that are used for tour and add up distance
        for (int i = 0; i < edges.size(); i++) {
            for (int j = 0; j < visited.size() - 1; j++) {
                if (((visited.get(j) == edges.get(i).getNode1()) &&
                        (visited.get(j + 1) == edges.get(i).getNode2()))) {
                    if (edges.get(i).getNode2() != edges.get(i).getNode1()) {
                        tourEdges.add(edges.get(i));
                        greedyDistance += edges.get(i).getDist();
                    }
                }
            }
        }
        for (int i = 0; i < greedyMatrix.length; i++) {
            System.out.print("\n\n" + i + "   ");
            for (int j = 0; j < greedyMatrix[i].length; j++) {
                System.out.print(greedyMatrix[i][j]+" ");
            }
            System.out.println();
        }

        List<Integer> solver = new ArrayList<>();


        for (int i = 0; i < visited.size(); i++)
            solver.add(visited.get(i) + 1);


        return solver;
    }

    public static double[][] generateGreedyMatrix(ArrayList<Integer> visited, double[][] oldMatrix) {
        int n = visited.size() - 1;
        double[][] newMatrix = new double[n][n];
        // row, column coordinates
        int r, c = 0;

        // add to the greedy matrix if and only if an edge is in the greedy path
        for (int i = 0; i < visited.size() - 1; i++) {
            r = visited.get(i);
            c = visited.get(i + 1);
            newMatrix[r][c] = oldMatrix[r][c];
            newMatrix[c][r] = oldMatrix[c][r];
        }

        return newMatrix;
    }

    public static ArrayList<Integer> greedyTSP(ArrayList<Edge> edges, ArrayList<Integer> visited, int current) {

        visited.add(current);

        for (int i = 0; i < edges.size(); i++) {
            if ((edges.get(i).getNode1() == current) &&
                    (visited.contains(edges.get(i).getNode2()) == false)) {
                visited = greedyTSP(edges, visited, edges.get(i).getNode2());
                break;
            } else if ((edges.get(i).getNode2() == current) &&
                    (visited.contains(edges.get(i).getNode1()) == false)) {
                visited = greedyTSP(edges, visited, edges.get(i).getNode1());
                break;
            }
        }

        return visited;
    }

    public static ArrayList<Edge> generateEdges(double[][] matrix) {
        ArrayList<Edge> edges = new ArrayList<Edge>();

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                edges.add(new Edge(i, j, matrix[i][j]));
            }
        }

        return edges;
    }

    public static void main(String[] args) {

        int n = 6;
        double[][] distanceMatrix = new double[n][n];
        for (double[] row : distanceMatrix) java.util.Arrays.fill(row, 10000);
        distanceMatrix[5][0] = 10;
        distanceMatrix[1][5] = 12;
        distanceMatrix[4][1] = 2;
        distanceMatrix[2][4] = 4;
        distanceMatrix[3][2] = 6;
        distanceMatrix[0][3] = 8;

        int startNode = 0;
        Greedy solver =
                new Greedy(distanceMatrix);
        List<Integer> solve = solver.solve();
        System.out.println(solve);

    }


}
