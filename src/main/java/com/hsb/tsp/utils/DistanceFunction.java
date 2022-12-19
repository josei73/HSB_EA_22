package com.hsb.tsp.utils;



import com.hsb.tsp.fieldTypesAndFormats.EdgeWeightType;
import com.hsb.tsp.graph.Node;

import java.util.function.BiFunction;



public class DistanceFunction {
    static final double PI = 3.141592;
    static final double RRR = 6378.388;

    public DistanceFunction() {
    }

    public static BiFunction<Node, Node, Integer> getDistanceFunction(EdgeWeightType edgeWeightType) {
        switch (edgeWeightType) {
            case EUC_2D:
                return getEuc2dFunction();
            case GEO:
                return getGeoFunction();
            case MAN_2D:
                return getMan2dFunction();
            case MAX_2D:
                return getMax2dFunction();
            case CEIL_2D:
                return getCeil2dFunction();
            case ATT:
                return getAttFunction();
            default:
                throw new IllegalArgumentException("" + edgeWeightType + " not implemented yet");
        }
    }

    private static BiFunction<Node, Node, Integer> getEuc2dFunction() {
        return (i, j) ->  (int)calculateEuc2d(i, j);
    }

    private static double calculateEuc2d(Node i, Node j) {
        double xd = i.getX() - j.getX();
        double yd = i.getY() - j.getY();
        return Math.sqrt(xd * xd + yd * yd) + 0.5;
    }

    private static BiFunction<Node, Node, Integer> getGeoFunction() {
        return (i, j) -> {
            double latitudeI = convertToRadians(i.getY());
            double longitudeI = convertToRadians(i.getX());
            double latitudeJ = convertToRadians(j.getY());
            double longitudeJ = convertToRadians(j.getX());
            double q1 = Math.cos(latitudeI - latitudeJ);
            double q2 = Math.cos(longitudeI - longitudeJ);
            double q3 = Math.cos(longitudeI + longitudeJ);
            return (int) (6378.388 * Math.acos(0.5 * ((1.0 + q1) * q2 - (1.0 - q1) * q3)) + 1.0);
        };


    }

    private static double convertToRadians(double x) {
        int deg = (int) x;
        double min = x - deg;
        return 3.141592 * ((double) deg + 5.0 * min / 3.0) / 180.0;
    }

    private static BiFunction<Node, Node, Integer> getMan2dFunction() {
        return (i, j) -> {
            double xd = Math.abs(i.getX() - j.getX());
            double yd = Math.abs(i.getY() - j.getY());
            return (int)(xd + yd);
        };
    }

    private static BiFunction<Node, Node, Integer> getMax2dFunction() {
        return (i, j) -> {
            double xd = Math.abs(i.getX() - j.getX());
            double yd = Math.abs(i.getY() - j.getY());
            return (int)Math.max(xd, yd);
        };
    }

    private static BiFunction<Node, Node, Integer> getCeil2dFunction() {
        return (i, j) -> (int)Math.ceil(calculateEuc2d(i, j));
    }

    private static BiFunction<Node, Node, Integer> getAttFunction() {
        return (i, j) -> {
            double xd = i.getX() - j.getX();
            double yd = i.getY() - j.getY();
            double rij = Math.sqrt((xd * xd + yd * yd) / 10.0);
            double tij = cutDecimal(rij);
            return (int)(tij < rij ? tij + 1.0 : tij);
        };
    }

    private static double cutDecimal(double x) {
        return (double)((int)x);
    }
}
