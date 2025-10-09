package com.arkanoid.core;

import javafx.scene.canvas.GraphicsContext;

/**
 * class GameObject contains.
 * void update()
 * boolean checkCollision (GameObject other)
 */
public abstract class GameObject {
    private double x;
    private double y;
    private double width;
    private double height;

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public abstract void update();
    public abstract void render(GraphicsContext gc);

    /**
     * check collision between 2 objects.
     * @param other other object.
     * @return collision exist or not.
     */
    public boolean checkCollision (GameObject other) {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }

    /**
     * Calculate distance between center of 2 GameObject.
     * @param other other GameObject.
     * @return distance between them.
     */
    public double distance (GameObject other) {
        double centerX = (getX() + getWidth()) / 2;
        double centerY = (getY() + getHeight()) / 2;
        double otherCenterX = (other.getX() + other.getWidth()) / 2;
        double otherCenterY = (other.getY() + other.getHeight()) / 2;
        return Math.sqrt(Math.pow(otherCenterX - centerX, 2)
                + Math.pow(otherCenterY - centerY, 2));
    }
}
