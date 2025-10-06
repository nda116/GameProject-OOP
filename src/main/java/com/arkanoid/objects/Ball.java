package com.arkanoid.objects;

import com.arkanoid.core.MovableObject;
import com.arkanoid.core.GameObject;

public class Ball extends MovableObject {
    private double speed;
    private double directionX;
    private double directionY;
    private final double screenWidth;
    private final double screenHeight;

    public Ball(double x, double y, double radius, double speed,
                double screenWidth, double screenHeight) {
        super(x, y, radius * 2, radius * 2, 0, 0);
        this.speed = speed;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        directionX = 0;
        directionY = 1;
        updateVelocity();
    }

    /**
     * Updates the velocity (dx, dy) based on speed and direction.
     */
    private void updateVelocity() {
        dx = speed * directionX;
        dy = speed * directionY;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        updateVelocity();
    }

    public double getDirectionX() {
        return directionX;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
        updateVelocity();
    }

    public double getDirectionY() {
        return directionY;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
        updateVelocity();
    }
}
