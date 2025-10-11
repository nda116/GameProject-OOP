package com.arkanoid.powerups;

import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for ExpandPowerUp
 */
public class ExpandPowerUp extends PowerUp{
    private static final long EFFECT_DURATION = 8000;

    public ExpandPowerUp (double x, double y) {
        super(x, y, EXPAND);
        setObjectImage("/images/powerups/expand_powerup.png");
    }

    @Override
    public void applyEffect (Paddle paddle, BallManager ballManager) {
        new Thread(() -> {
            double originalWidth = paddle.getWidth();
            paddle.setWidth(originalWidth * 1.5);
            try {
                Thread.sleep(EFFECT_DURATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            paddle.setWidth(originalWidth);
        }).start();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
