package com.arkanoid.entities.bricks;

import com.arkanoid.core.GameObject;

/**
 * void HPlost() Minus HP of brick.
 * boolean isAdjacent(Brick) check adjacent between 2 Bricks.
 */
public abstract class Brick extends GameObject {
    private int brickHP;
    private int brickScore;
    private int type;
    public static final int NORMAL = 0;
    public static final int INVINCIBLE = 1;
    public static final int EXPLOSION = 2;


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

    @Override
    public void update() {
    }
}

