package com.arkanoid.powerups;

import com.arkanoid.core.MovableObject;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;

import static com.arkanoid.core.GameManager.CANVAS_HEIGHT;

/**
 * This class represents power-ups for the game.
 * applyEffect(Paddle, BallManager) apply effect on balls and paddle base on type of powerup.
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
        super(x, y, WIDTH, HEIGHT,0 ,1);
        this.type = type;
    }

    /**
     * update position of powerup.
     * set isFalling to false if powerup out bound.
     */
    public void update() {
        move();
        if (getY() > CANVAS_HEIGHT) {
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
     * Apply certain effect for the power-up
     * @param paddle: paddle applied effect.
     * @param ballmanager: balls applied effect.
     */
    public abstract void applyEffect(Paddle paddle, BallManager ballmanager);
}
