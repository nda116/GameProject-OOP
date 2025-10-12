package com.arkanoid;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.arkanoid.core.GameManager;
import com.arkanoid.core.GameView;

/**
 * Main application class for the Arkanoid game.
 * Initializes the game window and starts the game loop.
 *
 */
public class Main extends Application {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    private static final String GAME_TITLE = "Arkanoid Game";

    private GameManager gameManager;
    private GameView gameView;

    @Override
    public void start(Stage primaryStage) {
        gameView = new GameView(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Initialize game manager with view
        gameManager = GameManager.getInstance();
        gameManager.init(gameView);

        // Create scene
        Scene scene = new Scene(gameView.getRoot(), WINDOW_WIDTH, WINDOW_HEIGHT);

        // Set up input handling
        scene.setOnKeyPressed(event -> gameManager.handleInput(event.getCode(), true));
        scene.setOnKeyReleased(event -> gameManager.handleInput(event.getCode(), false));

        // Configure stage
        primaryStage.setTitle(GAME_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Start the game
        gameManager.start();
    }

    @Override
    public void stop() {
        if (gameManager != null) {
            gameManager.stop();
        }
    }

    /**
     * Main entry point for the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
