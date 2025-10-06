package com.arkanoid.core.bricks;

import com.arkanoid.core.GameObject;

public class Bricks extends GameObject {
    private int brickHP;
    private String type;
    public static final String NORMAL = "normal";
    public static final String INVINCIBLE = "invincible";
    public static final String EXPLOSION = "explosion";
    public static final int EXPLOSION_RADIUS = 100;

    public Bricks(double x, double y, double width, double height, String type) {
        super(x, y, width, height);
        this.type = type;
        if (this.type.equals(NORMAL) || this.type.equals(EXPLOSION)) {
            brickHP = 1;
        } else if (this.type.equals(INVINCIBLE)) {
            brickHP = 100;
        }
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

    public void HPlost() {
        brickHP --;
    }

    /**
     * Calculate distance between center of 2 bricks.
     * @param other other brick.
     * @return distance between them.
     */
    public double Distance (Bricks other) {
        double thiscenterX = (getX() + getWidth()) / 2;
        double thiscenterY = (getY() + getHeight()) / 2;
        double othercenterX = (other.getX() + other.getWidth()) / 2;
        double othercenterY = (other.getY() + other.getHeight()) / 2;
        return Math.sqrt(Math.pow(othercenterX - thiscenterX, 2) + Math.pow(othercenterY - thiscenterY, 2));
    }
}

