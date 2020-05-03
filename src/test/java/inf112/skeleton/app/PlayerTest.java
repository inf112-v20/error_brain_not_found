package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Flag;
import inf112.skeleton.app.objects.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
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
        Deck deck = new Deck();
        game.setDeck(deck.getDeck());
        Vector2 pos = new Vector2(0,0);
        player = new Player(pos, 1);
        game.dealCards();
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
        player.pickUpFlag(flag);
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

    @Test
    public void fiveDamagePointsLockLastCardTest() {
        for (int damage = 0; damage < 5; damage++) {
            player.handleDamage();
        }
        player.updateRegisters();
        assertFalse(player.getRegisters().getRegisters().get(4).isOpen());
    }

    @Test
    public void sixDamagePointsLockSecondLastCardTest() {
        for (int damage = 0; damage < 6; damage++) {
            player.handleDamage();
        }
        player.updateRegisters();
        assertFalse(player.getRegisters().getRegisters().get(3).isOpen());
    }

    @Test
    public void oneDamageTokensGivePlayerOneLessDealtCardTest() {
        player.handleDamage();
        game.dealCards();
        assertEquals(8, player.getProgramCardsDealt());
    }

    @Test
    public void twoDamageTokensGivePlayerTwoLessCardsTest() {
        player.handleDamage();
        player.handleDamage();
        game.dealCards();
        assertEquals(7, player.getProgramCardsDealt());
    }

}
