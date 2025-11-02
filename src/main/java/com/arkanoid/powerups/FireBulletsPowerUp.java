package com.arkanoid.powerups;

import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import com.arkanoid.entities.bullets.BulletManager;
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
    public void applyEffect(Paddle paddle, BallManager ballManager, BulletManager bulletManager) {
        bulletManager.spawnBullets(paddle, this);
    }

    @Override
    public void removeEffect(Paddle paddle, BallManager ballManager, BulletManager bulletManager) {
        bulletManager.stopPowerUp();
        setObjectImage("/images/paddle/normal_paddle.png");
        setRemove(true);
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
