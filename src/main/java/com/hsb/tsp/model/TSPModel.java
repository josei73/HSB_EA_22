package com.hsb.tsp.model;

import com.hsb.tsp.fieldTypesAndFormats.EdgeWeightType;
import com.hsb.tsp.graph.Node;

import java.util.Map;

public class TSPModel {
    private String name;
    private Map<Integer, Node> nodes;
    private TSPTour tour;
    private EdgeWeightType type;

    public TSPModel(String name, Map<Integer, Node> nodes, EdgeWeightType type) {
        this.name = name;
        this.nodes = nodes;
        this.type = type;
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

    public TSPTour getTour() {
        return tour;
    }
    public void setTour(TSPTour tour) {
        this.tour = tour;
    }

    public EdgeWeightType getType() {
        return type;
    }
    public void setType(EdgeWeightType type) {
        this.type = type;
    }
}
