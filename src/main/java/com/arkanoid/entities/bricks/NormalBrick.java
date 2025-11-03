package com.arkanoid.entities.bricks;

import com.arkanoid.powerups.*;

import java.util.Random;

/**
 * Constructor for NormalBricks.
 * String getImagePath(String) add imagePath base on color of the bricks.
 */
public class NormalBrick extends Brick {
    private String color;
    private Random rand = new Random();

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

    /**
     * randomly drop powerup, the type powerup is random, add to powerupsList.
     * increase chance if last brick didnt drop powerup.
     * @param powerupmanager powerupManager with powerupsList.
     * @param numberNormalBrick Normal Brick in level left.
     * @param numberPowerUp PowerUp in level left.
     */
    public int dropPowerUp(PowerUpManager powerupmanager, int numberPowerUp,
                           int numberNormalBrick) {
        double powerUpDropChance = (double) numberPowerUp / numberNormalBrick;

        if (rand.nextFloat() < powerUpDropChance) {
            int type;
            boolean paddlePowerUpInList = false;
            for (PowerUp powerUp : powerupmanager.getPowerupList()) {
                if (powerUp.getType() == PowerUp.EXPAND || powerUp.getType() == PowerUp.FIREBULLET) {
                    paddlePowerUpInList = true;
                    break;
                }
            }
            if (paddlePowerUpInList) {  // if paddle or list have powerup already, drop other powerup
                type = rand.nextInt(3);
            } else {
                type = rand.nextInt(5);
            }
            //set up power
            if (type == PowerUp.SPLITBALL) {
                powerupmanager.addPowerUps(new SplitBallPowerUp(getX(), getY()));
            } else if (type == PowerUp.SLOWBALL) {
                powerupmanager.addPowerUps(new SlowBallPowerUp(getX(), getY()));
            } else if (type == PowerUp.FASTBALL){
                powerupmanager.addPowerUps(new FastBallPowerUp(getX(), getY()));
            } else if (type == PowerUp.FIREBULLET){
                powerupmanager.addPowerUps(new FireBulletsPowerUp(getX(), getY()));
            } else {
                powerupmanager.addPowerUps(new ExpandPowerUp(getX(), getY()));
            }
            numberPowerUp--;
        }

        return numberPowerUp;
    }

    public String getColorType() {
        return color;
    }
}