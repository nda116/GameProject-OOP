package com.arkanoid.entities.bricks;

import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for ExplosionBricks
 */
public class ExplosionBrick extends Brick {
    public static final int EXPLOSION_RADIUS = 50;

    public ExplosionBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Brick.EXPLOSION);
        setBrickHP(1);

        setObjectImage("/images/bricks/explosion_brick.png");
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
