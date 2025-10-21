package com.arkanoid.core;

import com.arkanoid.menu.*;
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

    private final StackPane root;
    private final Canvas canvas;
    private final GraphicsContext gc;

    // Menus
    private final Menu mainMenu;
    private final PauseMenu pauseMenu;

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
        pauseMenu = new PauseMenu(width, height);

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
                renderPauseMenu();
                break;

            case GAME_OVER:
                renderGameplay(gameManager);
                gc.setFill(Color.rgb(0, 0, 0, 0.6)); // 0.6 = độ trong suốt (0.0 -> trong suốt, 1.0 -> đen đặc)
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                gameManager.getGameOverScreen().render(gc);
                break;
        }
    }

    /** Renders the main menu. */
    private void renderMainMenu() {
        mainMenu.render(gc);
    }

    /** Renders the pause menu overlay. */
    private void renderPauseMenu() {
        pauseMenu.render(gc);
    }

    /** Renders the game over screen. */
    private void renderGameOver(GameManager gameManager) {
        renderGameplay(gameManager);
        gameManager.getGameOverScreen().render(gc);
    }

    /** Renders gameplay (bricks, paddle, ball, UI). */
    private void renderGameplay(GameManager gameManager) {
        gameManager.getBrickManager().renderBrickList(gc);
        gameManager.getPowerupManager().renderPowerUpList(gc);
        gameManager.getPaddle().render(gc);
        gameManager.getBallManager().renderBallList(gc);
        renderGameUI(gameManager);
        renderGameStateMessage(gameManager.getGameState());
    }

    /** Renders the game UI (score, lives, level, etc). */
    private void renderGameUI(GameManager gameManager) {
        gc.setFill(UI_TEXT_COLOR);
        gc.setFont(UI_FONT);

        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Score: " + gameManager.getScore(), 10, 25);
        gc.fillText("Lives: " + gameManager.getLives(), 10, 45);

        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("Level: " + gameManager.getLevel(), canvas.getWidth() - 10, 25);

        double powerUpTimer = gameManager.getPowerUpTimer();
        if (powerUpTimer > 0) {
            gc.fillText(String.format("Power-up: %.1fs", powerUpTimer),
                    canvas.getWidth() - 10, 45);
        }
    }

    /** Renders state-specific messages (READY, LEVEL COMPLETE). */
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

            case LEVEL_COMPLETE:
                gc.setFont(TITLE_FONT);
                gc.setFill(Color.LIGHTGREEN);
                gc.fillText("LEVEL COMPLETE!", centerX, centerY - 20);

                gc.setFont(MESSAGE_FONT);
                gc.fillText("Next level starting...", centerX, centerY + 30);
                break;

            default:
                break;
        }
    }

    public StackPane getRoot() {
        return root;
    }
}
