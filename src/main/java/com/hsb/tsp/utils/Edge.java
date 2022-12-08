package com.hsb.tsp.utils;

public class Edge {
    private int node1;
    private int node2;

    private double weight;

    public Edge(int node1, int node2, double nodeWeight) {
        this.node1 = node1;
        this.node2 = node2;
        weight = nodeWeight;
    }

    public int getNode1() {
        return this.node1;
    }

    public void setNode1(int node1) {
        this.node1 = node1;
    }

    public int getNode2() {
        return this.node2;
    }

    public void setNode2(int node2) {
        this.node2 = node2;
    }

    public double getDist() {
        return weight;
    }
    public void setDist(double nodeWeight) {
        weight = nodeWeight;
    }
    public boolean hasEndpoint(int id) {
        return (id == node1) || (id == node2);
    }

    public int getOppositeEndpoint(int id) {
        if (id == node1) return node2;
        if (id == node2) return node1;

        throw new IllegalArgumentException("Edge has not endpoint " +
                id);

    }

}