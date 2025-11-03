package com.arkanoid.core;

import com.arkanoid.menu.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * View class responsible for rendering the game.
 * Manages the canvas and drawing operations.
 */
public class GameView {

    private StackPane root;
    private Canvas canvas;
    private GraphicsContext gc;

    // Menus
    private Menu mainMenu;
    private PauseMenu pauseMenu;
    private GameOver gameOverMenu;
    private HighScoreMenu highScoreMenu;
    private SettingsMenu settingsMenu;

    private static final Color BACKGROUND_COLOR = Color.rgb(20, 20, 40);
    private static final Color UI_TEXT_COLOR = Color.CYAN;
    private static final Font UI_FONT = Font.font("Consolas", FontWeight.BOLD, 22);
    private static final Font TITLE_FONT = Font.font("Impact", FontWeight.BOLD, 48);
    private static final Font MESSAGE_FONT = Font.font("Impact", FontWeight.BOLD, 24);

    private String statusMessage = "";
    private long statusMessageTime = 0;
    private static final long STATUS_DURATION = 2000;
    /**
     * Constructs a GameView with specified dimensions.
     *
     * @param width width of the canvas
     * @param height height of the canvas
     */
    public GameView(double width, double height) {
        root = new StackPane();
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        mainMenu = new StartMenu(width, height);
        pauseMenu = new PauseMenu(width, height);
        gameOverMenu = new GameOver(width, height);
        highScoreMenu = new HighScoreMenu(width, height);
        settingsMenu = new SettingsMenu(width, height);

        clear();
    }

    /**
     * Clears the canvas with background color.
     */
    public void clear() {
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    /**
     * Main render method that delegates to appropriate renderer.
     *
     * @param gameManager the game manager with game state
     */
    public void render(GameManager gameManager) {
        clear();

        GameState state = gameManager.getGameState();

        switch (state) {
            case MENU:
                renderMainMenu();
                break;

            case SETTINGS:
                renderSettingsMenu();
                break;

            case HIGH_SCORES:
                renderHighScores();
                break;

            case READY:
            case PLAYING:
            case LEVEL_COMPLETE:
                renderGameplay(gameManager);
                break;

            case PAUSED:
                renderGameplay(gameManager);
                renderPauseMenu();
                renderStatusMessage();
                break;

            case GAME_OVER:
                renderGameplay(gameManager);
                gameOverMenu.render(gc);
                break;
        }
    }

    /**
     * Renders the main menu.
     */
    private void renderMainMenu() {
        mainMenu.render(gc);
    }

    /**
     * Renders the pause menu overlay.
     */
    private void renderPauseMenu() {
        pauseMenu.render(gc);
    }

    /**
     * Renders the high score menu.
     */
    private void renderHighScores() {
        highScoreMenu.render(gc);
    }

    /**
     * Renders the settings menu.
     */
    private void renderSettingsMenu() {
        settingsMenu.render(gc);
    }

    /**
     * Renders gameplay (bricks, paddle, ball, UI).
     *
     * @param gameManager the game manager
     */
    private void renderGameplay(GameManager gameManager) {
        // Render bricks
        gameManager.getBrickManager().renderBrickList(gc);

        // Render power-ups
        gameManager.getPowerupManager().renderPowerUpList(gc);

        // Render bullets
        gameManager.getBulletManager().renderBulletList(gc);


        // Render paddle and ball
        gameManager.getPaddle().render(gc);
        gameManager.getBallManager().renderBallList(gc);

        // Render UI
        renderGameUI(gameManager);

        // Render state-specific messages
        if (gameManager.getGameState() != GameState.GAME_OVER) {
            renderGameStateMessage(gameManager.getGameState());
        }

        renderStatusMessage();
    }

    /**
     * Renders the game UI (score, lives, level).
     *
     * @param gameManager the game manager
     */
    private void renderGameUI(GameManager gameManager) {
        gc.setFill(UI_TEXT_COLOR);
        gc.setFont(UI_FONT);

        // Score
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("" + gameManager.getScore(), 10, 25);

        // Lives
        int lives = gameManager.getLives();
        Image paddleIcon = gameManager.getPaddle().getObjectImage();
        double iconWidth = 44;
        double iconHeight = 11;
        double startX = 10;
        double startY = 35;

        for (int i = 0; i < lives; i++) {
            gc.drawImage(paddleIcon, startX + i * (iconWidth + 8), startY, iconWidth, iconHeight);
        }

        // Level
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("Level: " + gameManager.getLevel(), canvas.getWidth() - 10, 25);
    }

    /**
     * Renders messages based on game state.
     *
     * @param gameState current game state
     */
    private void renderGameStateMessage(GameState gameState) {
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;

        gc.setTextAlign(TextAlignment.CENTER);

        switch (gameState) {
            case READY:
                gc.setFont(MESSAGE_FONT);
                gc.setFill(Color.YELLOW);
                gc.fillText("Press SPACE to start", centerX, centerY + 80);
                gc.setFont(Font.font("Impact", 14));
                gc.fillText("Use LEFT/RIGHT arrows to move", centerX, centerY + 100);
                break;

            case GAME_OVER:
                break;

            case LEVEL_COMPLETE:
                gc.setFont(TITLE_FONT);
                gc.setFill(Color.LIGHTGREEN);
                gc.fillText("LEVEL COMPLETE!", centerX, centerY - 20);

                gc.setFont(MESSAGE_FONT);
                gc.fillText("Next level starting...", centerX, centerY + 30);
                break;

            case PLAYING:
                break;
        }
    }

    //Show save/load notification
    public void showStatusMessage(String message) {
        this.statusMessage = message;
        this.statusMessageTime = System.currentTimeMillis();
    }

    private void renderStatusMessage() {
        if (statusMessage == null || statusMessage.isEmpty()) return;

        long elapsed = System.currentTimeMillis() - statusMessageTime;
        if (elapsed > STATUS_DURATION) {
            statusMessage = "";
            return;
        }

        // Hiệu ứng mờ dần
        double alpha = 1.0 - (double) elapsed / STATUS_DURATION;
        gc.setGlobalAlpha(alpha);

        gc.setFont(Font.font("Consolas", FontWeight.BOLD, 22));
        gc.setFill(Color.LIGHTGREEN);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(statusMessage, canvas.getWidth() / 2, canvas.getHeight() - 40);

        gc.setGlobalAlpha(1.0);
    }

    public StackPane getRoot() {
        return root;
    }

    public GraphicsContext getGraphicsContext() {
        return gc;
    }

    public double getWidth() {
        return canvas.getWidth();
    }

    public double getHeight() {
        return canvas.getHeight();
    }

    public Menu getMainMenu() {
        return mainMenu;
    }

    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }

    public GameOver getGameOverMenu() {
        return gameOverMenu;
    }

    public HighScoreMenu getHighScoreMenu() {
        return highScoreMenu;
    }

    public SettingsMenu getSettingsMenu() {
        return settingsMenu;
    }

    public void resetGameOverMenu(int score) {
        gameOverMenu = new GameOver(getWidth(), getHeight());
        gameOverMenu.setScore(score);
    }
}