package com.hsb.tsp.model;

import com.hsb.tsp.graph.DistanceSection;
import com.hsb.tsp.model.TSPInstance;
import com.hsb.tsp.graph.EdgeWeightMatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSPTour {
    private List<Integer> nodes = new ArrayList();
    private int cost;

    public TSPTour() {
    }

    public void load(BufferedReader reader) throws IOException {
        String line = null;

        outer:
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.trim().split("\\s+");

            for (int i = 0; i < tokens.length; ++i) {
                int id = Integer.parseInt(tokens[i]);
                if (id == -1) {
                    break outer;
                }

                this.nodes.add(id);
            }
        }

    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public void setNodes(List<Integer> nodes) {
        this.nodes = nodes;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "TSPTour{" +
                "nodes=" + nodes +
                ", cost=" + cost +
                '}';
    }


    /**
     * Calculates and returns the total distance of this tour.  The total
     * distance includes the distance from the last node back to the first node
     * in the tour.
     *
     * @param problem the TSPLIB problem instance this tour is a solution for
     * @return the total distance of this tour
     */
    public int distance(TSPInstance problem) {
        DistanceSection distanceSection = problem.getDistanceSection();
        if(distanceSection instanceof EdgeWeightMatrix){
            return distanceForMatrix(distanceSection);
        }
        int result = 0;

        for (int i = 0; i < nodes.size()-1; i++) {
            result += distanceSection.getDistanceBetween(nodes.get(i), nodes.get(i + 1));
        }

        return result;
    }

    public int distanceForMatrix(DistanceSection distanceSection){
        int result = 0;

        for (int i = 0; i < nodes.size()-1; i++) {
            result += distanceSection.getDistanceBetween(nodes.get(i)-1, nodes.get(i + 1)-1);
        }

        return result;
    }
}
