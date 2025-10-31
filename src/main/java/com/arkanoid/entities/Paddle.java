package com.arkanoid.entities;

import com.arkanoid.core.MovableObject;

import com.arkanoid.entities.bullets.BulletManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

import static com.arkanoid.Main.WINDOW_HEIGHT;
import static com.arkanoid.Main.WINDOW_WIDTH;


/**
 * class Paddle contains.
 * void moveLeft(), void moveRight()
 */
public class Paddle extends MovableObject {
    private Timeline expandTimeline;
    private boolean appliedPowerUp;
    private double speed;
    private BulletManager bullets = new BulletManager();

    public Paddle(double x, double y, double width, double height,
                  double speed) {
        super(x, y, width, height, 0, 0);
        appliedPowerUp = false;
        this.speed = speed;

        setObjectImage("/images/paddle/normal_paddle.png");
    }

    public boolean isAppliedPowerUp() {
        return appliedPowerUp;
    }

    public void setAppliedPowerUp(boolean appliedPowerUp) {
        this.appliedPowerUp = appliedPowerUp;
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
        appliedPowerUp = false;
        if (expandTimeline != null) {
            expandTimeline.stop();
        }
        bullets.stopPowerUp();
        setX((WINDOW_WIDTH - getWidth()) / 2);
        setY(WINDOW_HEIGHT - 50);
        setObjectImage("/images/paddle/normal_paddle.png");
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

    /**
     * Expand paddle width in 8 seconds.
     */
    public void expand() {
        double originalWidth = getWidth();
        double expandLength = originalWidth * 0.4;

        appliedPowerUp = true;
        setX(getX() - expandLength / 2);
        setWidth(originalWidth + expandLength);
        setObjectImage("/images/paddle/expand_paddle.png");

        expandTimeline = new Timeline(new KeyFrame(Duration.seconds(8), e -> {
            appliedPowerUp = false;
            setWidth(originalWidth);
            setX(getX() + expandLength / 2);
            setObjectImage("/images/paddle/normal_paddle.png");
        }));

        expandTimeline.play();
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