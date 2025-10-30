package com.arkanoid.core;

import java.io.*;
import java.util.*;

/**
 * Manages high scores - reading from file and maintaining sorted list.
 */
public class HighScoreManager {

    private static final String HIGHSCORE_FILE = "/highscores.txt";
    private List<HighScoreEntry> highScores;

    public HighScoreManager() {
        highScores = new ArrayList<>();
        loadHighScores();
    }

    /**
     * Loads high scores from file.
     */
    private void loadHighScores() {
        try (InputStream is = getClass().getResourceAsStream(HIGHSCORE_FILE)) {
            if (is == null) {
                System.out.println("High score file not found");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
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

            // Sort by score descending
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
}