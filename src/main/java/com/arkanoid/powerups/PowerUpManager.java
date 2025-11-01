package com.arkanoid.powerups;

import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.BallManager;
import com.arkanoid.entities.bullets.BulletManager;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

/**
 * Manage power-ups for the game.
 * void addPowerUps(PowerUp newPowerup) add new powerup to powerupsList.
 * void updatePowerUp() update powerups from powerupsList.
 * void updatePowerUpList() Update powerupsList.
 * void renderPowerUpList(GraphicsContext gc) render powerups from powerupsList.
 */
public class PowerUpManager {
    private final ArrayList<PowerUp> powerupList = new ArrayList<>();
    private final Timer timer = new Timer(true);

    public ArrayList<PowerUp> getPowerupList() {
        return powerupList;
    }

    /**
     * add new powerup to powerupsList.
     * @param newPowerup new powerup.
     */
    public void addPowerUps(PowerUp newPowerup) {
        powerupList.add(newPowerup);
    }

    /**
     * update powerups from powerupsList.
     */
    public void updatePowerUp(double deltaTime) {
        for (PowerUp powerup : powerupList) {
            powerup.update(deltaTime);
        }
    }

    /**
     * Update powerupsList.
     * Remove powerups from powerupsList if is Falling false.
     */
    public void updatePowerUpList() {
        Iterator<PowerUp> it = powerupList.iterator();
        while (it.hasNext()) {
            PowerUp powerup = it.next();
            if (powerup.isRemove()) {
                it.remove();
            }
        }
    }

    public void clearPowerUpList(Paddle paddle, BallManager ballManager, BulletManager bulletManager) {
        for (PowerUp powerup : powerupList) {
            powerup.removeEffect(paddle, ballManager, bulletManager);
        }
    }

    /**
     * render powerups from powerupsList.
     * @param gc GraphicContext.
     */
    public void renderPowerUpList(GraphicsContext gc) {
        for (PowerUp powerUp : powerupList) {
            if (powerUp.isFalling()) {
                powerUp.render(gc);
            }
        }
    }
}