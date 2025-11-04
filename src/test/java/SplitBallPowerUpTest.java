import com.arkanoid.entities.Paddle;
import com.arkanoid.entities.balls.Ball;
import com.arkanoid.entities.balls.BallManager;
import com.arkanoid.entities.bullets.BulletManager;
import com.arkanoid.powerups.SplitBallPowerUp;
import org.junit.Test;

import static org.junit.Assert.*;

public class SplitBallPowerUpTest {
    // Init objects to test.
    Paddle paddle = new Paddle();
    BallManager ballManager = new BallManager();
    BulletManager bulletManager = new BulletManager();
    SplitBallPowerUp testSplitBallPowerUp = new SplitBallPowerUp(0, 0);


    @Test
    public void testSplitBall1 () {
        ballManager.addBall(new Ball());

        // set direction of ball.
        Ball first = ballManager.getBallsList().get(0);
        first.setDirectionX(Math.sqrt(3) / 2);
        first.setDirectionY((double) 1 / 2);

        // calculate direction of new ball.
        testSplitBallPowerUp.applyEffect(paddle, ballManager, bulletManager);
        Ball second = ballManager.getBallsList().get(1);

        // check result.
        assertEquals(1, second.getDirectionX(), 0.001);
        assertEquals(0, second.getDirectionY(), 0.001);
        assertEquals(1.00 / 2, first.getDirectionX(), 0.001);
        assertEquals(Math.sqrt(3) / 2, first.getDirectionY(), 0.001);
    }

    @Test
    public void testSplitBall2 () {
        ballManager.addBall(new Ball());
        double resultX = (Math.sqrt(6) + Math.sqrt(2)) / 4;
        double resultY = (Math.sqrt(6) - Math.sqrt(2)) / 4;

        // set direction of ball.
        Ball first = ballManager.getBallsList().get(0);
        first.setDirectionX(Math.cos(Math.toRadians(15)));
        first.setDirectionY(Math.sin(Math.toRadians(15)));

        // calculate direction of new ball and change old ball direction.
        testSplitBallPowerUp.applyEffect(paddle, ballManager, bulletManager);
        Ball second = ballManager.getBallsList().get(1);

        // check result.
        assertEquals(resultX, second.getDirectionX(), 0.001);
        assertEquals(- resultY, second.getDirectionY(), 0.001);
        assertEquals(1 / Math.sqrt(2), first.getDirectionX(), 0.001);
        assertEquals(1 / Math.sqrt(2), first.getDirectionY(), 0.001);
    }

    @Test
    public void testSplitBall3 () {
        ballManager.addBall(new Ball());

        // set direction of ball.
        Ball first = ballManager.getBallsList().get(0);
        first.setDirectionX((double) -1 / 2);
        first.setDirectionY(Math.sqrt(3) / 2 );

        // calculate direction of new ball.
        testSplitBallPowerUp.applyEffect(paddle, ballManager, bulletManager);
        Ball second = ballManager.getBallsList().get(1);

        // check result.
        assertEquals(0, second.getDirectionX(), 0.001);
        assertEquals(1, second.getDirectionY(), 0.001);
        assertEquals(-Math.sqrt(3) / 2, first.getDirectionX(), 0.001);
        assertEquals(1.00 / 2, first.getDirectionY(), 0.001);
    }
}
