package com.arkanoid.PowerUps;
import com.arkanoid.core.MovableObject;
import java.awt.*;

/**
 * This class represents power-ups for the game.
 * Each Power-Up is a movable object that falls from the position of a destroyed brick.
 * When it collides with the player's paddle, it triggers effect.
 */
public class PowerUps extends MovableObject {
    private int type;
    private boolean active = true;

    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    public PowerUps(double x, double y, int type) {
        super(x, y, WIDTH, HEIGHT,0 ,3);
        this.type = type;
    }


    public void update() {
        move();
    }

    @Override
    public void render() {

    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public int getType() {
        return type;
    }

}
