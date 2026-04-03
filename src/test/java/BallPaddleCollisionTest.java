import com.arkanoid.entities.*;
import com.arkanoid.entities.balls.Ball;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class BallPaddleCollisionTest {
    private Ball ball;
    private Paddle paddle;
    private static final double DELTA = 0.1;

    // Parameters for direction test
    @Parameterized.Parameter(0)
    public double xOffset;

    @Parameterized.Parameter(1)
    public double expectedDirXSign;

    @Parameterized.Parameters(name = "xOffset={0}, expectedDirSign={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {0, -1.0},       // Left edge
                {37.5, -0.5},    // Left quarter
                {75, 0.0},       // Center
                {112.5, 0.5},    // Right quarter
                {150, 1.0}       // Right edge
        });
    }

    @Before
    public void setUp() {
        paddle = new Paddle();
        paddle.setX(325);
        paddle.setY(550);
        ball = new Ball();
        ball.setX(400);
        ball.setY(500);
    }

    @Test
    public void testCollisionDetectionFromAbove() {
        ball.setX(paddle.getX() + 50);
        ball.setY(paddle.getY() - ball.getHeight());
        assertTrue(ball.checkPaddleCollision(paddle));
    }

    @Test
    public void testNoCollisionWithGap() {
        ball.setX(paddle.getX() + 50);
        ball.setY(paddle.getY() - ball.getHeight() - 10);
        assertFalse(ball.checkPaddleCollision(paddle));
    }

    @Test
    public void testNoCollisionBesidePaddle() {
        ball.setX(paddle.getX() + paddle.getWidth() + 5);
        ball.setY(paddle.getY());
        assertFalse(ball.checkPaddleCollision(paddle));
    }

    @Test
    public void testCollisionAtLeftEdge() {
        ball.setX(paddle.getX() - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setDirectionY(1);
        assertTrue(ball.checkPaddleCollision(paddle));
    }

    @Test
    public void testCollisionAtRightEdge() {
        ball.setX(paddle.getX() + paddle.getWidth() - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setDirectionY(1);
        assertTrue(ball.checkPaddleCollision(paddle));
    }

    @Test
    public void testBallBouncesUp() {
        ball.setX(paddle.getX() + paddle.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setDirectionY(1);

        ball.bounceOffPaddle(paddle);
        assertTrue(ball.getDirectionY() < 0);
    }

    @Test
    public void testBallDirectionAfterCenterHit() {
        ball.setX(paddle.getX() + (paddle.getWidth() - ball.getWidth()) / 2);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setDirectionX(0);
        ball.setDirectionY(1);

        ball.bounceOffPaddle(paddle);

        assertTrue(Math.abs(ball.getDirectionX()) < 0.3);
        assertTrue(ball.getDirectionY() < 0);
    }

    // JUnit4 Parameterized test
    @Test
    public void testBallDirectionVariesWithPosition() {
        ball.setX(paddle.getX() + xOffset - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setDirectionY(1);

        ball.bounceOffPaddle(paddle);

        if (expectedDirXSign < -0.1) assertTrue(ball.getDirectionX() < 0);
        else if (expectedDirXSign > 0.1) assertTrue(ball.getDirectionX() > 0);
        else assertTrue(Math.abs(ball.getDirectionX()) < 0.3);

        assertTrue(ball.getDirectionY() < 0);
    }

    @Test
    public void testSpeedConstantAfterBounce() {
        double initialSpeed = ball.getSpeed();

        ball.setX(paddle.getX() + 50);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setDirectionX(0.5);
        ball.setDirectionY(1);

        ball.bounceOffPaddle(paddle);

        assertEquals(initialSpeed, ball.getSpeed(), DELTA);
    }

    @Test
    public void testBallPositionCorrectedAfterBounce() {
        ball.setX(paddle.getX() + 50);
        ball.setY(paddle.getY());
        ball.setDirectionY(1);

        ball.bounceOffPaddle(paddle);
        assertTrue(ball.getY() + ball.getHeight() <= paddle.getY() + 0.1);
    }

    @Test
    public void testMultipleCollisionsInSequence() {
        ball.setX(paddle.getX() + paddle.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight() - 50);
        ball.setDirectionX(0);
        ball.setDirectionY(1);

        for (int i = 0; i < 100; i++) {
            ball.update(0.016);
            if (ball.checkPaddleCollision(paddle)) {
                ball.bounceOffPaddle(paddle);
                break;
            }
        }

        assertTrue(ball.getDirectionY() < 0);
        ball.setDirectionY(1);
        ball.setY(paddle.getY() - ball.getHeight() - 50);

        for (int i = 0; i < 100; i++) {
            ball.update(0.016);
            if (ball.checkPaddleCollision(paddle)) {
                ball.bounceOffPaddle(paddle);
                break;
            }
        }

        assertTrue(ball.getDirectionY() < 0);
    }

    @Test
    public void testBallAndPaddleMovingSameDirection() {
        ball.setX(paddle.getX() + 50);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setDirectionX(1);
        ball.setDirectionY(1);

        paddle.moveRight(0.016);

        if (ball.checkPaddleCollision(paddle)) {
            ball.bounceOffPaddle(paddle);
        }

        assertTrue(ball.getDirectionY() < 0);
    }

    @Test
    public void testBallAndPaddleMovingOppositeDirections() {
        ball.setX(paddle.getX() + 50);
        ball.setY(paddle.getY() - ball.getHeight());
        ball.setDirectionX(-1);
        ball.setDirectionY(1);

        paddle.moveRight(0.016);

        if (ball.checkPaddleCollision(paddle)) {
            ball.bounceOffPaddle(paddle);
        }

        assertTrue(ball.getDirectionY() < 0);
    }

    @Test
    public void testFastMovingBallCollision() {
        ball.setSpeed(1000);
        ball.setX(paddle.getX() + 50);
        ball.setY(paddle.getY() - 100);
        ball.setDirectionX(0);
        ball.setDirectionY(1);

        boolean detected = false;

        for (int i = 0; i < 20; i++) {
            ball.update(0.016);
            if (ball.checkPaddleCollision(paddle)) {
                detected = true;
                ball.bounceOffPaddle(paddle);
                break;
            }
        }

        assertTrue(detected);
    }

    @Test
    public void testCornerCollision() {
        ball.setX(paddle.getX() - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight() / 2);
        ball.setDirectionX(1);
        ball.setDirectionY(1);

        boolean colliding = ball.checkPaddleCollision(paddle);

        if (colliding) {
            ball.bounceOffPaddle(paddle);
            assertTrue(ball.getDirectionY() < 0 || ball.getDirectionX() != 1);
        }
    }

    @Test
    public void testRealisticGameScenario() {
        ball.setDefaultBall(paddle);
        ball.setDirectionX(0);
        ball.setDirectionY(-1);

        for (int i = 0; i < 300; i++) {
            ball.update(0.016);
            paddle.update(0.016);

            if (ball.checkPaddleCollision(paddle)) {
                ball.bounceOffPaddle(paddle);
            }
            if (ball.isOutOfBounds()) break;
        }

        assertNotEquals(ball.getY(), paddle.getY() - ball.getHeight(), DELTA);
    }
}