package com.arkanoid.entities.balls;

import com.arkanoid.core.MovableObject;
import com.arkanoid.core.GameObject;
import com.arkanoid.entities.Paddle;

import javafx.scene.canvas.GraphicsContext;

import static com.arkanoid.Main.WINDOW_HEIGHT;
import static com.arkanoid.Main.WINDOW_WIDTH;

/**
 * class Ball contains.
 * void updateVelocity()
 * void bounceOff(GameObject other), void bounceOffPaddle(Paddle paddle)
 * boolean isOutOfBounds(), void reset(double startX, double startY)
 */
public class Ball extends MovableObject {
    private double speed;
    private double directionX;
    private double directionY;
    private final static double BALL_RADIUS = 12;
    private final static double BALL_SPEED = 330;

    public Ball() {
        super(0, 0, BALL_RADIUS * 2, BALL_RADIUS * 2, 0, 0);
        this.speed = BALL_SPEED;

        setObjectImage("/images/ball.png");

        directionX = 0;
        directionY = 0;
        updateVelocity();
    }

    public Ball(double x, double y) {
        super(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2, 0, 0);
        this.speed = BALL_SPEED;

        setObjectImage("/images/ball.png");

        directionX = 0;
        directionY = 0;
        updateVelocity();
    }

    /**
     * set Ball position to middle of paddle
     * @param paddle paddle.
     */
    public void setDefaultBall(Paddle paddle) {
        setX(paddle.getX() + (paddle.getWidth() - getWidth()) / 2);
        setY(paddle.getY() - getHeight());
    }

    /**
     * Updates the velocity (dx, dy) based on speed and direction.
     */
    private void updateVelocity() {
        setDx(speed * directionX);
        setDy(speed * directionY);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
        updateVelocity();
    }

    public double getDirectionX() {
        return directionX;
    }

    public void setDirectionX(double directionX) {
        this.directionX = directionX;
        updateVelocity();
    }

    public double getDirectionY() {
        return directionY;
    }

    public void setDirectionY(double directionY) {
        this.directionY = directionY;
        updateVelocity();
    }

    /**
     * Handles bouncing off another GameObject.
     * Determines the collision side by overlap (minimum translation)
     * and reverses the appropriate direction. Handles corner collisions
     * by reflecting both axes.
     *
     * @param other The GameObject to bounce off from
     */
    public void bounceOff(GameObject other) {

        final double PUSH_EPS = 0.01; // small push to avoid sticking

        double ballLeft   = getX();
        double ballRight  = getX() + getWidth();
        double ballTop    = getY();
        double ballBottom = getY() + getHeight();

        double otherLeft   = other.getX();
        double otherRight  = other.getX() + other.getWidth();
        double otherTop    = other.getY();
        double otherBottom = other.getY() + other.getHeight();

        // overlap on each axis
        double overlapX = Math.min(ballRight,  otherRight)  - Math.max(ballLeft,  otherLeft);
        double overlapY = Math.min(ballBottom, otherBottom) - Math.max(ballTop, otherTop);

        // centers for pushing direction
        double ballCenterX = ballLeft + getWidth()  / 2.0;
        double ballCenterY = ballTop  + getHeight() / 2.0;
        double otherCenterX = otherLeft + other.getWidth() / 2.0;
        double otherCenterY = otherTop  + other.getHeight() / 2.0;

        if (overlapX <= 0 || overlapY <= 0) return;

        // corner case: very close overlaps -> reflect both axes
        final double CORNER_THRESHOLD = 1e-6;
        if (Math.abs(overlapX - overlapY) < CORNER_THRESHOLD) {
            directionX = -directionX;
            directionY = -directionY;
            updateVelocity();

            if (ballCenterX > otherCenterX) {
                setX(otherRight + PUSH_EPS);
            } else {
                setX(otherLeft - getWidth() - PUSH_EPS);
            }
            if (ballCenterY > otherCenterY) {
                setY(otherBottom + PUSH_EPS);
            } else {
                setY(otherTop - getHeight() - PUSH_EPS);
            }
            return;
        }

        // decide which axis to resolve (smallest overlap -> that axis caused collision)
        if (overlapX < overlapY) {
            // horizontal collision: reflect X
            directionX = -directionX;
            updateVelocity();

            if (ballCenterX > otherCenterX) {
                // ball came from right -> place to the right
                setX(otherRight + PUSH_EPS);
            } else {
                // ball came from left -> place to the left
                setX(otherLeft - getWidth() - PUSH_EPS);
            }
        } else {
            // vertical collision: reflect Y
            directionY = -directionY;
            updateVelocity();

            if (ballCenterY > otherCenterY) {
                // ball came from below -> place below
                setY(otherBottom + PUSH_EPS);
            } else {
                // ball came from above -> place above
                setY(otherTop - getHeight() - PUSH_EPS);
            }
        }
    }

    /**
     * Handles special bounce behavior with the paddle.
     * Bounce angle depends on where the ball hits the paddle.
     *
     * @param paddle The paddle to bounce off from
     */
    public void bounceOffPaddle(Paddle paddle) {
        double ballCenter = getX() + getWidth() / 2.0;
        double paddleCenter = paddle.getX() + paddle.getWidth() / 2.0;
        double hitPosition = (ballCenter - paddleCenter) / (paddle.getWidth() / 2.0);

        // clamps hit position to [-1,1], 0 center
        hitPosition = Math.max(-1.0, Math.min(1.0, hitPosition));

        // max bounce angle from vertical (use +/-75 degrees)
        double maxBounceAngle = Math.toRadians(75.0);
        double angle = hitPosition * maxBounceAngle;

        directionX = Math.sin(angle);
        directionY = -Math.cos(angle);
        updateVelocity();

        // Place ball above paddle
        setY(paddle.getY() - getHeight() - 0.01);
    }

    /**
     * Checks if the ball has fallen below the screen.
     *
     * @return true if ball is out of bounds, false otherwise
     */
    public boolean isOutOfBounds() {
        return getY() > WINDOW_HEIGHT;
    }

    @Override
    public void update(double deltaTime) {
        move(deltaTime);

        // Collision with left and right walls
        if (getX() <= 0 || getX() + getWidth() >= WINDOW_WIDTH) {
            directionX = -directionX;
            updateVelocity();
            setX(Math.max(0, Math.min(getX(), WINDOW_WIDTH - getWidth())));
        }

        // Collision with top wall
        if (getY() <= 0) {
            directionY = -directionY;
            updateVelocity();
            setY(0);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}