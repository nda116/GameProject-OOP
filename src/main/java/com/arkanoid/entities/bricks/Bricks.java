package com.arkanoid.entities.bricks;

import com.arkanoid.core.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * void HPlost() Minus HP of brick.
 */
public class Bricks extends GameObject {
    private int brickHP;
    private String type;
    public static final String NORMAL = "normal";
    public static final String INVINCIBLE = "invincible";
    public static final String EXPLOSION = "explosion";
    public static final int EXPLOSION_RADIUS = 35;

    private Image normalBrick;
    private Image inviBrick;
    private Image exploBrick;

    public Bricks(double x, double y, double width, double height, String type) {
        super(x, y, width, height);
        this.type = type;
        if (this.type.equals(NORMAL) || this.type.equals(EXPLOSION)) {
            brickHP = 1;
        } else if (this.type.equals(INVINCIBLE)) {
            brickHP = 100;
        }

        try {
            normalBrick = new Image(getClass().getResourceAsStream("/images/bricks/Normal_Blue.png"));
            inviBrick = new Image(getClass().getResourceAsStream("/images/bricks/Invincible_Brick.png"));
            exploBrick = new Image(getClass().getResourceAsStream("/images/bricks/Explosion_Brick.png"));
        } catch (Exception e) {
            System.out.println("Can not find image one of three type brick");
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

    /**
     * Minus HP of brick.
     */
    public void HPlost() {
        brickHP --;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(GraphicsContext gc) {
        switch (type) {
            case NORMAL -> gc.drawImage(normalBrick, getX(), getY(), getWidth(), getHeight());
            case EXPLOSION -> gc.drawImage(exploBrick, getX(), getY(), getWidth(), getHeight());
            case INVINCIBLE -> gc.drawImage(inviBrick, getX(), getY(), getWidth(), getHeight());
        }
    }
}

