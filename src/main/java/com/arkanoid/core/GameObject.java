package com.arkanoid.core;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private String imagePath;
    private ImageView imageView;

    public GameObject(double x, double y, double width, double height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;

        Image image = new Image(getClass().getResourceAsStream(imagePath));
        imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setX(x);
        imageView.setY(y);
    }

    public ImageView getImageView() {
        return imageView;
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public abstract void update();

    public void render(Group root) {
        if (!root.getChildren().contains(getImageView())) {
            root.getChildren().add(getImageView());
        }
    }

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
        double thiscenterX = (getX() + getWidth()) / 2;
        double thiscenterY = (getY() + getHeight()) / 2;
        double othercenterX = (other.getX() + other.getWidth()) / 2;
        double othercenterY = (other.getY() + other.getHeight()) / 2;
        return Math.sqrt(Math.pow(othercenterX - thiscenterX, 2) + Math.pow(othercenterY - thiscenterY, 2));
    }
}
