package com.arkanoid.entities.balls;

import com.arkanoid.entities.Paddle;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * void addBall(Ball) add new ball to ballsList.
 * void setDefault(Paddle) set default balls position.
 * void updateBall() update position and direction of balls from ballList.
 * void updateBallList() update ballsList, remove balls if out bound.
 * void renderBallList(GraphicsContext) render balls from bricksList.
 */
public class BallManager {
    private ArrayList<Ball> ballsList = new ArrayList<>();

    public ArrayList<Ball> getBallsList() {
        return ballsList;
    }

    public void addBall(Ball newBall) {
        ballsList.add(newBall);
    }

    public void setDefault(Paddle paddle) {
        for (Ball ball : ballsList) {
            ball.setDefaultBall(paddle);
        }
    }

    public void updateBall() {
        for (Ball ball : ballsList) {
            ball.update();
        }
    }

    public void updateBallList() {
        Iterator<Ball> it = ballsList.iterator();
        while (it.hasNext()) {
            Ball ball = it.next();
            if (ball.isOutOfBounds()) {
                it.remove();
            }
        }
    }

    public void renderBallList(GraphicsContext gc) {
        for (Ball ball : ballsList) {
            ball.render(gc);
        }
    }
}
