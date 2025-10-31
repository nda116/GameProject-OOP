package com.arkanoid.core;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class to manage all game sounds.
 * Handles background music and sound effects.
 */
public class SoundManager {

    private static SoundManager instance;

    // Sound effect types
    public enum Sound {
        PADDLE_HIT,
        BRICK_BREAK,
        EXPLOSION_BRICK,
        GLASS_BRICK,
        POWER_UP,
        GAME_OVER,
        LEVEL_COMPLETE,
        LOSE_LIFE,
        BUTTON
    }

    // Storage for sound effects
    private Map<Sound, MediaPlayer> soundEffects;

    // Background music player
    private MediaPlayer backgroundMusic;

    // Menu music player
    private MediaPlayer menuMusic;

    // Volume controls
    private double masterVolume = 1.0;
    private double musicVolume = 0.5;
    private double sfxVolume = 0.7;

    private boolean soundEnabled = true;
    private boolean musicEnabled = true;

    /**
     * Private constructor for Singleton pattern.
     */
    private SoundManager() {
        soundEffects = new HashMap<>();
        loadSounds();
    }

    /**
     * Gets the singleton instance of SoundManager.
     * @return the SoundManager instance
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Loads all sound files.
     */
    private void loadSounds() {
        try {
            // Load sound effects
            loadSoundEffect(Sound.PADDLE_HIT, "/sounds/paddle_hit.mp3");
            loadSoundEffect(Sound.BRICK_BREAK, "/sounds/brick_break.mp3");
            loadSoundEffect(Sound.EXPLOSION_BRICK, "/sounds/explosion_brick.mp3");
            loadSoundEffect(Sound.GLASS_BRICK, "/sounds/glass_brick.mp3");
            loadSoundEffect(Sound.POWER_UP, "/sounds/powerup.mp3");
            loadSoundEffect(Sound.GAME_OVER, "/sounds/game_over.mp3");
            loadSoundEffect(Sound.LEVEL_COMPLETE, "/sounds/level_complete.mp3");
            loadSoundEffect(Sound.LOSE_LIFE, "/sounds/lose_life.mp3");
            loadSoundEffect(Sound.BUTTON, "/sounds/button.wav");

            // Load background music
            loadBackgroundMusic("/sounds/background_music.mp3");

            // Load menu music
            loadMenuMusic("/sounds/menu.mp3");

        } catch (Exception e) {
            System.err.println("Error loading sounds: " + e.getMessage());
        }
    }

    /**
     * Loads a sound effect from file.
     * @param sound the sound type
     * @param filepath path to the sound file
     */
    private void loadSoundEffect(Sound sound, String filepath) {
        try {
            URL resource = getClass().getResource(filepath);
            if (resource == null) {
                System.err.println("Could not find sound file: " + filepath);
                return;
            }

            Media media = new Media(resource.toString());
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(sfxVolume * masterVolume);

            soundEffects.put(sound, player);
        } catch (Exception e) {
            System.err.println("Error loading sound effect " + sound + ": " + e.getMessage());
        }
    }

    /**
     * Loads background music from file.
     * @param filepath path to the music file
     */
    private void loadBackgroundMusic(String filepath) {
        try {
            URL resource = getClass().getResource(filepath);
            if (resource == null) {
                System.err.println("Could not find music file: " + filepath);
                return;
            }

            Media media = new Media(resource.toString());
            backgroundMusic = new MediaPlayer(media);
            backgroundMusic.setVolume(musicVolume * masterVolume);
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever

        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }

    /**
     * Loads menu music from file.
     * @param filepath path to the music file
     */
    private void loadMenuMusic(String filepath) {
        try {
            URL resource = getClass().getResource(filepath);
            if (resource == null) {
                System.err.println("Could not find music file: " + filepath);
                return;
            }

            Media media = new Media(resource.toString());
            menuMusic = new MediaPlayer(media);
            menuMusic.setVolume(musicVolume * masterVolume);
            menuMusic.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever

        } catch (Exception e) {
            System.err.println("Error loading menu music: " + e.getMessage());
        }
    }

    /**
     * Plays a sound effect.
     * @param sound the sound to play
     */
    public void playSound(Sound sound) {
        if (!soundEnabled) return;

        MediaPlayer player = soundEffects.get(sound);
        if (player != null) {
            // Reset to start and play
            player.stop();
            player.play();
        }
    }

    /**
     * Plays background music.
     */
    public void playBackgroundMusic() {
        if (!musicEnabled || backgroundMusic == null) return;

        backgroundMusic.play();
    }

    /**
     * Plays menu music.
     */
    public void playMenuMusic() {
        if (!musicEnabled || menuMusic == null) return;

        menuMusic.play();
    }

    /**
     * Stops background music.
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    /**
     * Stops background music.
     */
    public void stopMenuMusic() {
        if (menuMusic != null) {
            menuMusic.stop();
        }
    }

    /**
     * Pauses background music.
     */
    public void pauseBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }

    /**
     * Resumes background music.
     */
    public void resumeBackgroundMusic() {
        if (musicEnabled && backgroundMusic != null) {
            backgroundMusic.play();
        }
    }

    /**
     * Sets master volume (0.0 to 1.0).
     * @param volume the volume level
     */
    public void setMasterVolume(double volume) {
        this.masterVolume = Math.max(0.0, Math.min(1.0, volume));
        updateAllVolumes();
    }

    /**
     * Sets music volume (0.0 to 1.0).
     * @param volume the volume level
     */
    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(musicVolume * masterVolume);
        }
    }

    /**
     * Sets sound effects volume (0.0 to 1.0).
     * @param volume the volume level
     */
    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0.0, Math.min(1.0, volume));
        for (MediaPlayer player : soundEffects.values()) {
            player.setVolume(sfxVolume * masterVolume);
        }
    }

    /**
     * Updates all volume levels.
     */
    private void updateAllVolumes() {
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(musicVolume * masterVolume);
        }

        for (MediaPlayer player : soundEffects.values()) {
            player.setVolume(sfxVolume * masterVolume);
        }
    }

    /**
     * Enables or disables sound effects.
     * @param enabled true to enable, false to disable
     */
    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    /**
     * Enables or disables background music.
     * @param enabled true to enable, false to disable
     */
    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;

        if (enabled) {
            playBackgroundMusic();
        } else {
            stopBackgroundMusic();
        }
    }

    /**
     * Toggles sound effects on/off.
     */
    public void toggleSound() {
        soundEnabled = !soundEnabled;
    }

    /**
     * Toggles music on/off.
     */
    public void toggleMusic() {
        setMusicEnabled(!musicEnabled);
    }

    /**
     * Cleans up resources.
     */
    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }

        for (MediaPlayer player : soundEffects.values()) {
            player.stop();
            player.dispose();
        }

        soundEffects.clear();
    }

    // Getters
    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public double getMasterVolume() {
        return masterVolume;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public double getSfxVolume() {
        return sfxVolume;
    }
}