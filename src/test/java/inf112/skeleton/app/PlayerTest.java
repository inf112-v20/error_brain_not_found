package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Flag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        Vector2 pos = new Vector2(0,0);
        player = new Player(pos, 1);
    }

    @Test
    public void positionGivenToPlayerIsSameAsPlayerGivesTest() {
        assertEquals(0, player.getPosition().x, 0.01);

    }

    @Test
    public void whenNewXCoordinateIsGivenPlayersCoordinatesChangedTest() {
        player.setPosition(new Vector2(1,0));
        assertEquals(1, player.getPosition().x, 0.01);
    }

    @Test
    public void whenNewYCoordinateIsGivenPlayersCoordinatesChangedTest() {
        player.setPosition(new Vector2(0, 1));
        assertEquals(1, player.getPosition().y, 0.01);
    }

    @Test
    public void backupDirectionIsEastTest() {
        assertEquals(Direction.EAST, player.getDirection());
    }

    @Test
    public void whenPlayersStartDirectionIsTurnedLeftItGivesNorthDirectionTest() {
        assertEquals(Direction.NORTH, player.getDirection().turnLeft());
    }

    @Test
    public void whenPlayersStartDirectionIsTurnedRightItGivesSouthDirectionTest() {
        assertEquals(Direction.SOUTH, player.getDirection().turnRight());
    }

    @Test
    public void changingPositionDoesNotChangeBackupTest() {
        Vector2 backupPosition = player.getBackupPosition();
        player.setPosition(new Vector2(1, 0));
        assertEquals(backupPosition, player.getBackupPosition());
    }

    @Test
    public void backupPositionSameAsStartPositionTest() {
        assertEquals(player.getBackupPosition(), player.getPosition());
    }

    @Test
    public void playerPickedUpOneFlagDoesNotHaveAllFlagsTest() {
        Flag flag = new Flag(1, 0,0);
        player.pickUpFlag(flag, flag.getFlagnr());
        int numberOfFlags = 3;
        assertFalse(player.hasAllFlags(numberOfFlags));
    }

    @Test
    public void tryingToPickUpFlagWhenNotStandingOnFlagDoesNotWorkTest() {
        Flag flag = new Flag(1, 1,0);
        player.pickUpFlag(flag, flag.getFlagnr());

    }

    @Test
    public void startWithNoDamageTokensTest() {
        assertEquals(0, player.getDamageTokens());
    }

    @Test
    public void startWithThreeLivesTest() {
        assertEquals(3, player.getLifeTokens());
    }

    @Test
    public void takeDamageIncreasesDamageTokenTest() {
        int numberTokensBefore = player.getDamageTokens();
        player.handleDamage();
        assertEquals(numberTokensBefore+1, player.getDamageTokens());
    }

    @Test
    public void noLivesLeftPlayerIsDeadTest() {
        for (int livesTaken = 1; livesTaken <=3; livesTaken++) {
            player.decrementLifeTokens();
        }
        assertTrue(player.isDead());
    }

}
