package com.hsb.tsp.controller;


import com.hsb.tsp.graph.Node;
import com.hsb.tsp.modal.Tour;
import com.hsb.tsp.service.TSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TSPRestController {


    @Autowired
    private TSPService service;

    @GetMapping("/api/tour")
    public Tour getTOur() {
        return service.genData();

    }

    @GetMapping("/api/tsp/node")
    public Map<Integer, Node> getTSP() {
        return service.genNodeCordi();

    }


}
