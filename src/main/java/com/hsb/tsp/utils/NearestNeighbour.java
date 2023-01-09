package com.hsb.tsp.utils;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbour {

    private int size;

    private int distances [][];

    private boolean [] visited ;

    public NearestNeighbour(int [][] distances){
        this.distances=distances;
        size=distances.length;
        visited = new boolean[size];
    }



    public List<Integer> solve() {
        // Initialize the list of visited cities with the first city
        List<Integer> visitedCities = new ArrayList<>();
        visitedCities.add(0);
        visited[0]=true;

        // Iteratively visit the nearest unvisited city
        while (visitedCities.size() < size) {
            int currentCity = visitedCities.get(visitedCities.size() - 1);
            int nearestCity = -1;
            int minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < size; i++) {
                if (!visited[i] && distances[currentCity][i] < minDistance) {
                    nearestCity = i;
                    minDistance = distances[currentCity][i];

                }
            }
            visitedCities.add(nearestCity);
            visited[nearestCity]=true;
        }
        visitedCities.add(0);

        List<Integer> tour = new ArrayList<>();
        for (int i = 0; i < visitedCities.size(); i++) {
            tour.add(visitedCities.get(i)+1);
        }

        return tour;
    }

}