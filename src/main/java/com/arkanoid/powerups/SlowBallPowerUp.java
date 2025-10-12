package com.arkanoid.powerups;

import com.arkanoid.entities.balls.Ball;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for SlowBallPowerUp
 */
public class SlowBallPowerUp extends PowerUp{
    public SlowBallPowerUp (double x, double y) {
        super(x, y, SLOWBALL);
        setObjectImage("/images/powerups/slowball_powerup.png");
    }

    @Override
    public void applyEffect(Paddle paddle, BallManager ballmanager) {
        for (Ball ball : ballmanager.getBallsList()) {
            ball.setSpeed(ball.getSpeed() * 0.7);
            break;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
