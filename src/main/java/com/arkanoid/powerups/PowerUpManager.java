package com.arkanoid.powerups;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

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
    public void updatePowerUp() {
        for (PowerUp powerup : powerupList) {
            powerup.update();
        }
    }

    /**
     * Update powerupsList.
     * Remove powerups from powerupsList if is Falling false.
     */
    public void updatePowerUpList() {
        Iterator<PowerUp> it = powerupList.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            if (!p.isFalling()) {
                it.remove();
            }
        }
    }

    /**
     * render powerups from powerupsList.
     * @param gc GraphicContext.
     */
    public void renderPowerUpList(GraphicsContext gc) {
        for (PowerUp powerUp : powerupList) {
            powerUp.render(gc);
        }
    }
}