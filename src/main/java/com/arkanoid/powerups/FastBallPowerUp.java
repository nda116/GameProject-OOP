package com.arkanoid.powerups;

import com.arkanoid.entities.balls.Ball;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for FastBallPowerUp
 */
public class FastBallPowerUp extends PowerUp{
    public FastBallPowerUp (double x, double y) {
        super(x, y, FASTBALL);
        setObjectImage("/images/powerups/fastball_powerup.png");
    }

    @Override
    public void applyEffect(Paddle paddle, BallManager ballmanager) {
        for (Ball ball : ballmanager.getBallsList()) {
            ball.setSpeed(ball.getSpeed() * 1.6);
            break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
