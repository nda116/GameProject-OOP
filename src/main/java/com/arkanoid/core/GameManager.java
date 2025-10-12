package com.arkanoid.core;


import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.powerups.*;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameManager {
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 600;

    private Scene scene;
    private GraphicsContext gc;

    private int score;
    private Paddle paddle;
    private BallManager ballmanager;
    private BrickManager brickmanager;
    private PowerUpManager powerupmanager;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean gameStarted = false;

    public GameManager(Canvas canvas, Scene scene) {
        this.scene = scene;
        this.gc = canvas.getGraphicsContext2D();
    }

    public void start() {
        initGameObjects();
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                leftPressed = true;
            }
            if (e.getCode() == KeyCode.RIGHT) {
                rightPressed = true;
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                leftPressed = false;
            }
            if (e.getCode() == KeyCode.RIGHT) {
                rightPressed = false;
            }
            if (e.getCode() == KeyCode.SPACE && !gameStarted) {
                gameStarted = true;
                for (Ball ball : ballmanager.getBallsList()) {
                    ball.setDirectionY(-1); // Bắt đầu bay lên
                }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };
        timer.start();
    }

    private void initGameObjects() {
        brickmanager = new BrickManager();
        powerupmanager = new PowerUpManager();
        ballmanager = new BallManager();

        double paddleWidth = 150;
        double paddleHeight = 25;
        double paddleX = (CANVAS_WIDTH - paddleWidth) / 2;
        double paddleY = CANVAS_HEIGHT - 50;
        paddle = new Paddle(paddleX, paddleY, paddleWidth, paddleHeight, 8, CANVAS_WIDTH);

        int rows = 5;
        int cols = 10;
        double brickWidth = 15 * 5;
        double brickHeight = 7.5 * 5;
        double offsetX = 25;
        double offsetY = 20;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = offsetX + col * (brickWidth);
                double y = offsetY + row * (brickHeight);

                if (row == rows - 1 && col % 2 == 0) {
                    brickmanager.addBrick(new ExplosionBrick(x, y, brickWidth, brickHeight));
                } else if (row == 2 && col % 2 == 0) {
                    brickmanager.addBrick(new InvincibleBrick(x, y, brickWidth, brickHeight));
                } else if ((row + col) % 3 == 0) {
                    brickmanager.addBrick(new NormalBrick(x, y, brickWidth, brickHeight, NormalBrick.YELLOW));
                } else if ((row + col) % 3 == 1) {
                    brickmanager.addBrick(new NormalBrick(x, y, brickWidth, brickHeight, NormalBrick.RED));
                } else {
                    brickmanager.addBrick(new NormalBrick(x, y, brickWidth, brickHeight, NormalBrick.BLUE));
                }
            }
        }
    }

    private void update() {
        if (!gameStarted) {
            if (ballmanager.getBallsList().isEmpty()) {
                ballmanager.addBall(new Ball(0, 0, 20,3, CANVAS_WIDTH, CANVAS_HEIGHT));
            }
            ballmanager.setDefault(paddle);
        }

        if (leftPressed) {
            paddle.moveLeft();
        }
        if (rightPressed) {
            paddle.moveRight();
        }

        paddle.update();
        ballmanager.updateBall();
        powerupmanager.updatePowerUp();

        // Check collision between balls and paddle
        for (Ball ball : ballmanager.getBallsList()){
            if (checkCollision(ball, paddle)) {
                ball.bounceOffPaddle(paddle);
            }
        }

        // Check collision between balls and bricks
        for (Brick brick : brickmanager.getBricksList()) {
            for (Ball ball : ballmanager.getBallsList()){
                if (checkCollision(brick, ball)) {
                    brickmanager.updateBrickHP(brick);
                    ball.bounceOff(brick);
                }
            }
        }

        // Check collision between powerups and paddle
        for (PowerUp powerup : powerupmanager.getPowerupList()) {
            if (checkCollision(powerup, paddle)) {
                powerup.applyEffect(paddle, ballmanager);
                powerup.stopFalling();
            }
        }

        brickmanager.updateBrickList(powerupmanager, score);
        ballmanager.updateBallList();
        powerupmanager.updatePowerUpList();

        // reset if all ball out bound.
        if (ballmanager.getBallsList().isEmpty()) {
            gameStarted = false;
            paddle.setDefault();
            powerupmanager.getPowerupList().clear();
        }
    }

    private void render() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        powerupmanager.renderPowerUpList(gc);
        brickmanager.renderBrickList(gc);
        paddle.render(gc);
        ballmanager.renderBallList(gc);
    }

    /**
     * check collision between 2 objects.
     * @param a first object.
     * @param b seconf object.
     * @return collision or not.
     */
    public boolean checkCollision (GameObject a, GameObject b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }
}

