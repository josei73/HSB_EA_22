package org.example.graph;

import org.example.fieldTypesAndFormats.EdgeWeightType;
import org.example.fieldTypesAndFormats.NodeCoordType;
import org.example.fieldTypesAndFormats.EdgeWeightFormat;

import java.io.BufferedReader;
import java.io.IOException;

public class NodeCoordinates extends DistanceSection {
    private final int nodesSize;
    private NodeCoordType edgeWeightType;

    public NodeCoordinates(int nodesSize, EdgeWeightType edgeWeightType) {
        this.nodesSize = nodesSize;
        this.edgeWeightType = edgeWeightType.getNodeCoordType();
    }

    public NodeCoordinates(int nodesSize, NodeCoordType edgeWeightType) {
        this.nodesSize = nodesSize;
        this.edgeWeightType = edgeWeightType;
    }

    public void buildGraph(BufferedReader reader) throws IOException {
        for (int i = 0; i < this.nodesSize; ++i) {
            String line = reader.readLine();
            String[] tokens = line.trim().split("\\s+");
            Node node = this.mapToNode(tokens);
            this.addNode(node);
        }

    }
}
