package com.arkanoid.powerups;

import com.arkanoid.core.MovableObject;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import com.arkanoid.entities.bullets.BulletManager;

import static com.arkanoid.Main.WINDOW_HEIGHT;

/**
 * This class represents power-ups for the game.
 */
public abstract class PowerUp extends MovableObject {
    private int type;
    private boolean falling = true;
    private boolean remove = false;
    private static final int WIDTH = 30;
    private static final int HEIGHT = 30;
    public static final int SPLITBALL = 0;
    public static final int SLOWBALL = 1;
    public static final int FASTBALL = 2;
    public static final int FIREBULLET = 3;
    public static final int EXPAND = 4;

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public PowerUp(double x, double y, int type) {
        super(x, y, WIDTH, HEIGHT, 0, 110);
        this.type = type;
    }

    /**
     * Update position of powerup.
     * Stop falling if out of bounds.
     */
    public void update(double deltaTime) {
        if (isFalling()) {
            move(deltaTime);
        }
        if (getY() > WINDOW_HEIGHT) {
            remove = true;
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
    public abstract void applyEffect(Paddle paddle, BallManager ballManager,
                                     BulletManager bulletManager);

    /**
     * Remove (revert) the power-up effect after it expires.
     */
    public abstract void removeEffect(Paddle paddle, BallManager ballManager, BulletManager bulletManager);
}