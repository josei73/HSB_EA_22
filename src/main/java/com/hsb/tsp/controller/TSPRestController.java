package com.hsb.tsp.controller;


import com.hsb.tsp.modal.Tour;
import com.hsb.tsp.service.TSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
public class TSPRestController {


    @Autowired
    private TSPService service;

    @GetMapping("/api/tour")
    public Tour getTSP() {
        return service.genData();

    }


}
