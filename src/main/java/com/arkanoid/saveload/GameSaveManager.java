package com.arkanoid.saveload;

import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.powerups.*;

import java.io.*;

public class GameSaveManager {

    private static final String SAVE_PATH = "save/savegame.txt";

    public static void saveGame(Paddle paddle, BallManager ballManager,
                                BrickManager brickManager, PowerUpManager powerUpManager) {

        File saveDir = new File("save");
        if (!saveDir.exists()) saveDir.mkdirs();

        try (FileWriter writer = new FileWriter(SAVE_PATH)) {

            // --- Paddle ---
            writer.write(String.format("PADDLE %.2f %.2f%n", paddle.getX(), paddle.getY()));

            // --- Balls ---
            for (Ball ball : ballManager.getBallsList()) {
                writer.write(String.format(
                        "BALL %.2f %.2f %.3f %.3f%n",
                        ball.getX(), ball.getY(),
                        ball.getSpeedX(), ball.getSpeedY()
                ));
            }

            // --- Bricks ---
            for (Brick brick : brickManager.getBricksList()) {
                writer.write(String.format(
                        "BRICK %.2f %.2f %d %s%n",
                        brick.getX(), brick.getY(),
                        brick.getBrickHP(), brick.getType()
                ));
            }

            // --- PowerUps ---
            for (PowerUp powerUp : powerUpManager.getPowerupList()) {
                writer.write(String.format(
                        "POWERUP %.2f %.2f %s %b%n",
                        powerUp.getX(), powerUp.getY(),
                        powerUp.getType(), powerUp.isFalling()
                ));
            }

            System.out.println("[GameSaveManager] Game saved successfully.");

        } catch (IOException e) {
            System.err.println("[GameSaveManager] Error saving game:");
            e.printStackTrace();
        }
    }
}
