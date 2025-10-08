package com.arkanoid.entities.bricks;

/**
 * Constructor for NormalBricks.
 * String getImagePath(String) add imagePath base on color of the bricks.
 */
public class NormalBricks extends Bricks {
    private String color;
    public static final String RED = "red";
    public static final String YELLOW = "yellow";
    public static final String BLUE = "blue";

    public NormalBricks(double x, double y, double width, double height, String color) {
        super(x, y, width, height, Bricks.NORMAL, getImagePath(color));
        this.color = color;
        setBrickHP(1);
    }

    /**
     * Add imagePath base on color of the bricks.
     * @param color color of the bricks.
     * @return string imagePath.
     */
    private static String getImagePath(String color) {
        if (color.equals(RED)) {
            return "/bricks/Normal_Red.png";
        } else if (color.equals(YELLOW)) {
            return "/bricks/Normal_Yellow.png";
        } else if (color.equals(BLUE)) {
            return "/bricks/Normal_Blue.png";
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
}
