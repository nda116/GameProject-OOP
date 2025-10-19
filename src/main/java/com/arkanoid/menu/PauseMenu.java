package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;
import java.util.List;

/**
 * Pause menu overlay with resume and exit options.
 *
 * @author Arkanoid Team
 * @version 1.0
 */
public class PauseMenu {

    private List<Button> buttons;
    private double screenWidth;
    private double screenHeight;

    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 56);

    /**
     * Creates the pause menu.
     *
     * @param screenWidth screen width
     * @param screenHeight screen height
     */
    public PauseMenu(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.buttons = new ArrayList<>();

        createButtons();
    }

    /**
     * Creates pause menu buttons.
     */
    private void createButtons() {
        double buttonWidth = 250;
        double buttonHeight = 60;
        double startX = (screenWidth - buttonWidth) / 2;
        double startY = (screenHeight - buttonHeight) / 2;
        double spacing = 80;

        buttons.add(new Button(startX, startY, buttonWidth, buttonHeight, "RESUME"));
        buttons.add(new Button(startX, startY + spacing, buttonWidth, buttonHeight, "MAIN MENU"));
    }

    /**
     * Renders the pause menu overlay.
     *
     * @param gc graphics context
     */
    public void render(GraphicsContext gc) {
        // Draw semi-transparent overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, screenWidth, screenHeight);

        // Draw pause panel
        double panelWidth = 400;
        double panelHeight = 350;
        double panelX = (screenWidth - panelWidth) / 2;
        double panelY = (screenHeight - panelHeight) / 2;

        // Panel shadow
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRoundRect(panelX + 5, panelY + 5, panelWidth, panelHeight, 20, 20);

        // Panel background
        gc.setFill(Color.rgb(30, 30, 60));
        gc.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);

        // Panel border
        gc.setStroke(Color.rgb(70, 130, 180));
        gc.setLineWidth(3);
        gc.strokeRoundRect(panelX, panelY, panelWidth, panelHeight, 20, 20);

        // Draw "PAUSED" title
        gc.setFont(TITLE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.ORANGE);
        gc.fillText("PAUSED", screenWidth / 2, panelY + 80);

        // Draw buttons
        for (Button button : buttons) {
            button.render(gc);
        }

        // Draw instructions
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Press SPACE to resume", screenWidth / 2, panelY + panelHeight - 20);
    }

    /**
     * Gets the list of buttons.
     *
     * @return list of menu buttons
     */
    public List<Button> getButtons() {
        return buttons;
    }
}
