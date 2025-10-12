package com.arkanoid.entities.bricks;

import com.arkanoid.powerups.*;
import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

/**
 * Constructor for NormalBricks.
 * String getImagePath(String) add imagePath base on color of the bricks.
 */
public class NormalBrick extends Brick {
    private String color;
    private Random rand = new Random();
    private float powerUpDropChance = 0.1f; // start with 10%
    private final float MAX_DROP_CHANCE = 0.9f;// maximum 90%

    public static final String RED = "red";
    public static final String YELLOW = "yellow";
    public static final String BLUE = "blue";

    public NormalBrick(double x, double y, double width, double height, String color) {
        super(x, y, width, height, Brick.NORMAL);
        this.color = color;

        setBrickHP(1);
        setObjectImage(setBrickstat(color));
    }

    /**
     * Add imagePath base on color of the bricks.
     * @param color color of the bricks.
     * @return string imagePath.
     */
    private String setBrickstat(String color) {
        if (color.equals(RED)) {
            setBrickScore(50);
            return "/images/bricks/normal_red_brick.png";
        } else if (color.equals(YELLOW)) {
            setBrickScore(60);
            return "/images/bricks/normal_yellow_brick.png";
        } else if (color.equals(BLUE)) {
            setBrickScore(70);
            return "/images/bricks/normal_blue_brick.png";
        } else {
            return "";
        }

    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * set drop chance.
     */
    public void resetDropChance() {
        powerUpDropChance = 0.1f;
    }

    /**
     * randomly drop powerup, the type powerup is random, add to powerupsList.
     * increase chance if last brick didnt drop powerup.
     * @param powerupmanager powerupManager with powerupsList.
     */
    public void dropPowerUp(PowerUpManager powerupmanager) {
        if (rand.nextFloat() < powerUpDropChance) {
            int type = rand.nextInt(3); //set up power
            if (type == PowerUp.EXPAND) {
                powerupmanager.addPowerUps(new ExpandPowerUp(getX(), getY()));
            } else if (type == PowerUp.SLOWBALL) {
                powerupmanager.addPowerUps(new SlowBallPowerUp(getX(), getY()));
            } else {
                powerupmanager.addPowerUps(new FastBallPowerUp(getX(), getY()));
            }
            resetDropChance();;
        } else {
            powerUpDropChance += 0.2f;
            if (powerUpDropChance > MAX_DROP_CHANCE) {
                powerUpDropChance = MAX_DROP_CHANCE;
            }
        }

    }

    @Override
    public void render(GraphicsContext gc) {
        gc.drawImage(getObjectImage(), getX(), getY(), getWidth(), getHeight());
    }
}
