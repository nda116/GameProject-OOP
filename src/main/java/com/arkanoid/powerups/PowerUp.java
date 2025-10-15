package com.arkanoid.powerups;

import com.arkanoid.core.MovableObject;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;

import static com.arkanoid.Main.WINDOW_HEIGHT;

/**
 * This class represents power-ups for the game.
 */
public abstract class PowerUp extends MovableObject {
    private int type;
    private boolean falling = true;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    public static final int EXPAND = 0;
    public static final int SLOWBALL = 1;
    public static final int FASTBALL = 2;

    public PowerUp(double x, double y, int type) {
        super(x, y, WIDTH, HEIGHT, 0, 1);
        this.type = type;
    }

    /**
     * Update position of powerup.
     * Stop falling if out of bounds.
     */
    public void update() {
        move();
        if (getY() > WINDOW_HEIGHT) {
            stopFalling();
        }
    }

    public boolean isFalling() {
        return falling;
    }

    public void stopFalling() {
        falling = false;
    }

    public int getType() {
        return type;
    }

    /**
     * Apply the power-up effect.
     */
    public abstract void applyEffect(Paddle paddle, BallManager ballManager);

    /**
     * Remove (revert) the power-up effect after it expires.
     */
    public abstract void removeEffect(Paddle paddle, BallManager ballManager);
}