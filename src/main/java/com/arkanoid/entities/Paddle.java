package com.arkanoid.entities;

import com.arkanoid.core.MovableObject;

/**
 * class Paddle contains.
 * void moveLeft(), void moveRight()
 */
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
        setDx(-speed);
        move();
        setDx(0);
    }

    public void moveRight() {
        setDx(speed);
        move();
        setDx(0);
    }

    @Override
    public void update() {
        if (getX() < 0) {
            setX(0);
        }
        if (getX() + getWidth() > screenWidth) {
            setX(screenWidth - getWidth());
        }
    }

    @Override
    public void render() {

    }
}