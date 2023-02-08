package com.hsb.tsp.algorithms;

public class TspSimplex {
    private double[][] A;
    private double[] b;
    private double[] c;
    private int[] B;
    private int n;
    private int m;

    public TspSimplex(double[][] A, double[] b, double[] c, int n, int m) {
        this.A = A;
        this.b = b;
        this.c = c;
        this.n = c.length;
        this.m = b.length;
        System.out.println(n+" N M "+m);
        this.B = new int[m];
        for (int i = 0; i < m; i++) {
            B[i] = n + i;
        }
    }

    public void solve() {
        while (true) {
            int p = -1;
            for (int i = 0; i < n; i++) {
                if (c[i] > 0) {
                    p = i;
                    break;
                }
            }
            if (p == -1) {
                break;
            }

            double minRatio = Double.POSITIVE_INFINITY;
            int q = -1;
            for (int j = 0; j < m; j++) {
                if (A[j][p] > 0) {
                    double ratio = b[j] / A[j][p];
                    if (ratio < minRatio) {
                        minRatio = ratio;
                        q = j;
                    }
                }
            }
            if (q == -1) {
                System.out.println("Unbounded");
                return;
            }

            double[] tempA = A[q];
            A[q] = A[m - 1];
            A[m - 1] = tempA;

            double temp = b[q];
            b[q] = b[m - 1];
            b[m - 1] = temp;

            int tempB = B[q];
            B[q] = B[m - 1];
            B[m - 1] = tempB;

            double az = A[m - 1][p];
            b[m - 1] /= az;
            for (int j = 0; j < n; j++) {
                A[m - 1][j] /= az;
            }
            for (int i = 0; i < m - 1; i++) {
                double as = A[i][p];
                b[i] -= as * b[m - 1];
                for (int j = 0; j < n; j++) {
                    A[i][j] -= as * A[m - 1][j];
                }
            }
            c[p] /= az;
            for (int j = 0; j < n; j++) {
                if (j != p) {
                    c[j] -= c[p] * A[m - 1][j];
                }
            }
            m--;
        }
        System.out.println("ENDE");

        double[] x = new double[n];
        for (int i = 0; i < m; i++) {
            System.out.println(B[i]);
            x[B[i] - n] = b[i];
        }
    }
}