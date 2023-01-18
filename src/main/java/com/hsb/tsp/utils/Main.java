package com.hsb.tsp.utils;


import com.hsb.tsp.algorithms.*;
import com.hsb.tsp.model.TSPInstance;
import com.hsb.tsp.model.TSPTour;


import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Main {


    public static void main(String[] args) {
        TSPParser parser = new TSPParser();
        TSPInstance problem;
        String filename = "ulysses16.tsp";

        try {
            problem = parser.loadInstance(filename);
            System.out.println("load " + filename + " to test algorithms:");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        int[][] matrix = problem.getDistanceSection().getAdjMatrix(problem);
        System.out.println("===================================================================== Random");
        RandomTour randomTour = new RandomTour(matrix.length);
        long randomStart = System.nanoTime();
        randomTour.solve();
        long randomEnd = System.nanoTime() -randomStart;
        System.out.println(randomTour.getTour());
        TSPTour tour = new TSPTour();
        tour.setNodes(randomTour.getTour());
        System.out.println(tour.distance(problem));
        System.out.println(printTime(randomEnd));

        System.out.println("===================================================================== Nearest");
        NearestNeighbour neighbour = new NearestNeighbour(matrix);
        long nearestStart = System.nanoTime();
         neighbour.solve();
        List<Integer> list =   neighbour.getTour();
        long nearestEnd = System.nanoTime()-nearestStart;

        System.out.println(list);

        tour.setNodes(list);
        System.out.println(tour.distance(problem));
        System.out.println(printTime(nearestEnd));

        System.out.println("===================================================================== Christofides");

        long start3 = System.nanoTime();
        Algorithm christofides = new Christofides(matrix);
         christofides.solve();
        long end3 = System.nanoTime() - start3;


        TSPTour tour1 =new TSPTour();
        tour1.setNodes(christofides.getTour());


        System.out.println("Christofides "+christofides.getTour());
        System.out.println("Cost "+tour1.distance(problem));
        System.out.println(

                printTime(end3));

        System.out.println("===================================================================== Held");
        int startNode = 0;
        HeldRek solverr =
                new HeldRek(startNode, matrix);


        long start1 = System.nanoTime();


        System.out.println("Tour Held: "+solverr.getTour());
        long end2 = System.nanoTime() - start1;
        System.out.println("Cost "+solverr.getTourCost());
        System.out.println(

                printTime(end2));




        System.out.println("===================================================================== PTAS");

        Arora arora = new Arora(matrix);
        TSPTour checkTour = new TSPTour();
        long start5 = System.nanoTime();
        checkTour.setNodes(arora.getTour());
        long end5 = System.nanoTime() - start5;
        System.out.println("Tour Arora   "+checkTour.getNodes());
        System.out.println(" Cost "+checkTour.distance(problem));
        System.out.println(printTime(end5));

        System.out.println("===================================================================== Greedy");

        GreedyTSP greedyTSP = new GreedyTSP(matrix);


        long start = System.nanoTime();
        List<Integer> tour2 = greedyTSP.getTour();
        long end = System.nanoTime() - start;


        System.out.println("Greedy "+ tour2);
        System.out.println(" Cost "+greedyTSP.getTourCost());
        System.out.println(

                printTime(end));



        System.out.println("===================================================================== LP Branch And Bound");

        BAndB bandb = new BAndB(matrix);
        long start6 = System.nanoTime();
        checkTour.setNodes(bandb.getTour());
        long end6 = System.nanoTime() - start6;
        System.out.println("Tour Branch And Bound   "+checkTour.getNodes());
        System.out.println(" Cost "+bandb.getTourCost());
        System.out.println(printTime(end6));


        System.out.println("Check =============================================================");


    }

    public static String printTime(long end) {
        String msg = "(in ";
        if (end < 1000) {
            msg += "ns): " + String.format(Locale.ROOT, "%.3f", (float) (end));
        } else if (end >= 1000 && end < 1000000) {
            msg += "us): " + String.format(Locale.ROOT, "%.3f", ((float) end / 1000.0));
        } else if (end >= 1000000 && end < 1000000000) {
            msg += "ms): " + String.format(Locale.ROOT, "%.3f", ((float) end / 1000000.0));
        } else if (end >= 1000000000) {
            msg += "s): " + String.format(Locale.ROOT, "%.3f", ((float) end / 1000000000.0));
        }
        return msg;
    }





}
