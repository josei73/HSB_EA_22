package com.hsb.tsp.algorithms;

import java.util.ArrayList;
import java.util.List;

public class NearestNeighbour extends Algorithm {

    private int size;

    private int distances [][];

    private boolean [] visited ;

    private List<Integer> visitedCities;

    public NearestNeighbour(int [][] distances){
        this.distances=distances;
        size=distances.length;
        visited = new boolean[size];
        visitedCities= new ArrayList<>();
    }


    @Override
    public ArrayList<Integer> getTour() {
        ArrayList<Integer> tour = new ArrayList<>();
        for (int i = 0; i < visitedCities.size(); i++) {
            tour.add(visitedCities.get(i)+1);
        }

        return tour;

    }

    public void solve() {
        // Initialize the list of visited cities with the first city

        visitedCities.add(0);
        visited[0]=true;

        // Iteratively visit the nearest unvisited city
        while (visitedCities.size() < size) {
            int nearestCity = -1;
            for (int i = 0; i < size; i++) {
                if (!visited[i]) {
                    nearestCity = i;
                }
            }
            visitedCities.add(nearestCity);
            visited[nearestCity]=true;
        }
        visitedCities.add(0);


    }

}