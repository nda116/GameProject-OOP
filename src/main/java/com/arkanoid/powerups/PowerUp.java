package com.arkanoid.powerups;
import com.arkanoid.core.MovableObject;
import javafx.scene.canvas.GraphicsContext;

/**
 * This class represents power-ups for the game.
 * Each Power-Up is a movable object that falls from the position of a destroyed brick.
 * When it collides with the player's paddle, it triggers effect.
 */
public class PowerUp extends MovableObject {
    private int type;
    private boolean active = true;

    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;

    public PowerUp(double x, double y, int type) {
        super(x, y, WIDTH, HEIGHT,0 ,3);
        this.type = type;
    }


    public void update() {
        move();
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
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
