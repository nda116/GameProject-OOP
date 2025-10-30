package com.arkanoid.menu;

import com.arkanoid.core.HighScoreManager;
import com.arkanoid.core.HighScoreManager.HighScoreEntry;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;

/**
 * High score menu displaying top 10 scores.
 */
public class HighScoreMenu extends Menu {

    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 48);
    private static final Font HEADER_FONT = Font.font("Arial", FontWeight.BOLD, 24);
    private static final Font SCORE_FONT = Font.font("Arial", FontWeight.NORMAL, 20);
    private static final Font SUBTITLE_FONT = Font.font("Arial", FontWeight.NORMAL, 18);

    private HighScoreManager highScoreManager;

    /**
     * Creates the high score menu.
     *
     * @param screenWidth  screen width
     * @param screenHeight screen height
     */
    public HighScoreMenu(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
        setObjectImage("/images/menu/menu.png");
        highScoreManager = new HighScoreManager();
        createButtons();
    }

    /**
     * Creates back button.
     */
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
        List<HighScoreEntry> topScores = highScoreManager.getTopScores(10);
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
}