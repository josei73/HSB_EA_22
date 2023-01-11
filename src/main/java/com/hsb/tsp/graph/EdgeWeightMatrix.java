package com.hsb.tsp.graph;

import com.hsb.tsp.fieldTypesAndFormats.EdgeWeightFormat;
import com.hsb.tsp.model.TSPInstance;


import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class EdgeWeightMatrix extends DistanceSection {
    private int size;
    private EdgeWeightFormat edgeWeightFormat;

    private int[][] matrix;

    public EdgeWeightMatrix(int size, EdgeWeightFormat edgeWeightFormat) {
        this.size = size;
        this.edgeWeightFormat = edgeWeightFormat;
        matrix = new int[size][size];
    }

    private void readNextLine(BufferedReader reader, Queue<Integer> entries)
            throws IOException {
        String line = null;

        do {
            line = reader.readLine();

            if (line == null) {
                throw new EOFException("unexpectedly reached EOF");
            }
        } while ((line = line.trim()).isEmpty());

        String[] tokens = line.split("\\s+");

        for (int i = 0; i < tokens.length; i++) {
            entries.offer(Integer.parseInt(tokens[i]));
        }
    }

    @Override
    public double getDistanceBetween(int id1, int id2) {
        if ((id1 < 0) || (id1 > size-1)) {
            throw new IllegalArgumentException("no node with identifier " +
                    id1);
        }


        if ((id2 < 0) || (id2 > size-1)) {
            throw new IllegalArgumentException("no node with identifier " +
                    id2);
        }

        return matrix[id1][id2];
    }


    public void buildGraph(BufferedReader reader) throws IOException {

        Queue<Integer> entries = new LinkedList<Integer>();

        switch (edgeWeightFormat) {
            case FULL_MATRIX:
                putDataFullMatrix(reader, entries);

                break;
            case UPPER_ROW:
                upperRow(reader, entries);

                break;
            case UPPER_DIAG_ROW:
                UpperDiagRow(reader, entries);
            case LOWER_ROW:
                lowerRow(reader, entries);

                break;
            case LOWER_DIAG_ROW:
                putEdgeDataLowerDiagRow(reader, entries);

                break;
            case UPPER_COL:
                UpperCol(reader, entries);

                break;
            case UPPER_DIAG_COL:
                putEdgeDataForUpperDiagCol(reader, entries);

                break;
            case LOWER_COL:
                putEdgeDataForLowerCol(reader, entries);

                break;
            case LOWER_DIAG_COL:
                putEdgeDataForLowerDiagCol(reader, entries);

                break;
            default:
                throw new IllegalArgumentException("unsupported matrix type");
        }

        // sanity check
        if (!entries.isEmpty()) {
            throw new IOException("edge weight matrix is longer than expected");
        }
    }

    private void lowerRow(BufferedReader reader, Queue<Integer> entries) throws IOException {
        for (int i = 1; i < size; i++) {
            for (int j = 0; j < i; j++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    private void UpperDiagRow(BufferedReader reader, Queue<Integer> entries) throws IOException {
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    private void upperRow(BufferedReader reader, Queue<Integer> entries) throws IOException {
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    private void putDataFullMatrix(BufferedReader reader, Queue<Integer> entries) throws IOException {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
            }
        }
    }

    private void putEdgeDataLowerDiagRow(BufferedReader reader, Queue<Integer> entries) throws IOException {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i + 1; j++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    private void UpperCol(BufferedReader reader, Queue<Integer> entries) throws IOException {
        for (int j = 1; j < size; j++) {
            for (int i = 0; i < j; i++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    private void putEdgeDataForUpperDiagCol(BufferedReader reader, Queue<Integer> entries) throws IOException {
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < j + 1; i++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    private void putEdgeDataForLowerCol(BufferedReader reader, Queue<Integer> entries) throws IOException {
        for (int j = 0; j < size - 1; j++) {
            for (int i = j + 1; i < size; i++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    private void putEdgeDataForLowerDiagCol(BufferedReader reader, Queue<Integer> entries) throws IOException {
        for (int j = 0; j < size; j++) {
            for (int i = j; i < size; i++) {
                if (entries.isEmpty()) {
                    readNextLine(reader, entries);
                }

                matrix[i][j] = entries.poll();
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    @Override
    public int[] getNeighborsOf(int id) {
        int index = 0;
        int[] neighbors = new int[size-1];

        if ((id < 0) || (id > size-1)) {
            throw new IllegalArgumentException("no node with identifier " + id);
        }

        for (int i = 0; i < size; i++) {
            if (i != id) {
                neighbors[index++] = i;
            }
        }

        return neighbors;
    }

    @Override
    public int[][] getAdjMatrix(TSPInstance problem) {
        return matrix;
    }

}