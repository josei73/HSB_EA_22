package org.example.modal;

import org.example.graph.DistanceSection;
import org.example.utils.DistanceFunction;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tour {


        private final List<Integer> nodes = new ArrayList();

        public Tour() {
        }

        public void load(BufferedReader reader) throws IOException {
            String line = null;

            while((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");

                for(int i = 0; i < tokens.length; ++i) {
                    int id = Integer.parseInt(tokens[i]);
                    if (id == -1) {
                        break;
                    }

                    this.nodes.add(id);
                }
            }

        }


    /**
     * Calculates and returns the total distance of this tour.  The total
     * distance includes the distance from the last node back to the first node
     * in the tour.
     *
     * @param problem the TSPLIB problem instance this tour is a solution for
     * @return the total distance of this tour
     */
    public double distance(TSPLibInstance problem) {
        DistanceSection distanceSection = problem.getDistanceSection();
        double result = 0.0;

        for (int i = 0; i < nodes.size(); i++) {
            result += distanceSection.getDistanceBetween(nodes.get(i), nodes.get(i+1));
        }

        return result;
    }



}
