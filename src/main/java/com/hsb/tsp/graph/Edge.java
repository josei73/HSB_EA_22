package com.hsb.tsp.graph;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Edge {
    private int node1;
    private int node2;

    private double weight;

    public Edge(int node1, int node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public Edge(int node1, int node2, double nodeWeight) {
        this.node1 = node1;
        this.node2 = node2;
        weight = nodeWeight;
    }

    public double getDist() {
        return weight;
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