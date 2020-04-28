package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Flag;
import inf112.skeleton.app.objects.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class PlayerTest {

    private Player player;
    private RallyGame game;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setupGame("assets/maps/Risky Exchange.tmx");
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
