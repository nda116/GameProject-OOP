package com.arkanoid.core;

import com.arkanoid.entities.*;
import com.arkanoid.entities.balls.*;
import com.arkanoid.entities.bricks.*;
import com.arkanoid.entities.bullets.Bullet;
import com.arkanoid.entities.bullets.BulletManager;
import com.arkanoid.menu.*;
import com.arkanoid.powerups.*;
import com.arkanoid.saveload.*;


import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

import java.util.*;

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
    private BulletManager bulletManager;

    // Game state
    private int score;
    private int lives;
    private GameState gameState;
    private int level;

    private GameView gameView;

    // Game loop
    private AnimationTimer gameLoop;
    private static final double FIXED_TIME_STEP = 1.0 / 60.0; // 60 FPS
    private static final double MAX_ACCUMULATED_TIME = 0.25;
    private double accumulatedTime = 0.0;

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
     * Initializes game objects (Paddle, BallManager, PowerUpManager, BulletManager, BrickManager).
     */
    private void initGameObjects() {
        paddle = new Paddle();
        ballManager = new BallManager();
        powerupManager = new PowerUpManager();
        bulletManager = new BulletManager();
        brickManager = new BrickManager();
    }

    /**
     * end effect and clear all GameObject.
     */
    private void clearGameObject() {
        if (powerupManager != null && paddle != null
                && ballManager != null & bulletManager != null) {
            powerupManager.clearPowerUpList(paddle, ballManager, bulletManager);
        }
        if (paddle != null) {
            paddle = null;
        }
        if (ballManager != null) {
            ballManager.getBallsList().clear();
            ballManager = null;
        }
        if (bulletManager != null) {
            bulletManager = null;
        }
        if (brickManager != null) {
            brickManager = null;
        }
    }

    /**
     * Starts a new game from main menu.
     */
    public void startNewGame() {
        score = 0;
        lives = LIVES;
        level = 1;

        GameSaveManager.clearSave();
        clearGameObject();
        initGameObjects();
        brickManager.createBricksFromFile("/maps/level" + level + ".txt");;
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
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                // calculate delta time between 2 frames and convert from ms to second
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                // avoid spiral of death
                if (deltaTime > MAX_ACCUMULATED_TIME) {
                    deltaTime = MAX_ACCUMULATED_TIME;
                }

                accumulatedTime += deltaTime;

                // Update with fixed time step
                while (accumulatedTime >= FIXED_TIME_STEP) {
                    update(FIXED_TIME_STEP);
                    accumulatedTime -= FIXED_TIME_STEP;
                }

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
    public void update(double deltaTime) {
        if (gameState == GameState.PLAYING || gameState == GameState.READY) {
            handleContinuousInput(deltaTime);
        } else if (gameState == GameState.GAME_OVER || gameState == GameState.HIGH_SCORES) {
            return;
        } else {
            return;
        }


        if (gameState == GameState.READY) {
            if (ballManager.getBallsList().isEmpty()) {
                ballManager.addBall(new Ball());
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

        paddle.update(deltaTime);
        ballManager.updateBall(deltaTime);
        powerupManager.updatePowerUp(deltaTime);
        bulletManager.updateBullet(deltaTime);

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
    private void handleContinuousInput(double deltaTime) {
        if (pressedKeys.contains(KeyCode.LEFT)) {
            paddle.moveLeft(deltaTime);
        } else if (pressedKeys.contains(KeyCode.RIGHT)) {
            paddle.moveRight(deltaTime);
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
            } else if (selection == 1) { // Continue
                loadGame();
            } else if (selection == 2) { // High Scores
                showHighScores();
            } else if (selection == 3) { // Settings
                showSettings();
            } else if (selection == 4) { // Exit
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
            } else if (selection == 1) { // Save Game
                gameSave();
            } else if (selection == 2) { // Settings
                showSettingsFromPause();
            } else if (selection == 3) { // Main Menu
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
    private void showHighScores() {
        gameView.getHighScoreMenu().reloadHighScores();
        gameState = GameState.HIGH_SCORES;
    }

    /**
     * load saved game from file.
     */
    private void loadGame() {
        clearGameObject();
        initGameObjects();

        boolean success = GameLoadManager.loadGame(this);
        if (success) {
            System.out.println("Game loaded successfully!");
            SoundManager.getInstance().stopMenuMusic();
            SoundManager.getInstance().playBackgroundMusic();
        } else {
            System.out.println("Failed to load game.");
        }
    }

    private void gameSave() {
        powerupManager.clearPowerUpList(paddle, ballManager, bulletManager);

        boolean success = GameSaveManager.saveGame(this);
        if (success) {
            System.out.println("Game saved successfully!");
        } else {
            System.out.println("Failed to save game.");
            System.out.println("Failed to save game.");
        }

        returnToMainMenu();
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
    private void showSettings() {
        gameView.getSettingsMenu().setPreviousState(GameState.MENU);
        gameView.getSettingsMenu().resetSelection();
        gameState = GameState.SETTINGS;
    }

    /**
     * Shows the settings from pause.
     */
    private void showSettingsFromPause() {
        gameView.getSettingsMenu().setPreviousState(GameState.PAUSED);
        gameView.getSettingsMenu().resetSelection();
        gameState = GameState.SETTINGS;
    }

    /**
     * Check back in settings return to which state.
     */
    private void returnFromSettings() {
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
    private void returnToMainMenu() {
        clearGameObject();
        stop();
        gameState = GameState.MENU;
        gameView.getMainMenu().resetSelection();
        start();
        SoundManager.getInstance().playMenuMusic();
    }

    /**
     * Resumes the game from pause.
     */
    private void resumeGame() {
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
        clearGameObject();
        initGameObjects();
        int map = (level - 1) % NUMBER_OF_LEVEL + 1;
        brickManager.createBricksFromFile("/maps/level" + map + ".txt");
        gameState = GameState.READY;
    }

    /**
     * Handles game over state.
     */
    private void gameOver() {
        GameSaveManager.clearSave();
        gameState = GameState.GAME_OVER;
        gameView.resetGameOverMenu(score);
    }

    /**
     * Renders all game objects.
     */
    private void render() {
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

    public void setScore(int score) {
        this.score = score;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public GameView getGameView() {
        return gameView;
    }
}