package com.arkanoid.saveload;

import com.arkanoid.core.*;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.entities.bullets.*;
import com.arkanoid.powerups.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GameSaveManager {
    private static final String SAVE_PATH = "savegame.txt";

    public static boolean saveGame(GameManager manager) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_PATH))) {

            //Save global save
            writer.write(manager.getScore() + " " + manager.getLives() + " " + manager.getLevel());
            writer.newLine();

            //Save paddle
            Paddle paddle = manager.getPaddle();
            writer.write(String.format("PADDLE %.2f %.2f %.2f %.2f",
                    paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight()));
            writer.newLine();

            //Save balls
            for (Ball ball : manager.getBallManager().getBallsList()) {
                writer.write(String.format("BALL %.2f %.2f %.2f %.2f %.3f %.3f",
                        ball.getX(), ball.getY(), ball.getWidth() / 2,
                        ball.getSpeed(), ball.getDirectionX(), ball.getDirectionY()));
                writer.newLine();
            }

            //Save bricks
            for (Brick brick : manager.getBrickManager().getBricksList()) {
                if (brick instanceof NormalBrick nb) {
                    writer.write(String.format("BRICK %.2f %.2f %.2f %.2f %d %d %s",
                            nb.getX(), nb.getY(), nb.getWidth(), nb.getHeight(),
                            nb.getBrickHP(), nb.getType(), nb.getColorType()));
                } else {
                    writer.write(String.format("BRICK %.2f %.2f %.2f %.2f %d %d",
                            brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight(),
                            brick.getBrickHP(), brick.getType()));
                }
                writer.newLine();
            }

            //Save bullets
            for (Bullet bullet : manager.getBulletManager().getBulletsList()) {
                writer.write(String.format("BULLET %.2f %.2f", bullet.getX(), bullet.getY()));
                writer.newLine();
            }

            //Save power-ups
            for (PowerUp p : manager.getPowerupManager().getPowerupList()) {
                if (p.isRemove()) continue;
                writer.write("POWERUP " + p.getX() + " " + p.getY() + " " + p.getType() + " "
                        + p.isFalling() + " " + p.isRemove() + "\n");
            }


            manager.getGameView().showStatusMessage("Game Saved!");
            return true;

        } catch (IOException e) {
            manager.getGameView().showStatusMessage("Error saving game!");
            return false;
        }
    }
}