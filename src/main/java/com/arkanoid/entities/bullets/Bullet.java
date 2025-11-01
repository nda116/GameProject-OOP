package com.arkanoid.entities.bullets;

import com.arkanoid.core.MovableObject;
import javafx.scene.canvas.GraphicsContext;

/**
 * void deActive() deactive the bullet.
 */
public class Bullet extends MovableObject {
    private boolean active;

    public Bullet(double x, double y, double width, double height) {
        super(x, y, width, height, 0, -330);
        setObjectImage("/images/bullet.png");
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void deActive() {
        this.active = false;
    }

    @Override
    public void update(double deltaTime) {
        move(deltaTime);
        if (getY() < 0) {
            deActive();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
