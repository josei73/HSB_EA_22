package com.hsb.tsp.graph;
import com.hsb.tsp.fieldTypesAndFormats.EdgeDataFormat;
import com.hsb.tsp.parser.TSPLibInstance;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class EdgeData extends DistanceSection {
    private final int size;
    private EdgeDataFormat edgeDataFormat;
    private List<Edge> edges;

    public EdgeData(int size, EdgeDataFormat edgeDataFormat) {
        this.size = size;
        this.edgeDataFormat = edgeDataFormat;
        this.edges = new ArrayList();
    }

    public void buildGraph(BufferedReader reader) throws IOException {
        String line = null;
        if (EdgeDataFormat.EDGE_LIST.equals(this.edgeDataFormat)) {
            this.buildForEdgeList(reader);
        } else if (EdgeDataFormat.ADJ_LIST.equals(this.edgeDataFormat)) {
            this.buildForAdjList(reader);
        }

    }

    private void buildForAdjList(BufferedReader reader) throws IOException {
        String line = null;

        while((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.equals("-1")) {
                return;
            }

            String[] tokens = line.split("\\s+");
            int node1 = Integer.parseInt(tokens[0]);
            int node2 = Integer.parseInt(tokens[1]);
            this.addEdge(node1, node2);
        }

    }

    private void addEdge(int id1, int id2) {
        if (id1 >= 0 && id2 >= 0) {
            this.edges.add(new Edge(id1, id2));
        } else {
            throw new IllegalArgumentException("no node " + id1 + "  " + id2);
        }
    }

    private void buildForEdgeList(BufferedReader reader) throws IOException {
        String line = null;
        Queue<Integer> nodes = new LinkedList();

        while((line = reader.readLine()) != null) {
            String[] tokens = line.split("\\s+");

            for(int i = 0; i < tokens.length; ++i) {
                nodes.add(Integer.parseInt(tokens[i]));
            }

            this.createEdge(nodes);
        }

    }

    public int[] getNeighborsOf(int id) {
        if ((id < 0) || (id > size-1)) {
            throw new IllegalArgumentException("no node with identifier " + id);
        }

        List<Integer> neighbors = new ArrayList<Integer>();

        for (Edge edge : edges) {
            if (edge.hasEndpoint(id)) {
                neighbors.add(edge.getOppositeEndpoint(id));
            }
        }

        // copy neighbors to an array
        int[] result = new int[neighbors.size()];

        for (int i = 0; i < neighbors.size(); i++) {
            result[i] = neighbors.get(i);
        }

        return result;
    }



    @Override
    public double[][] getAdjMatrix(TSPLibInstance problem) {
        return new double[0][];
    }


    @Override
    public double getDistanceBetween(int id1, int id2) {
        if (isNeighbor(id1, id2)) {
            return 1.0;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }

    private void createEdge(Queue<Integer> nodes) {
        Integer nodeX = (Integer)nodes.poll();

        while((Integer)nodes.peek() != -1) {
            Integer adjacent = (Integer)nodes.poll();
            if (adjacent != null && adjacent != -1) {
                this.addEdge(nodeX, adjacent);
            }
        }

    }
}