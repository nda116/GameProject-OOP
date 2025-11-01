package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.*;

/**
 * High score menu displaying top 10 scores.
 */
public class HighScoreMenu extends Menu {

    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 48);
    private static final Font HEADER_FONT = Font.font("Arial", FontWeight.BOLD, 24);
    private static final Font SCORE_FONT = Font.font("Arial", FontWeight.NORMAL, 20);
    private static final Font SUBTITLE_FONT = Font.font("Arial", FontWeight.NORMAL, 18);

    private static final String HIGHSCORE_FILE = System.getProperty("user.dir") + "/highscores.txt";

    private List<HighScoreEntry> highScores;

    /**
     * Creates the high score menu.
     *
     * @param screenWidth  screen width
     * @param screenHeight screen height
     */
    public HighScoreMenu(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
        setObjectImage("/images/menu/menu.png");
        highScores = new ArrayList<>();
        loadHighScores();
        createButtons();
    }

    /**
     * Loads high scores from file.
     */
    private void loadHighScores() {
        File file = new File(HIGHSCORE_FILE);
        if (!file.exists()) {
            System.out.println("High score file not found: " + file.getAbsolutePath());
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && line.contains(":")) {
                    String[] parts = line.split(":");
                    if (parts.length == 2) {
                        try {
                            String name = parts[0].trim();
                            int score = Integer.parseInt(parts[1].trim());
                            highScores.add(new HighScoreEntry(name, score));
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid score format: " + line);
                        }
                    }
                }
            }

            highScores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));
        } catch (IOException e) {
            System.out.println("Error loading high scores: " + e.getMessage());
        }
    }


    /**
     * Gets top N high scores.
     * @param count number of scores to return
     * @return list of top high scores
     */
    public List<HighScoreEntry> getTopScores(int count) {
        int limit = Math.min(count, highScores.size());
        return new ArrayList<>(highScores.subList(0, limit));
    }

    /**
     * Gets all high scores.
     * @return list of all high scores
     */
    public List<HighScoreEntry> getAllScores() {
        return new ArrayList<>(highScores);
    }

    /**
     * Checks if a score qualifies for top 10.
     * @param score the score to check
     * @return true if score is in top 10
     */
    public boolean isTopScore(int score) {
        if (highScores.size() < 10) {
            return true;
        }
        return score > highScores.get(9).getScore();
    }
    
    @Override
    public void createButtons() {

    }

    /**
     * Renders the high score menu.
     *
     * @param gc graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        // Draw background
        if (getBackgroundImage() != null) {
            gc.drawImage(getBackgroundImage(), 0, 0, screenWidth, screenHeight);
        } else {
            gc.setFill(Color.DARKBLUE);
            gc.fillRect(0, 0, screenWidth, screenHeight);
        }

        // Draw semi-transparent overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, 0, screenWidth, screenHeight);

        // Draw title
        gc.setFill(Color.GOLD);
        gc.setFont(TITLE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("HIGH SCORES", screenWidth / 2, 80);

        // Draw table headers
        double startY = 150;
        double columnX1 = screenWidth / 2 - 180; // Rank column
        double columnX2 = screenWidth / 2 - 50;  // Name column
        double columnX3 = screenWidth / 2 + 100; // Score column

        gc.setFill(Color.WHITE);
        gc.setFont(HEADER_FONT);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("RANK", columnX1, startY);
        gc.fillText("NAME", columnX2, startY);
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("SCORE", columnX3 + 100, startY);

        // Draw separator line
        gc.setStroke(Color.GOLD);
        gc.setLineWidth(2);
        gc.strokeLine(columnX1 - 10, startY + 10, columnX3 + 110, startY + 10);

        // Draw high scores
        List<HighScoreEntry> topScores = getTopScores(10);
        gc.setFont(SCORE_FONT);

        double rowHeight = 35;
        double currentY = startY + 50;

        for (int i = 0; i < topScores.size(); i++) {
            HighScoreEntry entry = topScores.get(i);

            // Highlight top 3
            if (i == 0) {
                gc.setFill(Color.GOLD);
            } else if (i == 1) {
                gc.setFill(Color.SILVER);
            } else if (i == 2) {
                gc.setFill(Color.rgb(205, 127, 50)); // Bronze
            } else {
                gc.setFill(Color.WHITE);
            }

            // Draw rank
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(String.valueOf(i + 1), columnX1 + 10, currentY);

            // Draw name (truncate if too long)
            String name = entry.getName();
            if (name.length() > 15) {
                name = name.substring(0, 12) + "...";
            }
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText(name, columnX2, currentY);

            // Draw score
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText(String.valueOf(entry.getScore()), columnX3 + 100, currentY);

            currentY += rowHeight;
        }

        // Draw back button
        /*for (Button button : getButtons()) {
            button.render(gc);
        }*/

        // Draw instructions
        gc.setFill(Color.rgb(255, 255, 255, 0.7));
        gc.setFont(SUBTITLE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Press ENTER or ESC to return to menu",
                screenWidth / 2, screenHeight - 30);
    }

    /**
     * Inner class representing a high score entry.
     */
    public static class HighScoreEntry {
        private String name;
        private int score;

        public HighScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }

    public void reloadHighScores() {
        highScores.clear();
        loadHighScores();
    }

}