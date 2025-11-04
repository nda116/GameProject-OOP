package com.arkanoid.entities;

import com.arkanoid.core.MovableObject;

import javafx.scene.canvas.GraphicsContext;

import static com.arkanoid.Main.WINDOW_HEIGHT;
import static com.arkanoid.Main.WINDOW_WIDTH;


/**
 * class Paddle contains.
 * void moveLeft(), void moveRight()
 */
public class Paddle extends MovableObject {
    private double speed;
    private final static double PADDLE_WIDTH = 150;
    private final static double PADDLE_HEIGHT = 25;
    private final static double PADDLE_SPEED = 550;

    public Paddle() {
        super(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT, 0, 0);
        this.speed = PADDLE_SPEED;

        setObjectImage("/images/paddle/normal_paddle.png");
        this.setDefault();
    }

    public void moveLeft(double deltatime) {
        setDx(-speed);
        move(deltatime);
    }

    public void moveRight(double deltatime) {
        setDx(speed);
        move(deltatime);
    }

    /**
     * set paddle position to middle of screen.
     */
    public void setDefault() {
        setX((WINDOW_WIDTH - getWidth()) / 2);
        setY(WINDOW_HEIGHT - 50);
    }

    /**
     * Stops the paddle's horizontal movement.
     */
    public void stop() {
        setDx(0);
    }

    @Override
    public void update(double deltaTime) {
        if (getX() < 0) {
            setX(0);
        }
        if (getX() + getWidth() > WINDOW_WIDTH) {
            setX(WINDOW_WIDTH - getWidth());
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}