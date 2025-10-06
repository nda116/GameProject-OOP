package com.arkanoid.core;

public abstract class MovableObject extends GameObject{
    private double dx;
    private double dy;

    public MovableObject(double x, double y, double width, double height, double dx, double dy) {
        super(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void move() {
        setX(getX() + dx);
        setY(getY() + dy);
    }
}
