package com.arkanoid.entities.bricks;

/**
 * Constructor for InvincibleBricks.
 */
public class InvincibleBricks extends Bricks{
    public InvincibleBricks(double x, double y, double width, double height) {
        super(x, y, width, height, Bricks.INVINCIBLE, "/bricks/Invincible_Brick.png");
        setBrickHP(100);
    }
}
