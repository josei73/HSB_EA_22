package com.hsb.tsp.utils;

import com.hsb.tsp.fieldTypesAndFormats.*;
import com.hsb.tsp.graph.EdgeData;
import com.hsb.tsp.graph.EdgeWeightMatrix;
import com.hsb.tsp.graph.NodeCoordinates;
import com.hsb.tsp.model.TSPInstance;
import com.hsb.tsp.model.TSPTour;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
@NoArgsConstructor
@Component
public class TSPParser {
    final String PATH = "data/tsp/";


    public TSPInstance loadInstance(String filename) throws IOException {
        TSPInstance instance = new TSPInstance();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(PATH + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String line = null;
        while ((line = reader.readLine()) != null && !line.equals("EOF")) {
            line = line.trim();
            if (line.contains(":")) {
                parseFileSpecification(instance, line);
            } else {
                parseFileData(instance, line, reader);
            }
        }
        return instance;
    }


    private void parseFileSpecification(TSPInstance instance, String line) {
        if (line.isEmpty()) {
            return;
        }
        String[] tokens = line.split(":");
        String key = tokens[0].trim();
        String value = tokens[1].trim();
        switch (key) {
            case "NAME":
                instance.setName(value);
                break;
            case "COMMENT":
                if (instance.getComment() == null) {
                    instance.setComment(value);
                } else {
                    instance.setComment(instance.getComment() + "\n" + value);
                }
                break;
            case "TYPE":
                instance.setDataType(DataType.valueOf(value));
                break;
            case "DIMENSION":
                instance.setDimension(Integer.parseInt(value));
                break;
            case "EDGE_WEIGHT_TYPE":
                instance.setEdgeWeightType(EdgeWeightType.valueOf(value));
                break;
            case "EDGE_WEIGHT_FORMAT":
                instance.setEdgeWeightFormat(EdgeWeightFormat.valueOf(value));
                break;
            case "EDGE_DATA_FORMAT":
                instance.setEdgeDataFormat(EdgeDataFormat.valueOf(value));
                break;
            case "NODE_COORD_FORMAT":
                instance.setNodeCoordinateType(NodeCoordType.valueOf(value));
                break;
            case "DISPLAY_DATA_TYPE":
                instance.setDisplayDataType(DisplayDataType.valueOf(value));
                break;
        }
    }

    private void parseFileData(TSPInstance instance, String line, BufferedReader reader) throws IOException {
        if (line.isEmpty()) {
            return;
        }
        switch (line) {
            case "NODE_COORD_SECTION":
                if (instance.getNodeCoordinateType() == null) {
                    instance.setNodeCoordinateType(instance.getEdgeWeightType().getNodeCoordType());
                }
                instance.setDistanceSection(new NodeCoordinates(instance.getDimension(), instance.getEdgeWeightType()));
                instance.getDistanceSection().buildGraph(reader);
                break;
            case "EDGE_DATA_SECTION":
                instance.setDistanceSection(new EdgeData(instance.getDimension(), instance.getEdgeDataFormat()));
                instance.getDistanceSection().buildGraph(reader);
                break;
            case "EDGE_WEIGHT_SECTION":
                instance.setDistanceSection(new EdgeWeightMatrix(instance.getDimension(), instance.getEdgeWeightFormat()));
                instance.getDistanceSection().buildGraph(reader);
                break;
            case "FIXED_EDGES_SECTION":
                instance.setFixedEdge(new EdgeData(instance.getDimension(), EdgeDataFormat.EDGE_LIST));
                instance.getFixedEdge().buildGraph(reader);
                break;
            case "TOUR_SECTION":
            case "-1":
                TSPTour tour = new TSPTour();
                tour.load(reader);
                instance.setTour(tour);
                break;
            case "DISPLAY_DATA_SECTION":
                instance.setDisplayData(new NodeCoordinates(instance.getDimension(), NodeCoordType.TWOD_COORDS));
                instance.getDisplayData().buildGraph(reader);
                break;
        }
    }
}
