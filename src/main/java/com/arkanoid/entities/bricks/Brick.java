package com.arkanoid.entities.bricks;

import com.arkanoid.core.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * void HPlost() Minus HP of brick.
 * boolean isAdjacent(Brick) check adjacent between 2 Bricks.
 * void triggerFlash() start flash effect of Brick.
 *
 */
public abstract class Brick extends GameObject {
    private int brickHP;
    private int brickScore;
    private int type;
    private boolean isFlashing = false;
    private double flashEndTime = 0;
    public static final int NORMAL = 0;
    public static final int INVINCIBLE = 1;
    public static final int EXPLOSION = 2;
    public static final int INVISIBLE = 3;


    public Brick(double x, double y, double width, double height, int type) {
        super(x, y, width, height);
        this.type = type;
    }

    public int getBrickHP() {
        return brickHP;
    }

    public void setBrickHP(int brickHP) {
        this.brickHP = brickHP;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isFlashing() {
        return isFlashing;
    }

    public int getBrickScore() {
        return brickScore;
    }

    public void setBrickScore(int brickScore) {
        this.brickScore = brickScore;
    }

    /**
     * Minus HP of brick.
     */
    public void HPlost() {
        brickHP --;
    }

    /**
     * check adjacent between 2 Bricks.
     * @param other other Brick.
     * @return adjacent or not.
     */
    public boolean isAdjacent(Brick other) {
        double tolerance = 0.01;

        if (getX() == other. getX() && getY() == other.getY()) {
            return false;
        }
        return other.getX() >= getX() - getWidth() - tolerance
                && other.getX() < getX() + getWidth() + tolerance
                && other.getY() >= getY() - getHeight() - tolerance
                && other.getY() < getY() + getHeight() + tolerance;
    }

    /**
     * Render Brick from image.
     * if Brick is flashing, change color and alpha of the Brick until flashEndtime.
     * @param gc GraphicsConText
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());

        if (isFlashing) {
            gc.setGlobalAlpha(0.8);
            gc.setFill(Color.WHITE);
            gc.fillRect(getX(), getY(), getWidth(), getHeight());
            gc.setGlobalAlpha(1.0);

            if (System.currentTimeMillis() > flashEndTime) {
                isFlashing = false;
            }
        }
    }

    /**
     * start flash effect of Brick for 300ms.
     */
    public void triggerFlash() {
        isFlashing = true;
        flashEndTime = System.currentTimeMillis() + 300;
    }

    @Override
    public void update() {
    }
}

