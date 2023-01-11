package com.hsb.tsp.graph;



import com.hsb.tsp.model.TSPInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class DistanceSection {
    protected Map<Integer, Node> nodes = new HashMap();


    public DistanceSection() {
    }

    public abstract void buildGraph(BufferedReader var1) throws IOException;

    public void addNode(Node node) {
        this.nodes.put(node.getId(), node);
    }

    public abstract double getDistanceBetween(int id1, int id2);

    public Map<Integer, Node> getNodes() {
        return this.nodes;
    }

    public void setNodes(Map<Integer, Node> nodes) {
        this.nodes = nodes;
    }


    public abstract int[] getNeighborsOf(int id);


    public abstract int[][] getAdjMatrix(TSPInstance problem);

    protected Node mapToNode(String[] values) {
        return new Node(Integer.parseInt(values[0]), Double.parseDouble(values[1]), Double.parseDouble(values[2]));
    }


    public boolean isNeighbor(int id1, int id2) {
        int[] neighbors = getNeighborsOf(id1);

        for (int neighbor : neighbors) {
            if (neighbor == id2) {
                return true;
            }
        }

        return false;
    }
}
