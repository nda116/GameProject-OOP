package com.arkanoid.saveload;

import com.arkanoid.core.GameManager;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.powerups.*;

import java.io.*;

public class GameLoadManager {

    private static final String SAVE_PATH = "save/savegame.txt";

    public static boolean loadGame(GameManager manager) {
        File file = new File(SAVE_PATH);
        if (!file.exists()) {
            System.out.println("[GameLoadManager] No save file found.");
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            Paddle paddle = manager.getPaddle();
            BallManager ballManager = manager.getBallManager();
            BrickManager brickManager = manager.getBrickManager();
            PowerUpManager powerUpManager = manager.getPowerupManager();

            // Clear old objects
            ballManager.getBallsList().clear();
            brickManager.getBricksList().clear();
            powerUpManager.getPowerupList().clear();

            System.out.println("[GameLoadManager] Loading game data...");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(" ");
                if (parts.length == 0) continue;

                switch (parts[0]) {
                    case "PADDLE":
                        paddle.setX(Double.parseDouble(parts[1]));
                        paddle.setY(Double.parseDouble(parts[2]));
                        break;

                    case "BALL":
                        double bx = Double.parseDouble(parts[1]);
                        double by = Double.parseDouble(parts[2]);
                        double sx = Double.parseDouble(parts[3]);
                        double sy = Double.parseDouble(parts[4]);

                        Ball ball = new Ball(bx, by, 12, 3);
                        double magnitude = Math.sqrt(sx * sx + sy * sy);
                        if (magnitude != 0) {
                            ball.setDirectionX(sx / magnitude);
                            ball.setDirectionY(sy / magnitude);
                            ball.setSpeed(magnitude);
                        }
                        ballManager.getBallsList().add(ball);
                        break;

                    case "BRICK":
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        int hp = Integer.parseInt(parts[3]);
                        String type = parts.length > 4 ? parts[4] : "NORMAL";

                        Brick brick = brickManager.createBrick(type, x, y, hp);
                        brickManager.getBricksList().add(brick);
                        break;

                    case "POWERUP":
                        double puX = Double.parseDouble(parts[1]);
                        double puY = Double.parseDouble(parts[2]);
                        String pType = parts[3];
                        boolean falling = Boolean.parseBoolean(parts[4]);

                        PowerUp powerUp = powerUpManager.createPowerUp(pType, puX, puY);
                        if (!falling) powerUp.stopFalling();
                        powerUpManager.getPowerupList().add(powerUp);
                        break;
                }
            }

            System.out.println("[GameLoadManager] Game loaded successfully.");
            return true;

        } catch (Exception e) {
            System.err.println("[GameLoadManager] Error loading game:");
            e.printStackTrace();
            return false;
        }
    }
}
