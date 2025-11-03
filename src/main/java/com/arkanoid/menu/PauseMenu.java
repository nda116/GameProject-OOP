package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Pause menu overlay with resume and exit options.
 */
public class PauseMenu extends Menu {
    private static final Font TITLE_FONT = Font.font("Impact", FontWeight.BOLD, 40);

    /**
     * Creates the pause menu.
     *
     * @param screenWidth screen width
     * @param screenHeight screen height
     */
    public PauseMenu(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
        setObjectImage("/images/menu/pauseMenu.png");
        createButtons();
    }

    /**
     * Creates pause menu buttons.
     */
    @Override
    public void createButtons() {
        getButtons().clear();

        double buttonWidth = 200;
        double buttonHeight = 40;
        double startX = (screenWidth - buttonWidth) / 2;
        double startY = (screenHeight - buttonHeight) / 2 - 30;
        double spacing = 60;

        getButtons().add(new Button(startX, startY, buttonWidth, buttonHeight, "RESUME"));
        getButtons().add(new Button(startX, startY + spacing, buttonWidth, buttonHeight, "SAVE GAME"));
        getButtons().add(new Button(startX, startY + spacing * 2, buttonWidth, buttonHeight, "SETTINGS"));
        getButtons().add(new Button(startX, startY + spacing * 3, buttonWidth, buttonHeight, "MAIN MENU"));
    }

    /**
     * Renders the pause menu overlay.
     *
     * @param gc graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, screenWidth, screenHeight);

        double panelWidth = 400;
        double panelHeight = 350;
        double panelX = (screenWidth - panelWidth) / 2;
        double panelY = 35 + (screenHeight - panelHeight) / 2;

        gc.drawImage(getBackgroundImage(), panelX, panelY, panelWidth, panelHeight);

        gc.setFont(TITLE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.CYAN);
        gc.fillText("PAUSED", screenWidth / 2, panelY + 40);

        for (Button button : getButtons()) {
            button.render(gc);
        }
    }
}