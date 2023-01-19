package com.hsb.tsp.model;

import com.hsb.tsp.fieldTypesAndFormats.EdgeWeightType;
import com.hsb.tsp.graph.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
@Getter
@Setter
@ToString
public class TSPModel {
    private String name;
    private String problemName;
    private Map<Integer, Node> nodes;
    private TSPTour tour;
    private EdgeWeightType type;

    public  TSPModel(String name, Map<Integer, Node> nodes, EdgeWeightType type) {
        this.name = name;
        this.problemName = name.split(":")[0];
        this.nodes = nodes;
        this.tour = tour;
        this.type = type;
    }


    public TSPModel(String name, Map<Integer, Node> nodes,  EdgeWeightType type,TSPTour tour) {
        this.name = name;
        this.problemName = name.split(":")[0];
        this.nodes = nodes;
        this.tour = tour;
        this.type = type;
    }
}
