package com.arkanoid.entities.bricks;



/**
 * Constructor for ExplosionBricks
 */
public class ExplosionBrick extends Brick {

    public ExplosionBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Brick.EXPLOSION);

        setBrickHP(1);
        setBrickScore(20);
        setObjectImage("/images/bricks/explosion_brick.png");
    }
}
