package com.hsb.tsp.model;

import com.hsb.tsp.fieldTypesAndFormats.*;
import com.hsb.tsp.graph.DistanceSection;
import com.hsb.tsp.graph.Node;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
@Getter
@Setter
@ToString
@NoArgsConstructor

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
