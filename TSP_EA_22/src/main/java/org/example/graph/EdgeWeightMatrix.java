package org.example.graph;

import org.example.fieldTypesAndFormats.EdgeWeightFormat;

import java.io.BufferedReader;
import java.io.IOException;

public class EdgeWeightMatrix extends DistanceSection {
    private int size;
    private EdgeWeightFormat edgeWeightFormat;

    public EdgeWeightMatrix(int size, EdgeWeightFormat edgeWeightFormat) {
        this.size = size;
        this.edgeWeightFormat = edgeWeightFormat;
    }

    public void buildGraph(BufferedReader reader) throws IOException {
    }
}