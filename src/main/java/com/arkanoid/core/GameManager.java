package com.arkanoid.core;

import com.arkanoid.entities.*;
import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.entities.bullets.Bullet;
import com.arkanoid.powerups.*;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

import java.util.*;

import static com.arkanoid.Main.WINDOW_HEIGHT;
import static com.arkanoid.Main.WINDOW_WIDTH;
import static com.arkanoid.core.GameObject.checkCollision;

/**
 * Singleton class that manages the game logic and state.
 * Implements the Singleton pattern to ensure only one game instance exists.
 *
 */
public class GameManager {

    private static GameManager instance;

    // Game objects
    private Paddle paddle;
    private BallManager ballmanager;
    private BrickManager brickmanager;
    private PowerUpManager powerupmanager;

    // Game state
    private int score;
    private int lives;
    private GameState gameState;
    private int level;

    private GameView gameView;

    // Game loop
    private AnimationTimer gameLoop;

    // Input handling
    private Set<KeyCode> pressedKeys;

    // Power-up timing
    private PowerUp powerUp;
    private double powerUpTimer;

    private static final int LIVES = 3;


    /**
     * Enum representing different game states.
     */
    public enum GameState {
        MENU,
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
     * @param view game view for rendering
     */
    public void init(GameView view) {
        this.gameView = view;

        score = 0;
        lives = LIVES;
        level = 1;
        gameState = GameState.READY;

        initGameObjects(level);
    }

    /**
     * Initializes game objects (paddle, ball, bricks).
     */
    private void initGameObjects(int level) {
        // Create paddle
        double paddleWidth = 150;
        double paddleHeight = 25;
        double paddleX = (WINDOW_WIDTH - paddleWidth) / 2;
        double paddleY = WINDOW_HEIGHT - 50;
        paddle = new Paddle(paddleX, paddleY, paddleWidth, paddleHeight, 5);

        ballmanager = new BallManager();
        powerupmanager = new PowerUpManager();

        // createBricks();
        brickmanager = new BrickManager();
        brickmanager.createBricksFromFile("/maps/level" + level + ".txt");
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        gameState = GameState.MENU;

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
        if (gameState == GameState.PLAYING || gameState == GameState.READY) {
            handleContinuousInput();
        } else {
            return;
        }

        if (gameState == GameState.READY) {
            if (ballmanager.getBallsList().isEmpty()) {
                ballmanager.addBall(new Ball(0, 0, 12,3));
            }
            ballmanager.setDefault(paddle);
        }

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

        // Check collision between bullet and bricks
        for (Brick brick : brickmanager.getBricksList()) {
            for (Bullet bullet : paddle.getBullets().getBulletsList()){
                if (checkCollision(brick, bullet)) {
                    brickmanager.updateBrickHP(brick);
                    bullet.deActive();
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

        paddle.update();
        ballmanager.updateBall();
        powerupmanager.updatePowerUp();

        score += brickmanager.updateBrickList(powerupmanager);
        ballmanager.updateBallList();
        powerupmanager.updatePowerUpList();

        // Check if ball is lost
        if (ballmanager.getBallsList().isEmpty()) {
            lives--;
            if (lives <= 0) {
                gameOver();
            } else {
                paddle.setDefault();
                ballmanager.setDefault(paddle);
                powerupmanager.getPowerupList().clear();
                gameState = GameState.READY;
            }
        }

        if (brickmanager.getTotalScore() == 0) {
            levelComplete();
        }
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
            if (key == KeyCode.P) {
                if (gameState == GameState.MENU) {
                    gameState = GameState.READY;
                }
            }
            // Handle special keys
            if (key == KeyCode.SPACE) {
                if (gameState == GameState.READY) {
                    gameState = GameState.PLAYING;
                    for (Ball ball : ballmanager.getBallsList()) {
                        ball.setDirectionY(-1); // Bắt đầu bay lên
                    }
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
        initGameObjects(level);
        gameState = GameState.READY;
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
        initGameObjects(level);
        start();
    }

    /**
     * Renders all game objects.
     */
    private void render() {
        gameView.clear();

        // Render bricks
        brickmanager.renderBrickList(gameView.getGraphicsContext());

        // Render power-ups
        powerupmanager.renderPowerUpList(gameView.getGraphicsContext());

        // Render paddle and ball
        paddle.render(gameView.getGraphicsContext());
        ballmanager.renderBallList(gameView.getGraphicsContext());

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