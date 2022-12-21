package com.hsb.tsp.controller;


import com.hsb.tsp.graph.Node;
import com.hsb.tsp.parser.TSPLibInstance;
import com.hsb.tsp.service.TSPService;
import com.hsb.tsp.utils.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class TSPRestController {


    @Autowired
    private TSPService service;

    @GetMapping("/api/tour")
    public List<Integer> getTour() {
        return service.genData();
    }

    @GetMapping("/api/nodes")
    public Map<Integer, Node> getTSP() {
        return service.genNodeCoordinates();
    }


    @GetMapping("/api/nodes/{name}")
    public Map<Integer, Node> getTSPNodes(@PathVariable String name) {
        return service.genProblemNode(name);
    }

    @GetMapping("api/problems")
    public Set<String> getProblemsData() {
        return service.getProblemNames();
    }

    @GetMapping("api/algorithm")
    public List<String> getAlgorithms() {
        List<String> algorithmName = new ArrayList<>();

        algorithmName.add("Held-Karp");
        algorithmName.add("Greedy");
        algorithmName.add("Christofides");
        algorithmName.add("Arora");
        algorithmName.add("LP");
        return algorithmName;
    }

    @GetMapping("api/algorithm/{algoName}/nodes/{nodeName}")
    public List<Integer> getAlg(@PathVariable String algoName, @PathVariable String nodeName) {
        TSPLibInstance problem = service.getTSP(nodeName);
        Algorithm solution = service.getAlgo(algoName, problem);
        return solution.getTour();
    }
}
