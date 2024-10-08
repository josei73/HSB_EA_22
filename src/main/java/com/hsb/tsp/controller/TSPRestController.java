package com.hsb.tsp.controller;


import com.hsb.tsp.algorithms.Algorithm;
import com.hsb.tsp.exception.HeldKarpException;
import com.hsb.tsp.graph.Node;
import com.hsb.tsp.model.TSPInstance;
import com.hsb.tsp.model.TSPModel;
import com.hsb.tsp.model.TSPTour;
import com.hsb.tsp.service.TSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class TSPRestController {
    @Autowired
    private TSPService service;


    @GetMapping("api/problems")
    public List<TSPInstance> getProblems() {
        return service.getAllTSPInstances();
    }

    @GetMapping("api/problems/{name}")
    public TSPInstance getProblem(@PathVariable String name) {
        return service.getTSPInstance(name);
    }

    @GetMapping("/api/problems/{name}/nodes")
    public Map<Integer, Node> getTSPNodes(@PathVariable String name) {
        return service.getTSPInstanceNodes(name);
    }

    @GetMapping("api/problems/models")
    public List<TSPModel> getAllTSPModels() {
        return service.getAllTSPModels();
    }

    @GetMapping("api/algorithm/{algoName}/nodes/{nodeName}")
    public TSPModel getAlg(@PathVariable String algoName, @PathVariable String nodeName) throws HeldKarpException {
        TSPInstance problem = service.getTSPInstance(nodeName);


        Algorithm solution = service.getAlgo(algoName, problem);
        long solveStart = System.nanoTime();
        solution.solve();
        long solveEnd = System.nanoTime() - solveStart;

        TSPTour tour = new TSPTour();
        tour.setNodes(solution.getTour());
        tour.setCost(tour.distance(problem));
        tour.setTime(solveEnd);
        TSPModel model = service.getTSPModel(problem);
        model.setTour(tour);
        return model;
    }

}
