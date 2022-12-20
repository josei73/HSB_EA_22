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
            BufferedReader reader = new BufferedReader(new FileReader("data/tsp/ulysses22.tsp"));
            problem.load(reader);
            // problem.addTour(new BufferedReader(new FileReader("./data/tsp/ali535.opt.tour")));


        } catch (IOException var5) {
            throw new RuntimeException(var5);
        }



      int[][] matrix = problem.getDistanceSection().getAdjMatrix(problem);


        long start3 = System.nanoTime();
        Christofides christofides = new Christofides();
        List<Integer> tour = christofides.applyChristofides(matrix);
        long end3 = System.nanoTime() - start3;


        Tour tour1 =new Tour();
        tour1.setNodes(tour);


        System.out.println(tour);
        System.out.println("Cost "+tour1.distance(problem));
        System.out.println(

                printTime(end3));
        System.out.println("=====================================================================");

        GreedyTSP greedyTSP = new GreedyTSP(matrix);

        long start = System.nanoTime();
        List<Integer> tour2 = greedyTSP.getTour();
        long end = System.nanoTime() - start;

        System.out.println(

                printTime(end));
        System.out.println(tour2);

        System.out.println("===================================================================== Held");
        int startNode = 0;
        HeldRek solverr =
                new HeldRek(startNode, matrix);


        long start1 = System.nanoTime();


        System.out.println("Tour: "+solverr.getTour());
        long end2 = System.nanoTime() - start1;
        System.out.println("Tour cost: " + solverr.getTourCost());

        System.out.println(

                printTime(end2));




        PTAS ptas = new PTAS();
        System.out.println(ptas.solveTSP(matrix));

    /*

    GreedyTSP greedyTSP = new GreedyTSP(adjMatrix);

    long start = System.nanoTime();
    List<Integer> tour1 = greedyTSP.getTour();
    long end = System.nanoTime() - start;

        System.out.println(

    printTime(end));
        System.out.println(tour1);


        System.out.println("Check =============================================================");


    Kruskal kruskal = new Kruskal(matrix);

        kruskal.kruskalMST(matrix);
        System.out.println("kruksal");


        System.out.println("Maximum matching for the given graph is: "
                +

    maxMatching(matrix));


    Graph g = new Graph(matrix);
    int matching = g.maxMatching();


    // Print the maximum matching
        System.out.println("Maximum matching: "+matching);
    Hungarian hbm = new Hungarian(adjMatrix);
    int[] result = hbm.execute();
        System.out.println("Bipartite Matching: "+Arrays.toString(result));


    int startNode = 0;
    HeldRek solverr =
            new HeldRek(startNode, adjMatrix);


    long start1 = System.nanoTime();


        System.out.println("Tour: "+solverr.getTour());
    long end2 = System.nanoTime() - start1;
        System.out.println(

    printTime(end2));


        System.out.println("Tour cost: "+solverr.getTourCost());


       /*




        Node node1 = new Node(1,38.24, 20.42);
        Node node2 = new Node(15,35.49,14.32);

        int geoFunction = getGeoFunction(node1, node2);
        System.out.println(geoFunction+"    geooooooooooooooooo");


        double distanceBetween1 = problem.getDistanceSection().getDistanceBetween(node1.getId(), node2.getId());

        System.out.println(distanceBetween1+"---------------");


        Integer[] arr = {1,
                14,
                13,
                12,
                7,
                6,
                15,
                5,
                11,
                9,
                10,
                19,
                20,
                21,
                16,
                3,
                2,
                17,
                22,
                4,
                18,
                8,1};




        int startNode = 0;
        HeldRek solverr =
                new HeldRek(startNode, adjMatrix);


        long start1 = System.nanoTime();


        System.out.println("Tour: " + solverr.getTour());
        long end2 = System.nanoTime() - start1;
        System.out.println(printTime(end2));


        System.out.println("Tour cost: " + solverr.getTourCost());





        // Prepare maps and objects for the calculation
     //   HeldKarp calc = new HeldKarp(matrix, 0);

        // Time calculation
      /*
        long start = System.nanoTime();
        List<Integer> result = calc.calculateHeldKarp();
        long end = System.nanoTime() - start;



        Tour tour = new Tour();
        tour.setNodes(result);


        System.out.println(result);

        System.out.println(tour.distance(problem));

        System.out.println(printTime(end));


        Tour tour1 = new Tour();



        
        List<Integer> to = new ArrayList<>();
        to.addAll(Arrays.asList(arr));

        tour1.setNodes(to);

        System.out.println("Check =============================================================");

        System.out.println( tour1.distance(problem));


       /*
     GreedyTSP greedyTSP = new GreedyTSP(adjMatrix);

        long start = System.nanoTime();
        List<Integer> tour1 = greedyTSP.getTour();
        long end = System.nanoTime() - start;

        System.out.println(printTime(end));

        */


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
