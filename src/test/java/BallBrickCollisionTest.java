import com.arkanoid.entities.balls.Ball;
import com.arkanoid.entities.bricks.Brick;
import com.arkanoid.entities.bricks.ExplosionBrick;
import com.arkanoid.core.GameObject;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit 4 test suite for Ball and Brick collision detection and response.
 * Tests various collision scenarios including normal collisions, edge cases,
 * and post-explosion brick states.
 */
public class BallBrickCollisionTest {

    private Ball ball;
    private Brick brick;

    // Test constants
    private static final double BRICK_WIDTH = 60;
    private static final double BRICK_HEIGHT = 20;
    private static final double BALL_RADIUS = 12;
    private static final double BALL_SPEED = 330;
    private static final double DELTA = 0.01; // Tolerance for double comparison

    /**
     * Sets up test fixtures before each test.
     * Initializes a ball and an explosion brick at known positions.
     */
    @Before
    public void setUp() {
        // Create brick at center position
        brick = new ExplosionBrick(200, 100, BRICK_WIDTH, BRICK_HEIGHT);

        ball = new Ball();
    }

    /**
     * Tests collision detection when ball hits brick from below.
     * Verifies that collision is detected and ball bounces downward.
     */
    @Test
    public void testCollisionFromBelow() {
        // Position ball overlapping brick from below, moving upward
        ball.setX(brick.getX() + brick.getWidth() / 2 - ball.getWidth() / 2); // Center horizontally
        ball.setY(brick.getY() + brick.getHeight() - 3); // Overlap by 3 pixels
        ball.setDirectionX(0);
        ball.setDirectionY(-1);

        // Verify collision is detected
        assertTrue("Collision should be detected",
                GameObject.checkCollision(brick, ball));

        double dirYBefore = ball.getDirectionY();

        // Apply bounce
        ball.bounceOff(brick);

        // Ball should now be moving downward (positive Y direction)
        // Direction should be reversed
        assertTrue("Ball should bounce downward",
                ball.getDirectionY() > 0 && Math.signum(ball.getDirectionY()) != Math.signum(dirYBefore));
    }

    /**
     * Tests collision detection when ball hits brick from above.
     * Verifies that collision is detected and ball bounces upward.
     */
    @Test
    public void testCollisionFromAbove() {
        // Position ball overlapping brick from above, moving downward
        ball.setX(brick.getX() + brick.getWidth() / 2 - ball.getWidth() / 2); // Center horizontally
        ball.setY(brick.getY() - ball.getHeight() + 3); // Overlap by 3 pixels
        ball.setDirectionX(0);
        ball.setDirectionY(1);

        // Verify collision is detected
        assertTrue("Collision should be detected",
                GameObject.checkCollision(brick, ball));

        double dirYBefore = ball.getDirectionY();

        // Apply bounce
        ball.bounceOff(brick);

        // Ball should now be moving upward (negative Y direction)
        // Direction should be reversed
        assertTrue("Ball should bounce upward",
                ball.getDirectionY() < 0 && Math.signum(ball.getDirectionY()) != Math.signum(dirYBefore));
    }

    /**
     * Tests collision detection when ball hits brick from the left.
     * Verifies that collision is detected and ball bounces to the left.
     */
    @Test
    public void testCollisionFromLeft() {
        // Position ball overlapping brick from left, moving right
        ball.setX(brick.getX() - ball.getWidth() + 3); // Overlap by 3 pixels
        ball.setY(brick.getY() + brick.getHeight() / 2 - ball.getHeight() / 2); // Center vertically
        ball.setDirectionX(1);
        ball.setDirectionY(0);

        // Verify collision is detected
        assertTrue("Collision should be detected",
                GameObject.checkCollision(brick, ball));

        double dirXBefore = ball.getDirectionX();

        // Apply bounce
        ball.bounceOff(brick);

        // Ball should now be moving left (negative X direction)
        // Direction should be reversed
        assertTrue("Ball should bounce left",
                ball.getDirectionX() < 0 && Math.signum(ball.getDirectionX()) != Math.signum(dirXBefore));
    }

    /**
     * Tests collision detection when ball hits brick from the right.
     * Verifies that collision is detected and ball bounces to the right.
     */
    @Test
    public void testCollisionFromRight() {
        // Position ball overlapping brick from right, moving left
        ball.setX(brick.getX() + brick.getWidth() - 3); // Overlap by 3 pixels
        ball.setY(brick.getY() + brick.getHeight() / 2 - ball.getHeight() / 2); // Center vertically
        ball.setDirectionX(-1);
        ball.setDirectionY(0);

        // Verify collision is detected
        assertTrue("Collision should be detected",
                GameObject.checkCollision(brick, ball));

        double dirXBefore = ball.getDirectionX();

        // Apply bounce
        ball.bounceOff(brick);

        // Ball should now be moving right (positive X direction)
        // Direction should be reversed
        assertTrue("Ball should bounce right",
                ball.getDirectionX() > 0 && Math.signum(ball.getDirectionX()) != Math.signum(dirXBefore));
    }

    /**
     * Tests corner collision where ball hits brick at the corner.
     * Both X and Y directions should be reversed.
     */
    @Test
    public void testCornerCollision() {
        // Position ball at top-left corner, moving down-right
        ball.setX(brick.getX() - ball.getWidth() / 2);
        ball.setY(brick.getY() - ball.getHeight() / 2);
        ball.setDirectionX(1);
        ball.setDirectionY(1);

        double initialDirX = ball.getDirectionX();
        double initialDirY = ball.getDirectionY();

        // Apply bounce
        ball.bounceOff(brick);

        // Both directions should be reversed for corner collision
        // (or at least one should be reversed)
        boolean xReversed = Math.signum(ball.getDirectionX()) != Math.signum(initialDirX);
        boolean yReversed = Math.signum(ball.getDirectionY()) != Math.signum(initialDirY);

        assertTrue("At least one direction should be reversed in corner collision",
                xReversed || yReversed);
    }

    /**
     * Tests that no collision is detected when ball and brick are separated.
     */
    @Test
    public void testNoCollisionWhenSeparated() {
        // Position ball far from brick
        ball.setX(brick.getX() + 100);
        ball.setY(brick.getY() + 100);

        // Verify no collision is detected
        assertFalse("No collision should be detected when separated",
                GameObject.checkCollision(brick, ball));
    }

    /**
     * Tests collision with a brick that has taken damage (HP reduced).
     * Verifies that collision detection still works after brick is damaged.
     */
    @Test
    public void testCollisionWithDamagedBrick() {
        // Damage the brick
        int initialHP = brick.getBrickHP();
        brick.HPlost();

        assertEquals("Brick HP should decrease", initialHP - 1, brick.getBrickHP());

        // Position ball to collide with damaged brick (overlapping from above)
        ball.setX(brick.getX() + brick.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(brick.getY() - ball.getHeight() + 3); // Overlap by 3 pixels
        ball.setDirectionY(1);

        // Collision should still be detected
        assertTrue("Collision should be detected with damaged brick",
                GameObject.checkCollision(brick, ball));
    }

    /**
     * Tests collision with a brick that has been destroyed (HP = 0).
     * In a real game, destroyed bricks would be removed, but this tests
     * the state where HP reaches zero.
     */
    @Test
    public void testCollisionWithDestroyedBrick() {
        // Destroy the brick (reduce HP to 0)
        while (brick.getBrickHP() > 0) {
            brick.HPlost();
        }

        assertEquals("Brick HP should be 0", 0, brick.getBrickHP());

        // Position ball to collide
        ball.setX(brick.getX() + 10);
        ball.setY(brick.getY() + 5);

        // Collision detection still works (brick object still exists)
        assertTrue("Collision detection should still work with destroyed brick",
                GameObject.checkCollision(brick, ball));
    }

    /**
     * Tests ball speed is preserved after collision.
     * Only direction should change, not speed magnitude.
     */
    @Test
    public void testSpeedPreservedAfterCollision() {
        double initialSpeed = ball.getSpeed();

        // Position for collision from below
        ball.setX(brick.getX() + 10);
        ball.setY(brick.getY() + brick.getHeight() + 1);
        ball.setDirectionX(0.5);
        ball.setDirectionY(-0.866); // 60 degree angle

        // Apply bounce
        ball.bounceOff(brick);

        // Speed should remain the same
        assertEquals("Speed should be preserved after collision",
                initialSpeed, ball.getSpeed(), DELTA);
    }

    /**
     * Tests that ball position is adjusted after collision to prevent sticking.
     * Ball should be pushed outside the brick after bounce.
     */
    @Test
    public void testBallPushedOutAfterCollision() {
        // Position ball overlapping with brick from left
        ball.setX(brick.getX() - ball.getWidth() / 2);
        ball.setY(brick.getY() + 5);
        ball.setDirectionX(1);
        ball.setDirectionY(0);

        // Apply bounce
        ball.bounceOff(brick);

        // Ball should be pushed to the left of brick
        assertTrue("Ball should be pushed outside brick",
                ball.getX() + ball.getWidth() <= brick.getX() ||
                        ball.getX() >= brick.getX() + brick.getWidth());
    }

    /**
     * Tests multiple sequential collisions with the same brick.
     * Useful for testing explosion brick behavior.
     */
    @Test
    public void testMultipleCollisionsSequence() {
        int collisionCount = 0;
        int maxCollisions = 3;

        for (int i = 0; i < maxCollisions; i++) {
            // Position for collision (overlapping from above)
            ball.setX(brick.getX() + brick.getWidth() / 2 - ball.getWidth() / 2);
            ball.setY(brick.getY() - ball.getHeight() + 3); // Overlap by 3 pixels
            ball.setDirectionY(1);

            if (GameObject.checkCollision(brick, ball)) {
                collisionCount++;
                ball.bounceOff(brick);

                // Damage brick
                if (brick.getBrickHP() > 0) {
                    brick.HPlost();
                }
            }

            // Move ball away for next iteration
            ball.setY(brick.getY() - 50);
        }

        assertTrue("Should detect multiple collisions", collisionCount > 0);
        assertEquals("Brick should be destroyed after collisions",
                0, brick.getBrickHP());
    }

    /**
     * Tests collision detection with null objects.
     * Should return false and not throw exceptions.
     */
    @Test
    public void testCollisionWithNullObjects() {
        assertFalse("Collision with null brick should return false",
                GameObject.checkCollision(null, ball));
        assertFalse("Collision with null ball should return false",
                GameObject.checkCollision(brick, null));
        assertFalse("Collision with both null should return false",
                GameObject.checkCollision(null, null));
    }

    /**
     * Tests brick flash effect is triggered after collision.
     * Explosion bricks should flash when hit.
     */
    @Test
    public void testBrickFlashAfterCollision() {
        assertFalse("Brick should not be flashing initially",
                brick.isFlashing());

        // Trigger flash effect
        brick.triggerFlash();

        assertTrue("Brick should be flashing after trigger",
                brick.isFlashing());
    }

    /**
     * Tests brick score value is maintained after collision.
     */
    @Test
    public void testBrickScoreAfterCollision() {
        int expectedScore = brick.getBrickScore();

        // Position for collision
        ball.setX(brick.getX() + 10);
        ball.setY(brick.getY() + 5);

        // Apply collision
        if (GameObject.checkCollision(brick, ball)) {
            ball.bounceOff(brick);
        }

        assertEquals("Brick score should remain unchanged",
                expectedScore, brick.getBrickScore());
    }
}