package com.hsb.tsp.utils;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;


import java.util.ArrayList;
import java.util.List;

public class TSPWithSimplex {

    private  int[][] distances;
    private  int numCities;
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

    public  TSPWithSimplex (){

    }

    public List<Integer> solve() {
        int numVariables = (numCities * (numCities - 1)) ;
       //  numVariables = numCities*numCities;
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



        List<LinearConstraint> constraints = new ArrayList<>();

        for (int i = 0; i < numCities; i++) {
            double[] constraint = new double[numVariables];
            for (int j = i * (numCities-1); j < (i+1) * (numCities -1); j++) {
                constraint[j] = 1;

            }
            constraints.add(new LinearConstraint(constraint, Relationship.EQ, 1));
        }


        for(int j = 0; j < numCities; j++) {
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
        System.out.println( "=================================================== part 21");


        constraints.forEach(linearConstraint ->{
            double[] doubles = linearConstraint.getCoefficients().toArray();
            for (int i = 0; i < doubles.length; i++) {
                System.out.print(doubles[i]+" ");
            }
            System.out.println();
        } );





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
                if(i * numCities + j < values.length)
                if (values[i * numCities + j] > 0.5) {
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





  /*  private List<LinearConstraint> getSubtourEliminationConstraints() {
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

   */

   /* private List<LinearConstraint> getSubtourEliminationConstraints() {
        List<LinearConstraint> subtourConstraints = new ArrayList<>();
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                if (i == j) {
                    continue;
                }
                for (int k = 0; k < numCities; k++) {
                    if (i == k || j == k) {
                        continue;
                    }
                    double[] constraint = new double[numCities * numCities];
                    int constraintIndex = 0;
                    for (int a = 0; a < numCities; a++) {
                        for (int b = 0; b < numCities; b++) {
                            if (a == i && b == j) {
                                constraint[constraintIndex] = 1;
                            } else if (a == j && b == k) {
                                constraint[constraintIndex] = -1;
                            }
                            constraintIndex++;
                        }
                    }
                    subtourConstraints.add(new LinearConstraint(constraint, Relationship.LEQ, 0));
                }
            }
        }
        return subtourConstraints;
    }

    */






}
