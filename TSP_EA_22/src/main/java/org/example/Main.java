package org.example;


import org.example.graph.Node;
import org.example.modal.TSPLibInstance;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Map;

public class Main {


    public static void main(String[] args) {
        System.out.println("Hello world!");
        TSPLibInstance problem = new TSPLibInstance();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/tsp/pr76.tsp"));
            problem.load(reader);
        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }

        System.out.println(problem.getName());
        Map<Integer, Node> nodes = problem.getDistanceSection().getNodes();
        Iterator it = nodes.entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            PrintStream var10000 = System.out;
            Object var10001 = pair.getKey();
            var10000.println("" + var10001 + " = " + pair.getValue());
            it.remove();
        }

    }
}
