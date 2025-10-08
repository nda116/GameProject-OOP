package com.arkanoid.entities.bricks;

import com.arkanoid.core.GameObject;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * void addBrick(Bricks) add new brick to brickList.
 * void updateBrickHP(Ball) update bricks HP in brickList.
 * void updateBrickList () update brickList, remove bricks which have HP <= 0.
 * void renderBrickList(Group) add imageView of all bricks from bricklist to Scene tree.
 */
public class BricksManager {
    private ArrayList<Bricks> bricksList = new ArrayList<>();

    public void addBrick (Bricks new_brick) {
        bricksList.add(new_brick);
    }

    /**
     * update bricks HP in brickList.
     * minus HP if collision between ball and brick occur.
     * if brick type is EXPLOSION, minus HP of surround bricks.
     * @param object Movable Object.
     */
    public void updateBrickHP(GameObject object) {
        for (int i = 0; i < bricksList.size(); i++) {
            Bricks current = bricksList.get(i);
            if (current.checkCollision(object)) {
                current.HPlost();
                if (current.getType().equals(Bricks.EXPLOSION)) {
                    for (int j = 0; j < bricksList.size(); j++){
                        Bricks check = bricksList.get(i);
                        if(check != current && current.distance(check) < ExplosionBricks.EXPLOSION_RADIUS) {
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
    public void updateBrickList() {
        Iterator<Bricks> it = bricksList.iterator();
        while(it.hasNext()) {
            Bricks b = it.next();
            if (b.getBrickHP() <= 0) {
                it.remove();
            }
        }
    }

    /**
     * add imageView of all bricks from bricklist to Scene tree.
     * @param root Scene tree.
     */
    public void renderBrickList(Group root) {
        for (int i = 0; i < bricksList.size(); i++) {
            bricksList.get(i).render(root);
        }
    }
}
