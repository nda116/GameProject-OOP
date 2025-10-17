package com.arkanoid.powerups;

import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.Ball;
import com.arkanoid.entities.balls.BallManager;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

/**
 * "Extra Ball" power-up.
 * <p>
 * When collected, this power-up spawns an additional ball
 * that launches upward from the paddle in a random direction.
 * The extra ball behaves exactly like normal balls in the game.
 * </p>
 */
public class ExtraBallPowerUp extends PowerUp {
    private static final double DEFAULT_RADIUS = 8.0;
    private static final double DEFAULT_SPEED = 6.0;
    private static final Random random = new Random();

    /**
     * Creates a new ExtraBallPowerUp at the given position.
     *
     * @param x the X-coordinate of the power-up's position
     * @param y the Y-coordinate of the power-up's position
     */
    public ExtraBallPowerUp(double x, double y) {
        super(x, y, EXTRABALL);
        setObjectImage("/images/powerups/extra_ball_powerup.png");
    }

    /**
     * Spawns an additional ball when the power-up is collected.
     * @param paddle      the paddle that collects the power-up
     * @param ballManager the manager responsible for tracking all active balls
     */
    @Override
    public void applyEffect(Paddle paddle, BallManager ballManager) {
        if (ballManager == null || paddle == null) return;

        double radius = DEFAULT_RADIUS;
        double speed = DEFAULT_SPEED;

        if (!ballManager.getBallsList().isEmpty()) {
            Ball sample = ballManager.getBallsList().get(0);
            radius = sample.getWidth() / 2.0;
            speed = sample.getSpeed();
        }

        double startX = paddle.getX() + paddle.getWidth() / 2.0 - radius;
        double startY = paddle.getY() - radius * 2.0 - 1.0;

        Ball newBall = new Ball(startX, startY, radius, speed);

        double angleDeg = 30.0 + random.nextDouble() * 120.0;
        double angleRad = Math.toRadians(angleDeg);
        double dirX = (random.nextBoolean() ? 1 : -1) * Math.sin(angleRad);
        double dirY = -Math.cos(angleRad);

        newBall.setDirectionX(dirX);
        newBall.setDirectionY(dirY);

        ballManager.addBall(newBall);
    }

    @Override
    public void removeEffect(Paddle paddle, BallManager ballManager) {
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
