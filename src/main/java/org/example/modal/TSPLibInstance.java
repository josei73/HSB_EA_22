package org.example.modal;

import org.example.fieldTypesAndFormats.*;
import org.example.graph.DistanceSection;
import org.example.graph.EdgeData;
import org.example.graph.EdgeWeightMatrix;
import org.example.graph.NodeCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPLibInstance {
    private String name;
    private DataType dataType;
    private String comment;
    private int dimension;
    private EdgeWeightType edgeWeightType;
    private EdgeWeightFormat edgeWeightFormat;
    private EdgeDataFormat edgeDataFormat;
    private NodeCoordType nodeCoordinateType;
    private DisplayDataType displayDataType;
    private DistanceSection distanceSection;
    private DistanceSection fixedEdge;
    private DistanceSection displayData;
    private List<Tour> tours = new ArrayList();

    public TSPLibInstance() {
    }

    public void load(BufferedReader reader) throws IOException {
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.equals("NODE_COORD_SECTION")) {
                    if (this.nodeCoordinateType == null) {
                        this.nodeCoordinateType = this.edgeWeightType.getNodeCoordType();
                    }

                    this.distanceSection = new NodeCoordinates(this.dimension, this.edgeWeightType);
                    this.distanceSection.buildGraph(reader);
                } else if (line.equals("EDGE_DATA_SECTION")) {
                    this.distanceSection = new EdgeData(this.dimension, this.edgeDataFormat);
                    this.distanceSection.buildGraph(reader);
                } else if (line.equals("FIXED_EDGES_SECTION")) {
                    this.fixedEdge = new EdgeData(this.dimension, EdgeDataFormat.EDGE_LIST);
                    this.fixedEdge.buildGraph(reader);
                } else if (line.equals("TOUR_SECTION") && line.equals("-1")) {
                    Tour tour = new Tour();
                    tour.load(reader);
                    tours.add(tour);
                } else if (line.equals("DISPLAY_DATA_SECTION")) {
                    this.displayData = new NodeCoordinates(this.dimension, NodeCoordType.TWOD_COORDS);
                    this.displayData.buildGraph(reader);
                } else if (line.equals("EDGE_WEIGHT_SECTION")) {
                    this.distanceSection = new EdgeWeightMatrix(this.dimension, this.edgeWeightFormat);
                    this.distanceSection.buildGraph(reader);
                } else {
                    if (line.equals("EOF")) {
                        break;
                    }

                    if (!line.isEmpty()) {
                        String[] tokens = line.split(":");
                        String key = tokens[0].trim();
                        System.out.println(tokens[0]);
                        String value = tokens[1].trim();
                        if (key.equals("NAME")) {
                            this.name = value;
                        } else if (key.equals("COMMENT")) {
                            if (this.comment == null) {
                                this.comment = value;
                            } else {
                                this.comment = this.comment + "\n" + value;
                            }
                        } else if (key.equals("TYPE")) {
                            this.dataType = DataType.valueOf(value);
                        } else if (key.equals("DIMENSION")) {
                            this.dimension = Integer.parseInt(value);
                        } else if (key.equals("EDGE_WEIGHT_TYPE")) {
                            this.edgeWeightType = EdgeWeightType.valueOf(value);
                        } else if (key.equals("EDGE_WEIGHT_FORMAT")) {
                            this.edgeWeightFormat = EdgeWeightFormat.valueOf(value);
                        } else if (key.equals("EDGE_DATA_FORMAT")) {
                            this.edgeDataFormat = EdgeDataFormat.valueOf(value);
                        } else if (key.equals("NODE_COORD_FORMAT")) {
                            this.nodeCoordinateType = NodeCoordType.valueOf(value);
                        } else if (key.equals("DISPLAY_DATA_TYPE")) {
                            this.displayDataType = DisplayDataType.valueOf(value);
                        }
                    }
                }
            }

        } finally {
            if (reader != null) {
                reader.close();
            }

        }

        if (this.nodeCoordinateType == null) {
            this.nodeCoordinateType = NodeCoordType.NO_COORDS;
        }

        if (this.displayDataType == null) {
            if (NodeCoordType.NO_COORDS.equals(this.nodeCoordinateType)) {
                this.displayDataType = DisplayDataType.NO_DISPLAY;
            } else if (this.displayData != null) {
                this.displayDataType = DisplayDataType.TWOD_DISPLAY;
            } else {
                this.displayDataType = DisplayDataType.COORD_DISPLAY;
            }
        }

    }

    public void addTour(BufferedReader reader) throws IOException {
        TSPLibInstance problem = new TSPLibInstance();
        problem.load(reader);
        if (problem.getDataType().equals(DataType.TOUR)) {
            this.tours.addAll(problem.getTours());
        } else {
            throw new IllegalArgumentException("not a tour file");
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public EdgeWeightType getEdgeWeightType() {
        return this.edgeWeightType;
    }

    public void setEdgeWeightType(EdgeWeightType edgeWeightType) {
        this.edgeWeightType = edgeWeightType;
    }

    public EdgeWeightFormat getEdgeWeightFormat() {
        return this.edgeWeightFormat;
    }

    public void setEdgeWeightFormat(EdgeWeightFormat edgeWeightFormat) {
        this.edgeWeightFormat = edgeWeightFormat;
    }

    public EdgeDataFormat getEdgeDataFormat() {
        return this.edgeDataFormat;
    }

    public void setEdgeDataFormat(EdgeDataFormat edgeDataFormat) {
        this.edgeDataFormat = edgeDataFormat;
    }

    public NodeCoordType getNodeCoordinateType() {
        return this.nodeCoordinateType;
    }

    public void setNodeCoordinateType(NodeCoordType nodeCoordinateType) {
        this.nodeCoordinateType = nodeCoordinateType;
    }

    public DisplayDataType getDisplayDataType() {
        return this.displayDataType;
    }

    public void setDisplayDataType(DisplayDataType displayDataType) {
        this.displayDataType = displayDataType;
    }

    public List<Tour> getTours() {
        return this.tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    public DistanceSection getDistanceSection() {
        return this.distanceSection;
    }

    public void setDistanceSection(DistanceSection distanceSection) {
        this.distanceSection = distanceSection;
    }
}
