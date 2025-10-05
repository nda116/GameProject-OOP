package com.arkanoid.objects;

import com.arkanoid.core.MovableObject;

public class Ball extends MovableObject{
    private double speed;
    private double directionX;
    private double directionY;

    public Ball(double x, double y, double radius, double speed) {
        super(x, y, radius * 2, radius * 2, 0, 0);
        this.speed = speed;
        this.directionX = 0;
        this.directionY = 0;
    }

    public double getDirectionX() {
        return directionX;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
    }

    public double getDirectionY() {
        return directionY;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
    }
}
