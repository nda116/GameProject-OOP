package com.arkanoid.entities.bricks;

public class ExplosionBricks extends Bricks{
    public static final int EXPLOSION_RADIUS = 100;

    public ExplosionBricks(double x, double y, double width, double height) {
        super(x, y, width, height, Bricks.EXPLOSION, "/bricks/Explosion_Brick.png");
        setBrickHP(1);
    }
}
