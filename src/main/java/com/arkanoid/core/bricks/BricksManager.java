package com.arkanoid.core.bricks;

import java.util.ArrayList;
import java.util.Iterator;

public class BricksManager {
    private ArrayList<Bricks> bricksList = new ArrayList<>();

    public void addBrick (Bricks new_Brick) {
        bricksList.add(new_Brick);
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


}
