package com.arkanoid.entities.bricks;


/**
 * Constructor for InvincibleBricks.
 */
public class InvincibleBrick extends Brick {
    public InvincibleBrick(double x, double y, double width, double height) {
        super(x, y, width, height, Brick.INVINCIBLE);

        setBrickHP(1000);
        setBrickScore(0);
        setObjectImage("/images/bricks/invincible_brick.png");
    }
}
