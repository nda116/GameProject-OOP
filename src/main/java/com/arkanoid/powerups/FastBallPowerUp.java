package com.arkanoid.powerups;

import com.arkanoid.entities.balls.Ball;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import com.arkanoid.entities.bullets.BulletManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

/**
 * Constructor for FastBallPowerUp
 */
public class FastBallPowerUp extends PowerUp{
    private Timeline timeline;
    private static final long EFFECT_DURATION = 8;

    public FastBallPowerUp (double x, double y) {
        super(x, y, FASTBALL);
        setObjectImage("/images/powerups/fastball_powerup.png");
    }

    @Override
    public void applyEffect(Paddle paddle, BallManager ballManager, BulletManager bulletManager) {
        for (Ball ball : ballManager.getBallsList()) {
            ball.setSpeed(ball.getSpeed() * 1.4);
        }

        timeline = new Timeline(new KeyFrame(Duration.seconds(EFFECT_DURATION), e -> {
            for (Ball ball : ballManager.getBallsList()) {
                ball.setSpeed(ball.getSpeed() / 1.4);
            }
            setRemove(true);
        }));

        timeline.play();
    }

    @Override
    public void removeEffect(Paddle paddle, BallManager ballManager, BulletManager bulletManager) {
        if (timeline != null) {
            timeline.stop();
        }
        for (Ball ball : ballManager.getBallsList()) {
            ball.setSpeed(ball.getSpeed() / 1.4);
        }
        setRemove(true);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}