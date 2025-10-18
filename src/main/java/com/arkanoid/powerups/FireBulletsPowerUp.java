package com.arkanoid.powerups;

import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for FireBulletsPowerUp
 */
public class FireBulletsPowerUp extends PowerUp {
    public FireBulletsPowerUp(double x, double y) {
        super(x, y, FIREBULLET);
        setObjectImage("/images/powerups/fire_bullets_powerup.png");
    }

    @Override
    public void applyEffect(Paddle paddle, BallManager ballManager) {
        paddle.fireBullet();
    }

    @Override
    public void removeEffect(Paddle paddle, BallManager ballManager) {

    }

    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
