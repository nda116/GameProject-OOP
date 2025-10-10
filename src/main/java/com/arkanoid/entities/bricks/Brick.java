package com.arkanoid.entities.bricks;

import com.arkanoid.core.GameObject;

import java.util.logging.XMLFormatter;

/**
 * void HPlost() Minus HP of brick.
 * boolean isAdjacent(Brick) check adjacent between 2 Bricks.
 */
public abstract class Brick extends GameObject {
    private int brickHP;
    private String type;
    public static final String NORMAL = "normal";
    public static final String INVINCIBLE = "invincible";
    public static final String EXPLOSION = "explosion";


    public Brick(double x, double y, double width, double height, String type) {
        super(x, y, width, height);
        this.type = type;
    }

    public int getBrickHP() {
        return brickHP;
    }

    public void setBrickHP(int brickHP) {
        this.brickHP = brickHP;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public void update() {
    }
}

