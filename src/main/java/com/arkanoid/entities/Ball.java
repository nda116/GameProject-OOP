package com.arkanoid.entities;

import com.arkanoid.core.MovableObject;
import com.arkanoid.core.GameObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * class Ball contains.
 * void updateVelocity()
 * void bounceOff(GameObject other)
 * boolean isOutOfBounds(), void reset(double startX, double startY)
 */
public class Ball extends MovableObject {
    private double speed;
    private double directionX;
    private double directionY;
    private final double screenWidth;
    private final double screenHeight;

    public Ball(double x, double y, double radius, double speed,
                double screenWidth, double screenHeight) {
        super(x, y, radius * 2, radius * 2, 0, 0);
        this.speed = speed;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        setObjectImage("/images/ball.png");

        directionX = 0;
        directionY = 1;
        updateVelocity();
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
     * Determines the collision side and reverses the appropriate direction.
     *
     * @param other The GameObject to bounce off from
     */
    public void bounceOff(GameObject other) {
        if (!checkCollision(other)) return;

        // Calculate collision position
        double ballCenterX = getX() + getWidth() / 2;
        double ballCenterY = getY() + getHeight() / 2;
        double objCenterX = other.getX() + other.getWidth() / 2;
        double objCenterY = other.getY() + other.getHeight() / 2;

        double deltaX = ballCenterX - objCenterX;
        double deltaY = ballCenterY - objCenterY;

        // Determine collision direction
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Collision from left or right
            directionX = -directionX;
            updateVelocity();
            if (deltaX > 0) {
                setX(other.getX() + other.getWidth());
            } else {
                setX(other.getX() - getWidth());
            }
        } else {
            // Collision from top or bottom
            directionY = -directionY;
            updateVelocity();
            if (deltaY > 0) {
                setY(other.getY() + other.getHeight());
            } else {
                setY(other.getY() - getHeight());
            }
        }
    }

    /**
     * Handles special bounce behavior with the paddle.
     * The bounce angle depends on where the ball hits the paddle.
     *
     * @param paddle The paddle to bounce off from
     */
    public void bounceOffPaddle(Paddle paddle) {
        if (!checkCollision(paddle)) return;

        // Calculate reflection angle based on hit position on paddle
        double ballCenter = getX() + getWidth() / 2;
        double paddleCenter = paddle.getX() + paddle.getWidth() / 2;
        double hitPosition = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);

        // Clamp hitPosition to [-1, 1]
        hitPosition = Math.max(-1, Math.min(1, hitPosition));

        // Calculate reflection angle (30 to 150 degrees)
        double angle = hitPosition * Math.PI / 3; // -60 to +60 degrees

        directionX = Math.sin(angle);
        directionY = -Math.cos(angle);
        updateVelocity();

        // Place ball above paddle
        setY(paddle.getY() - getHeight());
    }

    /**
     * Checks if the ball has fallen below the screen.
     *
     * @return true if ball is out of bounds, false otherwise
     */
    public boolean isOutOfBounds() {
        return getY() > screenHeight;
    }

    /**
     * Resets the ball to a starting position.
     *
     * @param startX The x-coordinate for the reset position
     * @param startY The y-coordinate for the reset position
     */
    public void reset(double startX, double startY) {
        setX(startX);
        setY(startY);

        directionX = 0;
        directionY = 1;
        updateVelocity();
    }

    @Override
    public void update() {
        move();

        // Collision with left and right walls
        if (getX() <= 0 || getX() + getWidth() >= screenWidth) {
            directionX = -directionX;
            updateVelocity();
            setX(Math.max(0, Math.min(getX(), screenWidth - getWidth())));
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

        //System.out.println("Paddle: (" + getX() + ", " + getY() + ")");
    }
}