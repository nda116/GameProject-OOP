package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Main menu screen with background and buttons.
 */
public class Menu {

    private List<Button> buttons;
    private int selectedIndex;
    private Image backgroundImage;
    private double screenWidth;
    private double screenHeight;

    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 72);
    private static final Font SUBTITLE_FONT = Font.font("Arial", FontWeight.NORMAL, 20);

    /**
     * Creates the main menu.
     *
     * @param screenWidth screen width
     * @param screenHeight screen height
     */
    public Menu(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.buttons = new ArrayList<>();

        loadImages();
        createButtons();
    }

    /**
     * Loads menu images (background, logo).
     */
    private void loadImages() {
        try {
            backgroundImage = new Image("/images/menu/menu.png");
        } catch (Exception e) {
            System.out.println("Background image not found");
            backgroundImage = null;
        }
    }

    /**
     * Creates menu buttons.
     */
    private void createButtons() {
        double buttonWidth = 300;
        double buttonHeight = 60;
        double startX = (screenWidth - buttonWidth) / 2;
        double startY = screenHeight / 2 + 50;
        double spacing = 80;

        /*buttons.add(new Button(startX, startY, buttonWidth, buttonHeight,
                "/images/menu/new_game_button.png"));
        buttons.add(new Button(startX, startY + spacing, buttonWidth, buttonHeight,
                "/images/menu/exit_button.png"));*/
        buttons.add(new Button(startX, startY, buttonWidth, buttonHeight, "NEW GAME"));
        buttons.add(new Button(startX, startY + spacing, buttonWidth, buttonHeight, "EXIT"));
    }

    /**
     * Moves selection up.
     */
    public void selectPrevious() {
        selectedIndex--;
        if (selectedIndex < 0) {
            selectedIndex = buttons.size() - 1;
        }
        updateSelection();
    }

    /**
     * Moves selection down.
     */
    public void selectNext() {
        selectedIndex++;
        if (selectedIndex >= buttons.size()) {
            selectedIndex = 0;
        }
        updateSelection();
    }

    /**
     * Updates button selection states.
     */
    private void updateSelection() {
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setSelected(i == selectedIndex);
        }
    }

    /**
     * Gets the currently selected option index.
     *
     * @return selected index (0 = New Game, 1 = Exit)
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Renders the main menu.
     *
     * @param gc graphics context
     */
    public void render(GraphicsContext gc) {
        // Draw background
        gc.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight);

        // Draw buttons
        for (Button button : buttons) {
            button.render(gc);
        }

        // Draw instructions
        gc.setFill(Color.rgb(255, 255, 255, 0.7));
        gc.setFont(SUBTITLE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Use ↑↓ arrows to navigate, ENTER to select",
                screenWidth / 2, screenHeight - 30);
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
