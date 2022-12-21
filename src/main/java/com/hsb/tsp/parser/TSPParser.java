package com.hsb.tsp.parser;

import com.hsb.tsp.fieldTypesAndFormats.*;
import com.hsb.tsp.graph.DistanceSection;
import com.hsb.tsp.graph.EdgeData;
import com.hsb.tsp.graph.EdgeWeightMatrix;
import com.hsb.tsp.graph.NodeCoordinates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TSPParser {
    final String PATH = "data/tsp/";

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
    private List<Tour> tours = new ArrayList();
    private BufferedReader reader;
    public TSPParser() {}

    public static void main(String[] args) throws IOException {
        TSPParser problem = new TSPParser();
        //problem.loadInstance("a280.tsp");
    }

    public void loadInstance(String filename) throws IOException {
        try {
            reader = new BufferedReader(new FileReader(PATH + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String line = "";
        while (!line.equals("EOF")) {
            line = reader.readLine();
            line = line.trim();
            if(line.contains(":")) {
                parseFileSpecification(line);
            } else {
                parseFileData(line, reader);
            }
        }
    }

    public void addOptimalTour(String filename) throws IOException {
        TSPParser problem = new TSPParser();
        problem.loadInstance(filename);
        if (problem.getDataType().equals(DataType.TOUR)) {
            this.tours.addAll(problem.getTours());
        } else {
            throw new IllegalArgumentException("not a tour file");
        }
    }

    public Set<String> loadAllNames() {
        File[] listOfFiles = new File(PATH).listFiles((dir, name) -> name.toLowerCase().endsWith(".tsp"));
        Set<String> problemNames = new HashSet<>();
        for (File file : listOfFiles) {
            try {
                reader = new BufferedReader(new FileReader(file));
                problemNames.add(parseProblemName(reader));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return problemNames;
    }

    private void parseFileSpecification(String line) {
        if(line.isEmpty()) { return; }
        String[] tokens = line.split(":");
        String key = tokens[0].trim();
        String value = tokens[1].trim();
        switch (key) {
            case "NAME":
                this.name = value;
                break;
            case "COMMENT":
                if (this.comment == null) {
                    this.comment = value;
                } else {
                    this.comment = this.comment + "\n" + value;
                }
                break;
            case "TYPE":
                this.dataType = DataType.valueOf(value);
                break;
            case "DIMENSION":
                this.dimension = Integer.parseInt(value);
                break;
            case "EDGE_WEIGHT_TYPE":
                this.edgeWeightType = EdgeWeightType.valueOf(value);
                break;
            case "EDGE_WEIGHT_FORMAT":
                this.edgeWeightFormat = EdgeWeightFormat.valueOf(value);
                break;
            case "EDGE_DATA_FORMAT":
                this.edgeDataFormat = EdgeDataFormat.valueOf(value);
                break;
            case "NODE_COORD_FORMAT":
                this.nodeCoordinateType = NodeCoordType.valueOf(value);
                break;
            case "DISPLAY_DATA_TYPE":
                this.displayDataType = DisplayDataType.valueOf(value);
                break;
        }
    }

    private void parseFileData(String line, BufferedReader reader) throws IOException {
        if(line.isEmpty()) { return; }
        switch (line) {
            case "NODE_COORD_SECTION":
                if (this.nodeCoordinateType == null) {
                    this.nodeCoordinateType = this.edgeWeightType.getNodeCoordType();
                }
                this.distanceSection = new NodeCoordinates(this.dimension, this.edgeWeightType);
                this.distanceSection.buildGraph(reader);
                break;
            case "EDGE_DATA_SECTION":
                this.distanceSection = new EdgeData(this.dimension, this.edgeDataFormat);
                this.distanceSection.buildGraph(reader);
                break;
            case "EDGE_WEIGHT_SECTION":
                this.distanceSection = new EdgeWeightMatrix(this.dimension, this.edgeWeightFormat);
                this.distanceSection.buildGraph(reader);
                break;
            case "FIXED_EDGES_SECTION":
                this.fixedEdge = new EdgeData(this.dimension, EdgeDataFormat.EDGE_LIST);
                this.fixedEdge.buildGraph(reader);
                break;
            case "TOUR_SECTION":
            case "-1":
                Tour tour = new Tour();
                tour.load(reader);
                tours.add(tour);
                break;
            case "DISPLAY_DATA_SECTION":
                this.displayData = new NodeCoordinates(this.dimension, NodeCoordType.TWOD_COORDS);
                this.displayData.buildGraph(reader);
                break;
        }
    }

    private String parseProblemName(BufferedReader reader) throws IOException {
        String name = null;
        String comment = null;
        String line = "";

        while (!line.equals("EOF")) {
            line = reader.readLine();
            line = line.trim();
            if (!line.contains(":")) {
                break;
            }

            String[] tokens = line.split(":");
            String key = tokens[0].trim();
            String value = tokens[1].trim();

            if (key.equals("NAME")) {
                name = value;
            }
            if (key.equals("COMMENT")) {
                comment = value;
            }
        }
        return name + ": " +comment;
    }

    public String getName() {
        return name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public String getComment() {
        return comment;
    }

    public int getDimension() {
        return dimension;
    }

    public EdgeWeightType getEdgeWeightType() {
        return edgeWeightType;
    }

    public EdgeWeightFormat getEdgeWeightFormat() {
        return edgeWeightFormat;
    }

    public EdgeDataFormat getEdgeDataFormat() {
        return edgeDataFormat;
    }

    public NodeCoordType getNodeCoordinateType() {
        return nodeCoordinateType;
    }

    public DisplayDataType getDisplayDataType() {
        return displayDataType;
    }

    public DistanceSection getDistanceSection() {
        return distanceSection;
    }

    public DistanceSection getFixedEdge() {
        return fixedEdge;
    }

    public DistanceSection getDisplayData() {
        return displayData;
    }

    public List<Tour> getTours() {
        return tours;
    }
}
