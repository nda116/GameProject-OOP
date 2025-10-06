package com.arkanoid.objects;

import com.arkanoid.core.MovableObject;

public class Paddle extends MovableObject {
    private double speed;
    private final double screenWidth;

    public Paddle(double x, double y, double width, double height,
                  double speed, double screenWidth) {
        super(x, y, width, height, 0, 0);
        this.speed = speed;
        this.screenWidth = screenWidth;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void moveLeft() {
        dx = -speed;
        move();
        dx = 0;
    }

    public void moveRight() {
        dx = speed;
        move();
        dx = 0;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext gc) {

    }
}