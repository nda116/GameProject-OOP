package com.arkanoid.powerups;

import com.arkanoid.entities.balls.Ball;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for SlowBallPowerUp
 */
public class SlowBallPowerUp extends PowerUp{
    private static final long EFFECT_DURATION = 5000;

    public SlowBallPowerUp (double x, double y) {
        super(x, y, SLOWBALL);
        setObjectImage("/images/powerups/slowball_powerup.png");
    }

    @Override
    public void applyEffect(Paddle paddle, BallManager ballManager) {
        new Thread(() -> {
            for (Ball ball : ballManager.getBallsList()) {
                ball.setSpeed(ball.getSpeed() * 0.6);
            }
            try {
                Thread.sleep(EFFECT_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Ball ball : ballManager.getBallsList()) {
                ball.setSpeed(ball.getSpeed() / 0.6);
            }
        }).start();
    }

    @Override
    public void removeEffect(Paddle paddle, BallManager ballManager) { }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}