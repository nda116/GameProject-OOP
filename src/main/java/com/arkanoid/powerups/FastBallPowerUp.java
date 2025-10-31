package com.arkanoid.powerups;

import com.arkanoid.entities.balls.Ball;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import com.arkanoid.entities.bullets.BulletManager;
import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for FastBallPowerUp
 */
public class FastBallPowerUp extends PowerUp{
    private static final long EFFECT_DURATION = 5000;

    public FastBallPowerUp (double x, double y) {
        super(x, y, FASTBALL);
        setObjectImage("/images/powerups/fastball_powerup.png");
    }

    @Override
    public void applyEffect(Paddle paddle, BallManager ballManager, BulletManager bulletManager) {
        new Thread(() -> {
            for (Ball ball : ballManager.getBallsList()) {
                ball.setSpeed(ball.getSpeed() * 1.4);
            }
            try {
                Thread.sleep(EFFECT_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Ball ball : ballManager.getBallsList()) {
                ball.setSpeed(ball.getSpeed() / 1.4);
            }
        }).start();
        setRemove(true);
    }

    @Override
    public void removeEffect(Paddle paddle, BallManager ballManager, BulletManager bulletManager) {
        // No removal needed
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}