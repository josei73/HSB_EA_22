package com.hsb.tsp.model;

import com.hsb.tsp.fieldTypesAndFormats.*;
import com.hsb.tsp.graph.DistanceSection;
import com.hsb.tsp.graph.Node;

import java.util.HashMap;
import java.util.Map;

public class TSPInstance {
    // fields for simple file parsing
    private String name;
    private DataType dataType;
    private String comment;
    private int dimension;
    private EdgeWeightType edgeWeightType;
    private EdgeWeightFormat edgeWeightFormat;
    private EdgeDataFormat edgeDataFormat;
    private NodeCoordType nodeCoordinateType;
    private DisplayDataType displayDataType;

    // fields for parsing depending on previous fields
    private DistanceSection distanceSection;
    private DistanceSection fixedEdge;
    private DistanceSection displayData;

    // fields for view
    private TSPTour tour;

    public TSPInstance() {}

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }
    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getDimension() {
        return dimension;
    }
    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public EdgeWeightType getEdgeWeightType() {
        return edgeWeightType;
    }
    public void setEdgeWeightType(EdgeWeightType edgeWeightType) {
        this.edgeWeightType = edgeWeightType;
    }

    public EdgeWeightFormat getEdgeWeightFormat() {
        return edgeWeightFormat;
    }
    public void setEdgeWeightFormat(EdgeWeightFormat edgeWeightFormat) {
        this.edgeWeightFormat = edgeWeightFormat;
    }

    public EdgeDataFormat getEdgeDataFormat() {
        return edgeDataFormat;
    }
    public void setEdgeDataFormat(EdgeDataFormat edgeDataFormat) {
        this.edgeDataFormat = edgeDataFormat;
    }

    public NodeCoordType getNodeCoordinateType() {
        return nodeCoordinateType;
    }
    public void setNodeCoordinateType(NodeCoordType nodeCoordinateType) {
        this.nodeCoordinateType = nodeCoordinateType;
    }

    public DisplayDataType getDisplayDataType() {
        return displayDataType;
    }
    public void setDisplayDataType(DisplayDataType displayDataType) {
        this.displayDataType = displayDataType;
    }

    public DistanceSection getDistanceSection() {
        return distanceSection;
    }
    public void setDistanceSection(DistanceSection distanceSection) {
        this.distanceSection = distanceSection;
    }

    public DistanceSection getFixedEdge() {
        return fixedEdge;
    }
    public void setFixedEdge(DistanceSection fixedEdge) {
        this.fixedEdge = fixedEdge;
    }

    public DistanceSection getDisplayData() {
        return displayData;
    }
    public void setDisplayData(DistanceSection displayData) {
        this.displayData = displayData;
    }

    public TSPTour getTour() {
        return tour;
    }
    public void setTour(TSPTour tour) {
        this.tour = tour;
    }

    public String getProblemName() {
        try {
            String lines[] = this.getComment().split("\\r?\\n");
            return this.getName() + ": " + lines[0];
        } catch (NullPointerException e) {
            throw new NullPointerException("Instance Name or Comment is null");
        }
    }

    public Map<Integer, Node> getNodes() {
        //TODO some instances have nodes saved in a different variable
        if (this.getEdgeWeightType() == EdgeWeightType.EXPLICIT) {
            if (this.displayDataType == null) {
                return new HashMap<>();
            } else {
                return this.getDisplayData().getNodes();
            }
        } else {
            return this.getDistanceSection().getNodes();
        }
    }
}
