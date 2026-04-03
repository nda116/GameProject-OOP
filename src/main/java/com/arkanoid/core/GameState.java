package com.arkanoid.core;

/**
 * Enum representing all possible game states including menus.
 */
public enum GameState {
    /** AppLauncher menu state */
    MENU,

    /** Ready to start playing */
    READY,

    /** Currently playing */
    PLAYING,

    /** Game is paused */
    PAUSED,

    /** Game over state */
    GAME_OVER,

    /** Level completed */
    LEVEL_COMPLETE,

    HIGH_SCORES,

    SETTINGS
}