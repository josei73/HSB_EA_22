package com.hsb.tsp.utils;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;




import java.util.ArrayList;
import java.util.List;

public class TSPWithSimplex {

    private final int[][] distances;
    private final int numCities;

    public TSPWithSimplex(int[][] distances) {
        this.distances = distances;
        this.numCities = distances.length;
    }

    public List<Integer> solve() {
        int[] path = new int[numCities];

        // Define the objective function
        double[] costs = new double[numCities * numCities];
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                costs[i * numCities + j] = distances[i][j];
            }
        }
        LinearObjectiveFunction f = new LinearObjectiveFunction(costs, 0);

        // Define the constraints
        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        for (int i = 0; i < numCities; i++) {
            double[] constraint = new double[numCities * numCities];
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

        List<Integer> sol = new ArrayList<>();
        sol.add(path[0]+1);
        for (int i = 1; i < path.length; i++) {
            sol.add(path[i]+1);
        }
        sol.add(path[0]+1);



        return sol;
    }
}
