package com.arkanoid;

import com.arkanoid.entities.Ball;
import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.bricks.*;

import com.arkanoid.entities.bricks.ExplosionBrick;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int CANVAS_WIDTH = 800;
    private static final int CANVAS_HEIGHT = 600;

    private Canvas canvas;
    private GraphicsContext gc;

    private Paddle paddle;
    private Ball ball;

    private BricksManager level1;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        initGameObjects();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, CANVAS_WIDTH, CANVAS_HEIGHT);

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
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };
        timer.start();

        primaryStage.setTitle("Arkanoid Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void initGameObjects() {
        double paddleWidth = 150;
        double paddleHeight = 25;
        double paddleX = (CANVAS_WIDTH - paddleWidth) / 2;
        double paddleY = CANVAS_HEIGHT - 50;
        paddle = new Paddle(paddleX, paddleY, paddleWidth, paddleHeight, 8, CANVAS_WIDTH);

        double ballRadius = 20;
        double ballX = CANVAS_WIDTH / 2 - ballRadius;
        double ballY = CANVAS_HEIGHT / 2 - ballRadius;
        ball = new Ball(ballX, ballY, ballRadius, 5, CANVAS_WIDTH, CANVAS_HEIGHT);

        level1 = new BricksManager();

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
                    level1.addBrick(new ExplosionBrick(x, y, brickWidth, brickHeight));
                } else if (row == 2 && col % 2 == 0) {
                    level1.addBrick(new InvincibleBrick(x, y, brickWidth, brickHeight));
                } else if ((row + col) % 3 == 0) {
                    level1.addBrick(new NormalBrick(x, y, brickWidth, brickHeight, NormalBrick.YELLOW));
                } else if ((row + col) % 3 == 1) {
                    level1.addBrick(new NormalBrick(x, y, brickWidth, brickHeight, NormalBrick.RED));
                } else {
                    level1.addBrick(new NormalBrick(x, y, brickWidth, brickHeight, NormalBrick.BLUE));
                }
            }
        }
    }

    private void update() {
        if (leftPressed) {
            paddle.moveLeft();
        }
        if (rightPressed) {
            paddle.moveRight();
        }

        paddle.update();

        ball.update();

        if (ball.checkCollision(paddle)) {
            ball.bounceOffPaddle(paddle);
        }



        // Check collision with bricks
        level1.updateBrickHP(ball);
        level1.updateBrickList();

        if (ball.isOutOfBounds()) {
            ball.reset(CANVAS_WIDTH / 2 - ball.getWidth() / 2, CANVAS_HEIGHT / 2
                    - ball.getHeight() / 2);
        }
    }

    private void render() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

        level1.renderBrickList(gc);

        paddle.render(gc);
        ball.render(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
