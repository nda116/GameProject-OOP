package com.arkanoid.core;

import com.arkanoid.entities.*;
import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.entities.bullets.Bullet;
import com.arkanoid.entities.bullets.BulletManager;
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
    public long frame = 0;
    private static GameManager instance;

    // Game objects
    private Paddle paddle;
    private BallManager ballManager;
    private BrickManager brickManager;
    private PowerUpManager powerupManager;
    private BulletManager bulletManager;

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

    private static final int LIVES = 3;
    private static final int NUMBER_OF_LEVEL = 4;

    /**
     * Private constructor for Singleton pattern.
     */
    private GameManager() {
        pressedKeys = new HashSet<>();
    }

    /**
     * Gets the singleton instance of GameManager.
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
        SoundManager.getInstance().playMenuMusic();
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

        ballManager = new BallManager();
        powerupManager = new PowerUpManager();
        bulletManager = new BulletManager();

        // create Bricks
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
        SoundManager.getInstance().stopMenuMusic();
        SoundManager.getInstance().playBackgroundMusic();
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
                frame ++;
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
        } else if (gameState == GameState.GAME_OVER || gameState == GameState.HIGH_SCORES) {
            return;
        } else {
            return;
        }


        if (gameState == GameState.READY) {
            if (ballManager.getBallsList().isEmpty()) {
                ballManager.addBall(new Ball(0, 0, 12,3));
            }
            ballManager.setDefault(paddle);
        }

        // Check collision between balls and paddle
        for (Ball ball : ballManager.getBallsList()){
            if (checkCollision(ball, paddle)) {
                ball.bounceOffPaddle(paddle);
                SoundManager.getInstance().playSound(SoundManager.Sound.PADDLE_HIT);
            }
        }

        // Check collision between balls and bricks
        for (Brick brick : brickManager.getBricksList()) {
            for (Ball ball : ballManager.getBallsList()){
                if (checkCollision(brick, ball)) {
                    brickManager.updateBrickHP(brick);
                    ball.bounceOff(brick);
                    if (brick.getType() == Brick.EXPLOSION) {
                        SoundManager.getInstance().playSound(SoundManager.Sound.EXPLOSION_BRICK);
                    } else if (brick.getType() == Brick.GLASS) {
                        SoundManager.getInstance().playSound(SoundManager.Sound.GLASS_BRICK);
                    } else {
                        SoundManager.getInstance().playSound(SoundManager.Sound.BRICK_BREAK);
                    }
                }
            }
        }

        // Check collision between bullet and bricks
        for (Brick brick : brickManager.getBricksList()) {
            for (Bullet bullet : bulletManager.getBulletsList()){
                if (checkCollision(brick, bullet)) {
                    brickManager.updateBrickHP(brick);
                    bullet.deActive();
                }
            }
        }

        // Check collision between powerups and paddle
        for (PowerUp powerup : powerupManager.getPowerupList()) {
            if (checkCollision(powerup, paddle)) {
                if (powerup.isFalling()) {
                    powerup.applyEffect(paddle, ballManager, bulletManager);
                    powerup.stopFalling();
                    SoundManager.getInstance().playSound(SoundManager.Sound.POWER_UP);
                }
            }
        }

        paddle.update();
        ballManager.updateBall();
        powerupManager.updatePowerUp();
        bulletManager.updateBullet();

        score += brickManager.updateBrickList(powerupManager);
        ballManager.updateBallList();
        powerupManager.updatePowerUpList();
        bulletManager.updateBulletList();

        // Check if ball is lost
        if (ballManager.getBallsList().isEmpty()) {
            lives--;
            if (lives <= 0) {
                gameOver();
                SoundManager.getInstance().pauseBackgroundMusic();
                SoundManager.getInstance().playSound(SoundManager.Sound.GAME_OVER);
            } else {
                powerupManager.clearPowerUpList(paddle, ballManager, bulletManager);
                paddle.setDefault();
                ballManager.setDefault(paddle);
                gameState = GameState.READY;
                SoundManager.getInstance().playSound(SoundManager.Sound.LOSE_LIFE);
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

            if (gameState == GameState.MENU) {
                handleMenuInput(key);
            } else if (gameState == GameState.SETTINGS) {
                handleSettingsInput(key);
            } else if (gameState == GameState.PAUSED) {
                handlePauseMenuInput(key);
            } else if (gameState == GameState.GAME_OVER) {
                handleGameOverInput(key);
            } else if (gameState == GameState.HIGH_SCORES) {
                handleHighScoreInput(key);
            } else if (gameState == GameState.READY || gameState == GameState.PLAYING) {
                handleGameplayInput(key);
            }
        } else {
            pressedKeys.remove(key);
        }
    }

    /**
     * Handles main menu input.
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
            } else if (selection == 1) { // High Scores
                showHighScores();
            } else if (selection == 2) { // Settings
                showSettings();
            } else if (selection == 3) { // Exit
                SoundManager.getInstance().dispose();
                System.exit(0);
            }
        }
        SoundManager.getInstance().playSound(SoundManager.Sound.BUTTON);
    }

    /**
     * Handles pause menu input.
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
            } else if (selection == 1) { // Settings
                showSettingsFromPause();
            }
            else if (selection == 2) { // Main Menu
                returnToMainMenu();
            }
        } else if (key == KeyCode.ESCAPE || key == KeyCode.SPACE) {
            resumeGame();
        }
    }

    /**
     * Handles game over input.
     * @param key the key code
     */
    private void handleGameOverInput(KeyCode key) {
        GameOver go = gameView.getGameOverMenu();

        boolean wasNameEntered = go.isNameEntered();
        go.handleInput(key);

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
     * Handles high score menu input.
     * @param key the key code
     */
    private void handleHighScoreInput(KeyCode key) {
        if (key == KeyCode.ENTER || key == KeyCode.ESCAPE) {
            returnToMainMenu();
        }
    }

    /**
     * Shows the high score menu.
     */
    public void showHighScores() {
        gameView.getHighScoreMenu().reloadHighScores();
        gameState = GameState.HIGH_SCORES;
    }

    /**
     * Handles settings menu input.
     * @param key the key code
     */
    private void handleSettingsInput(KeyCode key) {
        gameView.getSettingsMenu().handleInput(key);

        if (key == KeyCode.ENTER && gameView.getSettingsMenu().isBackSelected()) {
            returnFromSettings();
        } else if (key == KeyCode.ESCAPE) {
            returnFromSettings();
        }

        SoundManager.getInstance().playSound(SoundManager.Sound.BUTTON);
    }

    /**
     * Shows the settings menu.
     */
    public void showSettings() {
        gameView.getSettingsMenu().setPreviousState(GameState.MENU);
        gameView.getSettingsMenu().resetSelection();
        gameState = GameState.SETTINGS;
    }

    /**
     * Shows the settings from pause.
     */
    public void showSettingsFromPause() {
        gameView.getSettingsMenu().setPreviousState(GameState.PAUSED);
        gameView.getSettingsMenu().resetSelection();
        gameState = GameState.SETTINGS;
    }

    /**
     * Check back in settings return to which state.
     */
    public void returnFromSettings() {
        GameState previous = gameView.getSettingsMenu().getPreviousState();

        if (previous == GameState.PAUSED) {
            gameState = GameState.PAUSED;
            gameView.getPauseMenu().resetSelection();
        } else {
            returnToMainMenu();
        }
    }

    /**
     * Handles gameplay input.
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
                SoundManager.getInstance().pauseBackgroundMusic();
            }
        } else if (key == KeyCode.ESCAPE && gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
            gameView.getPauseMenu().resetSelection();
            SoundManager.getInstance().pauseBackgroundMusic();
        }
    }

    /**
     * Returns to main menu.
     */
    public void returnToMainMenu() {
        stop();
        gameState = GameState.MENU;
        gameView.getMainMenu().resetSelection();
        start();
        SoundManager.getInstance().playMenuMusic();
    }

    /**
     * Resumes the game from pause.
     */
    public void resumeGame() {
        gameState = GameState.PLAYING;
        SoundManager.getInstance().resumeBackgroundMusic();
    }

    /**
     * Handles level completion.
     */
    private void levelComplete() {
        gameState = GameState.LEVEL_COMPLETE;
        level++;

        SoundManager.getInstance().pauseBackgroundMusic();
        SoundManager.getInstance().playSound(SoundManager.Sound.LEVEL_COMPLETE);
        SoundManager.getInstance().playBackgroundMusic();

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
        initGameObjects((level - 1) % NUMBER_OF_LEVEL + 1);
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

    public BulletManager getBulletManager() {
        return bulletManager;
    }
}