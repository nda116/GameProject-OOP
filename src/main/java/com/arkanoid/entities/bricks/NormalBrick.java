package com.arkanoid.entities.bricks;

import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for NormalBricks.
 * String getImagePath(String) add imagePath base on color of the bricks.
 */
public class NormalBrick extends Brick {
    private String color;
    public static final String RED = "red";
    public static final String YELLOW = "yellow";
    public static final String BLUE = "blue";

    public NormalBrick(double x, double y, double width, double height, String color) {
        super(x, y, width, height, Brick.NORMAL);
        this.color = color;
        setBrickHP(1);

        setObjectImage(getImagePath(color));
    }

    /**
     * Add imagePath base on color of the bricks.
     * @param color color of the bricks.
     * @return string imagePath.
     */
    private static String getImagePath(String color) {
        if (color.equals(RED)) {
            return "/images/bricks/normal_red_brick.png";
        } else if (color.equals(YELLOW)) {
            return "/images/bricks/normal_yellow_brick.png";
        } else if (color.equals(BLUE)) {
            return "/images/bricks/normal_blue_brick.png";
        } else {
            return "";
        }

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
