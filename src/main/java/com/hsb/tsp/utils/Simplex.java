package com.hsb.tsp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;

public class Simplex {
    private double[][] tableau;
    private int[] basis;
    private int m, n;
    private ArrayList<Double> objective;

    public Simplex(double[][] A, double[] b, double[] c) {
        m = b.length;
        n = c.length;
        tableau = new double[m + 1][n + m + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tableau[i][j] = A[i][j];
            }
        }
        for (int i = 0; i < m; i++) {
            tableau[i][n + i] = 1;
        }
        for (int j = 0; j < n; j++) {
            tableau[m][j] = c[j];
        }
        for (int i = 0; i < m; i++) {
            tableau[i][m + n] = b[i];
        }
        basis = new int[m];
        for (int i = 0; i < m; i++) {
            basis[i] = n + i;
        }
        objective = new ArrayList<>();
    }

    public void pivot(int r, int c) {
        double pivot = tableau[r][c];
        for (int j = 0; j < tableau[0].length; j++) {
            tableau[r][j] /= pivot;
        }
        for (int i = 0; i < tableau.length; i++) {
            if (i != r) {
                double multiplier = tableau[i][c];
                for (int j = 0; j < tableau[0].length; j++) {
                    tableau[i][j] -= multiplier * tableau[r][j];
                }
            }
        }
        int temp = basis[r];
        basis[r] = c;
        objective.add(tableau[m][c]);
        for (int i = 0; i < m; i++) {
            if (basis[i] == c) {
                basis[i] = temp;
                break;
            }
        }
    }

    public void simplex() {
        while (true) {
            int q = -1;
            for (int j = 0; j < n; j++) {
                if (tableau[m][j] > 0) {
                    q = j;
                    break;
                }
            }
            if (q == -1) {
                break;
            }
            int p = -1;
            for (int i = 0; i < m; i++) {
                if (tableau[i][q] > 0) {
                    if (p == -1) {
                        p = i;
                    } else if (tableau[i][m + n] / tableau[i][q] < tableau[p][m + n] / tableau[p][q]) {
                        p = i;
                    }
                }
            }
        }
    }

    public static void primalSimplex(double[][] A, double[] b, double[] c) {
        int m = b.length;
        int n = c.length;
        double[][] tableau = new double[m + 1][n + m + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tableau[i][j] = A[i][j];
            }
        }
        for (int i = 0; i < m; i++) {
            tableau[i][n + i] = 1;
        }
        for (int j = 0; j < n; j++) {
            tableau[m][j] = c[j];
        }
        for (int i = 0; i < m; i++) {
            tableau[i][m + n] = b[i];
        }
        int[] basis = new int[m];
        for (int i = 0; i < m; i++) {
            basis[i] = n + i;
        }
        while (true) {
            int q = -1;
            for (int j = 0; j < n; j++) {
                if (tableau[m][j] > 0) {
                    q = j;
                    break;
                }
            }
            if (q == -1) {
                break;
            }
            int p = -1;
            for (int i = 0; i < m; i++) {

                if (tableau[i][q] > 0) {
                    if (p == -1) {
                        p = i;
                    } else if (tableau[i][m + n] / tableau[i][q] < tableau[p][m + n] / tableau[p][q]) {
                        p = i;
                    }
                }
            }
            System.out.println();
            if (p == -1) {
                for (int i = 0; i < tableau.length; i++) {
                    for (int j = 0; j < tableau[i].length; j++) {
                        System.out.print(tableau[i][j]+" ");
                    }
                    System.out.println();
                }
                throw new ArithmeticException("Linear program is unbounded");
            }
            double pivot = tableau[p][q];
            for (int j = 0; j < tableau[0].length; j++) {
                tableau[p][j] /= pivot;
            }
            for (int i = 0; i < tableau.length; i++) {
                if (i != p) {
                    double multiplier = tableau[i][q];
                    for (int j = 0; j < tableau[0].length; j++) {
                        tableau[i][j] -= multiplier * tableau[p][j];
                    }
                }
            }
            int temp = basis[p];
            basis[p] = q;
            for (int i = 0; i < m; i++) {
                if (basis[i] == q) {
                    basis[i] = temp;
                    break;
                }
            }
        }
        System.out.println("Optimal solution: " + tableau[m][m + n]);
        System.out.print("Basic variables: ");
        //  for (int i = 0; i < m; i++) {
        //    if

        // }
    }
}


/*
public class Simplex {
    private static final double EPS = 1e-3;
    private int n, m;
    private double[][] A;
    private double[] b, c;
    private double v;
    int totalSteps = 0;
    private int[] N, B;  // nonbasic & basic


    public Simplex(int n, int m, double[][] A, double[] b, double[] c, double v) {
        this.n = n;
        this.m = A.length;
        System.out.println("HHHH "+m);
        this.v = v;

        this.A = new double[m][n + 1];
        // for (int i = 0; i < m; i++) {
        //   System.arraycopy(A[i], 0, this.A[i], 0, n);
        System.out.println(A.length);
        System.out.println(n);
        System.out.println(m+" BIN");
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                this.A[i][j] = A[i][j];


       /* a = new double[m+2][n+m+m+1];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = A[i][j];

        */
    /*
        this.b = b.clone();
        this.c = c.clone();

        this.N = new int[n + 1];
        this.B = new int[m];
    }


    // pivot yth variable around xth constraint
    public void pivot(int x, int y) {


        totalSteps++;

        // first rearrange the x-th row
        for (int j = 0; j < n; j++) {
            if (j != y) {
                A[x][j] /= -A[x][y];
            }
        }
        b[x] /= -A[x][y];
        A[x][y] = 1.0 / A[x][y];

        // now rearrange the other rows
        for (int i = 0; i < m; i++) {
            if (i != x) {
                for (int j = 0; j < n; j++) {
                    if (j != y) {
                        A[i][j] += A[i][y] * A[x][j];
                    }
                }
                b[i] += A[i][y] * b[x];
                A[i][y] *= A[x][y];
            }
        }

        // now rearrange the objective function
        for (int j = 0; j < n; j++) {
            if (j != y) {
                c[j] += c[y] * A[x][j];
            }
        }
        v += c[y] * b[x];
        c[y] *= A[x][y];

        // finally, swap the basic & nonbasic variable
        // Swap the basic & nonbasic variable
        int temp = B[x];
        B[x] = N[y];
        N[y] = temp;
    }

    // Run a single iteration of the simplex algorithm.
    // Returns: 0 if OK, 1 if STOP, -1 if UNBOUNDED

    public int iterateSimplex() {
        double vPrev = v;

        List<Integer> vars = new ArrayList<>();
        int bestVar = -1;
        for (int j = 0; j < n; j++) {
            if (c[j] > 0) {
                vars.add(j);
            }
        }
        if (vars.isEmpty()) return 1;

        bestVar = vars.get(new Random().nextInt(vars.size()));

        double maxConstr = Double.POSITIVE_INFINITY;
        int bestConstr = -1;
        for (int i = 0; i < m; i++) {
            if (A[i][bestVar] < 0) {
                double currConstr = -b[i] / A[i][bestVar];
                if (currConstr < maxConstr) {
                    maxConstr = currConstr;
                    bestConstr = i;
                }
            }
        }
        if (Double.isInfinite(maxConstr)) return -1;
        else pivot(bestConstr, bestVar);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(Math.ceil(A[i][j]) - A[i][j]) < EPS)
                    A[i][j] = Math.ceil(A[i][j]);
                else if (Math.abs(Math.floor(A[i][j]) - A[i][j]) < EPS)
                    A[i][j] = Math.floor(A[i][j]);
            }
        }

        for (int i = 0; i < m; i++) {
            if (Math.abs(Math.ceil(b[i]) - b[i]) < EPS) b[i] = Math.ceil(b[i]);
            if (Math.abs(Math.floor(b[i]) - b[i]) < EPS) b[i] = Math.floor(b[i]);
        }
        for (int j = 0; j < n; j++) {
            if (Math.abs(Math.ceil(c[j]) - c[j]) < EPS) c[j] = Math.ceil(c[j]);
            if (Math.abs(Math.floor(c[j]) - c[j]) < EPS) c[j] = Math.floor(c[j]);
        }

        if (Math.abs(Math.ceil(v) - v) < EPS) v = Math.ceil(v);
        if (Math.abs(Math.floor(v) - v) < EPS) v = Math.floor(v);

        if (Math.abs(v - vPrev) > EPS) System.out.println("Iteration " + totalSteps + ": " + v);

        return 0;
    }

    // (Possibly) converts the LP into a slack form with a feasible basic solution.
    // Returns 0 if OK, -1 if INFEASIBLE
    public int initialiseSimplex() {
        // Implementation here
        return 0;
    }

    public int initializeSimplex() {
        totalSteps = 0;

        int k = -1;
        double min_b = -1;
        for (int i = 0; i < m; i++) {
            if (k == -1 || b[i] < min_b) {
                k = i;
                min_b = b[i];
            }
        }

        if (b[k] >= 0) { // basic solution feasible!
            for (int j = 0; j < n; j++) N[j] = j;
            for (int i = 0; i < m; i++) B[i] = n + i;
            return 0;
        }

        // generate auxiliary LP
        n++;
        for (int j = 0; j < n; j++) N[j] = j;
        for (int i = 0; i < m; i++) B[i] = n + i;

        // store the objective function
        double[] c_old = new double[n];
        for (int j = 0; j < n - 1; j++) c_old[j] = c[j];
        double v_old = v;

        // aux. objective function
        c[n - 1] = -1;
        for (int j = 0; j < n - 1; j++) c[j] = 0;
        v = 0;
        // aux. coefficients
        for (int i = 0; i < m; i++) A[i][n - 1] = 1;

        // perform initial pivot
        pivot(k, n - 1);

        // now solve aux. LP
        int code = 0;
        while ((code = iterateSimplex()) == 0) {
        }

        assert code == 1; // aux. LP cannot be unbounded!!!

        if (v != 0) {
            return -1; // infeasible!
        }

        int z_basic = -1;
        for (int i = 0; i < m; i++) {
            if (B[i] == n - 1) {
                z_basic = i;
                break;
            }
        }

        // if x_n basic, perform one degenerate pivot to make it nonbasic
        if (z_basic != -1) pivot(z_basic, n - 1);

        int z_nonbasic = -1;
        for (int j = 0; j < n; j++) {
            if (N[j] == n - 1) {
                z_nonbasic = j;
                break;
            }
        }
        assert z_nonbasic != -1;

        for (int i = 0; i < m; i++) {
            A[i][z_nonbasic] = A[i][n - 1];
        }
        int temp = N[z_nonbasic];
        N[z_nonbasic] = N[n - 1];
        N[n - 1] = temp;

        n--;
        for (int j = 0; j < n; j++) {
            if (N[j] > n) N[j]--;
        }
        //for (int i = 0
        for (int i = 0; i < m; i++) if (B[i] > n) B[i]--;

        for (int j = 0; j < n; j++) c[j] = 0;
        v = v_old;

        for (int j = 0; j < n; j++) {
            boolean ok = false;
            for (int jj = 0; jj < n; jj++) {
                if (j == N[jj]) {
                    c[jj] += c_old[j];
                    ok = true;
                    break;
                }
            }
            if (ok) continue;
            for (int i = 0; i < m; i++) {
                if (j == B[i]) {
                    for (int jj = 0; jj < n; jj++) {
                        c[jj] += c_old[j] * A[i][jj];
                    }
                    v += c_old[j] * b[i];
                    break;
                }
            }
        }

        return 0;
    }

    // Runs the simplex algorithm to optimise the LP.
    // Returns a Tuple containing a vector of -1s if unbounded, -2s if infeasible.
    public double[] simplex() {
        // Implementation here


        System.out.println("Running the Simplex algorithm with " + n + " variables and " + m + " constraints.");

        if (initializeSimplex() == -1) {
            return null;
        }

        int code;
        while ((code = iterateSimplex()) != 0) {
        }
        ;

        if (code == -1) {
            return null;
        }

        double[] ret = new double[n + m];
        for (int j = 0; j < n; j++) {
            ret[N[j]] = 0;
        }
        for (int i = 0; i < m; i++) {
            ret[B[i]] = b[i];
        }

        System.out.println("Finished in " + totalSteps + " iterations.");

        return ret;
    }
}

*/
