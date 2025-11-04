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

    /**
     * clear file savegame.txt
     */
    public static void clearSave() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_PATH, false))) {
            writer.write("");
            System.out.println("Save file cleared successfully.");
        } catch (IOException e) {
            System.out.println("Failed to clear save file: " + e.getMessage());
        }
    }

    /**
     * save current game to file.
     * @param manager current game manager.
     * @return save game success or not.
     */
    public static boolean saveGame(GameManager manager) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_PATH))) {

            //Save global save
            writer.write(manager.getScore() + " " + manager.getLives() + " " + manager.getLevel());
            writer.newLine();

            //Save paddle
            Paddle paddle = manager.getPaddle();
            writer.write(String.format("PADDLE %.2f",
                    paddle.getX()));
            writer.newLine();

            //Save balls
            for (Ball ball : manager.getBallManager().getBallsList()) {
                writer.write(String.format("BALL %.2f %.2f %.3f %.3f",
                        ball.getX(), ball.getY(),
                        ball.getDirectionX(), ball.getDirectionY()));
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

            writer.write(String.format("BRICKMANAGER %d %d %d",
                    manager.getBrickManager().getTotalScore(),
                    manager.getBrickManager().getNumberNormalBrick(),
                    manager.getBrickManager().getNumberPowerUp()));
            writer.newLine();

            manager.getGameView().showStatusMessage("Game Saved!");
            return true;

        } catch (IOException e) {
            manager.getGameView().showStatusMessage("Error saving game!");
            return false;
        }
    }
}