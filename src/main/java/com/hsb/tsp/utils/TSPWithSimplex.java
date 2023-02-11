package com.hsb.tsp.utils;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;


import java.util.ArrayList;
import java.util.List;

public class TSPWithSimplex {

    private final int[][] distances;
    private final int numCities;
    private double[] x;
    private double[] y;
    private double[] upperBound;

    public TSPWithSimplex(int[][] distances) {
        this.distances = distances;
        this.numCities = distances.length;
        upperBound = new double[numCities * numCities];
        this.x = new double[numCities];
        this.y = new double[numCities];
    }

    public List<Integer> solve() {
        int numVariables = (numCities * (numCities - 1)) / 2;
        // numVariables = numCities*numCities;
        int[] path = new int[numCities];

        // Define the objective function
        int index = 0;
        double[] costs = new double[numVariables];
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                costs[index++] = distances[i][j];
            }
        }

        LinearObjectiveFunction f = new LinearObjectiveFunction(costs, 0);


        // Define the constraints

        List<LinearConstraint> constraints = new ArrayList<LinearConstraint>();


       /* for (int i = 0; i < numCities; i++) {
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

        */


        int constraintIndex = 0;
        for (int i = 0; i < numCities; i++) {
            double[] constraint = new double[numVariables];
            for (int j = i + 1; j < numCities; j++) {
                constraint[constraintIndex] = 1;
                constraintIndex++;
            }

            constraints.add(new LinearConstraint(constraint, Relationship.EQ, 1));
        }


        for (int j = 0; j < numCities; j++) {
            double[] constraint = new double[numVariables];
            constraintIndex = 0;
            for (int i = 0; i < numCities; i++) {
                if (i == j) {
                    continue;
                }
                constraint[constraintIndex] = 1;
                constraintIndex++;
            }
            constraints.add(new LinearConstraint(constraint, Relationship.EQ, 1));
        }


        //  constraints.addAll(getSubtourEliminationConstraints());


        System.out.println(" Simplex Anfang ");
        SimplexSolver solver = new SimplexSolver();
        PointValuePair optSolution = solver.optimize(f, new
                        LinearConstraintSet(constraints),
                GoalType.MINIMIZE, new
                        NonNegativeConstraint(true));


        // Extract the solution
        double[] values = optSolution.getPoint();
        for (int i = 0; i < values.length; i++) {
            System.out.println(" Values " + values[i]);
        }
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (values[i * numCities + j] > 0.5) {
                    path[i] = j;
                }
            }
        }


      /*  for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                if (values[index] == 1) {
                    System.out.println("Take edge " + i + "-" + j);
                }
                index++;
            }
        }

       */
        // add the integer constraint

        double[] point = optSolution.getPoint();
        double bestTour = optSolution.getValue();
        for (int i = 0; i < numCities; i++) {
            upperBound[i] = Math.ceil(point[i]);
        }
        int k = 0;
        while (k < numCities) {
            for (int i = 0; i < numCities; i++) {
                costs[i] = -Math.floor(point[i]);
                RealVector coefficients = constraints.get(i).getCoefficients();
                double[] doubles = coefficients.toArray();
                doubles[i] = upperBound[i];
                System.out.println(doubles.length + " Läe " + upperBound.length);
                constraints.set(i, new LinearConstraint(doubles, Relationship.EQ, 1));
            }
            System.out.println(" While");
            optSolution = solver.optimize(f, new LinearConstraintSet(constraints), GoalType.MINIMIZE, new SimpleBounds(point, upperBound));
            point = optSolution.getPoint();
            for (int i = 0; i < point.length; i++) {
                System.out.println(" JJJ P " + point[i]);
            }
            for (int i = 0; i < numCities; i++) {
                if (point[i] > 0 && point[i] < 1) {
                    upperBound[i] = Math.ceil(point[i]);
                    k = 0;
                    break;
                } else {
                    k++;
                }
            }
        }

        for (int i = 0; i < point.length; i++) {
            System.out.println(point[i] + " Point");
        }

        // print the solution

        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (point[i * numCities + j] > 0.5) {
                    path[i] = j;

                }
            }
        }
        ArrayList<Integer> sol = new ArrayList<>();
        sol.add(path[0] + 1);
        for (int i = 1; i < path.length; i++) {
            sol.add(path[i] + 1);
        }
        sol.add(path[0] + 1);


        return sol;


    }


    private List<LinearConstraint> getSubtourEliminationConstraints() {
        List<LinearConstraint> constraints = new ArrayList<>();

        for (int s = 1; s < numCities; s++) {
            for (int t = s + 1; t < numCities; t++) {
                double[] coefficients = new double[numCities * numCities];
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
