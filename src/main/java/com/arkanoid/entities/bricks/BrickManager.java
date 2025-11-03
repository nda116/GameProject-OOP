package com.arkanoid.entities.bricks;


import javafx.animation.PauseTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import com.arkanoid.powerups.PowerUpManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import static com.arkanoid.Main.WINDOW_WIDTH;

/**
 * void addBrick(Bricks) add new brick to brickList.
 * void updateBrickHP(Ball) update bricks HP in brickList.
 * void explosionTrigger (Brick) Triggers a chain explosion starting from the specified brick.
 * void updateBrickList () update brickList, remove bricks which have HP <= 0.
 * void renderBrickList(GraphicsContext) render bricks from bricksList.
 */
public class BrickManager {
    private final ArrayList<Brick> bricksList = new ArrayList<>();
    private int totalScore;
    private int numberPowerUp;
    private int numberNormalBrick;
    private static final int BRICK_ROWS = 7;
    private static final int BRICK_COLS = 10;

    public int getTotalScore() {
        return totalScore;
    }

    public ArrayList<Brick> getBricksList() {
        return bricksList;
    }

    private void addBrick (Brick newBrick) {
        bricksList.add(newBrick);

        if (newBrick.getType() == Brick.NORMAL) {
            numberNormalBrick++;
        }

        totalScore += newBrick.getBrickScore();
    }

    /**
     * Create Brick map each level from file .txt.
     * @param resourcePath type String
     */
    public void createBricksFromFile(String resourcePath) {
        double brickWidth = WINDOW_WIDTH / 10.0;
        double brickHeight = brickWidth / 2;
        double offsetX = 0;
        double offsetY = 60;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(resourcePath)))) {
            String firstLine = br.readLine();
            if (firstLine != null) {
                try {
                    numberPowerUp = Integer.parseInt(firstLine.trim());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid number powerup: " + firstLine);
                }
            }

            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < BRICK_ROWS) {
                String[] tokens = line.trim().split("\\s+"); // separate by spaces

                for (int col = 0; col < tokens.length && col < BRICK_COLS; col++) {
                    String code = tokens[col];
                    double x = offsetX + col * brickWidth;
                    double y = offsetY + row * brickHeight;

                    switch (code) {
                        case "01":
                            addBrick(new NormalBrick(x, y, brickWidth, brickHeight, NormalBrick.YELLOW));
                            break;
                        case "02":
                            addBrick(new NormalBrick(x, y, brickWidth, brickHeight, NormalBrick.BLUE));
                            break;
                        case "03":
                            addBrick(new NormalBrick(x, y, brickWidth, brickHeight, NormalBrick.RED));
                            break;
                        case "04":
                            addBrick(new InvincibleBrick(x, y, brickWidth, brickHeight));
                            break;
                        case "05":
                            addBrick(new ExplosionBrick(x, y, brickWidth, brickHeight));
                            break;
                        case "06":
                            addBrick(new GlassBrick(x, y, brickWidth, brickHeight));
                            break;
                        case "00":
                        default:
                            break;
                    }
                }
                row++;
            }
        } catch (Exception e) {
            System.out.println("Can not find " + resourcePath);
        }
    }

    /**
     * update bricks HP in brickList.
     * minus HP if collision between ball and brick occur.
     * if brick type is EXPLOSION, minus HP of surround bricks.
     * @param brick brick to updateHP.
     */
    public void updateBrickHP(Brick brick) {
        brick.HPlost();

        if (brick.getType() == Brick.EXPLOSION) {
            ArrayList<Brick> startWave = new ArrayList<>();
            startWave.add(brick);
            explosionTrigger(startWave);
        }
    }

    /**
     * Triggers a chain explosion starting from the specified brick.
     * find Bricks surround explosive Brick and remove them at the same time.
     * @param currentWave ArrayList contains Bricks from current wave.
     */
    private void explosionTrigger(ArrayList<Brick> currentWave) {
        if (currentWave.isEmpty()) return;

        ArrayList<Brick> nextWave = new ArrayList<>();

        for (Brick current : currentWave) {
            for (Brick brick : bricksList) {
                if (brick.getBrickHP() > 0 && current.isAdjacent(brick)) {
                    brick.triggerFlash();
                    brick.HPlost();

                    if (brick.getType() == Brick.EXPLOSION && !nextWave.contains(brick)) {
                        nextWave.add(brick);
                    }
                }
            }
        }

        if (!nextWave.isEmpty()) {
            PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
            pause.setOnFinished(e -> explosionTrigger(nextWave));
            pause.play();
        }
    }

    /**
     * update brickList, remove bricks which have HP <= 0.
     * @param powerupmanager current powerupmanager.
     */
    public int updateBrickList(PowerUpManager powerupmanager) {
        Iterator<Brick> it = bricksList.iterator();
        int score = 0;

        while(it.hasNext()) {
            Brick brick = it.next();

            if (brick.getBrickHP() <= 0 && !brick.isFlashing()) {
                score += brick.getBrickScore();
                totalScore -= brick.getBrickScore();
                it.remove();

                if (brick.getType() == Brick.NORMAL) {
                    numberPowerUp = ((NormalBrick) brick).dropPowerUp(powerupmanager,
                            numberPowerUp, numberNormalBrick);
                    numberNormalBrick--;
                }
            }
        }
        return score;
    }

    /**
     * render bricks from bricksList.
     * @param gc GraphicContext.
     */
    public void renderBrickList(GraphicsContext gc) {
        //copy to avoid errors when bricksList changes while render
        ArrayList<Brick> copy = new ArrayList<>(bricksList);

        //render from copy
        for (Brick brick : copy) {
            brick.render(gc);
        }
    }

    public void recalculateTotalScore() {
        this.totalScore = 0;
        for (Brick b : bricksList) {
            if (!(b instanceof InvincibleBrick)) {
                this.totalScore += b.getBrickScore();
            }
        }
    }

}