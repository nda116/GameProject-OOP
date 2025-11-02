package com.arkanoid.powerups;

import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import com.arkanoid.entities.bullets.BulletManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

/**
 * Constructor for ExpandPowerUp
 */
public class ExpandPowerUp extends PowerUp {
    private Timeline timeline;
    private double originalWidth;
    private static final long EFFECT_DURATION = 8;

    public ExpandPowerUp (double x, double y) {
        super(x, y, EXPAND);
        setObjectImage("/images/powerups/expand_powerup.png");
    }

    @Override
    public void applyEffect (Paddle paddle, BallManager ballManager, BulletManager bulletManager) {
        originalWidth = paddle.getWidth();
        double expandLength = originalWidth * 0.4;

        paddle.setX(paddle.getX() - expandLength / 2);
        paddle.setWidth(originalWidth + expandLength);
        paddle.setObjectImage("/images/paddle/expand_paddle.png");

        timeline = new Timeline(new KeyFrame(Duration.seconds(EFFECT_DURATION), e -> {
            paddle.setWidth(originalWidth);
            paddle.setX(paddle.getX() + expandLength / 2);
            paddle.setObjectImage("/images/paddle/normal_paddle.png");
            setRemove(true);
        }));

        timeline.play();
    }

    @Override
    public void removeEffect(Paddle paddle, BallManager ballManager, BulletManager bulletManager) {
        if (timeline != null) {
            timeline.stop();
        }
        paddle.setWidth(originalWidth);
        paddle.setObjectImage("/images/paddle/normal_paddle.png");
        setRemove(true);
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}