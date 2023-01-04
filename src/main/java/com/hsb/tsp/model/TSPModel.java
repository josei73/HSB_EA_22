package com.hsb.tsp.model;

import com.hsb.tsp.graph.Node;

import java.util.Map;

public class TSPModel {
    private String name;
    private Map<Integer, Node> nodes;

    public TSPModel(String name, Map<Integer, Node> nodes) {
        this.name = name;
        this.nodes = nodes;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }
    public void setNodes(Map<Integer, Node> nodes) {
        this.nodes = nodes;
    }
}
