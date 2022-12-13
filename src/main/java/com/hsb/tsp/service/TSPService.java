package com.hsb.tsp.service;


import com.hsb.tsp.graph.Node;
import com.hsb.tsp.parser.TSPLibInstance;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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


}
