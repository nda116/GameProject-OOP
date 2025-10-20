package com.arkanoid.entities.bricks;

import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for ExplosionBricks
 */
public class InvisibleBrick extends Brick {
    public InvisibleBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Brick.INVISIBLE);

        setBrickHP(2);
        setBrickScore(30);
        setObjectImage("/images/bricks/invisible_brick.png");
    }

    @Override
    public void render(GraphicsContext gc) {
        if (getBrickHP() == 1) {
            super.render(gc);
        }
    }
}