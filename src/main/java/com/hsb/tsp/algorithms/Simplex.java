package com.hsb.tsp.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
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

    public Simplex(double[][] A, double[] b, double[] c) {
        this.c = c;
        this.A = A;
        this.b = b;
        this.m = c.length;
        this.n = b.length;
        this.basis = new int[m];
        this.rc = new double[n];
        System.out.println(m + " M");
        Arrays.fill(basis, -1);
        for (int i = 0; i < n; i++) {
            basis[i] = i;
        }
        for (int i = 0; i < m; i++) {
            basis[i] = n + i;
            System.out.println(basis[i]);
        }


    }

    public Simplex() {
    }

    private static final String UNBOUNDED = "Unbounded";

    public double[] tspSimplex(double[][] A, double[] b, double[] c) {
        int n = A.length;
        int m = A[0].length;
        int[] B = new int[n];
        for (int i = 0; i < n; i++) {
            B[i] = m + i;
        }
        double[] x = new double[m + n];
        while (true) {
            int z = -1;
            for (int i = 0; i < m; i++) {
                if (c[i] < 0) {
                    z = i;
                    break;
                }
            }
            if (z == -1) {
                break;
            }
            int s = -1;
            double azs = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                if (A[i][z] > 0) {
                    double value = b[i] / A[i][z];
                    if (value < azs) {
                        s = i;
                        azs = value;
                    }
                }
            }
            if (azs == Double.POSITIVE_INFINITY) {
                return null;
            }
            int l = 0;
            for (int i = 0; i < n; i++) {
                if (B[i] == s + m) {
                    l = i;
                    break;
                }
            }
            x[z] = azs;
            for (int i = 0; i < n; i++) {
                if (i != l) {
                    b[i] -= A[i][z] * azs;
                    A[i][z] = 0;
                }
            }
            b[s] /= A[s][z];
            for (int j = 0; j < m; j++) {
                if (j != z) {
                    A[s][j] /= A[s][z];
                }
            }
            A[s][z] = 1;
            c[z] = 0;
            for (int j = 0; j < m; j++) {
                if (j != z) {
                    c[j] -= c[z] * A[s][j];
                    for (int i = 0; i < n; i++) {
                        if (i != l) {
                            A[i][j] -= A[i][z] * A[s][j];
                        }
                    }
                }
            }
            B[l] = z;
        }
        for (int i = m; i < m + n; i++) {
            if (x[i] < 0) {
                return null;
            }
        }
        return x;
    }
/*
    public static Double[] simplex(double[][] A, double[] b, double[] c) {
        int n = c.length;
        int m = b.length;
        int[] B = new int[m];
        for (int i = 0; i < m; i++) {
            B[i] = n + i;
        }
        while (true) {
            int q = -1;
            for (int i = 0; i < n; i++) {
                if (c[i] < 0) {
                    q = i;
                    break;
                }
            }
            System.out.println(" Q"+q);
            if (q == -1) {
                break;
            }
            double[] aq = A[q];
            double minRatio = Double.POSITIVE_INFINITY;
            int p = -1;
            for (int i = 0; i < m; i++) {
                if (aq[i] > 0) {
                    double ratio = b[i] / aq[i];
                    if (ratio < minRatio) {
                        minRatio = ratio;
                        p = i;
                    }
                }
            }
            if (p == -1) {
                return null;
            }
            int z = B[p];
            double[] az = A[z];
            double cs = c[z] / az[p];
            c[z] = cs;
            for (int i = 0; i < n; i++) {
                if (i != z) {
                    c[i] = c[i] - cs * az[i];
                    A[i][p] = 0;
                    for (int j = 0; j < m; j++) {
                        A[i][j] = A[i][j] - cs * az[j];
                    }
                }
            }
            b[p] = b[p] / az[p];
            for (int i = 0; i < m; i++) {
                if (i != p) {
                    b[i] = b[i] - b[p] * az[i];
                    A[z][i] = 0;
                }
            }
            B[p] = q;
        }
        Double[] x = new Double[n];
        Arrays.fill(x, 0.0);
        for (int i = 0; i < m; i++) {
            int j = B[i];
            if (j < n) {
                x[j] = b[i];
            }
        }
        return x;
    }


/*
    public double[] simplex(double[][] A, double[] b, double[] c) {
        int n = c.length;
        int m = b.length;
        List<Integer> B = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            B.add(n + i);
        }
        while (true) {
            int q = -1;
            for (int i = 0; i < n; i++) {
                if (c[i] > 0) {
                    q = i;
                    break;
                }
            }
            System.out.println("End "+q);
            if (q == -1) {
                break;
            }
            double minRatio = Double.POSITIVE_INFINITY;
            int s = -1;
            for (int i = 0; i < m; i++) {
                if (A[i][q] > 0) {
                    double ratio = b[i] / A[i][q];
                    if (ratio < minRatio) {
                        minRatio = ratio;
                        s = i;
                    }
                }
            }
            if (s == -1) {
                return null;
            }
            double az = A[s][q];
            b[s] /= az;
            for (int j = 0; j < n; j++) {
                A[s][j] /= az;
            }
            for (int i = 0; i < m; i++) {
                if (i != s && A[i][q] != 0) {
                    double a = A[i][q];
                    b[i] -= a * b[s];
                    for (int j = 0; j < n; j++) {
                        A[i][j] -= a * A[s][j];
                    }
                }
            }
            c[q] /= az;
            for (int j = 0; j < n; j++) {
                c[j] -= c[q] * A[s][j];
            }
            B.remove((Integer) (n + s));
            B.add(q);
        }
        double[] x = new double[n];
        for (int i = 0; i < B.size(); i++) {
            int j = B.get(i);
            if (j < n) {
                x[j] = 0;
            } else {
                x[B.get(i) - n] = b[j - n];
            }
        }
        return x;
    }


 */


    public String simplex(double[][] A, double[] b, double[] c) {
        int n = c.length;
        int m = b.length;

        System.out.println(n + " N ");
        System.out.println(m + " M ");

        int[] B = new int[m];
        for (int i = 0; i < m; i++) {
            B[i] = n + i;
        }


        while (true) {
            int z = -1;
            for (int i = 0; i < n; i++) {
                if (c[i] > 0) {
                    z = i;
                    break;
                }
            }


            if (z == -1) {
                break;
            }

            int s = -1;
            double minRatio = Double.POSITIVE_INFINITY;
            for (int i = 0; i < m; i++) {
                if (A[i][z] > 0) {
                    double ratio = b[i] / A[i][z];
                    if (ratio < minRatio) {
                        s = i;
                        minRatio = ratio;
                    }
                }
            }


            if (s == -1) {
                return UNBOUNDED;
            }


            double[] temp = A[s];


            A[s] = A[0];
            A[0] = temp;


            double temp2 = b[s];
            b[s] = b[0];
            b[0] = temp2;

            int t = B[s];
            B[s] = B[0];
            B[0] = t;

            double azs = A[0][z];
            for (int j = 0; j < n; j++) {
                A[0][j] /= azs;
            }
            b[0] /= azs;


            for (int i = 1; i < m; i++) {
                double cs = A[i][z];
                for (int j = 0; j < n; j++) {
                    A[i][j] -= cs * A[0][j];
                }
                b[i] -= cs * b[0];
            }



            c[z] =  azs/c[z];

            for (int j = 0; j < c.length; j++) {
                if (j != z) {
                 //   System.out.println(c[j] + " minus " + c[z] * A[0][j]);
                    c[j] -= c[z] * A[z][j]; // A[0][j]
                //    System.out.println(c[j] + " Werde kleiner " + j);
                }
            }
            //   System.out.println(" ENd");
            A[z][s] = 1;
        }

        double[] x = new double[n];
        for (int i = 0; i < m; i++) {
            if (B[i] < n) {
                x[B[i]] = b[i];
            }
        }
      //  System.out.println("END");

        return Arrays.toString(x);
    }




    public void solve() {
        while (true) {
            // Compute reduced costs
            for (int j = 0; j < n; j++) {
                rc[j] = c[j];
                for (int i = 0; i < m; i++) {
                    rc[j] -= c[basis[i]] * A[j][i];

                }
            }


            // Check optimality
            int j = 0;
            System.out.println(rc.length + " Länge");
            for (j = 0; j < n; j++) {
                System.out.println(rc[j] + " rc");
                if (rc[j] < 0) break;
            }
            System.out.println(" UPDATe INDEX " + j);
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
            System.out.println(" UPDATe " + p + " INDEX " + q);
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

    public void getSolution() {
        for (int i = 0; i < basis.length; i++) {
            System.out.print(basis[i] + " ");
        }
        System.out.println("Matrix");
        System.out.println();
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }
    }


}

/*
public class Simplex {

    private double[][] distances;

    // the number of constraints
    int m;
    // the number of variables
    int n;
    // the constraint matrix
    double[][] A;
    // the right-hand side values
    double[] b;
    // the objective function coefficients
    double[] c;
    // the basic variables
    int[] basic;
    // the non-basic variables
    int[] nonbasic;


    public Simplex(double[][] A, double[] b, double[] c) {
        this.m = b.length;
        this.n = c.length;
        this.A = new double[m][n];

        System.out.println("n " + n + " m" + m);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.A[i][j] = A[i][j];
            }
        }
        this.b = Arrays.copyOf(b, m);
        this.c = Arrays.copyOf(c, n);
        this.basic = new int[m];
        this.nonbasic = new int[n];
        for (int i = 0; i < m; i++) {
            basic[i] = n + i;
        }
        for (int i = 0; i < n; i++) {
            nonbasic[i] = i;
        }
    }






    public double[] gen() {
        return primalerSimplex(A, b, c, basic);
    }


    public double[] primalerSimplex(double[][] A, double[] b, double[] c, int[] B) {
        while (true) {
            // find the index of the leaving variable
            int l = -1;
            double minRatio = Double.POSITIVE_INFINITY;
            for (int i = 0; i < b.length; i++) {
                if (b[i] < 0) {
                    return b;
                } else if (b[i] > 0) {
                    for (int j = 0; j < c.length; j++) {
                        if (A[i][j] > 0) {
                            double ratio = c[j] / A[i][j];
                            if (ratio < minRatio) {
                                l = i;
                                minRatio = ratio;
                            }
                        }
                    }
                }
            }
            if (l == -1) {
                break;
            }

            // find the index of the entering variable
            int e = -1;
            minRatio = Double.POSITIVE_INFINITY;
            for (int j = 0; j < c.length; j++) {
                if (A[l][j] < 0) {
                    double ratio = b[l] / -A[l][j];
                    if (ratio < minRatio) {
                        e = j;
                        minRatio = ratio;
                    }
                }
            }
            if (e == -1) {
                return b;
            }

            // update the basic variables and objective coefficients
            B[l] = e;
            c[e] = b[l] / A[l][e];
            for (int i = 0; i < b.length; i++) {
                if (i != l) {
                    b[i] -= A[i][e] * c[e];
                    A[i][e] = 0;
                }
            }
            for (int j = 0; j < c.length; j++) {
                if (j != e) {
                    c[j] -= A[l][j] * c[e];
                    A[l][j] = 0;
                }
            }
            A[l][e] = 1;
        }
        double[] x = new double[c.length];
        for (int i = 0; i < B.length; i++) {
            x[B[i]] = b[i];
        }
        return x;
    }

}




/*
 ----------------------------------------

 public class Simplex {
    private int n, m;
    private double[][] A;
    private double[] b, c;
    private double v;
    private int[] N, B;  // nonbasic & basic

    public Simplex(int n, int m, double[][] A, double[] b, double[] c, double v) {
        this.n = n;
        this.m = m;
        this.A = A;
        this.b = b;
        this.c = c;
        this.v = v;
    }

    // pivot yth variable around xth constraint
    public void pivot(int x, int y) {
        // Implementation here
    }

    // Run a single iteration of the simplex algorithm.
    // Returns: 0 if OK, 1 if STOP, -1 if UNBOUNDED
    public int iterateSimplex() {
        // Implementation here
        return 0;
    }

    // (Possibly) converts the LP into a slack form with a feasible basic solution.
    // Returns 0 if OK, -1 if INFEASIBLE
    public int initialiseSimplex() {
        // Implementation here
        return 0;
    }

    // Runs the simplex algorithm to optimise the LP.
    // Returns a Tuple containing a vector of -1s if unbounded, -2s if infeasible.
    public Tuple simplex() {
        // Implementation here
        return new Tuple(new ArrayList<Double>(), 0.0, 0);
    }
}

import java.util.Arrays;

public class TSPSimplex {
  private static final int MAX_ITER = 100000;
  private static final double EPSILON = 1e-6;

  private int N;
  private double[][] distances;

  public TSPSimplex(double[][] distances) {
    this.N = distances.length;
    this.distances = distances;
  }

  public double solve() {
    int M = N * (N - 1) / 2;
    double[] c = new double[M];
    double[][] A = new double[N][M];
    double[] b = new double[N];

    int k = 0;
    for (int i = 0; i < N; i++) {
      for (int j = i + 1; j < N; j++) {
        c[k] = distances[i][j];
        b[i] = 1;
        A[j][k] = 1;
        A[i][k++] = 1;
      }
    }

    double[] x = new double[M];
    Arrays.fill(x, 1.0 / M);

    int[] basis = new int[N];
    for (int i = 0; i < N; i++) {
      basis[i] = M + i;
    }

    for (int iter = 0; iter < MAX_ITER; iter++) {
      int nonBasis = 0;
      for (int i = 0; i < M; i++) {
        if (x[i] > EPSILON) {
          nonBasis = i;
          break;
        }
      }

      if (nonBasis == -1) {
        break;
      }

      int entering = nonBasis;
      int leaving = -1;
      double minRatio = Double.MAX_VALUE;
      for (int i = 0; i < N; i++) {
        if (A[i][entering] > EPSILON) {
          double ratio = b[i] / A[i][entering];
          if (ratio < minRatio) {
            minRatio = ratio;
            leaving = i;
          }
        }
      }

      if (leaving == -1) {
        break;
      }

      x[entering] = 0;
      x[basis[leaving]] = minRatio;
      b[leaving] /= A[leaving][entering];
      for (int j = 0; j < M; j++) {
        if (j != entering) {
          A[leaving][j] /= A[leaving][entering];
        }
      }
      for (int i = 0; i < N; i++) {
        if (i != leaving) {
          b[i] -= A[i][entering] * b[leaving];
          for (int j = 0; j < M; j++) {
            if (j

 */


