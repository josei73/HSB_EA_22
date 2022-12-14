package com.hsb.tsp.service;


import com.hsb.tsp.graph.Node;
import com.hsb.tsp.parser.TSPLibInstance;
import com.hsb.tsp.utils.Algo;
import com.hsb.tsp.utils.GreedyTSP;
import com.hsb.tsp.utils.HeldRek;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class TSPService {


    public List<Integer> genData() {
        TSPLibInstance problem = new TSPLibInstance();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/tsp/pr76.tsp"));
            problem.load(reader);
            problem.addTour(new BufferedReader(new FileReader("./data/tsp/pr76.opt.tour")));
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }


        System.out.println(problem.getName());
        Map<Integer, Node> nodes = problem.getDistanceSection().getNodes();
        Iterator it = nodes.entrySet().iterator();

        return problem.getTours().get(0).getNodes();

    }

    public Map<Integer, Node> genNodeCoordinates() {
        TSPLibInstance problem = new TSPLibInstance();

        try (BufferedReader reader = new BufferedReader(new FileReader("data/tsp/pr76.tsp"));) {
            problem.load(reader);
            problem.addTour(new BufferedReader(new FileReader("./data/tsp/pr76.opt.tour")));
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }


        return problem.getDistanceSection().getNodes();

    }

    public Map<Integer, Node> genProblemNode(String name) {
        TSPLibInstance problem = new TSPLibInstance();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/tsp/" + name+".tsp"));
            problem.load(reader);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }


        return problem.getDistanceSection().getNodes();

    }

    public TSPLibInstance getTSP(String name) {
        TSPLibInstance problem = new TSPLibInstance();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/tsp/" + name+".tsp"));
            problem.load(reader);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }


        return problem;

    }


    public Set<String> getProblemNames() {


        File folder = new File("data/tsp/");
        File[] listOfFiles = folder.listFiles();

        Set<String> files = new HashSet<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                //System.out.println("File " + listOfFiles[i].getName().subSequence(0,listOfFiles[i].getName().indexOf(".")));
                files.add((String) listOfFiles[i].getName().subSequence(0, listOfFiles[i].getName().indexOf(".")));
            }
        }

        System.out.println(files);


        return files;
    }

    public Algo getAlgo(String name, TSPLibInstance tspLibInstance) {
        double[][] adjMatrix = tspLibInstance.getDistanceSection().getAdjMatrix(tspLibInstance);

        switch (name) {
            case "Held-Karp":
                return new HeldRek(adjMatrix);
            case "Greedy":
                return new GreedyTSP(adjMatrix);

            default:
                throw new IllegalArgumentException("No Algo exist");
        }
    }
}
