package com.arkanoid.powerups;

import com.arkanoid.entities.Ball;
import com.arkanoid.entities.Paddle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manage power-ups for the game.
 * This class stores and updates power-ups, check collision and activates power-ups.
 */
public class PowerUpsManager {
    private final ArrayList<PowerUps> powerUps = new ArrayList<>();
    private Paddle paddle;
    private final List<Ball> balls;
    private int screenHeight = 600;

    private long doubleScoreStartTime;
    private static final long EFFECT_DURATION = 8000;

    public PowerUpsManager(Paddle paddle, List<Ball> balls) {
        this.paddle = paddle;
        this.balls = balls;
    }

    public void addPowerUps(PowerUps p) {
        powerUps.add(p);
    }

    /**
     * Update power-ups.
     * Check if power-ups falls down the screen or deactivated.
     */
    public void update() {
        Iterator<PowerUps> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUps p = it.next();
            p.update();

            if (p.getY() > screenHeight || !p.isActive()) {
                it.remove();
                continue;
            }

            if (p.checkCollision(paddle)) {
                applyEffect(p);
                p.deactivate();
                it.remove();
            }
        }
    }

    /**
     * Apply certain effect for the power-up
     * @param p power-ups
     *          first power-up: extend paddle
     *          second power-up: decrease ball's speed
     *          third power-up: increase ball's speed
     * Random power-up will be picked when dropped
     */
    private void applyEffect(PowerUps p) {
        switch (p.getType()) {
            case 0:
                new Thread(() -> {
                    double originalWidth = paddle.getWidth();
                    paddle.setWidth(originalWidth * 1.5);
                    try {
                        Thread.sleep(EFFECT_DURATION);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    paddle.setWidth(originalWidth);
                }).start();
                break;
            case 1:
                for (Ball b : balls) {
                    b.setSpeed(b.getSpeed() * 0.7);
                }
                break;
            case 2:
                for (Ball b : balls) {
                    b.setSpeed(b.getSpeed() * 1.6);
                }
                break;
            default:
                System.err.println("Unknown powerup type: " + p.getType());
        }
    }

    public ArrayList<PowerUps> getPowerUps() {
        return powerUps;
    }
}
