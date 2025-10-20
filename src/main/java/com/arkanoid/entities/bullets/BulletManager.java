package com.arkanoid.entities.bullets;

import com.arkanoid.entities.Paddle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * void spawnBullets(Paddle) create bullets and shoot from side of the paddle.
 * void stopPowerUp() stop create bullets and clear bulletsList.
 * void updateBullet() update bullets from bulletsList.
 * void updateBulletList update bulletsList.
 * void renderBulletList(GraphicsContext) render bullets from bulletsList.
 */
public class BulletManager {
    private Timeline bulletTimeline;;
    private final ArrayList<Bullet> bulletsList = new ArrayList<>();
    private static final int BULLET_WIDTH = 5;
    private static final int BULLET_HEIGHT = 10;

    public ArrayList<Bullet> getBulletsList() {
        return bulletsList;
    }

    /**
     * create bullets and shoot from side of the paddle.
     * @param paddle paddle to shoot.
     */
    public void spawnBullets(Paddle paddle) {
        int totalShots = 10;
        double spawnDelay = 0.2;

        bulletTimeline = new Timeline(new KeyFrame(Duration.seconds(spawnDelay), event -> {
            double leftPaddleX = paddle.getX();
            double rightPaddleX = paddle.getX() + paddle.getWidth() - BULLET_WIDTH;

            bulletsList.add(new Bullet(leftPaddleX, paddle.getY(), BULLET_WIDTH, BULLET_HEIGHT));
            bulletsList.add(new Bullet(rightPaddleX, paddle.getY(), BULLET_WIDTH, BULLET_HEIGHT));
        }));

        bulletTimeline.setCycleCount(totalShots);
        bulletTimeline.play();
    }

    /**
     * stop create bullets and clear bulletsList.
     */
    public void stopPowerUp () {
        if (bulletTimeline != null){
            bulletTimeline.stop();
        }
        bulletsList.clear();
    }

    /**
     * update bullets from bulletsList.
     */
    public void updateBullet() {
        for (Bullet bullet : bulletsList) {
            bullet.update();
        }
    }

    /**
     * update bulletsList.
     * remove bullet that active is false.
     */
    public void updateBulletList() {
        Iterator<Bullet> it = bulletsList.iterator();

        while(it.hasNext()) {
            Bullet bullet = it.next();
            if (!bullet.isActive()) {
                it.remove();
            }
        }
    }

    /**
     * render bullets from bulletsList.
     * @param gc GraphicContext.
     */
    public void renderBulletList(GraphicsContext gc) {
        for (Bullet bullet : bulletsList) {
            bullet.render(gc);
        }
    }
}
