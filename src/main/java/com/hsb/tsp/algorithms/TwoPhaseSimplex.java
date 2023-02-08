package com.hsb.tsp.algorithms;

import java.util.ArrayList;

/******************************************************************************
 *  Compilation:  javac TwoPhaseSimplex.java
 *  Execution:    java TwoPhaseSimplex
 *  Dependencies: System.out.java
 *
 *  Given an m-by-n matrix A, an m-length vector b, and an
 *  n-length vector c, solve the  LP { max cx : Ax <= b, x >= 0 }.
 *  Unlike Simplex.java, this version does not assume b >= 0,
 *  so it needs to find a basic feasible solution in Phase I.
 *
 *  Creates an (m+1)-by-(n+m+1) simplex tableaux with the
 *  RHS in column m+n, the objective function in row m, and
 *  slack variables in columns m through m+n-1.
 *
 ******************************************************************************/

public class TwoPhaseSimplex {
    private static final double EPSILON = 1.0E-8;

    private double[][] a;   // tableaux
    // row m   = objective function
    // row m+1 = artificial objective function
    // column n to n+m-1 = slack variables
    // column n+m to n+m+m-1 = artificial variables

    private int m;          // number of constraints
    private int n;
    private int numofCities;// number of original variables

    private int[] basis;    // basis[i] = basic variable corresponding to row i

    // sets up the simplex tableaux
    public TwoPhaseSimplex(double[][] A, double[] b, double[] c,int numofCities) {
        m = b.length;
        System.out.println(" C len"+c.length);
        n = c.length;
        System.out.println(" N Len "+n);
        this.numofCities=numofCities;

        a = new double[m+2][n+m+m+1];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = A[i][j];
        for (int i = 0; i < m; i++)
            a[i][n+i] = 1.0;
        for (int i = 0; i < m; i++)
            a[i][n+m+m] = b[i];
        for (int j = 0; j < n; j++)
            a[m][j] = c[j];

        // if negative RHS, multiply by -1
       for (int i = 0; i < m; i++) {
            if (b[i] < 0) {
                for (int j = 0; j <= n+m+m; j++)
                    a[i][j] = -a[i][j];
            }
        }



        // artificial variables form initial basis
        for (int i = 0; i < m; i++)
            a[i][n+m+i] = 1.0;
        for (int i = 0; i < m; i++)
            a[m+1][n+m+i] = -1.0;
        for (int i = 0; i < m; i++)
            pivot(i, n+m+i);

        basis = new int[m];
        for (int i = 0; i < m; i++)
            basis[i] = n + m + i;

        System.out.println("before phase I");
        show();

        phase1();

        System.out.println("before phase II");
         show();

        phase2();

        System.out.println("after phase II");
         show();


        for (int i = 0; i < m; i++) {
            System.out.println(" bi ");
            if (basis[i] < numofCities) {
                System.out.println(" Basic Main "+basis[i]+" "+n);
            }
        }




        // check optimality conditions
        assert check(A, b, c);
    }

    public TwoPhaseSimplex(double[][] A, double[] b, double[] c) {
        m = b.length;
        System.out.println(" C len"+c.length);
        n = c.length;
        System.out.println(" N Len "+n);


        a = new double[m+2][n+m+m+1];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = A[i][j];
        for (int i = 0; i < m; i++)
            a[i][n+i] = 1.0;
        for (int i = 0; i < m; i++)
            a[i][n+m+m] = b[i];
        for (int j = 0; j < n; j++)
            a[m][j] = c[j];

        // if negative RHS, multiply by -1
        for (int i = 0; i < m; i++) {
            if (b[i] < 0) {
                for (int j = 0; j <= n+m+m; j++)
                    a[i][j] = -a[i][j];
            }
        }


        // artificial variables form initial basis
        for (int i = 0; i < m; i++)
            a[i][n+m+i] = 1.0;
        for (int i = 0; i < m; i++)
            a[m+1][n+m+i] = -1.0;
        for (int i = 0; i < m; i++)
            pivot(i, n+m+i);

        basis = new int[m];
        for (int i = 0; i < m; i++)
            basis[i] = n + m + i;

        System.out.println("before phase I");
        show();

        phase1();

        System.out.println("before phase II");
        show();

        phase2();

        System.out.println("after phase II");
        show();






        // check optimality conditions
        assert check(A, b, c);
    }


    // run phase I simplex algorithm to find basic feasible solution
    private void phase1() {
        while (true) {

            // find entering column q
            int q = bland1();
            if (q == -1) break;  // optimal

            // find leaving row p
            int p = minRatioRule(q);
            assert p != -1 : "Entering column = " + q;

            // pivot
            pivot(p, q);

            // update basis
            basis[p] = q;
            // show();
        }
        if (a[m+1][n+m+m] > EPSILON) throw new ArithmeticException("Linear program is infeasible");
    }

    public ArrayList<Integer>  getBasis() {

        System.out.println(" Basis");
        ArrayList<Integer> tour = new ArrayList<>();
        tour.add(basis[0]+1);
        for (int i = 1; i < basis.length; i++) {
            tour.add(basis[i]+1);
        }
        tour.add(basis[0]+1);
        return  tour;
    }

    // run simplex algorithm starting from initial basic feasible solution
    private void phase2() {
        while (true) {

            // find entering column q
            int q = bland2();
            if (q == -1) break;  // optimal

            // find leaving row p
            int p = minRatioRule(q);
            if (p == -1) throw new ArithmeticException("Linear program is unbounded");

            // pivot
            pivot(p, q);

            // update basis
            basis[p] = q;
        }
    }

    // lowest index of a non-basic column with a positive cost - using artificial objective function
    private int bland1() {
        for (int j = 0; j < n+m; j++)
            if (a[m+1][j] > EPSILON) return j;
        return -1;  // optimal
    }

    // lowest index of a non-basic column with a positive cost
    private int bland2() {
        for (int j = 0; j < n+m; j++)
            if (a[m][j] > EPSILON) return j;
        return -1;  // optimal
    }


    // find row p using min ratio rule (-1 if no such row)
    private int minRatioRule(int q) {
        int p = -1;
        for (int i = 0; i < m; i++) {
            // if (a[i][q] <= 0) continue;
            if (a[i][q] <= EPSILON) continue;
            else if (p == -1) p = i;
            else if ((a[i][n+m+m] / a[i][q]) < (a[p][n+m+m] / a[p][q])) p = i;
        }
        return p;
    }

    // pivot on entry (p, q) using Gauss-Jordan elimination
    private void pivot(int p, int q) {

        // everything but row p and column q
        for (int i = 0; i <= m+1; i++)
            for (int j = 0; j <= n+m+m; j++)
                if (i != p && j != q) a[i][j] -= a[p][j] * a[i][q] / a[p][q];

        // zero out column q
        for (int i = 0; i <= m+1; i++)
            if (i != p) a[i][q] = 0.0;

        // scale row p
        for (int j = 0; j <= n+m+m; j++)
            if (j != q) a[p][j] /= a[p][q];
        a[p][q] = 1.0;
    }

    // return optimal objective value
    public double value() {
        return -a[m][n+m+m];
    }

    // return primal solution vector
    public double[] primal() {
        double[] x = new double[n];
        for (int i = 0; i < m; i++)
            if (basis[i] < n) x[basis[i]] = a[i][n+m+m];
        return x;
    }

    // return dual solution vector
    public double[] dual() {
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
            y[i] = -a[m][n+i];
        return y;
    }


    // is the solution primal feasible?
    private boolean isPrimalFeasible(double[][] A, double[] b) {
        double[] x = primal();

        // check that x >= 0
        for (int j = 0; j < x.length; j++) {
            if (x[j] < 0.0) {
                System.out.println("x[" + j + "] = " + x[j] + " is negative");
                return false;
            }
        }

        // check that Ax <= b
        for (int i = 0; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            if (sum > b[i] + EPSILON) {
                System.out.println("not primal feasible");
                System.out.println("b[" + i + "] = " + b[i] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }

    // is the solution dual feasible?
    private boolean isDualFeasible(double[][] A, double[] c) {
        double[] y = dual();

        // check that y >= 0
        for (int i = 0; i < y.length; i++) {
            if (y[i] < 0.0) {
                System.out.println("y[" + i + "] = " + y[i] + " is negative");
                return false;
            }
        }

        // check that yA >= c
        for (int j = 0; j < n; j++) {
            double sum = 0.0;
            for (int i = 0; i < m; i++) {
                sum += A[i][j] * y[i];
            }
            if (sum < c[j] - EPSILON) {
                System.out.println("not dual feasible");
                System.out.println("c[" + j + "] = " + c[j] + ", sum = " + sum);
                return false;
            }
        }
        return true;
    }

    // check that optimal value = cx = yb
    private boolean isOptimal(double[] b, double[] c) {
        double[] x = primal();
        double[] y = dual();
        double value = value();

        // check that value = cx = yb
        double value1 = 0.0;
        for (int j = 0; j < x.length; j++)
            value1 += c[j] * x[j];
        double value2 = 0.0;
        for (int i = 0; i < y.length; i++)
            value2 += y[i] * b[i];
        if (Math.abs(value - value1) > EPSILON || Math.abs(value - value2) > EPSILON) {
            System.out.println("value = " + value + ", cx = " + value1 + ", yb = " + value2);
            return false;
        }

        return true;
    }

    private boolean check(double[][]A, double[] b, double[] c) {
        return isPrimalFeasible(A, b) && isDualFeasible(A, c) && isOptimal(b, c);
    }

    // print tableaux
    public void show() {
        System.out.println("m = " + m);
        System.out.println("n = " + n);
        System.out.println("Array Length "+a.length);
        for (int i = 0; i <= m+1; i++) {
            for (int j = 0; j <= n+m+m; j++) {
                System.out.printf("%7.2f ", a[i][j]);
                if (j == n+m-1 || j == n+m+m-1) System.out.print(" |");
            }
            System.out.println();
        }
        System.out.print("basis = ");
        for (int i = 0; i < m; i++)
            System.out.print(basis[i] + " ");
        System.out.println();
        System.out.println();
    }



    public static ArrayList<Integer> test(double[][] A, double[] b, double[] c) {
        TwoPhaseSimplex lp;
        try {
            lp = new TwoPhaseSimplex(A, b, c);


        }
        catch (ArithmeticException e) {
            System.out.println(e);
            return null;
        }

        System.out.println("value = " + lp.value());
        double[] x = lp.primal();
        for (int i = 0; i < x.length; i++)
            System.out.println("x[" + i + "] = " + x[i]);
        double[] y = lp.dual();
        for (int j = 0; j < y.length; j++)
            System.out.println("y[" + j + "] = " + y[j]);


        System.out.println(" Basis");
        ArrayList<Integer> tour = new ArrayList<>();

        tour.add(lp.basis[0]+1);


        for (int i = 1; i < lp.basis.length; i++) {
            tour.add(lp.basis[i]+1);
        }
        tour.add(lp.basis[0]+1);
        return  tour;


    }
    public static ArrayList<Integer> test(double[][] A, double[] b, double[] c,int num) {
        TwoPhaseSimplex lp;
        try {
            lp = new TwoPhaseSimplex(A, b, c,num);


        }
        catch (ArithmeticException e) {
            System.out.println(e);
            return null;
        }

        System.out.println("value = " + lp.value());
        double[] x = lp.primal();
        for (int i = 0; i < x.length; i++)
            System.out.println("x[" + i + "] = " + x[i]);
        double[] y = lp.dual();
        for (int j = 0; j < y.length; j++)
            System.out.println("y[" + j + "] = " + y[j]);


        System.out.println(" Basis");
        ArrayList<Integer> tour = new ArrayList<>();

        tour.add(lp.basis[0]+1);


        for (int i = 1; i < lp.basis.length; i++) {
            tour.add(lp.basis[i]+1);
        }
        tour.add(lp.basis[0]+1);
        return  tour;


    }

    // x0 = 12, x1 = 28, opt = 800
    public static void test1() {
        double[] c = {  13.0,  23.0 };
        double[] b = { 480.0, 160.0, 1190.0 };
        double[][] A = {
                {  5.0, 15.0 },
                {  4.0,  4.0 },
                { 35.0, 20.0 },
        };
        test(A, b, c);
    }

    // dual of test1():  x0 = 12, x1 = 28, opt = 800
    public static void test2() {
        double[] b = {  -13.0,  -23.0 };
        double[] c = { -480.0, -160.0, -1190.0 };
        double[][] A = {
                {  -5.0, -4.0, -35.0 },
                { -15.0, -4.0, -20.0 }
        };
        test(A, b, c);
    }

    public static void test3() {
        double[][] A = {
                { -1,  1,  0 },
                {  1,  4,  0 },
                {  2,  1,  0 },
                {  3, -4,  0 },
                {  0,  0,  1 },
        };
        double[] c = { 1, 1, 1 };
        double[] b = { 5, 45, 27, 24, 4 };
        test(A, b, c);
    }

    // unbounded
    public static void test4() {
        double[] c = { 2.0, 3.0, -1.0, -12.0 };
        double[] b = {  3.0,   2.0 };
        double[][] A = {
                { -2.0, -9.0,  1.0,  9.0 },
                {  1.0,  1.0, -1.0, -2.0 },
        };
        test(A, b, c);
    }

    // degenerate - cycles if you choose most positive objective function coefficient
    public static void test5() {
        double[] c = { 10.0, -57.0, -9.0, -24.0 };
        double[] b = {  0.0,   0.0,  1.0 };
        double[][] A = {
                { 0.5, -5.5, -2.5, 9.0 },
                { 0.5, -1.5, -0.5, 1.0 },
                { 1.0,  0.0,  0.0, 0.0 },
        };
        test(A, b, c);
    }

    // floating-point EPSILON needed in min-ratio test
    public static void test6() {
        double[] c = { -1, -1, -1, -1, -1, -1, -1, -1, -1 };
        double[] b = { -0.9, 0.2, -0.2, -1.1, -0.7, -0.5, -0.1, -0.1, -1 };
        double[][] A = {
                { -2,  1,  0,  0,  0,  0,  0,  0,  0 },
                {  1, -2, -1,  0,  0,  0,  0,  0,  0 },
                {  0, -1, -2, -1,  0,  0,  0,  0,  0 },
                {  0,  0, -1, -2, -1, -1,  0,  0,  0 },
                {  0,  0,  0, -1, -2, -1,  0,  0,  0 },
                {  0,  0,  0, -1, -1, -2, -1,  0,  0 },
                {  0,  0,  0,  0,  0, -1, -2, -1,  0 },
                {  0,  0,  0,  0,  0,  0, -1, -2, -1 },
                {  0,  0,  0,  0,  0,  0,  0, -1, -2 }
        };
        test(A, b, c);
    }

  
 public static void main(String[] args) {
     System.out.println("----- test 1 --------------------");
        test1();

     System.out.println();
     System.out.println("----- test 2 --------------------");
        test2();

        System.out.println();
        System.out.println("----- test 3 --------------------");
        test3();

        System.out.println();
        System.out.println("----- test 4 --------------------");
        test4();

        System.out.println();
        System.out.println("----- test 5 --------------------");
        test5();

        System.out.println();
        System.out.println("----- test 6 --------------------");
        test6();

        System.out.println("----- test random ---------------");
       /* int m = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        double[] c = new double[n];
        double[] b = new double[m];
        double[][] A = new double[m][n];
        for (int j = 0; j < n; j++)
            c[j] = StdRandom.uniformInt(1000);
        for (int i = 0; i < m; i++)
            b[i] = StdRandom.uniformInt(1000) - 200;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                A[i][j] = StdRandom.uniformInt(100) - 20;
        test(A, b, c);
        
        */

    }
/*
    import java.util.Arrays;

public class TSP_Simplex {
  // Number of cities
  int n;
  // Distance matrix
  double[][] dist;
  // Objective function coefficients
  double[] c;
  // Right-hand side values of constraints
  double[] b;
  // Constraint matrix
  double[][] A;
  // Basis indices
  int[] basis;
  // Reduced costs
  double[] rc;

  public TSP_Simplex(double[][] dist) {
    this.dist = dist;
    this.n = dist.length;
    this.c = new double[n * (n - 1) / 2];
    this.b = new double[n];
    this.A = new double[n][n * (n - 1) / 2];
    this.basis = new int[n];
    this.rc = new double[n * (n - 1) / 2];
    Arrays.fill(b, 1);
    int idx = 0;
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        c[idx] = dist[i][j];
        idx++;
      }
    }
    idx = 0;
    for (int i = 0; i < n; i++) {
      for (int j = i + 1; j < n; j++) {
        A[i][idx] = 1;
        idx++;
      }
      idx = 0;
      for (int j = i + 1; j < n; j++) {
        A[j][idx] = 1;
        idx++;
      }
    }
    Arrays.fill(basis, -1);
    for (int i = 0; i < n; i++) {
      basis[i] = i;
    }
  }

    public void solve() {
    while (true) {
      // Compute reduced costs
      for (int i = 0; i < n; i++) {
        rc[i] = c[i];
        for (int j = 0; j < m; j++) {
          rc[i] -= A[j][i] * b[j];
        }
      }

      // Check optimality
      boolean optimal = true;
      int enter = -1;
      for (int i = 0; i < n; i++) {
        if (rc[i] > 0) {
          optimal = false;
          enter = i;
          break;
        }
      }
      if (optimal) {
        break;
      }

      // Choose leaving variable
      int leave = -1;
      double minRatio = Double.POSITIVE_INFINITY;
      for (int i = 0; i < m; i++) {
        if (A[i][enter] > 0) {
          double ratio = b[i] / A[i][enter];
          if (ratio < minRatio) {
            minRatio = ratio;
            leave = i;
          }
        }
      }

      // Perform pivot operation
      double pivot = A[leave][enter];
      A[leave][enter] = 1;
      b[leave] /= pivot;
      for (int i = 0; i < n; i++) {
        if (i != enter) {
          A[leave][i] /= pivot;
        }
      }
      c[enter] /= pivot;
      for (int i = 0; i < m; i++) {
        if (i != leave) {
          double factor = A[i][enter];
          A[i][enter] = 0;
          b[i] -= factor * b[leave];
          for (int j = 0; j < n; j++) {
            if (j != enter) {
              A[i][j] -= factor * A[leave][j];
            }
          }
          c[enter] -= factor * c[leave];
        }
      }
      // Update basis
      basis[p] = q;
      c[q] = rc[q];
      rc
    */


    /*
    import java.util.ArrayList;
import java.util.List;

public class Simplex {
  // Number of constraints
  int m;
  // Number of variables
  int n;
  // Objective function coefficients
  double[] c;
  // Right-hand side values of constraints
  double[] b;
  // Constraint matrix
  double[][] A;
  // Basis indices
  int[] basis;
  // Reduced costs
  double[] rc;

  public Simplex(double[] c, double[][] A, double[] b) {
    this.c = c;
    this.A = A;
    this.b = b;
    this.m = b.length;
    this.n = c.length;
    this.basis = new int[m];
    this.rc = new double[n];
    for (int i = 0; i < m; i++) {
      basis[i] = n + i;
    }
  }

  public void solve() {
    while (true) {
      // Compute reduced costs
      for (int j = 0; j < n; j++) {
        rc[j] = c[j];
        for (int i = 0; i < m; i++) {
          rc[j] -= c[basis[i]] * A[i][j];
        }
      }
      // Check optimality
      int j = 0;
      for (j = 0; j < n; j++) {
        if (rc[j] > 0) break;
      }
      if (j == n) break;
      // Choose pivot column
      int p = j;
      // Choose pivot row
      int q = 0;
      double minRatio = Double.POSITIVE_INFINITY;
      for (int i = 0; i < m; i++) {
        if (A[i][p] <= 0) continue;
        double ratio = b[i] / A[i][p];
        if (ratio < minRatio) {
          q = i;
          minRatio = ratio;
        }
      }
      // Update basis and c
      basis[q] = p;
      c[p] = minRatio;
      // Update b and A
      b[q] /= A[q][p];
      for (int jj = 0; jj < n; jj++) {
        A[q][jj] /= A[q][p];
      }
      for (int i = 0; i < m; i++) {
        if (i == q) continue;
        b[i] -= A[i][p] * b[q];
        for (int jj = 0; jj < n; jj++) {
          A[i][jj] -= A[i][p] * A[q][jj];
        }
      }
    }
  }

  public double[] getSolution() {
    double[] x = new double[n];
    List<Integer> list = new ArrayList<>();
    for (int i = 0; i < m; i++) {
      if (basis[i] < n) {
        x

     */


}