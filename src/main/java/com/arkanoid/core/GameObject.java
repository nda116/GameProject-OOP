package com.arkanoid.core;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * class GameObject contains.
 * void update()
 * void render(GraphicsContext) render GameObject.
 */
public abstract class GameObject {
    private double x;
    private double y;
    private double width;
    private double height;
    private Image objectImage;

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

    public Image getObjectImage() {
        return objectImage;
    }

    /**
     * set ObjectImage from new image path.
     * @param imagePath path to image.
     */
    public void setObjectImage(String imagePath) {
        try {
            objectImage = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            System.out.println("Can not find" + imagePath);
        }
    }

    public abstract void update();
    public abstract void render(GraphicsContext gc);

    /**
     * check collision between 2 objects.
     * @param a first object.
     * @param b seconf object.
     * @return collision or not.
     */
    public static boolean checkCollision (GameObject a, GameObject b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }
}
