package com.arkanoid.saveload;

import com.arkanoid.core.*;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.entities.bullets.*;
import com.arkanoid.powerups.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GameLoadManager {
    private static final String SAVE_PATH = "savegame.txt";

    public static boolean loadGame(GameManager manager) {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_PATH))) {
            manager.setLoadingGame(true);

            String line = reader.readLine();
            if (line == null) return false;

            // Read global info
            String[] mainData = line.split(" ");
            int score = Integer.parseInt(mainData[0]);
            int lives = Integer.parseInt(mainData[1]);
            int level = Integer.parseInt(mainData[2]);

            // Reset game objects
            manager.stop();
            manager.setLevel(level);
            java.lang.reflect.Method initGameObjects =
                    GameManager.class.getDeclaredMethod("initGameObjects", int.class);

            // Clear current lists
            manager.getBrickManager().getBricksList().clear();
            manager.getBallManager().getBallsList().clear();
            manager.getPowerupManager().getPowerupList().clear();
            manager.getBulletManager().getBulletsList().clear();

            // Read data from save
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                switch (data[0]) {
                    case "PADDLE" -> {
                        Paddle paddle = manager.getPaddle();
                        paddle.setX(Double.parseDouble(data[1]));
                        paddle.setY(Double.parseDouble(data[2]));
                        paddle.setObjectImage("/images/paddle/normal_paddle.png");
                    }

                    case "BALL" -> {
                        double x = Double.parseDouble(data[1]);
                        double y = Double.parseDouble(data[2]);
                        double r = Double.parseDouble(data[3]);
                        double speed = Double.parseDouble(data[4]);
                        double dirX = Double.parseDouble(data[5]);
                        double dirY = Double.parseDouble(data[6]);

                        Ball ball = new Ball(x, y, r, speed);
                        ball.setDirectionX(dirX);
                        ball.setDirectionY(dirY);
                        manager.getBallManager().addBall(ball);
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
                        manager.getBrickManager().getBricksList().add(newBrick);
                    }
                }
            }
            if (manager.getPowerupManager() != null) {
                manager.getPowerupManager().clearPowerUpList(
                        manager.getPaddle(),
                        manager.getBallManager(),
                        manager.getBulletManager()
                );
            }


            // Restore main state
            manager.getBrickManager().recalculateTotalScore();
            manager.setScore(score);
            manager.setLives(lives);

            Paddle paddle = manager.getPaddle();

            PowerUpManager powerUpManager = manager.getPowerupManager();
            BallManager ballManager = manager.getBallManager();
            BulletManager bulletManager = manager.getBulletManager();

            powerUpManager.clearPowerUpList(paddle, ballManager, bulletManager);
            powerUpManager.getPowerupList().clear();

            paddle.setWidth(150);
            paddle.setHeight(25);
            paddle.setObjectImage("/images/paddle/normal_paddle.png");

            for (Ball ball : manager.getBallManager().getBallsList()) {
                ball.setSpeed(330);
            }

            //Continue immediately after loading (Can change later)
            manager.setGameState(GameState.PAUSED);

            manager.start();
            manager.setLoadingGame(false);

            System.out.println("Game loaded successfully!");
            manager.getGameView().showStatusMessage("Game Loaded!");
            return true;

        } catch (Exception e) {
            manager.getGameView().showStatusMessage("Can't load the game!");
            e.printStackTrace();
            return false;
        }
    }
}