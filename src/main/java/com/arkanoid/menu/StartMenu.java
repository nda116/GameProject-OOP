package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;

/**
 * Start menu screen with background and buttons.
 */
public class StartMenu extends Menu {

    /**
     * Creates the start menu.
     *
     * @param screenWidth  screen width
     * @param screenHeight screen height
     */
    public StartMenu(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
        setObjectImage("/images/menu/menu.jpg");
        createButtons();
    }

    /**
     * Creates start menu buttons.
     */
    @Override
    public void createButtons() {
        double buttonWidth = 200;
        double buttonHeight = 40;
        double startX = 200 + (screenWidth - buttonWidth) / 2;
        double startY = (screenHeight / 2) - 80;
        double spacing = 60;

        getButtons().add(new Button(startX, startY, buttonWidth, buttonHeight, "NEW GAME"));
        getButtons().add(new Button(startX, startY + spacing, buttonWidth, buttonHeight, "CONTINUE"));
        getButtons().add(new Button(startX, startY + spacing * 2, buttonWidth, buttonHeight, "HIGH SCORES"));
        getButtons().add(new Button(startX, startY + spacing * 3, buttonWidth, buttonHeight, "SETTINGS"));
        getButtons().add(new Button(startX, startY + spacing * 4, buttonWidth, buttonHeight, "EXIT"));
    }

    /**
     * Renders the start menu.
     *
     * @param gc graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getBackgroundImage(), 0, 0, screenWidth, screenHeight);

        for (Button button : getButtons()) {
            button.render(gc);
        }
    }
}
