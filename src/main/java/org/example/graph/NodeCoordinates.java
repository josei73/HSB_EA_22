package org.example.graph;

import org.example.fieldTypesAndFormats.EdgeWeightType;
import org.example.fieldTypesAndFormats.NodeCoordType;
import org.example.utils.DistanceFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public class NodeCoordinates extends DistanceSection {
    private final int nodesSize;
    private NodeCoordType edgeWeightType;
    BiFunction<Node, Node, Integer> distanceFunction;

    public NodeCoordinates(int nodesSize, EdgeWeightType edgeWeightType) {
        this.nodesSize = nodesSize;
        this.edgeWeightType = edgeWeightType.getNodeCoordType();
        distanceFunction = DistanceFunction.getDistanceFunction(edgeWeightType);
    }

    public NodeCoordinates(int dimension, NodeCoordType twodCoords) {
        this.nodesSize = dimension;
        this.edgeWeightType = twodCoords;
    }

    public void buildGraph(BufferedReader reader) throws IOException {
        for (int i = 0; i < this.nodesSize; ++i) {
            String line = reader.readLine();
            String[] tokens = line.trim().split("\\s+");
            Node node = this.mapToNode(tokens);
            this.addNode(node);
        }
    }

    @Override
    public double getDistanceBetween(int id1, int id2) {
        Node node1 = nodes.get(id1);
        Node node2 = nodes.get(id2);

        if( node1 == null) throw new NoSuchElementException("Node1 not found");
        if( node2 == null) throw new NoSuchElementException("Node2 not found");

       return distanceFunction.apply(node1,node2);
    }
}
