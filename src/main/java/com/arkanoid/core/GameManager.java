package com.arkanoid.core;

import com.arkanoid.entities.*;
import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.entities.bullets.Bullet;
import com.arkanoid.menu.*;
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
    private BallManager ballManager;
    private BrickManager brickManager;
    private PowerUpManager powerupManager;

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

        // Start at main menu
        gameState = GameState.MENU;
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
        paddle = new Paddle(paddleX, paddleY, paddleWidth, paddleHeight, 4);

        ballManager = new BallManager();
        powerupManager = new PowerUpManager();

        // createBricks();
        brickManager = new BrickManager();
        brickManager.createBricksFromFile("/maps/level" + level + ".txt");
    }

    /**
     * Starts a new game from main menu.
     */
    public void startNewGame() {
        score = 0;
        lives = LIVES;
        level = 1;

        initGameObjects(level);
        gameState = GameState.READY;
        start();
    }

    /**
     * Starts the game loop.
     */
    public void start() {
        if (gameLoop != null) {
            gameLoop.stop();
        }

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
        } else if (gameState == GameState.GAME_OVER) {
            render();
            return;
        } else {
            return;
        }


        if (gameState == GameState.READY) {
            if (ballManager.getBallsList().isEmpty()) {
                ballManager.addBall(new Ball(0, 0, 12,7));
            }
            ballManager.setDefault(paddle);
        }

        // Check collision between balls and paddle
        for (Ball ball : ballManager.getBallsList()){
            if (checkCollision(ball, paddle)) {
                ball.bounceOffPaddle(paddle);
            }
        }

        // Check collision between balls and bricks
        for (Brick brick : brickManager.getBricksList()) {
            for (Ball ball : ballManager.getBallsList()){
                if (checkCollision(brick, ball)) {
                    brickManager.updateBrickHP(brick);
                    ball.bounceOff(brick);
                }
            }
        }

        // Check collision between bullet and bricks
        for (Brick brick : brickManager.getBricksList()) {
            for (Bullet bullet : paddle.getBullets().getBulletsList()){
                if (checkCollision(brick, bullet)) {
                    brickManager.updateBrickHP(brick);
                    bullet.deActive();
                }
            }
        }

        // Check collision between powerups and paddle
        for (PowerUp powerup : powerupManager.getPowerupList()) {
            if (checkCollision(powerup, paddle)) {
                powerup.applyEffect(paddle, ballManager);
                powerup.stopFalling();
            }
        }

        paddle.update();
        ballManager.updateBall();
        powerupManager.updatePowerUp();

        score += brickManager.updateBrickList(powerupManager);
        ballManager.updateBallList();
        powerupManager.updatePowerUpList();

        // Check if ball is lost
        if (ballManager.getBallsList().isEmpty()) {
            lives--;
            if (lives <= 0) {
                gameOver();
            } else {
                paddle.setDefault();
                ballManager.setDefault(paddle);
                powerupManager.getPowerupList().clear();
                gameState = GameState.READY;
            }
        }

        if (brickManager.getTotalScore() == 0) {
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

            // Handle menu navigation
            if (gameState == GameState.MENU) {
                handleMenuInput(key);
            } else if (gameState == GameState.PAUSED) {
                handlePauseMenuInput(key);
            } else if (gameState == GameState.GAME_OVER) {
                handleGameOverInput(key);
            } else if (gameState == GameState.READY || gameState == GameState.PLAYING) {
                handleGameplayInput(key);
            }
        } else {
            pressedKeys.remove(key);
        }
    }

    /**
     * Handles main menu input.
     *
     * @param key the key code
     */
    private void handleMenuInput(KeyCode key) {
        if (key == KeyCode.UP) {
            gameView.getMainMenu().selectPrevious();
        } else if (key == KeyCode.DOWN) {
            gameView.getMainMenu().selectNext();
        } else if (key == KeyCode.ENTER) {
            int selection = gameView.getMainMenu().getSelectedIndex();
            if (selection == 0) { // New Game
                startNewGame();
            } else if (selection == 1) { // Exit
                System.exit(0);
            }
        }
    }

    /**
     * Handles pause menu input.
     *
     * @param key the key code
     */
    private void handlePauseMenuInput(KeyCode key) {
        if (key == KeyCode.UP) {
            gameView.getPauseMenu().selectPrevious();
        } else if (key == KeyCode.DOWN) {
            gameView.getPauseMenu().selectNext();
        } else if (key == KeyCode.ENTER) {
            int selection = gameView.getPauseMenu().getSelectedIndex();
            if (selection == 0) { // Resume
                resumeGame();
            } else if (selection == 1) { // Main Menu
                returnToMainMenu();
            }
        } else if (key == KeyCode.ESCAPE || key == KeyCode.SPACE) {
            resumeGame();
        }
    }

    /**
     * Handles game over input.
     *
     * @param key the key code
     */
    private void handleGameOverInput(KeyCode key) {
        GameOver go = gameView.getGameOverMenu();

        boolean wasNameEntered = go.isNameEntered();
        go.handleInput(key);
        render();

        if (!wasNameEntered && go.isNameEntered()) {
            return;
        }

        if (go.isNameEntered() && key == KeyCode.ENTER) {
            String selection = go.confirmSelection();
            if (selection != null) {
                if (selection.equalsIgnoreCase("RESTART")) {
                    startNewGame();
                } else if (selection.equalsIgnoreCase("BACK TO MENU")) {
                    returnToMainMenu();
                }
            }
        }
    }




    /**
         * Handles gameplay input.
         *
         * @param key the key code
         */
    private void handleGameplayInput(KeyCode key) {
        if (key == KeyCode.SPACE) {
            if (gameState == GameState.READY) {
                gameState = GameState.PLAYING;
                for (Ball ball : ballManager.getBallsList()) {
                    ball.setDirectionY(-1);
                }
            } else if (gameState == GameState.PLAYING) {
                gameState = GameState.PAUSED;
                gameView.getPauseMenu().resetSelection();
            }
        } else if (key == KeyCode.ESCAPE && gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
            gameView.getPauseMenu().resetSelection();
        }
    }

    /**
     * Returns to main menu.
     */
    public void returnToMainMenu() {
        stop();
        gameState = GameState.MENU;
        start(); // Restart loop for menu rendering
    }

    /**
     * Resumes the game from pause.
     */
    public void resumeGame() {
        gameState = GameState.PLAYING;
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
        gameView.resetGameOverMenu(score);
    }

    /**
     * Resets the game to initial state.
     */
    private void reset() {
        score = 0;
        lives = LIVES;
        level = 1;
        initGameObjects(level);
        gameState = GameState.PLAYING;
    }

    /**
     * Renders all game objects.
     */
    private void render() {
        gameView.clear();

        gameView.render(this);
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

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int getLevel() {
        return level;
    }

    public double getPowerUpTimer() {
        return powerUpTimer;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public BallManager getBallManager() {
        return ballManager;
    }

    public BrickManager getBrickManager() {
        return brickManager;
    }

    public PowerUpManager getPowerupManager() {
        return powerupManager;
    }
}