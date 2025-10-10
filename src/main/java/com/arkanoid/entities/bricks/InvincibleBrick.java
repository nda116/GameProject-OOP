package com.arkanoid.entities.bricks;

import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for InvincibleBricks.
 */
public class InvincibleBrick extends Brick {
    public InvincibleBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Brick.INVINCIBLE);
        setBrickHP(100);

        setObjectImage("/images/bricks/invincible_brick.png");
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());

    }
}
