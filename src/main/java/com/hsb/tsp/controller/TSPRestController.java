package com.hsb.tsp.controller;


import com.hsb.tsp.graph.Node;
import com.hsb.tsp.service.TSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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


}
