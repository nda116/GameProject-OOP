package com.arkanoid.saveload;

import com.arkanoid.core.*;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.entities.bullets.*;
import com.arkanoid.powerups.*;

import java.io.BufferedReader;
import java.io.FileReader;

public class GameLoadManager {
    private static final String SAVE_PATH = "savegame.txt";

    /**
     * laod game from file.
     * @param gameManager current game manager.
     * @return laodGame success or not.
     */
    public static boolean loadGame(GameManager gameManager) {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_PATH))) {
            String line = reader.readLine();
            if (line == null) {
                gameManager.getGameView().showStatusMessage("Save file is empty!");
                return false;
            }

            // Read global info
            String[] mainData = line.split(" ");
            int score = Integer.parseInt(mainData[0]);
            int lives = Integer.parseInt(mainData[1]);
            int level = Integer.parseInt(mainData[2]);

            // Reset game objects
            gameManager.stop();

            // Read data from save
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                switch (data[0]) {
                    case "PADDLE" -> {
                        Paddle paddle = gameManager.getPaddle();
                        paddle.setX(Double.parseDouble(data[1]));
                    }

                    case "BALL" -> {
                        double x = Double.parseDouble(data[1]);
                        double y = Double.parseDouble(data[2]);
                        double dirX = Double.parseDouble(data[3]);
                        double dirY = Double.parseDouble(data[4]);

                        Ball ball = new Ball(x, y);
                        ball.setDirectionX(dirX);
                        ball.setDirectionY(dirY);
                        gameManager.getBallManager().addBall(ball);
                    }

                    case "BRICK" -> {
                        double x = Double.parseDouble(data[1]);
                        double y = Double.parseDouble(data[2]);
                        double w = Double.parseDouble(data[3]);
                        double h = Double.parseDouble(data[4]);
                        int hp = Integer.parseInt(data[5]);
                        int type = Integer.parseInt(data[6]);
                        Brick newBrick;

                        if (type == Brick.NORMAL) {
                            String color = (data.length > 7) ? data[7] : NormalBrick.BLUE;
                            newBrick = new NormalBrick(x, y, w, h, color);
                        } else if (type == Brick.GLASS) {
                            newBrick = new GlassBrick(x, y, w, h);
                        } else if (type == Brick.EXPLOSION) {
                            newBrick = new ExplosionBrick(x, y, w, h);
                        } else if (type == Brick.INVINCIBLE) {
                            newBrick = new InvincibleBrick(x, y, w, h);
                        } else {
                            newBrick = new NormalBrick(x, y, w, h, NormalBrick.RED);
                        }

                        newBrick.setBrickHP(hp);
                        gameManager.getBrickManager().getBricksList().add(newBrick);
                    }

                    case "BRICKMANAGER" -> {
                        int totalScore = Integer.parseInt(data[1]);
                        int numberNormalBrick = Integer.parseInt(data[2]);
                        int numberPowerUp = Integer.parseInt(data[3]);
                        gameManager.getBrickManager().setTotalScore(totalScore);
                        gameManager.getBrickManager().setNumberNormalBrick(numberNormalBrick);
                        gameManager.getBrickManager().setNumberPowerUp(numberPowerUp);
                    }
                }
            }

            // Restore main state
            gameManager.setGameState(GameState.PAUSED);

            gameManager.setLevel(level);
            gameManager.setScore(score);
            gameManager.setLives(lives);
            gameManager.start();

            gameManager.getGameView().showStatusMessage("Game Loaded!");
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}