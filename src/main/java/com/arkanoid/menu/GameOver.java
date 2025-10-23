package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Game Over screen with name input, leaderboard save, and button navigation.
 */
public class GameOver {
    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 64);
    private static final Font MESSAGE_FONT = Font.font("Arial", FontWeight.BOLD, 22);
    private static final Font INPUT_FONT = Font.font("Consolas", FontWeight.NORMAL, 26);
    private final List<Button> buttons;
    private final StringBuilder playerName = new StringBuilder();
    private final double width;
    private final double height;
    private int selectedIndex = 0;
    private boolean nameEntered = false;
    private int score;

    private static final String FILE_PATH = "highscores.txt";

    public GameOver(double width, double height) {
        this.width = width;
        this.height = height;
        buttons = new ArrayList<>();

        double btnWidth = 280;
        double btnHeight = 60;
        double startX = (width - btnWidth) / 2;
        double startY = height / 2 + 80;
        double spacing = 80;

        buttons.add(new Button(startX, startY, btnWidth, btnHeight, "RESTART"));
        buttons.add(new Button(startX, startY + spacing, btnWidth, btnHeight, "BACK TO MENU"));
        updateSelection();
    }

    /** Set the final score before showing the Game Over screen. */
    public void setScore(int score) {
        this.score = score;
        this.playerName.setLength(0);
        this.nameEntered = false;
        this.selectedIndex = 0;
        updateSelection();
    }

    /** Render Game Over screen with overlay and input/buttons. */
    public void render(GraphicsContext gc) {
        // Dark overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, width, height);

        gc.setTextAlign(TextAlignment.CENTER);

        // Title
        gc.setFont(TITLE_FONT);
        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", width / 2, height / 2 - 140);

        // Score
        gc.setFont(MESSAGE_FONT);
        gc.setFill(Color.WHITE);
        gc.fillText("Your Score: " + score, width / 2, height / 2 - 80);

        if (!nameEntered) {
            gc.setFill(Color.LIGHTGRAY);
            gc.fillText("Enter your name:", width / 2, height / 2 - 20);

            // Draw name
            gc.setFont(INPUT_FONT);
            gc.setFill(Color.YELLOW);
            String nameText = playerName.toString();
            gc.fillText(nameText, width / 2, height / 2 + 20);

            // Blink cursor
            if (System.currentTimeMillis() % 1000 < 500) {
                javafx.scene.text.Text t = new javafx.scene.text.Text(nameText);
                t.setFont(INPUT_FONT);
                double textWidth = t.getLayoutBounds().getWidth();

                gc.setTextAlign(TextAlignment.LEFT);
                double startX = width / 2 - textWidth / 2;
                gc.fillText("_", startX + textWidth + 2, height / 2 + 20);
                gc.setTextAlign(TextAlignment.CENTER);
            }

            gc.setFont(MESSAGE_FONT);
            gc.setFill(Color.rgb(255, 255, 255, 0.6));
            gc.fillText("Press ENTER to confirm", width / 2, height / 2 + 70);

        } else {
            gc.setFont(MESSAGE_FONT);
            gc.setFill(Color.LIMEGREEN);
            gc.fillText("Score saved for " + playerName
                    +
                    "!", width / 2, height / 2 - 30);

            for (Button b : buttons) {
                b.render(gc);
            }

            gc.setFont(MESSAGE_FONT);
            gc.setFill(Color.rgb(255, 255, 255, 0.6));
            gc.fillText("Use ↑ ↓ to select and ENTER to confirm", width / 2, height - 40);
        }
    }


    /** Handle keyboard input for name entry or menu selection. */
    public void handleInput(KeyCode key) {
        if (!nameEntered) {
            // Name entry mode
            if (key == KeyCode.ENTER) {
                String name = playerName.length() > 0 ? playerName.toString() : "ANON";
                saveHighScore(name, score);
                nameEntered = true;
            } else if (key == KeyCode.BACK_SPACE && playerName.length() > 0) {
                playerName.deleteCharAt(playerName.length() - 1);
            } else if (key.isLetterKey() && playerName.length() < 10) {
                playerName.append(key.getName().toUpperCase());
            }
        } else {
            // Button selection mode
            if (key == KeyCode.UP) {
                selectPrevious();
            } else if (key == KeyCode.DOWN) {
                selectNext();
            }
        }
    }

    /** When user presses ENTER in button selection mode */
    public String confirmSelection() {
        if (!nameEntered) return null;

        if (selectedIndex == 0) {
            return "RESTART";
        } else if (selectedIndex == 1) {
            return "BACK TO MENU";
        }
        return null;
    }


    private void selectPrevious() {
        selectedIndex--;
        if (selectedIndex < 0) selectedIndex = buttons.size() - 1;
        updateSelection();
    }

    private void selectNext() {
        selectedIndex++;
        if (selectedIndex >= buttons.size()) selectedIndex = 0;
        updateSelection();
    }

    private void updateSelection() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setSelected(i == selectedIndex);
        }
    }

    public boolean isNameEntered() {
        return nameEntered;
    }

    public String getPlayerName() {
        return playerName.toString();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /** Save to high scores.txt (top 10 only). */
    private void saveHighScore(String name, int score) {
        try {
            List<String> lines = new ArrayList<>();
            File file = new File(FILE_PATH);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                }
            }

            lines.add(name + ":" + score);

            // Sort by descending score
            lines.sort((a, b) -> {
                int sa = Integer.parseInt(a.split(":")[1]);
                int sb = Integer.parseInt(b.split(":")[1]);
                return Integer.compare(sb, sa);
            });

            // Keep top 10
            if (lines.size() > 10) lines = lines.subList(0, 10);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String s : lines) {
                    writer.write(s);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
