package org.example.graph;

public class Node {
    private Integer id;
    private double x;
    private double y;

    public Node(Integer id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String toString() {
        return "Node{id=" + this.id + ", x=" + this.x + ", y=" + this.y + "}";
    }
}

