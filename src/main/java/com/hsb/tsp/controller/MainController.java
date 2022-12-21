package com.hsb.tsp.controller;


import com.hsb.tsp.service.TSPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private TSPService service;

    @GetMapping("")
    public String viewHomePage(Model model) {
        model.addAttribute("title", "Travelling Salesperson Problem");
        model.addAttribute("problems", service.getProblemNames());
        model.addAttribute("algorithms", service.getAlgorithmNames());
        return "index";
    }

}
