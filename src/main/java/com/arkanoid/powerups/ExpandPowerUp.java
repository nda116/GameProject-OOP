package com.arkanoid.powerups;

import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for ExpandPowerUp
 */
public class ExpandPowerUp extends PowerUp {
    private static final long EFFECT_DURATION = 8000;

    public ExpandPowerUp (double x, double y) {
        super(x, y, EXPAND);
        setObjectImage("/images/powerups/expand_powerup.png");
    }

    @Override
    public void applyEffect (Paddle paddle, BallManager ballManager) {
        paddle.expand();
    }

    @Override
    public void removeEffect(Paddle paddle, BallManager ballManager) {

    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}