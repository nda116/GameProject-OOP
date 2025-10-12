package com.arkanoid.entities.bricks;

import javafx.scene.canvas.GraphicsContext;
import com.arkanoid.powerups.PowerUpManager;

import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * void addBrick(Bricks) add new brick to brickList.
 * void updateBrickHP(Ball) update bricks HP in brickList.
 * void explosionTrigger (Brick) Triggers a chain explosion starting from the specified brick.
 * void updateBrickList () update brickList, remove bricks which have HP <= 0.
 * void renderBrickList(GraphicsContext) render bricks from bricksList.
 */
public class BrickManager {
    private final ArrayList<Brick> bricksList = new ArrayList<>();

    public void addBrick (Brick newBrick) {
        bricksList.add(newBrick);
    }

    public ArrayList<Brick> getBricksList() {
        return bricksList;
    }

    /**
     * update bricks HP in brickList.
     * minus HP if collision between ball and brick occur.
     * if brick type is EXPLOSION, minus HP of surround bricks.
     * @param brick brick to updateHP.
     */
    public void updateBrickHP(Brick brick) {
        brick.HPlost();
        if (brick.getType() == Brick.EXPLOSION) {
            explosionTrigger(brick);
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

            for (Brick brick : bricksList){
                if(brick.getBrickHP() > 0 && current.isAdjacent(brick)) {
                    brick.HPlost();
                    if(brick.getType() == Brick.EXPLOSION) {
                        explosionQueue.add(brick);
                    }
                }
            }
        }
    }

    /**
     * update brickList, remove bricks which have HP <= 0.
     */
    public void updateBrickList(PowerUpManager powerupmanager, int score) {
        Iterator<Brick> it = bricksList.iterator();

        while(it.hasNext()) {
            Brick brick = it.next();
            if (brick.getBrickHP() <= 0) {
                score += brick.getBrickScore();
                it.remove();
                if (brick.getType() == Brick.NORMAL) {
                    ((NormalBrick) brick).dropPowerUp(powerupmanager);
                }
            }
        }
    }

    /**
     * render bricks from bricksList.
     * @param gc GraphicContext.
     */
    public void renderBrickList(GraphicsContext gc) {
        for (Brick brick : bricksList) {
            brick.render(gc);
        }
    }
}
