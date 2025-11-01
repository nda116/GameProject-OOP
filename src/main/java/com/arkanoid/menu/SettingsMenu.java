package com.arkanoid.menu;

import com.arkanoid.core.SoundManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * Settings menu for audio controls.
 */
public class SettingsMenu extends Menu {

    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 48);
    private static final Font SETTING_FONT = Font.font("Arial", FontWeight.BOLD, 24);
    private static final Font VALUE_FONT = Font.font("Arial", FontWeight.NORMAL, 20);

    private int selectedSetting = 0; // 0=Master, 1=Music, 2=SFX, 3=Back

    private double masterVolume;
    private double musicVolume;
    private double sfxVolume;

    /**
     * Creates the settings menu.
     *
     * @param screenWidth  screen width
     * @param screenHeight screen height
     */
    public SettingsMenu(double screenWidth, double screenHeight) {
        super(screenWidth, screenHeight);
        loadCurrentSettings();
        createButtons();
    }

    /**
     * Loads current volume settings from SoundManager.
     */
    private void loadCurrentSettings() {
        SoundManager sm = SoundManager.getInstance();
        masterVolume = sm.getMasterVolume();
        musicVolume = sm.getMusicVolume();
        sfxVolume = sm.getSfxVolume();
    }

    /**
     * Creates settings menu buttons.
     */
    @Override
    public void createButtons() {
        double buttonWidth = 300;
        double buttonHeight = 60;
        double startX = (screenWidth - buttonWidth) / 2;
        double startY = screenHeight - 120;

        getButtons().add(new Button(startX, startY, buttonWidth, buttonHeight, "BACK TO MENU"));
    }

    /**
     * Handles input for settings menu.
     *
     * @param key the key code
     */
    public void handleInput(KeyCode key) {
        if (key == KeyCode.UP) {
            selectedSetting--;
            if (selectedSetting < 0) {
                selectedSetting = 3;
            }
        } else if (key == KeyCode.DOWN) {
            selectedSetting++;
            if (selectedSetting > 3) {
                selectedSetting = 0;
            }
        } else if (key == KeyCode.LEFT) {
            adjustVolume(-0.1);
        } else if (key == KeyCode.RIGHT) {
            adjustVolume(0.1);
        }
    }

    /**
     * Adjusts the currently selected volume setting.
     *
     * @param delta change amount (-0.1 or 0.1)
     */
    private void adjustVolume(double delta) {
        SoundManager sm = SoundManager.getInstance();

        switch (selectedSetting) {
            case 0:
                masterVolume = Math.max(0.0, Math.min(1.0, masterVolume + delta));
                sm.setMasterVolume(masterVolume);
                break;
            case 1:
                musicVolume = Math.max(0.0, Math.min(1.0, musicVolume + delta));
                sm.setMusicVolume(musicVolume);
                break;
            case 2:
                sfxVolume = Math.max(0.0, Math.min(1.0, sfxVolume + delta));
                sm.setSfxVolume(sfxVolume);
                break;
        }
    }

    /**
     * Checks if Back button is selected.
     *
     * @return true if back is selected
     */
    public boolean isBackSelected() {
        return selectedSetting == 3;
    }

    /**
     * Renders the settings menu.
     *
     * @param gc graphics context
     */
    @Override
    public void render(GraphicsContext gc) {
        // Draw semi-transparent background
        gc.setFill(Color.rgb(20, 20, 40, 0.95));
        gc.fillRect(0, 0, screenWidth, screenHeight);

        // Draw title
        gc.setFill(Color.YELLOW);
        gc.setFont(TITLE_FONT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("SETTINGS", screenWidth / 2, 100);

        // Draw settings
        double startY = 200;
        double spacing = 80;

        // Master Volume
        renderVolumeSetting(gc, "Master Volume", masterVolume, 0, startY);

        // Music Volume
        renderVolumeSetting(gc, "Music Volume", musicVolume, 1, startY + spacing);

        // SFX Volume
        renderVolumeSetting(gc, "Sound Effects Volume", sfxVolume, 2, startY + spacing * 2);

        // Draw instructions
        gc.setFill(Color.rgb(255, 255, 255, 0.7));
        gc.setFont(VALUE_FONT);
        gc.fillText("Use ↑↓ to select, ←→ to adjust volume", screenWidth / 2 - 170, startY + spacing * 3);

        // Draw Back button
        for (Button button : getButtons()) {
            button.setSelected(selectedSetting == 3);
            button.render(gc);
        }
    }

    /**
     * Renders a volume setting with slider.
     *
     * @param gc graphics context
     * @param label setting label
     * @param volume current volume (0.0 to 1.0)
     * @param index setting index
     * @param y y-position
     */
    private void renderVolumeSetting(GraphicsContext gc, String label, double volume, int index, double y) {
        boolean isSelected = (selectedSetting == index);

        // Draw selection indicator
        if (isSelected) {
            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText(">>", screenWidth / 2 - 280, y + 10);
        }

        // Draw label
        gc.setFill(isSelected ? Color.YELLOW : Color.WHITE);
        gc.setFont(SETTING_FONT);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(label, screenWidth / 2 - 250, y);

        // Draw volume bar background
        double barX = screenWidth / 2 + 50;
        double barY = y - 20;
        double barWidth = 200;
        double barHeight = 20;

        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(barX, barY, barWidth, barHeight);

        // Draw volume bar fill
        gc.setFill(isSelected ? Color.LIGHTGREEN : Color.GREEN);
        gc.fillRect(barX, barY, barWidth * volume, barHeight);

        // Draw volume bar border
        gc.setStroke(isSelected ? Color.YELLOW : Color.WHITE);
        gc.setLineWidth(isSelected ? 3 : 2);
        gc.strokeRect(barX, barY, barWidth, barHeight);

        // Draw volume percentage
        gc.setFill(isSelected ? Color.YELLOW : Color.WHITE);
        gc.setFont(VALUE_FONT);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(String.format("%d%%", (int)(volume * 100)), barX + barWidth + 15, y);
    }

    /**
     * Resets selection to first setting.
     */
    @Override
    public void resetSelection() {
        selectedSetting = 0;
    }
}