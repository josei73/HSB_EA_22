package com.hsb.tsp.algorithms;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbour extends Algorithm {

    private int size;

    private int distances[][];

    private boolean[] visited;

    private List<Integer> visitedCities;


    public NearestNeighbour(int[][] distances) {
        this.distances = distances;
        size = distances.length;
        visited = new boolean[size];
        visitedCities = new ArrayList<>();
    }


    @Override
    public ArrayList<Integer> getTour() {
        ArrayList<Integer> tour = new ArrayList<>();
        for (int i = 0; i < visitedCities.size(); i++) {
            tour.add(visitedCities.get(i) + 1);
        }

        return tour;

    }

    public void solve() {
        // Initialize the list of visited cities with the first city

        for (int i = 0; i < size; i++) {
            visited[i] = false;
        }
        int startNode = 0;
        visited[startNode] = true;
        visitedCities.add(startNode);

        int nodeIndex = startNode;
        for (int i = 0; i < size - 1; i++) {
            double minDistance = Double.MAX_VALUE;
            int nearestNeighborIndex = -1;
            for (int j = 0; j < size; j++) {
                if (!visited[j]) {
                    double distance = distances[nodeIndex][j];
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestNeighborIndex = j;
                    }
                }
            }
            nodeIndex = nearestNeighborIndex;
            visited[nodeIndex] = true;
            visitedCities.add(nodeIndex);
        }
        visitedCities.add(startNode);


    }

}