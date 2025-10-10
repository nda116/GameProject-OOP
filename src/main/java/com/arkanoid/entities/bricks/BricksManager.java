package com.arkanoid.entities.bricks;

import com.arkanoid.entities.Ball;
import javafx.scene.canvas.GraphicsContext;
import com.arkanoid.PowerUps.PowerUps;
import com.arkanoid.PowerUps.PowerUpsManager;

import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * void addBrick(Bricks) add new brick to brickList.
 * void updateBrickHP(Ball) update bricks HP in brickList.
 * void explosionTrigger (Brick) Triggers a chain explosion starting from the specified brick.
 * void updateBrickList () update brickList, remove bricks which have HP <= 0.
 * void renderBrickList(GraphicsContext) render bricks from bricksList.
 */
public class BricksManager {
    private ArrayList<Brick> bricksList = new ArrayList<>();

    private Random rand = new Random();
    private PowerUpsManager powerUpsManager;
    private float powerUpDropChance = 0.1f; // start with 10%
    private final float MAX_DROP_CHANCE = 0.9f; // maximum 90%

    public BricksManager(PowerUpsManager powerUpManager) {
        this.powerUpsManager = powerUpsManager;
    }
    public void resetDropChance() {
        powerUpDropChance = 0.1f;
    }

    public void addBrick (Brick newBrick) {
        bricksList.add(newBrick);
    }

    /**
     * update bricks HP in brickList.
     * minus HP if collision between ball and brick occur.
     * if brick type is EXPLOSION, minus HP of surround bricks.
     * @param ball ball.
     */
    public void updateBrickHP(Ball ball) {
        for (int i = 0; i < bricksList.size(); i++) {
            Brick current = bricksList.get(i);
            if (current.checkCollision(ball)) {
                current.HPlost();
                ball.bounceOff(current);
                if (current.getType().equals(Brick.EXPLOSION)) {
                    explosionTrigger(current);
                }
            }
        }
    }

    /**
     * Triggers a chain explosion starting from the specified brick.
     * @param start start of the chain explosion.
     */
    private void explosionTrigger (Brick start) {
        Queue<Brick> explosionQueue = new ArrayDeque<>();
        explosionQueue.add(start);

        while (!explosionQueue.isEmpty()) {
            Brick current = explosionQueue.poll();

            for (int j = 0; j < bricksList.size(); j++){
                Brick check = bricksList.get(j);
                if(check.getBrickHP() > 0 && current.isAdjacent(check)) {
                    check.HPlost();
                    if(check.getType().equals(Brick.EXPLOSION)) {
                        explosionQueue.add(check);
                    }
                }
            }
        }
    }

    /**
     * update brickList, remove bricks which have HP <= 0.
     */
    public void updateBrickList() {
        Iterator<Brick> it = bricksList.iterator();

        while(it.hasNext()) {
            Brick b = it.next();
            if (b.getBrickHP() <= 0) {
                it.remove();
            }
        }
    }

    /**
     * render bricks from bricksList.
     * @param gc GraphicContext.
     */
    public void renderBrickList(GraphicsContext gc) {
        for (int i = 0; i < bricksList.size(); i++) {
            bricksList.get(i).render(gc);
        }
    }

    public void onBrickDestroyed(Bricks brick) {
        if (rand.nextFloat() < powerUpDropChance) {
            int type = rand.nextInt(2); //set up power
            PowerUps p = new PowerUps(brick.getX(), brick.getY(), type);
            if (powerUpsManager != null) {
                powerUpsManager.addPowerUps(p);
            }
        }

        powerUpDropChance += 0.2f;
        if (powerUpDropChance > MAX_DROP_CHANCE) {
            powerUpDropChance = MAX_DROP_CHANCE;
        }
    }
}
