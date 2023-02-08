package com.hsb.tsp.utils;

import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;

import java.util.*;

import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
import java.util.List;

public class TspDfjFormulation {

    private int numCities;
    private double[][] distances;

    public TspDfjFormulation(int numCities, double[][] distances) {
        this.numCities = numCities;
        this.distances = distances;
    }

    public void solve() {
        // Define the number of variables and their lower and upper bounds
        int numVariables = numCities * (numCities - 1) / 2;


        // Define the objective function
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(getObjectiveCoefficients(), 0);

        // Define the constraints
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.addAll(getFlowConservationConstraints());
        constraints.addAll(getSubtourEliminationConstraints());

        // Solve the problem using the simplex solver
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.optimize(new MaxIter(100), objectiveFunction, new LinearConstraintSet(constraints), GoalType.MINIMIZE, new NonNegativeConstraint(true));

        // Extract the solution
        double[] values = solution.getPoint();
        int index = 0;
        System.out.println("Ausgabe ");
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                if (values[index] == 1) {
                    System.out.println("Take edge " + i + "-" + j);
                }
                index++;
            }
        }
    }

    private double[] getObjectiveCoefficients() {
        double[] coefficients = new double[numCities * (numCities - 1) / 2];
        int index = 0;
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                coefficients[index] = distances[i][j];
                index++;
            }
        }
        return coefficients;
    }

    private List<LinearConstraint> getFlowConservationConstraints() {
        List<LinearConstraint> constraints = new ArrayList<>();

        for (int k = 0; k < numCities; k++) {
            double[] coefficients = new double[numCities * (numCities - 1) / 2];
            int index = 0;
            for (int i = 0; i < numCities; i++) {
                for (int j = i + 1; j < numCities; j++) {
                    if (i == k) {
                        coefficients[index] = 1;
                    } else if (j == k) {
                        coefficients[index] = -1;
                    } else {
                        coefficients[index] = 0;
                    }
                    index++;
                }
            }
            constraints.add(new LinearConstraint(coefficients, Relationship.EQ, 0));
        }

        return constraints;
    }

    private List<LinearConstraint> getSubtourEliminationConstraints() {
        List<LinearConstraint> constraints = new ArrayList<>();

        for (int s = 1; s < numCities; s++) {
            for (int t = s + 1; t < numCities; t++) {
                double[] coefficients = new double[numCities * (numCities - 1) / 2];
                int index = 0;
                for (int i = 0; i < numCities; i++) {
                    for (int j = i + 1; j < numCities; j++) {
                        if ((i == s && j == t) || (i == t && j == s)) {
                            coefficients[index] = 1;
                        } else {
                            coefficients[index] = 0;
                        }
                        index++;
                    }
                }
                constraints.add(new LinearConstraint(coefficients, Relationship.LEQ, numCities - 2));
            }
        }

        return constraints;
    }


}
