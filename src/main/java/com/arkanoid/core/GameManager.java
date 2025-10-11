package com.arkanoid.core;

import com.arkanoid.entities.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.powerups.PowerUp;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import java.util.*;

/**
 * Singleton class that manages the game logic and state.
 * Implements the Singleton pattern to ensure only one game instance exists.
 *
 */
public class GameManager {

    private static GameManager instance;

    // Game objects
    private Paddle paddle;
    private Ball ball;
    private BrickManager level1;

    // Game state
    private int score;
    private int lives;
    private GameState gameState;
    private int level;

    private double screenWidth;
    private double screenHeight;

    private GameView gameView;

    // Game loop
    private AnimationTimer gameLoop;

    // Input handling
    private Set<KeyCode> pressedKeys;

    // Power-up timing
    private PowerUp powerUp;
    private double powerUpTimer;

    private static final int LIVES = 3;
    private static final int BRICK_ROWS = 5;
    private static final int BRICK_COLS = 10;

    /**
     * Enum representing different game states.
     */
    public enum GameState {
        READY,
        PLAYING,
        PAUSED,
        GAME_OVER,
        LEVEL_COMPLETE
    }

    /**
     * Private constructor for Singleton pattern.
     */
    private GameManager() {
        pressedKeys = new HashSet<>();
    }

    /**
     * Gets the singleton instance of GameManager.
     *
     * @return the GameManager instance
     */
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Initializes the game with screen dimensions and view.
     *
     * @param width screen width
     * @param height screen height
     * @param view game view for rendering
     */
    public void init(double width, double height, GameView view) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.gameView = view;

        score = 0;
        lives = LIVES;
        level = 1;
        gameState = GameState.READY;
        
        initGameObjects();
    }

    /**
     * Initializes game objects (paddle, ball, bricks).
     */
    private void initGameObjects() {
        // Create paddle
        double paddleWidth = 150;
        double paddleHeight = 25;
        double paddleX = (screenWidth - paddleWidth) / 2;
        double paddleY = screenHeight - 50;
        paddle = new Paddle(paddleX, paddleY, paddleWidth, paddleHeight, 8, screenWidth);

        // Create ball
        double ballRadius = 16;
        double ballX = screenWidth / 2 - ballRadius;
        double ballY = screenHeight / 2 - ballRadius;
        ball = new Ball(ballX, ballY, ballRadius, 5, screenWidth, screenHeight);

        // Create bricks
        createBricks();
    }

    /**
     * Creates the brick layout for the current level.
     */
    private void createBricks() {
        level1 = new BrickManager();

        int rows = 5;
        int cols = 10;
        double brickWidth = 15 * 5;
        double brickHeight = 7.5 * 5;
        double offsetX = 25;
        double offsetY = 60;

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

    /**
     * Starts the game loop.
     */
    public void start() {
        gameState = GameState.PLAYING;

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };

        gameLoop.start();
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    /**
     * Updates all game objects and logic.
     */
    public void update() {
        if (gameState != GameState.PLAYING) {
            return;
        }

        // Handle input
        handleContinuousInput();

        // Update paddle
        paddle.update();

        // Update ball
        ball.update();

        // Check collisions
        checkCollisions();

        // Check if ball is lost
        if (ball.isOutOfBounds()) {
            lives--;
            if (lives <= 0) {
                gameOver();
            } else {
                ball.reset(screenWidth / 2 - ball.getWidth() / 2, screenHeight / 2
                        - ball.getHeight() / 2);
                gameState = GameState.READY;
                // Auto-resume after 2 second
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (gameState == GameState.READY) {
                            gameState = GameState.PLAYING;
                        }
                    }
                }, 2000);
            }
        }

        // Update power-ups


        /* Check level completion
        if (isLevelComplete()) {
            levelComplete();
        }*/
    }

    /**
     * Handles continuous key input (for paddle movement).
     */
    private void handleContinuousInput() {
        if (pressedKeys.contains(KeyCode.LEFT)) {
            paddle.moveLeft();
        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
            paddle.moveRight();
        } else {
            paddle.stop();
        }
    }

    /**
     * Handles keyboard input.
     *
     * @param key the key code
     * @param pressed true if key is pressed, false if released
     */
    public void handleInput(KeyCode key, boolean pressed) {
        if (pressed) {
            pressedKeys.add(key);

            // Handle special keys
            if (key == KeyCode.SPACE) {
                if (gameState == GameState.READY) {
                    gameState = GameState.PLAYING;
                } else if (gameState == GameState.PLAYING) {
                    gameState = GameState.PAUSED;
                } else if (gameState == GameState.PAUSED) {
                    gameState = GameState.PLAYING;
                }
            } else if (key == KeyCode.R && gameState == GameState.GAME_OVER) {
                reset();
            }
        } else {
            pressedKeys.remove(key);
        }
    }

    /**
     * Checks all collisions in the game.
     */
    private void checkCollisions() {
        // Check collision with paddle
        if (ball.checkCollision(paddle)) {
            ball.bounceOffPaddle(paddle);
        }

        // Check collision with bricks
        level1.updateBrickHP(ball);
        level1.updateBrickList();

        // Power-up collection
    }


    /**
     * Checks if the current level is complete.
     *
     * @return true if all bricks are destroyed
     */
    private boolean isLevelComplete() {

        return true;
    }

    /**
     * Handles level completion.
     */
    private void levelComplete() {
        gameState = GameState.LEVEL_COMPLETE;
        level++;

        // Wait 2 seconds then start next level
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startNextLevel();
            }
        }, 2000);
    }

    /**
     * Starts the next level.
     */
    private void startNextLevel() {
        createBricks();
        ball.reset(screenWidth / 2 - ball.getWidth() / 2, screenHeight / 2
                - ball.getHeight() / 2);
        gameState = GameState.PLAYING;
    }

    /**
     * Handles game over state.
     */
    private void gameOver() {
        gameState = GameState.GAME_OVER;
        stop();
    }

    /**
     * Resets the game to initial state.
     */
    private void reset() {
        score = 0;
        lives = LIVES;
        level = 1;
        initGameObjects();
        start();
    }

    /**
     * Renders all game objects.
     */
    private void render() {
        gameView.clear();

        // Render bricks
        level1.renderBrickList(gameView.getGraphicsContext());

        // Render power-ups

        // Render paddle and ball
        paddle.render(gameView.getGraphicsContext());
        ball.render(gameView.getGraphicsContext());

        // Render UI
        gameView.renderUI(score, lives, level, gameState, powerUpTimer);
    }

    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int getLevel() {
        return level;
    }
}