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
    private Image image;
    private boolean selected;

    private static final Color NORMAL_COLOR = Color.rgb(70, 130, 180);
    private static final Color SELECTED_COLOR = Color.rgb(100, 160, 210);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = Font.font("Arial", FontWeight.BOLD, 24);

    /**
     * Creates a text-based button.
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
    }

    /**
     * Renders the button.
     *
     * @param gc graphics context
     */
    public void render(GraphicsContext gc) {
        // Draw text button
        Color buttonColor = selected ? SELECTED_COLOR : NORMAL_COLOR;

        // Draw button background with shadow
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillRoundRect(x + 5, y + 5, width, height, 10, 10);

        gc.setFill(buttonColor);
        gc.fillRoundRect(x, y, width, height, 10, 10);

        // Draw border
        gc.setStroke(Color.rgb(50, 100, 150));
        gc.setLineWidth(2);
        gc.strokeRoundRect(x, y, width, height, 10, 10);

        // Draw selected
        if (selected) {
            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText(">>", x - 40, y + height / 2 + 10);
        }

        // Draw text
        gc.setFill(TEXT_COLOR);
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
