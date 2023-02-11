package com.hsb.tsp.algorithms;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;

import java.util.ArrayList;
import java.util.List;


public class TSPBranchAndCut {

    // Number of cities
    private int n;
    // Distance matrix between cities
    private double[][] dist;
    // The number of variables in the LP relaxation
    private int numVariables;
    // The best solution found so far
    private double bestSolution;
    // The best route found so far
    private int[] bestRoute;
    // The constraints for the linear program
    private List<LinearConstraint> constraints;
    // The objective function for the linear program
    private LinearObjectiveFunction objFunction;
    // The simplex solver for the linear program
    private SimplexSolver solver;

    public TSPBranchAndCut(double[][] dist) {
        this.dist = dist;
        this.n = dist.length;
        this.numVariables = n * (n - 1) / 2;
        this.bestSolution = Double.MAX_VALUE;
        this.bestRoute = new int[n];
        this.constraints = new ArrayList<>();
        this.objFunction = new LinearObjectiveFunction(new double[numVariables], 0);
        this.solver = new SimplexSolver();
    }

  /*  public void run() {
        // Set up the constraints for the LP relaxation
        setUpConstraints();
        // Solve the LP relaxation
        PointValuePair solution = solver.optimize(objFunction, constraints, new NonNegativeConstraint(true));
        // Check if the LP solution is better than the best solution found so far
        if (solution.getValue() < bestSolution) {
            // Store the best solution found so far
            bestSolution = solution.getValue();
            // Extract the best route from the LP solution
            extractRoute(solution.getPoint());
            // Branch on the LP solution and solve the subproblems
            branchAndCut();
        }
    }

    private void setUpConstraints() {


        int constraintIndex = 0;
        for (int i = 0; i < n; i++) {
            double[] constraint = new double[numVariables];
            for (int j = i + 1; j < n; j++) {
                constraint[constraintIndex] = 1;
                constraintIndex++;
            }
            for (int j = 0; j < constraint.length; j++) {
                System.out.print(constraint[j]+" ");
            }
            System.out.print(" index j "+i);
            System.out.println();
            constraints.add(new LinearConstraint(constraint, Relationship.EQ, 1));
        }



        for (int j = 0; j < n; j++) {
            double[] constraint = new double[numVariables];
            constraintIndex = 0;
            for (int i = 0; i < n; i++) {
                if (i == j) {
                    continue;
                }
                constraint[constraintIndex] = 1;
                constraintIndex++;
            }
            constraints.add(new LinearConstraint(constraint, Relationship.EQ, 1));
        }

        // Constraint 1: each city can be visited only once
        for (int i = 0; i < n; i++) {
            double[] constraint = new double[numVariables];
            int index = 0;
            for (int j = 0; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (j == i || k == i) {
                        constraint[index++] = 1;
                    } else {
                        constraint[index++] = 0;
                    }
                }
            }
          //  constraints.add(new LinearConstraint(constr));
        }
    }

   */
}