package com.hsb.tsp.utils;


import com.hsb.tsp.parser.TSPLibInstance;
import com.hsb.tsp.parser.Tour;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Main {


    public static void main(String[] args) {
        TSPLibInstance problem = new TSPLibInstance();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/tsp/ulysses16.tsp"));
            problem.load(reader);
            // problem.addTour(new BufferedReader(new FileReader("./data/tsp/ali535.opt.tour")));


        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }


        int[][] matrix = problem.getDistanceSection().getAdjMatrix(problem);
        System.out.println("===================================================================== Random");
        RandomTour randomTour = new RandomTour(matrix.length);
        long randomStart = System.nanoTime();
        randomTour.solve();
        long randomEnd = System.nanoTime() -randomStart;
        System.out.println(randomTour.getTour());
        Tour tour = new Tour();
        tour.setNodes(randomTour.getTour());
        System.out.println(tour.distance(problem));
        System.out.println(printTime(randomEnd));

        System.out.println("===================================================================== Nearest");
        NearestNeighbour neighbour = new NearestNeighbour(matrix);
        long nearestStart = System.nanoTime();
        List<Integer> list = neighbour.solve();
        long nearestEnd = System.nanoTime()-nearestStart;

        System.out.println(list);

        tour.setNodes(list);
        System.out.println(tour.distance(problem));
        System.out.println(printTime(nearestEnd));

        System.out.println("===================================================================== Christofides");

        long start3 = System.nanoTime();
        Christofides christofides = new Christofides();
        List<Integer> chrisTour = christofides.applyChristofides(matrix);
        long end3 = System.nanoTime() - start3;


        Tour tour1 =new Tour();
        tour1.setNodes(chrisTour);


        System.out.println("Christofides "+chrisTour);
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

        PTAS ptas = new PTAS(matrix);
        Tour checkTour = new Tour();
        long start5 = System.nanoTime();
        checkTour.setNodes(ptas.getTour());
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
