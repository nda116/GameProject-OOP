package com.arkanoid.entities.bricks;

import com.arkanoid.core.GameObject;
import javafx.scene.Group;

/**
 * void HPlost() Minus HP of brick.
 */
public abstract class Bricks extends GameObject {
    private int brickHP;
    private String type;
    public static final String NORMAL = "normal";
    public static final String INVINCIBLE = "invincible";
    public static final String EXPLOSION = "explosion";


    public Bricks(double x, double y, double width, double height, String type, String imagePath) {
        super(x, y, width, height, imagePath);
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

    @Override
    public void update() {
    }
}

