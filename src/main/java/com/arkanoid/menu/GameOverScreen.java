package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.*;

/**
 * Represents the Game Over screen where the player can enter their name,
 * view their score, and save it to a high score list.
 * Also displays restart/menu options after confirmation.
 */
public class GameOverScreen {
    private final double width;
    private final double height;

    private int score;
    private String playerName = "";
    private boolean nameConfirmed = false;

    private static final Font TITLE_FONT = new Font("Arial", 72);
    private static final Font TEXT_FONT = new Font("Arial", 28);
    private static final Font SMALL_FONT = new Font("Arial", 18);

    private static final String FILE_PATH = "highscores.txt";

    /**
     * Constructs a GameOverScreen with given width and height.
     * @param width screen width
     * @param height screen height
     */
    public GameOverScreen(double width, double height) {
        this.width = width;
        this.height = height;
    }

    /** Sets the player's final score. */
    public void setScore(int score) {
        this.score = score;
        this.playerName = "";
        this.nameConfirmed = false;
    }

    /**
     * Handles key input for entering the player name and confirming it.
     * Supports letters, digits, Backspace, and Enter.
     * @param key The pressed key as a string.
     */
    public void handleInput(String key) {
        if (nameConfirmed) return;

        if ("ENTER".equalsIgnoreCase(key)) {
            if (playerName.trim().isEmpty()) {
                playerName = "Anon";
            }
            nameConfirmed = true;
            saveHighScore(playerName, score);

        } else if ("BACKSPACE".equalsIgnoreCase(key)) {
            if (!playerName.isEmpty()) {
                playerName = playerName.substring(0, playerName.length() - 1);
            }

        } else if (key.length() == 1 && playerName.length() < 10) {
            char c = key.charAt(0);
            if (Character.isLetterOrDigit(c)) {
                playerName += Character.toUpperCase(c); // Convert to uppercase for consistency
            }
        }
    }

    /**
     * Renders the Game Over screen, including score display,
     * name input prompt, and action hints.
     * @param gc GraphicsContext for drawing
     */
    public void render(GraphicsContext gc) {
        // Background overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, width, height);

        gc.setTextAlign(TextAlignment.CENTER);

        // Title
        gc.setFont(TITLE_FONT);
        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", width / 2, height / 2 - 150);

        // Score
        gc.setFont(TEXT_FONT);
        gc.setFill(Color.WHITE);
        gc.fillText("Your Score: " + score, width / 2, height / 2 - 90);

        // Input section
        if (!nameConfirmed) {
            gc.setFill(Color.LIGHTGRAY);
            gc.fillText("Enter your name:", width / 2, height / 2 - 30);

            // 💡 Show the name being typed + blinking cursor
            gc.setFill(Color.YELLOW);
            String blinkingCursor = (System.currentTimeMillis() % 1000 < 500) ? "_" : "";
            gc.fillText(playerName + blinkingCursor, width / 2, height / 2 + 20);

            gc.setFont(SMALL_FONT);
            gc.setFill(Color.LIGHTGRAY);
            gc.fillText("Press ENTER to confirm", width / 2, height / 2 + 70);

        } else {
            gc.setFont(TEXT_FONT);
            gc.setFill(Color.LIGHTGREEN);
            gc.fillText("Saved as: " + playerName, width / 2, height / 2);

            gc.setFont(SMALL_FONT);
            gc.setFill(Color.LIGHTGRAY);
            gc.fillText("Press [R] to Restart or [M] for Menu", width / 2, height / 2 + 60);
        }
    }

    /** Returns true if player has confirmed their name. */
    public boolean isNameConfirmed() {
        return nameConfirmed;
    }

    /**
     * Saves the high score to a text file (top 10 scores only).
     * Each line in the file is formatted as: name:score
     * @param name Player's name
     * @param score Player's score
     */
    private void saveHighScore(String name, int score) {
        try {
            List<String> scores = new ArrayList<>();

            File file = new File(FILE_PATH);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        scores.add(line);
                    }
                }
            }

            scores.add(name + ":" + score);

            // Sort scores descending
            scores.sort((a, b) -> {
                int sa = Integer.parseInt(a.split(":")[1]);
                int sb = Integer.parseInt(b.split(":")[1]);
                return Integer.compare(sb, sa);
            });

            // Keep top 10
            if (scores.size() > 10) {
                scores = scores.subList(0, 10);
            }

            // Write back
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String s : scores) {
                    writer.write(s);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Helper method to render with updated score. */
    public void render(GraphicsContext gc, int score) {
        this.score = score;
        render(gc);
    }
}
