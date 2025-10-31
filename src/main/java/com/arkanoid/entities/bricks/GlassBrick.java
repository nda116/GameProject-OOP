package com.arkanoid.entities.bricks;

import javafx.scene.canvas.GraphicsContext;

/**
 * Constructor for ExplosionBricks
 */
public class GlassBrick extends Brick {
    public GlassBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Brick.GLASS);

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