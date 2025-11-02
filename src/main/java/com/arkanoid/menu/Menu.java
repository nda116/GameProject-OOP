package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all menu types.
 */
public abstract class Menu {

    private List<Button> buttons;
    private int selectedIndex;
    private Image backgroundImage;

    protected double screenWidth;
    protected double screenHeight;

    /**
     * Base constructor for all menus.
     *
     * @param screenWidth screen width
     * @param screenHeight screen height
     */
    public Menu(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.buttons = new ArrayList<>();
    }

    /**
     * set ObjectImage from new image path.
     * @param imagePath path to image.
     */
    protected void setObjectImage(String imagePath) {
        try {
            backgroundImage = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            System.out.println("Can not find" + imagePath);
        }
    }

    public abstract void createButtons();

    public abstract void render(GraphicsContext gc);

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
     * Resets selection to first button.
     */
    public void resetSelection() {
        selectedIndex = 0;
        updateSelection();
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }
}
