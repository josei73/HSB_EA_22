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


        int[] path = new int[numCities];
        // Define the objective function
        int index = 0;
        double[] costs = new double[numCities * numCities];
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                costs[index++] = distances[i][j];
            }
        }

        LinearObjectiveFunction f = new LinearObjectiveFunction(costs, 0);


        // Define the constraints

        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        for (int i = 0; i < numCities; i++) {
            double[] constraint = new double[numCities * numCities];
            index = 0;
            for (int j = 0; j < numCities; j++) {
                constraint[i * numCities + j] = 1;
            }
            constraints.add(new LinearConstraint(constraint, Relationship.EQ, 1));
        }
        for (int j = 0; j < numCities; j++) {
            double[] constraint = new double[numCities * numCities];
            for (int i = 0; i < numCities; i++) {
                constraint[i * numCities + j] = 1;
            }
            constraints.add(new LinearConstraint(constraint, Relationship.EQ, 1));
        }

        //  constraints.addAll(getSubtourEliminationConstraints());


        SimplexSolver solver = new SimplexSolver();
        PointValuePair optSolution = solver.optimize(f, new
                        LinearConstraintSet(constraints),
                GoalType.MINIMIZE, new
                        NonNegativeConstraint(true));


        // Extract the solution
        double[] values = optSolution.getPoint();
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (values[i * numCities + j] > 0.5) {
                    path[i] = j;
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




