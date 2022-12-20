package com.hsb.tsp.utils;

public class NearestNeighbour {
}

/*
private static List<Integer> getInitialApproximation(TSP tsp) {
  // Initialize the list of visited cities with the first city
  List<Integer> visitedCities = new ArrayList<>();
  visitedCities.add(0);

  // Iteratively visit the nearest unvisited city
  while (visitedCities.size() < tsp.numCities) {
    int currentCity = visitedCities.get(visitedCities.size() - 1);
    int nearestCity = -1;
    int minDistance = Integer.MAX_VALUE;
    for (int i = 0; i < tsp.numCities; i++) {
      if (!visitedCities.contains(i) && tsp.distances[currentCity][i] < minDistance) {
        nearestCity = i;
        minDistance = tsp.distances[currentCity][i];
      }
    }
    visitedCities.add(nearestCity);
  }

  return visitedCities;
}

 */