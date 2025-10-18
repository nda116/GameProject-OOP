package com.arkanoid.entities;

import com.arkanoid.core.MovableObject;

import com.arkanoid.entities.bullets.BulletManager;
import javafx.scene.canvas.GraphicsContext;

import static com.arkanoid.Main.WINDOW_HEIGHT;
import static com.arkanoid.Main.WINDOW_WIDTH;


/**
 * class Paddle contains.
 * void moveLeft(), void moveRight()
 */
public class Paddle extends MovableObject {
    private double speed;
    private BulletManager bullets = new BulletManager();

    public Paddle(double x, double y, double width, double height,
                  double speed) {
        super(x, y, width, height, 0, 0);
        this.speed = speed;

        setObjectImage("/images/paddle/normal_paddle.png");
    }

    public BulletManager getBullets() {
        return bullets;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void moveLeft() {
        setDx(-speed);
        move();
    }

    public void moveRight() {
        setDx(speed);
        move();
    }

    /**
     * set paddle position to middle of screen, remove powerup.
     */
    public void setDefault() {
        setX((WINDOW_WIDTH - getWidth()) / 2);
        setY(WINDOW_HEIGHT - 50);
        bullets.stopPowerUp();
    }

    /**
     * Stops the paddle's horizontal movement.
     */
    public void stop() {
        setDx(0);
    }

    /**
     * Create bullets and start fire them.
     */
    public void fireBullet() {
        bullets.spawnBullets(this);
    }

    @Override
    public void update() {
        if (getX() < 0) {
            setX(0);
        }
        if (getX() + getWidth() > WINDOW_WIDTH) {
            setX(WINDOW_WIDTH - getWidth());
        }
        bullets.updateBullet();
        bullets.updateBulletList();
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
        bullets.renderBulletList(gc);
    }
}