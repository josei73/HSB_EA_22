package com.hsb.tsp.utils;


import com.hsb.tsp.algorithms.*;
import com.hsb.tsp.algorithms.Simplex;
import com.hsb.tsp.exception.HeldKarpException;
import com.hsb.tsp.model.TSPInstance;
import com.hsb.tsp.model.TSPTour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class Main {


    public static void main(String[] args) throws HeldKarpException {
        TSPParser parser = new TSPParser();
        TSPInstance problem;
        String filename = "ulysses16.tsp";

        try {
            problem = parser.loadInstance(filename);
            System.out.println("load " + filename + " to test algorithms:");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int[][] matrix = problem.getDistanceSection().getAdjMatrix(problem);

        double[][] simplex = copyToDouble(matrix);



        int n = matrix.length;
        int m = (n * (n - 1)) / 2;
        double[][] A = new double[n][m]; // coefficient matrix for the constraints


        for (int k = 0; k < n; k++) {
            double[] coefficients = new double[m];
          int  index = 0;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
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
            A[k]=coefficients;
          //  constraints.add(new LinearConstraint(coefficients, Relationship.EQ, 2));
        }



        double[] b = new double[n]; // right-hand side vector for the constraints
        double[] c = new double[m]; // coefficient vector for the objective function
        char[] sense = new char[n]; // sense of the constraints (e.g. '=' for equality constraints)
        System.out.println(A.length);
        System.out.println(A[0].length);
        System.out.println(c.length);
        System.out.println(b.length);
        for (int i = 0; i < n; i++) {
            b[i] = 1;
            sense[i] = '=';
        }
        int z = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                c[z++] = simplex[i][j]; // cost of traveling from city i to city j

            }
        }

       // TspDfjFormulation formulation = new TspDfjFormulation(matrix.length,simplex);
       // formulation.solve();
        TSPWithSimplex tsp = new TSPWithSimplex(matrix);

        List<Integer> sol = tsp.solve();

        TSPTour tspTour = new TSPTour();
        tspTour.setNodes(sol);
        System.out.println(sol);
        System.out.println(tspTour.distance(problem));





          //generateTSPConstraints(n, simplex,problem);
        //     generateConstraints(n,simplex,new double[n],c);


        //  numCities * (numCities - 1) / 2;
        double[][] constraintMatrix = new double[matrix.length * (matrix.length - 1) / 2][matrix.length * (matrix.length - 1) / 2];
        int p = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = i + 1; j < matrix.length; j++) {
                constraintMatrix[i][p] = 1; // constraint that ensures city i is visited
                constraintMatrix[matrix.length + j][p++] = 1; // constraint that ensures city j is visited
            }
        }

        //Simplex simplex1 = new Simplex(n,simp_n,constraintMatrix,rhs,c,0);
        // simplex1.simplex();
        //  Simplex.divideBySmallest(matrix);

        //  divideByMin(simplex);
        // divideByMinCol(simplex);


        // System.out.println(objCoeffs);


/*        ArrayList<Integer> list = TwoPhaseSimplex.test(constraintMatrix, b, c);
        System.out.println(list.size());

        TSPTour tourll = new TSPTour();

        tourll.setNodes(list);
        System.out.println(list);
        System.out.println(tourll.distance(problem));

 */


        // Simplex simplex1 = new Simplex();
        //TspSimplex tspSimplex = new TspSimplex(A,b,c,A[0].length,A.length);
        // tspSimplex.solve();

        /* A = new double[2 * n][n*n];
         b = new double[2 * n];

        // Every city must be visited exactly once
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                A[i][i * numCities + j] = 1;
            }
            b[i] = 1;
        }

        // Every city must have exactly one outgoing edge
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++) {
                A[numCities + i][i * numCities + j] = 1;
            }
            b[numCities + i] = 1;
        }

        // Solve the linear programming problem
        // ...

        // Print the solution



        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }

         */

     //   TwoPhaseSimplex twoPhaseSimplex = new TwoPhaseSimplex(A,b,c);




        //   simplex1.simplex(constraintMatrix,b,c);
        //  simplex1.solve();
        //simplex1.getSolution();
        // simplex1.solve();
        // simplex1.getSolution();
        // double[] gen = simplex1.Simplex(A,b,c);
        //for (int i = 0; i < gen.length; i++) {
        //  System.out.print(gen[i]+" ");
        // }




    /*
        int[][] matrix = problem.getDistanceSection().getAdjMatrix(problem);
        System.out.println("===================================================================== Random");
        RandomTour randomTour = new RandomTour(matrix.length);
        long randomStart = System.nanoTime();
        randomTour.solve();
        long randomEnd = System.nanoTime() -randomStart;
        System.out.println(randomTour.getTour());
        TSPTour tour = new TSPTour();
        tour.setNodes(randomTour.getTour());
        System.out.println(tour.distance(problem));
        System.out.println(printTime(randomEnd));

        System.out.println("===================================================================== Nearest");
        NearestNeighbour neighbour = new NearestNeighbour(matrix);
        long nearestStart = System.nanoTime();
         neighbour.solve();
        List<Integer> list =   neighbour.getTour();
        long nearestEnd = System.nanoTime()-nearestStart;

        System.out.println(list);

        tour.setNodes(list);
        System.out.println(tour.distance(problem));
        System.out.println(printTime(nearestEnd));

        System.out.println("===================================================================== Christofides");

        long start3 = System.nanoTime();
        Algorithm christofides = new Christofides(matrix);
         christofides.solve();
        long end3 = System.nanoTime() - start3;


        TSPTour tour1 =new TSPTour();
        tour1.setNodes(christofides.getTour());


        System.out.println("Christofides "+christofides.getTour());
        System.out.println("Cost "+tour1.distance(problem));
        System.out.println(

                printTime(end3));

        System.out.println("===================================================================== Held");
        int startNode = 0;
        HeldKarp solverr =
                new HeldKarp(startNode, matrix);


        long start1 = System.nanoTime();


        System.out.println("Tour Held: "+solverr.getTour());
        long end2 = System.nanoTime() - start1;
        System.out.println("Cost "+solverr.getTourCost());
        System.out.println(

                printTime(end2));






        System.out.println("===================================================================== PTAS");

        Arora arora = new Arora(matrix);
        TSPTour checkTour = new TSPTour();
        long start5 = System.nanoTime();
        checkTour.setNodes(arora.getTour());
        long end5 = System.nanoTime() - start5;
        System.out.println("Tour Arora   "+checkTour.getNodes());
        System.out.println(" Cost "+checkTour.distance(problem));
        System.out.println(printTime(end5));

        System.out.println("===================================================================== Greedy");

        GreedyTSP greedyTSP = new GreedyTSP(matrix);


        long start = System.nanoTime();
        List<Integer> tour2 = greedyTSP.getTour();
        long end = System.nanoTime() - start;


        System.out.println("Greedy "+ tour2);
        System.out.println(" Cost "+greedyTSP.getTourCost());
        System.out.println(

                printTime(end));





        System.out.println("===================================================================== LP Branch And Bound");

        BAndB bandb = new BAndB(matrix);
        long start6 = System.nanoTime();
        checkTour.setNodes(bandb.getTour());
        long end6 = System.nanoTime() - start6;
        System.out.println("Tour Branch And Bound   "+checkTour.getNodes());
        System.out.println(" Cost "+bandb.getTourCost());
        System.out.println(printTime(end6));


        System.out.println("Check =============================================================");

     */


    }



    public static String printTime(long end) {
        String msg = "(in ";
        if (end < 1000) {
            msg += "ns): " + String.format(Locale.ROOT, "%.3f", (float) (end));
        } else if (end >= 1000 && end < 1000000) {
            msg += "us): " + String.format(Locale.ROOT, "%.3f", ((float) end / 1000.0));
        } else if (end >= 1000000 && end < 1000000000) {
            msg += "ms): " + String.format(Locale.ROOT, "%.3f", ((float) end / 1000000.0));
        } else if (end >= 1000000000) {
            msg += "s): " + String.format(Locale.ROOT, "%.3f", ((float) end / 1000000000.0));
        }
        return msg;
    }



    public static double[][] copyToDouble(int[][] intMatrix) {
        double[][] doubleMatrix = new double[intMatrix.length][intMatrix[0].length];
        for (int i = 0; i < intMatrix.length; i++) {
            for (int j = 0; j < intMatrix[i].length; j++) {
                doubleMatrix[i][j] = intMatrix[i][j];
            }
        }
        return doubleMatrix;
    }




    public static void generateTSPConstraints(int numCities, double[][] distances, TSPInstance problem) {
        int numVariables = numCities * (numCities - 1) / 2;
        int numConstraints = numCities + (numCities * (numCities - 1) / 2);

        double[][] A = new double[numConstraints][numVariables];
        double[] b = new double[numConstraints];
        double[] c = new double[numVariables];

        int variableIndex = 0;
        int constraintIndex = 0;

        // Generate constraints for each city being visited once
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                A[constraintIndex][variableIndex] = 1;
                variableIndex++;
            }
            b[constraintIndex] = 1;
            constraintIndex++;
        }

        // Generate constraints for each city having exactly one outgoing edge
        for (int i = 0; i < numCities; i++) {
            int variableStart = (i * (numCities - 1)) / 2;
            int variableEnd = variableStart + (numCities - i - 1);
            for (int j = variableStart; j < variableEnd; j++) {
                A[constraintIndex][j] = 1;
            }
            b[constraintIndex] = 1;
            constraintIndex++;
        }


        // Generate constraints for each city having exactly one incoming edge
        for (int j = 0; j < numCities; j++) {
            int variableStart = j * (numCities - 1) / 2;
            int variableEnd = variableStart + (numCities - j - 1);
            for (int i = variableStart; i < variableEnd; i++) {
                A[constraintIndex][i] = 1;
            }
            b[constraintIndex] = 1;
            constraintIndex++;
        }


        // Generate no sub-tour constraints
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                for (int k = j + 1; k < numCities; k++) {
                    int variable1 = (i * (numCities - 1)) / 2 + (j - i - 1);
                    int variable2 = (j * (numCities - 1)) / 2 + (k - j - 1);
                    int variable3 = (i * (numCities - 1)) / 2 + (k - i - 1);

                    A[i][variable1] = 1;
                    A[i][variable2] = 1;
                    A[i][variable3] = 1;
                    b[i] = 2;
                    constraintIndex++;
                }
            }
        }


        // Generate the costs
        // ...
        int index = 0;
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                c[index] = distances[i][j];
                index++;
            }
        }



      /*Simplex simplex3 = new Simplex();
        System.out.println(c.length+" "+b.length);
       simplex3.simplex(A,b,c);

        ArrayList<Integer> list = TwoPhaseSimplex.test(A, b, c);
        System.out.println(list);

        TSPTour tourll = new TSPTour();

        tourll.setNodes(list);
        System.out.println(list);
        System.out.println(tourll.distance(problem));

       */


    }

    public static void generateConstraints(int numCities, double[][] distances, double[] b, double[] c) {
        int numVariables = (numCities * (numCities - 1)) / 2;
        int numConstraints = numCities + (numCities * (numCities - 3)) / 2;

        int index = 0;
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                c[index] = distances[i][j];
                index++;
            }
        }
        System.out.println(numConstraints + " NUmmmmm " + numVariables);
        double[][] A = new double[numCities * 2][numVariables];

        index = 0;
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 1; j < numCities; j++) {
                A[i][index] = 1;
                index++;
            }
        }

        index = 0;
        for (int i = numCities; i < numCities * 2; i++) {
            for (int j = 0; j < numVariables; j++) {
                A[i][j] = 0;
            }
        }

        index = 0;
        for (int i = 0; i < numCities; i++) {
            for (int j = i + 2; j < numCities; j++) {
                for (int k = j + 1; k < numCities; k++) {
                    A[numCities + i][getVariableIndex(i, j, numCities)] = 1;
                    A[numCities + i][getVariableIndex(j, k, numCities)] = 1;
                    A[numCities + i][getVariableIndex(k, i, numCities)] = 1;
                    index++;
                }
            }
        }

        for (int i = 0; i < numCities; i++) {
            b[i] = 2;
        }

        // for (int i = numCities; i < numConstraints; i++) {
        //     b[i] = 2;
        // }

        System.out.println(" iN DER mETHODE");
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }





    }

    public static int[] generateTour(int[] solution, int numCities) {
        for (int i = 0; i < solution.length; i++) {
            System.out.println(solution[i]);
        }
        int[] tour = new int[numCities];
        boolean[] visited = new boolean[numCities];
        int nextCity = 0;
        tour[0] = nextCity;
        visited[nextCity] = true;

        for (int i = 1; i < numCities; i++) {
            for (int j = 0; j < numCities - 1; j++) {
                if (visited[j]) {
                    continue;
                }
                int index = 0;
                if (j < nextCity) {
                    index = (j * (numCities - 1)) + nextCity - (j + 1) * j / 2;
                } else {
                    index = (j * (numCities - 1)) + nextCity - (j + 1) * j / 2 + 1;
                }
                if (solution[index] == 1) {
                    nextCity = j;
                    tour[i] = nextCity;
                    visited[nextCity] = true;
                    break;
                }
            }
        }

        for (int i = 0; i < tour.length; i++) {
            System.out.print(tour[i] + " ");
        }

        return tour;
    }

    public static int[] generateTour(double[][] tableau, int numCities) {
        int[] tour = new int[numCities];
        boolean[] visited = new boolean[numCities];
        int nextCity = 0;
        tour[0] = nextCity;
        visited[nextCity] = true;

        for (int i = 1; i < numCities; i++) {
            for (int j = 0; j < numCities - 1; j++) {
                if (visited[j]) {
                    continue;
                }
                int index = 0;
                if (j < nextCity) {
                    index = (j * (numCities - 1)) + nextCity - (j + 1) * j / 2;
                } else {
                    index = (j * (numCities - 1)) + nextCity - (j + 1) * j / 2 + 1;
                }
                if (tableau[index][numCities + 1] == 1.0) {
                    nextCity = j;
                    tour[i] = nextCity;
                    visited[nextCity] = true;
                    break;
                }
            }
        }

        for (int i = 0; i < tour.length; i++) {
            System.out.print(tour[i] + " ");
        }

        return tour;
    }


    private static int getVariableIndex(int i, int j, int numCities) {
        if (j < i) {
            int temp = i;
            i = j;
            j = temp;
        }

        return (numCities - 1) * i - (i * (i - 1)) / 2 + j - i - 1;
    }


}
