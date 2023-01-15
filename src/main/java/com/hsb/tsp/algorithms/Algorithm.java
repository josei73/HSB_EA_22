package com.hsb.tsp.algorithms;

import java.util.ArrayList;


public abstract class Algorithm {
    final String name;

    protected Algorithm(String name) {
        this.name = name;
    }

    public abstract ArrayList<Integer> getTour();
    public abstract void solve();

    public String getName() {
        return name;
    }
}
