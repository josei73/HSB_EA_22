package com.hsb.tsp.controller;


import com.hsb.tsp.graph.Node;
import com.hsb.tsp.parser.TSPLibInstance;
import com.hsb.tsp.service.TSPService;
import com.hsb.tsp.utils.Algo;
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
    public List<Integer> getTOur() {
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
    public List<String> getAlg() {
        List<String> alg = new ArrayList<>();

        alg.add("Held-Karp");
        alg.add("Greedy");
        alg.add("Christofides");
        alg.add("Arora");
        alg.add("LP");
        return alg;
    }

    @GetMapping("api/algorithm/{algoName}/nodes/{nodeName}")
    public List<Integer> getAlg(@PathVariable String algoName, @PathVariable String nodeName) {
        TSPLibInstance problem = service.getTSP(nodeName);
        Algo algo = service.getAlgo(algoName, problem);
        return algo.getTour();
    }








}
