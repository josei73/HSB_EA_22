package com.hsb.tsp.algorithms;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
import java.util.List;


public class Simplex extends Algorithm {

    private final int[][] distances;

    private ArrayList<Integer> sol;
    private final int numCities;
    // Number of constraints


    public Simplex(int[][] distances) {
        this.distances = distances;
        this.numCities = distances.length;
        sol = new ArrayList<>();

    }


    @Override
    public ArrayList<Integer> getTour() {
        return sol;
    }

    public void solve() {


        int numVariables = (numCities * (numCities - 1));
        int[] path = new int[numCities];

        // Define the objective function
        int index = 0;
        double[] costs = new double[numVariables];
        for (int i = 0; i < distances.length; i++) {
            for (int j = 0; j < distances[i].length; j++) {
                if (i != j)
                    costs[index++] = distances[i][j];
            }
        }

        LinearObjectiveFunction f = new LinearObjectiveFunction(costs, 0);

        // Define the constraints
        List<LinearConstraint> constraints = new ArrayList<>();
        for (int i = 0; i < numCities; i++) {
            double[] constraint = new double[numVariables];
            for (int j = i * (numCities - 1); j < (i + 1) * (numCities - 1); j++) {
                constraint[j] = 1;

            }
            constraints.add(new LinearConstraint(constraint, Relationship.EQ, 1));
        }


        for (int j = 0; j < numCities; j++) {
            double[] constraint = new double[numVariables];

            for (int i = 0; i < numCities; i++) {
                if (i != j)
                    if (i < j) {
                        constraint[i * (numCities - 1) + (j - 1)] = 1;
                    } else {
                        constraint[i * (numCities - 1) + j] = 1;
                    }
            }
            constraints.add(new LinearConstraint(constraint, Relationship.EQ, 1));
        }




        System.out.println(" Simplex Anfang ");
        SimplexSolver solver = new SimplexSolver();
        PointValuePair optSolution = solver.optimize(f, new
                        LinearConstraintSet(constraints),
                GoalType.MINIMIZE, new
                        NonNegativeConstraint(true));


        // Extract the solution
        double[] values = optSolution.getPoint();

        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i != j)
                    if (i < j) {
                        if (values[i * (numCities - 1) + (j - 1)] > 0.5) {
                            path[i] = j;
                        }
                    } else {
                        if (values[i * (numCities - 1) + j] > 0.5) {
                            path[i] = j;
                        }
                    }
            }
        }

        sol.add(path[0] + 1);
        for (int i = 1; i < path.length; i++) {
            sol.add(path[i] + 1);
        }
        sol.add(path[0] + 1);


    }

}




