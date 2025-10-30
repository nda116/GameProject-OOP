package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Game Over screen with name input, leaderboard save, and button navigation.
 */
public class GameOver extends Menu {
    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 64);
    private static final Font MESSAGE_FONT = Font.font("Arial", FontWeight.BOLD, 22);
    private static final Font INPUT_FONT = Font.font("Consolas", FontWeight.NORMAL, 26);

    private final StringBuilder playerName = new StringBuilder();
    private boolean isNameEntered = false;
    private int score;

    private static final String FILE_PATH = "highscores.txt";

    public GameOver(double width, double height) {
        super(width, height);
        setObjectImage("/images/menu/gameover.png");
        createButtons();
        resetSelection();
    }

    @Override
    public void createButtons() {
        getButtons().clear();
        double btnWidth = 280;
        double btnHeight = 60;
        double startX = (screenWidth - btnWidth) / 2;
        double startY = screenHeight / 2 + 80;
        double spacing = 80;

        getButtons().add(new Button(startX, startY, btnWidth, btnHeight, "RESTART"));
        getButtons().add(new Button(startX, startY + spacing, btnWidth, btnHeight, "BACK TO MENU"));
    }

    public void setScore(int score) {
        this.score = score;
        this.playerName.setLength(0);
        this.isNameEntered = false;
        resetSelection();
    }

    @Override
    public void render(GraphicsContext gc) {
        if (getBackgroundImage() != null) {
            gc.drawImage(getBackgroundImage(), 0, 0, screenWidth, screenHeight);
            gc.setFill(Color.rgb(0, 0, 0, 0.6));
            gc.fillRect(0, 0, screenWidth, screenHeight);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, screenWidth, screenHeight);
        }

        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(TITLE_FONT);
        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", screenWidth / 2, screenHeight / 2 - 140);

        gc.setFont(MESSAGE_FONT);
        gc.setFill(Color.WHITE);
        gc.fillText("Your Score: " + score, screenWidth / 2, screenHeight / 2 - 80);

        if (!isNameEntered) {
            gc.setFill(Color.LIGHTGRAY);
            gc.fillText("Enter your name:", screenWidth / 2, screenHeight / 2 - 20);

            gc.setFont(INPUT_FONT);
            gc.setFill(Color.YELLOW);
            String nameText = playerName.toString();
            gc.fillText(nameText, screenWidth / 2, screenHeight / 2 + 20);

            // Blinking cursor
            if (System.currentTimeMillis() % 1000 < 500) {
                javafx.scene.text.Text t = new javafx.scene.text.Text(nameText);
                t.setFont(INPUT_FONT);
                double textWidth = t.getLayoutBounds().getWidth();
                gc.setTextAlign(TextAlignment.LEFT);
                double startX = screenWidth / 2 - textWidth / 2;
                gc.fillText("_", startX + textWidth + 2, screenHeight / 2 + 20);
                gc.setTextAlign(TextAlignment.CENTER);
            }

            gc.setFont(MESSAGE_FONT);
            gc.setFill(Color.rgb(255, 255, 255, 0.6));
            gc.fillText("Press ENTER to confirm", screenWidth / 2, screenHeight / 2 + 70);

        } else {
            gc.setFont(MESSAGE_FONT);
            gc.setFill(Color.LIMEGREEN);
            gc.fillText("Score saved for " + playerName + "!", screenWidth / 2, screenHeight / 2 - 30);

            for (Button b : getButtons()) {
                b.render(gc);
            }

            gc.setFont(MESSAGE_FONT);
            gc.setFill(Color.rgb(255, 255, 255, 0.6));
            gc.fillText("Use ↑ ↓ to select and ENTER to confirm", screenWidth / 2, screenHeight - 40);
        }
    }

    public void handleInput(KeyCode key) {
        if (!isNameEntered) {
            if (key == KeyCode.ENTER) {
                String name = playerName.length() > 0 ? playerName.toString() : "ANON";
                saveHighScore(name, score);
                isNameEntered = true;
            } else if (key == KeyCode.BACK_SPACE && playerName.length() > 0) {
                playerName.deleteCharAt(playerName.length() - 1);
            } else if (key.isLetterKey() && playerName.length() < 10) {
                playerName.append(key.getName().toUpperCase());
            }
        } else {
            if (key == KeyCode.UP) {
                selectPrevious();
            } else if (key == KeyCode.DOWN) {
                selectNext();
            }
        }
    }

    public String confirmSelection() {
        if (!isNameEntered) return null;
        int index = getSelectedIndex();
        if (index == 0) return "RESTART";
        if (index == 1) return "BACK TO MENU";
        return null;
    }

    public boolean isNameEntered() {
        return isNameEntered;
    }

    public String getPlayerName() {
        return playerName.toString();
    }

    private void saveHighScore(String name, int score) {
        try {
            List<String> lines = new ArrayList<>();
            File file = new File(FILE_PATH);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) lines.add(line);
                }
            }

            lines.add(name + ":" + score);
            lines.sort((a, b) -> {
                int sa = Integer.parseInt(a.split(":")[1]);
                int sb = Integer.parseInt(b.split(":")[1]);
                return Integer.compare(sb, sa);
            });

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
