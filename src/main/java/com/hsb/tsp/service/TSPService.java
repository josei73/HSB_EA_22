package com.hsb.tsp.service;


import com.hsb.tsp.algorithms.*;
import com.hsb.tsp.exception.HeldKarpException;
import com.hsb.tsp.fieldTypesAndFormats.EdgeWeightType;
import com.hsb.tsp.graph.Node;
import com.hsb.tsp.model.TSPInstance;
import com.hsb.tsp.model.TSPModel;
import com.hsb.tsp.utils.TSPParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class TSPService {
    @Autowired
    private TSPParser parser;

    public TSPService() {}



    public Set<String> getAlgorithmNames() {
        Set<String> algorithmNames = new HashSet<>();
        algorithmNames.add("Random Tour");
        algorithmNames.add("Greedy");
        algorithmNames.add("Nearest Neighbour");
        algorithmNames.add("Christofides");
        algorithmNames.add("Held-Karp");
        algorithmNames.add("Arora");
        algorithmNames.add("Simplex");

        return algorithmNames;
    }

    public Algorithm getAlgo(String name, TSPInstance instance) throws HeldKarpException {
        int[][] adjMatrix = instance.getDistanceSection().getAdjMatrix(instance);

        switch (name) {
            case "Held-Karp":
                return new HeldKarp(adjMatrix);
            case "Greedy":
                return new GreedyTSP(adjMatrix);
            case "Christofides":
                return new Christofides(adjMatrix);
            case "Random Tour":
                return new RandomTour(adjMatrix.length);
            case "Arora":
                return new Arora(adjMatrix);
            case "Nearest Neighbour":
                return new NearestNeighbour(adjMatrix);
            case "Simplex":
                return new Simplex(adjMatrix);



            default:
                throw new IllegalArgumentException("No Algo exist");
        }
    }

    /**
     * loads an TSP instance
     * @param name problem name
     * @return TSPInstance object
     */
    public TSPInstance getTSPInstance(String name) {
        try {
            return this.parser.loadInstance(name + ".tsp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * loads an TSP instance
     * @param name problem name
     * @return a map of <index, node>
     */
    public Map<Integer, Node> getTSPInstanceNodes(String name) {
        TSPInstance problem;
        try {
            problem = this.parser.loadInstance(name + ".tsp");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return problem.getDistanceSection().getNodes();
    }

    /**
     * loads all TSP instances
     * @return a List of TSPInstance objects
     */
    public List<TSPInstance> getAllTSPInstances() {
        File[] listOfFiles = new File("data/tsp/").listFiles((dir, name) -> name.toLowerCase().endsWith(".tsp"));
        List<TSPInstance> instances = new ArrayList<>();
        for (File file : listOfFiles) {
            try {
                instances.add(this.parser.loadInstance(file.getName()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return instances;
    }

    public TSPModel getTSPModel(TSPInstance instance) {
        EdgeWeightType type = instance.getEdgeWeightType();

        TSPModel model = new TSPModel(instance.getProblemName(), instance.getNodes(), type);
        if(model.getNodes().isEmpty()) {
            model.setLinks(getAdjacencyMatrix(instance));
            int size = instance.getDistanceSection().getAdjMatrix(instance).length;
            model.setNumberOfNodes(size);
        } else {
            model.setNumberOfNodes(model.getNodes().size());
        }
        return model;
    }

    /**
     * loads all TSP instances
     * @return a List of Strings containing problem name and comment
     */
    public List<TSPModel> getAllTSPModels() {
        List<TSPInstance> instances = getAllTSPInstances();
        List<TSPModel> models = new ArrayList<>();
        for(TSPInstance instance : instances) {
            models.add(getTSPModel(instance));
        }
        return models;
    }

    public List<Map<String, Integer>> getAdjacencyMatrix(TSPInstance instance) {
        int[][] matrix = instance.getDistanceSection().getAdjMatrix(instance);
        List<Map<String, Integer>> links = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] > 0) {
                    Map<String, Integer> link = new HashMap<>();
                    link.put("distance", matrix[i][j]);
                    link.put("source", i+1);
                    link.put("target", j+1);
                    links.add(link);
                }
            }
        }
        return links;
    }
}
