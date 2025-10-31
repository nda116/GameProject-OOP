package com.arkanoid.saveload;

import java.util.List;

public class GameSaveData {
    public int score;
    public int lives;
    public int level;
    public double paddleX, paddleY;
    public List<BallData> balls;
    public List<BrickData> bricks;
    public List<PowerUpData> powerUps;
}

class BallData {
    public double x, y, vx, vy;
}

class BrickData {
    public int type;
    public boolean destroyed;
}

class PowerUpData {
    public String type;
    public double x, y;
    public boolean active;
}
