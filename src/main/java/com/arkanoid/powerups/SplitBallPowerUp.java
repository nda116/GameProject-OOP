package com.arkanoid.powerups;

import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.Ball;
import com.arkanoid.entities.balls.BallManager;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the "Split Ball" power-up.
 * <p>
 * When collected, this power-up makes each active ball
 * split into two — the original continues its path,
 * and the new ball launches from the same position
 * with an angle offset of 45 degrees.
 * </p>
 */
public class SplitBallPowerUp extends PowerUp {
    private static final double ANGLE_OFFSET = Math.toRadians(45);

    /**
     * Creates a new SplitBallPowerUp at the given position.
     *
     * @param x the X-coordinate of the power-up's position
     * @param y the Y-coordinate of the power-up's position
     */
    public SplitBallPowerUp(double x, double y) {
        super(x, y, SPLITBALL);
        setObjectImage("/images/powerups/split_ball_powerup.png");
    }

    /**
     * Duplicates each existing ball with a 45-degree direction offset.
     *
     * @param paddle      the paddle that collects the power-up
     * @param ballManager the manager responsible for all active balls
     */
    @Override
    public void applyEffect(Paddle paddle, BallManager ballManager) {
        if (ballManager == null) return;

        List<Ball> newBalls = new ArrayList<>();

        for (Ball ball : ballManager.getBallsList()) {
            double x = ball.getX();
            double y = ball.getY();
            double radius = ball.getWidth() / 2.0;
            double speed = ball.getSpeed();

            double dirX = ball.getDirectionX();
            double dirY = ball.getDirectionY();

            double newDirX = dirX * Math.cos(ANGLE_OFFSET) - dirY * Math.sin(ANGLE_OFFSET);
            double newDirY = dirX * Math.sin(ANGLE_OFFSET) + dirY * Math.cos(ANGLE_OFFSET);

            Ball splitBall = new Ball(x, y, radius, speed);
            splitBall.setDirectionX(newDirX);
            splitBall.setDirectionY(newDirY);
            newBalls.add(splitBall);
        }

        for (Ball b : newBalls) {
            ballManager.addBall(b);
        }
    }

    /**
     * This effect is permanent; no removal is required.
     */
    @Override
    public void removeEffect(Paddle paddle, BallManager ballManager) {
        // No removal needed
    }

    /**
     * Draws the power-up on screen.
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
