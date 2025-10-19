package com.arkanoid.core;

import com.arkanoid.menu.Menu;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    //Menu
    private Menu mainMenu;

    private static final Color BACKGROUND_COLOR = Color.rgb(20, 20, 40);
    private static final Color UI_TEXT_COLOR = Color.WHITE;
    private static final Font UI_FONT = Font.font("Arial", FontWeight.BOLD, 18);
    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 48);
    private static final Font MESSAGE_FONT = Font.font("Arial", FontWeight.BOLD, 24);

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

        mainMenu = new Menu(width, height);

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

            case READY:
            case PLAYING:
            case LEVEL_COMPLETE:
                renderGameplay(gameManager);
                break;

            case PAUSED:
                renderGameplay(gameManager);
                break;

            case GAME_OVER:
                renderGameplay(gameManager);
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
     * Renders gameplay (bricks, paddle, ball, UI).
     *
     * @param gameManager the game manager
     */
    private void renderGameplay(GameManager gameManager) {
        // Render bricks
        gameManager.getBrickManager().renderBrickList(gc);

        // Render power-ups
        gameManager.getPowerupManager().renderPowerUpList(gc);

        // Render paddle and ball
        gameManager.getPaddle().render(gc);
        gameManager.getBallManager().renderBallList(gc);

        // Render UI
        renderGameUI(gameManager);

        // Render state-specific messages
        renderGameStateMessage(gameManager.getGameState());
    }

    /**
     * Renders the game UI (score, lives, level, power-up timer).
     *
     * @param gameManager the game manager
     */
    private void renderGameUI(GameManager gameManager) {
        gc.setFill(UI_TEXT_COLOR);
        gc.setFont(UI_FONT);

        // Score
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Score: " + gameManager.getScore(), 10, 25);

        // Lives
        gc.fillText("Lives: " + gameManager.getLives(), 10, 45);

        // Level
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("Level: " + gameManager.getLevel(), canvas.getWidth() - 10, 25);

        // Power-up timer
        double powerUpTimer = gameManager.getPowerUpTimer();
        if (powerUpTimer > 0) {
            gc.fillText(String.format("Power-up: %.1fs", powerUpTimer),
                    canvas.getWidth() - 10, 45);
        }
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
                gc.fillText("Press SPACE to start", centerX, centerY);
                gc.setFont(Font.font("Arial", 14));
                gc.fillText("Use LEFT/RIGHT arrows or A/D to move", centerX, centerY + 30);
                break;

            case PAUSED:
                gc.setFont(TITLE_FONT);
                gc.setFill(Color.ORANGE);
                gc.fillText("PAUSED", centerX, centerY - 20);
                gc.setFont(MESSAGE_FONT);
                gc.fillText("Press SPACE to continue", centerX, centerY + 30);
                break;

            case GAME_OVER:
                // Semi-transparent overlay
                gc.setFill(Color.rgb(0, 0, 0, 0.7));
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                gc.setFont(TITLE_FONT);
                gc.setFill(Color.RED);
                gc.fillText("GAME OVER", centerX, centerY - 40);

                gc.setFont(MESSAGE_FONT);
                gc.setFill(Color.WHITE);
                gc.fillText("Press R to restart", centerX, centerY + 20);
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
}