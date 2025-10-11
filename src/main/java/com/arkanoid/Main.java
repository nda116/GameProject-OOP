package com.arkanoid;

import com.arkanoid.core.GameManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Canvas canvas= new Canvas(GameManager.CANVAS_WIDTH, GameManager.CANVAS_HEIGHT);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, GameManager.CANVAS_WIDTH, GameManager.CANVAS_HEIGHT);

        GameManager game = new GameManager(canvas, scene);
        game.start();

        primaryStage.setTitle("Arkanoid Game");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
