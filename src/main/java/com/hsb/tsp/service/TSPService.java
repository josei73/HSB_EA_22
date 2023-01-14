package com.hsb.tsp.service;


import com.hsb.tsp.fieldTypesAndFormats.EdgeWeightType;
import com.hsb.tsp.graph.Node;
import com.hsb.tsp.model.TSPInstance;
import com.hsb.tsp.model.TSPModel;
import com.hsb.tsp.parser.TSPParser;
import com.hsb.tsp.utils.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class TSPService {
    TSPParser parser;

    public TSPService() {
        this.parser = new TSPParser();
    }

    public List<Integer> genData() {
        TSPInstance problem = null;
        try {
            problem = this.parser.loadInstance("pr76.tsp");
            //problem.setTours(this.parser.addOptimalTour("./data/tsp/pr76.opt.tour"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        System.out.println(problem.getName());
        Map<Integer, Node> nodes = problem.getDistanceSection().getNodes();
        Iterator it = nodes.entrySet().iterator();

        return problem.getTours().get(0).getNodes();
    }

    public Map<Integer, Node> genNodeCoordinates() {
        TSPInstance problem = null;
        try {
            problem = this.parser.loadInstance("pr76.tsp");
            //problem.setTours(this.parser.addOptimalTour("./data/tsp/pr76.opt.tour"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return problem.getDistanceSection().getNodes();

    }

    public Set<String> getAlgorithmNames() {

        File folder = new File("data/tsp/");
        File[] listOfFiles = folder.listFiles();

        Set<String> files = new HashSet<>();

        return files;
    }

    public Algorithm getAlgo(String name, TSPInstance instance) {
        int[][] adjMatrix = instance.getDistanceSection().getAdjMatrix(instance);

        switch (name) {
            case "Held-Karp":
                return new HeldRek(adjMatrix);
            case "Greedy":
                return new GreedyTSP(adjMatrix);
            case "Christofides":
                return new HeldRek(adjMatrix);
            case "Random":
                return new RandomTour(adjMatrix.length);
            case "Arora":
                return new PTAS(adjMatrix);


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

    /**
     * loads all TSP instances
     * @return a List of Strings containing problem name and comment
     */
    public List<TSPModel> getAllTSPModels() {
        List<TSPInstance> instances = getAllTSPInstances();
        List<TSPModel> models = new ArrayList<>();
        for(TSPInstance instance : instances) {
            String lines[] = instance.getComment().split("\\r?\\n");
            String name = instance.getName() + ": " + lines[0];
            Map<Integer, Node> nodes;
            EdgeWeightType type = instance.getEdgeWeightType();
            //TODO some instances have nodes saved in a different variable
            if (instance.getEdgeWeightType() == EdgeWeightType.EXPLICIT) {
                if (instance.displayDataType == null) {
                    nodes = new HashMap<>();
                } else {
                    nodes = instance.getDisplayData().getNodes();
                }
            } else {
                nodes = instance.getDistanceSection().getNodes();
            }

            models.add(new TSPModel(name, nodes, type));
        }
        return models;
    }
}
