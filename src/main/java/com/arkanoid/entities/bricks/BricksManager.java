package com.arkanoid.entities.bricks;

import java.util.ArrayList;
import java.util.Iterator;

import com.arkanoid.entities.Ball;
import com.arkanoid.PowerUps.PowerUps;
import com.arkanoid.PowerUps.PowerUpsManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BricksManager {
    private ArrayList<Bricks> bricksList = new ArrayList<>();

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

    public void addBrick (Bricks newBrick) {
        bricksList.add(newBrick);
    }

    /**
     * update bricks HP in brickList.
     * minus HP if collision between ball and brick occur.
     * if brick type is EXPLOSION, minus HP of surround bricks.
     * @param ball Movable Object.
     */
    public void updateBrickHP(Ball ball) {
        for (int i = 0; i < bricksList.size(); i++) {
            Bricks current = bricksList.get(i);
            if (current.checkCollision(ball)) {
                current.HPlost();
                if (current.getType().equals(Bricks.EXPLOSION)) {
                    for (int j = 0; j < bricksList.size(); j++){
                        Bricks check = bricksList.get(i);
                        if(check != current && current.Distance(check) < Bricks.EXPLOSION_RADIUS) {
                            check.HPlost();
                        }
                    }
                }
            }
        }
    }

    /**
     * update brickList, remove bricks which have HP <= 0.
     */
    public void updateBrickList () {
        Iterator<Bricks> it = bricksList.iterator();
        while(it.hasNext()) {
            Bricks b = it.next();
            if (b.getBrickHP() <= 0) {
                it.remove();
            }
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

