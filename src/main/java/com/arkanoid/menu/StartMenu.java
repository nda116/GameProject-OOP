package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Start menu screen with background and buttons.
 */
public class StartMenu extends Menu {

    private static final Font SUBTITLE_FONT = Font.font("Arial", FontWeight.NORMAL, 20);

    /**
     * Creates the start menu.
     *
     * @param screenWidth  screen width
     * @param screenHeight screen height
     */
    public StartMenu(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
        setObjectImage("/images/menu/menu.png");
        createButtons();
    }

    /**
     * Creates start menu buttons.
     */
    @Override
    public void createButtons() {
        double buttonWidth = 300;
        double buttonHeight = 60;
        double startX = (screenWidth - buttonWidth) / 2;
        double startY = screenHeight / 2 + 50;
        double spacing = 80;

        /*buttons.add(new Button(startX, startY, buttonWidth, buttonHeight,
                "/images/menu/new_game_button.png"));
        buttons.add(new Button(startX, startY + spacing, buttonWidth, buttonHeight,
                "/images/menu/exit_button.png"));*/
        getButtons().add(new Button(startX, startY, buttonWidth, buttonHeight, "NEW GAME"));
        getButtons().add(new Button(startX, startY + spacing, buttonWidth, buttonHeight, "CONTINUE GAME"));
        getButtons().add(new Button(startX, startY + 2 * spacing, buttonWidth, buttonHeight, "EXIT"));
    }

    /**
     * Renders the start menu.
     *
     * @param gc graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        // Draw background if available
        if (getBackgroundImage() != null) {
            gc.drawImage(getBackgroundImage(), 0, 0, screenWidth, screenHeight);
        } else {
            gc.setFill(Color.DARKBLUE);
            gc.fillRect(0, 0, screenWidth, screenHeight);
        }

        // Draw buttons
        for (Button button : getButtons()) {
            button.render(gc);
        }

        // Draw instructions
        gc.setFill(Color.rgb(255, 255, 255, 0.7));
        gc.setFont(SUBTITLE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Use ↑↓ arrows to navigate, ENTER to select",
                screenWidth / 2, screenHeight - 30);
    }
}
