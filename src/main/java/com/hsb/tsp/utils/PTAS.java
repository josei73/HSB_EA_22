package com.hsb.tsp.utils;


import java.util.ArrayList;
import java.util.List;
/*
public class PTAS {
    private static final double EPSILON = 0.1; // tolerance

    public static List<Integer> solveTSP(double[][] distances) {
        int n = distances.length;
        double L = 0; // lower bound on the optimal solution
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                L += distances[i][j];
            }
        }
        L = L / (n - 1); // average distance between points

        // construct a sequence of increasingly fine grids
        List<Integer> tour = new ArrayList<>();
        for (double k = 1; ; k *= 1 + EPSILON) {
            tour = findTour(distances, L / k);
            if (tour != null) {
                break;
            }
        }

        return tour;
    }

    private static List<Integer> findTour(double[][] distances, double L) {
        // ... implement the algorithm to find a tour using the grid with
        // edge lengths L, and return the tour or null if no tour was found

        return null;
    }
}


  import java.util.ArrayList;
  import java.util.List;

  public class PTAS {
      private static final double EPSILON = 0.1;  // approximation factor

      public static List<Integer> tsp(int[][] distances) {
          // Initialize the solution with a nearest neighbor tour
          GreedyTSP greedyTSP = new GreedyTSP(distances);

          List<Integer> solution = greedyTSP.getTour();

          // Iteratively improve the solution using 2-opt
          boolean improved = true;
         while (improved) {
              improved = false;
              for (int i = 0; i < solution.size() - 1; i++) {
                  for (int j = i + 2; j < solution.size() - 1; j++) {
                      double oldDistance = distances[solution.get(i)][solution.get(i + 1)]
                                          + distances[solution.get(j)][solution.get(j + 1)];
                      double newDistance = distances[solution.get(i)][solution.get(j)]
                                          + distances[solution.get(i + 1)][solution.get(j + 1)];
                      if (newDistance < oldDistance) {
                          // Reverse the order of the cities between i and j
                          List<Integer> sublist = solution.subList(i + 1, j + 1);
                          sublist = reverse(sublist);
                          solution = merge(solution, sublist, i + 1);
                          improved = true;
                      }
                  }
              }
          }

          return solution;
      }




     */

public class PTAS {
    private static final double EPSILON = 0.1; // approximation factor

    public List<Integer> solveTSP(int[][] distances) {
        // Initialize solution with a greedy algorithm


        GreedyTSP greedyTSP = new GreedyTSP(distances);

        List<Integer> greedy = greedyTSP.getTour();
        List<Integer> solution = new ArrayList<>();
        System.out.println(greedy);
        greedy.forEach(integer -> {
            solution.add(--integer);
        });
        System.out.println("After");
        System.out.println(solution);

        // Iteratively improve solution
        while (true) {
            double improvement = 0;
            for (int i = 0; i < solution.size()-1; i++) {
                for (int j = i + 2; j < solution.size()-1; j++) {
                    // Calculate improvement by reversing sub-tour
                    double oldDistance = distances[solution.get(i)][solution.get(i + 1)]
                            + distances[solution.get(j)][solution.get(j + 1)];
                    double newDistance = distances[solution.get(i)][solution.get(j)]
                            + distances[solution.get(i + 1)][solution.get(j + 1)];
                    double tempImprovement = oldDistance - newDistance;

                    // If improvement is greater than EPSILON, reverse sub-tour
                    if (tempImprovement > EPSILON) {
                        improvement += tempImprovement;
                        reverse(solution, i + 1, j - 1);
                    }
                }
            }
            // If no improvement is made, return solution
            if (improvement <= 1 + EPSILON) {
                return solution;
            }
        }
    }


    private void reverse(List<Integer> solution, int i, int j) {
        // Reverse the order of a sub-tour
        for (int k = 0; k <= (j - i) / 2; k++) {
            int temp = solution.get(i + k);
            solution.set(i + k, solution.get(j - k));
            solution.set(j - k, temp);
        }
    }
}




/*
function ArorasAlgorithm(TSP tsp, double epsilon):
  // Get an initial approximation of the solution using a greedy algorithm
  solution = getInitialApproximation(tsp)

  // Iteratively refine the solution until it is within the desired approximation factor
  while true:
    // Make small adjustments to the solution and re-optimize using a local search algorithm
    solution = optimizeSolution(solution)

    // Calculate the approximation factor
    approxFactor = calculateApproximationFactor(solution, tsp)

    // If the approximation factor is within the desired range, return the solution
    if approxFactor <= 1 + epsilon:
      return solution

function getInitialApproximation(TSP tsp):
  // Initialize the list of visited cities with the first city
  visitedCities = [0]

  // Iteratively visit the nearest unvisited city
  while len(visitedCities) < tsp.numCities:
    currentCity = visitedCities[-1]
    nearestCity = -1
    minDistance = INF
    for i = 0 to tsp.numCities - 1:
      if i not in visitedCities and tsp.distances[currentCity][i] < minDistance:
        nearestCity = i
        minDistance = tsp.distances[currentCity][i]
    visitedCities.append(nearestCity)
  return visitedCities

function optimizeSolution(solution):
  // Implement the local search algorithm here (e.g. 2-opt)
  ...

function calculateApproximationFactor(solution, TSP tsp):
  // Calculate the length of the solution
  solutionLength = calculateSolutionLength(solution, tsp)

  // Calculate the length of the optimal solution
  optimalLength = calculateOptimalLength(tsp)

  // Return the approximation factor
  return solutionLength / optimalLength

 */


/**
 * import java.util.ArrayList;
 * import java.util.List;
 * <p>
 * class TSP {
 * int[][] distances;
 * int numCities;
 * <p>
 * public TSP(int[][] distances) {
 * this.distances = distances;
 * this.numCities = distances.length;
 * }
 * }
 * <p>
 * public class ArorasAlgorithm {
 * public static List<Integer> solve(TSP tsp, double epsilon) {
 * // Get an initial approximation of the solution using a greedy algorithm
 * List<Integer> currentSolution = getInitialApproximation(tsp);
 * <p>
 * // Iteratively refine the solution until it is within the desired approximation factor
 * while (true) {
 * // Make small adjustments to the solution and re-optimize using a local search algorithm
 * currentSolution = optimizeSolution(currentSolution);
 * <p>
 * // Calculate the approximation factor
 * double approxFactor = calculateApproximationFactor(currentSolution, tsp);
 * <p>
 * // If the approximation factor is within the desired range, return the solution
 * if (approxFactor <= 1 + epsilon) {
 * return currentSolution;
 * }
 * }
 * }
 * <p>
 * private static List<Integer> getInitialApproximation(TSP tsp) {
 * // Initialize the list of visited cities with the first city
 * List<Integer> visitedCities = new ArrayList<>();
 * visitedCities.add(0);
 * <p>
 * // Iteratively visit the nearest unvisited city
 * while (visitedCities.size() < tsp.numCities) {
 * int currentCity = visitedCities.get(visitedCities.size() - 1);
 * int nearestCity = -1;
 * int minDistance = Integer.MAX_VALUE;
 * for (int i = 0; i < tsp.numCities; i++) {
 * if (!visitedCities.contains(i) && tsp.distances[currentCity][i] < minDistance) {
 * nearestCity = i;
 * minDistance = tsp.distances[currentCity][i];
 * }
 * }
 * visitedCities.add(nearestCity);
 * }
 * <p>
 * return visitedCities;
 * }
 * <p>
 * private static List<Integer> optimizeSolution(List<Integer> solution) {
 * // Implement the local search algorithm here (e.g. 2-opt)
 * ...
 * }
 * <p>
 * private static double calculateApproximationFactor(List<Integer> solution, TSP tsp) {
 * // Calculate the length of the solution
 * int solutionLength = calculateSolutionLength(solution, tsp);
 * <p>
 * // Calculate the length of the optimal solution
 * int optimalLength = calculateOptimalLength(tsp);
 * <p>
 * // Return the approximation factor
 * return (double) solutionLength / optimalLength;
 * }
 * }
 */