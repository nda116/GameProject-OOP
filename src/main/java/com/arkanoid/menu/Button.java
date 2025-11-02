package com.arkanoid.menu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;

/**
 * Custom button class for menu system.
 */
public class Button {

    private double x;
    private double y;
    private double width;
    private double height;
    private String text;
    private Image buttonImage;
    private boolean selected;

    private static final Color NORMAL_COLOR = Color.rgb(255, 255, 255);
    private static final Color SELECTED_COLOR = Color.rgb(0, 255, 255);
    private static final Font BUTTON_FONT = Font.font("Impact", FontWeight.NORMAL, 24);

    /**
     * Creates a text-based button with image background.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param width button width
     * @param height button height
     * @param text button text
     */
    public Button(double x, double y, double width, double height, String text) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.selected = false;

        try {
            this.buttonImage = new Image(getClass().getResourceAsStream("/images/menu/button.png"));
        } catch (Exception e) {
            System.err.println("Could not load button.png: " + e.getMessage());
        }
    }

    /**
     * Renders the button.
     *
     * @param gc graphics context
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(buttonImage, x, y, width, height);

        // Draw border (highlight when selected)
        if (selected) {
            gc.setStroke(Color.rgb(0, 255, 255));
            gc.setLineWidth(4);
            gc.strokeRoundRect(x, y, width, height, 10, 10);
        }

        // Draw selected
        if (selected) {
            gc.setFill(Color.rgb(0, 255, 255));
            gc.setFont(Font.font("Impact", FontWeight.BOLD, 32));
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText("►", x - 45, y + height / 2 + 10);
        }

        // Draw text with color based on selection
        Color textColor = selected ? SELECTED_COLOR : NORMAL_COLOR;
        gc.setFill(textColor);
        gc.setFont(BUTTON_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text, x + width / 2, y + height / 2 + 8);
    }

    public String getText() {
        return text;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}