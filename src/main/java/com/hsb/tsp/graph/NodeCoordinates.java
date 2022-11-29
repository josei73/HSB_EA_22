package com.hsb.tsp.graph;



import com.hsb.tsp.fieldTypesAndFormats.EdgeWeightType;
import com.hsb.tsp.fieldTypesAndFormats.NodeCoordType;
import com.hsb.tsp.utils.DistanceFunction;

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

    public NodeCoordinates(int nodesSize, NodeCoordType edgeWeightType) {
        this.nodesSize = nodesSize;
        this.edgeWeightType = edgeWeightType;
    }

    @Override
    public int[] getNeighborsOf(int id) {
        int index = 0;
        int[] neighbors = new int[nodesSize-1];

        if (!nodes.containsKey(id)) {
            throw new IllegalArgumentException("no node with identifier " + id);
        }

        for (Node node : nodes.values()) {
            if (node.getId() != id) {
                neighbors[index++] = node.getId();
            }
        }

        return neighbors;
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
