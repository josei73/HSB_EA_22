package com.hsb.tsp.algorithms;

import java.util.ArrayList;
import java.util.Random;

public class RandomTour extends Algorithm {

    private int size;


    private ArrayList<Integer> tour = new ArrayList<>();
    private boolean ranSolver = false;

    public RandomTour(int size) {
        this.size = size;
    }


    public ArrayList<Integer> getTour() {
        if (!ranSolver) solve();
        return tour;
    }


    public void solve() {
        createRandomTour();
        ranSolver = true;
    }

    private int[] createRandomTour() {
        // init array
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }

        Random random = new Random();

        for (int i = 0; i < size; ++i) {
            int index = random.nextInt(i + 1);
            // Simple swap
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }


        for (int i = 0; i < size; ++i)
            tour.add(array[i] + 1);
        tour.add(array[0] + 1);
        return array;
    }
}
