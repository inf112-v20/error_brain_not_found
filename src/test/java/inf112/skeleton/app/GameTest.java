package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Belt;
import inf112.skeleton.app.objects.player.Player;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import static org.mockito.Mockito.mock;

public class GameTest {

    private RallyGame game;
    private Player player;
    private ArrayList<Belt> belts;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setupGame("assets/maps/Risky Exchange.tmx");
        Board board = game.getBoard();
        player = new Player(new Vector2(0, 0), 5);
        board.addPlayer(player);
        this.belts = board.getBelts();
    }

    /**
     * Give the player ten damage tokens.
     * @param player
     */
    private void fillUpDamageTokens(Player player) {
        for (int takeDamage = 1; takeDamage <= 10; takeDamage++) {
            player.handleDamage();
        }
    }

    @Test
    public void lifeDecreasedWhenCollectedTenDamageTokensTest() {
        int livesBefore = player.getLifeTokens();
        fillUpDamageTokens(player);
        game.decreaseLives();
        assertEquals(livesBefore-1, player.getLifeTokens());
    }

    @Test
    public void respawnIfTenCollectedDamageTokensTest() {
        // Move player out of backupposition
        game.getBoard().movePlayer(player);
        fillUpDamageTokens(player);
        game.decreaseLives();
        game.respawnPlayers();
        assertTrue(player.isInBackupState());
    }

    @Test
    public void deadPlayerAreRemovedFromGameTest() {
        // Kill player
        for (int livesTaken = 1; livesTaken <= 3; livesTaken++) {
            fillUpDamageTokens(player);
            player.decrementLifeTokens();
            game.decreaseLives();
        }
        game.respawnPlayers();
        assertFalse(game.players.contains(player));
    }

    @Test
    public void playerMovesOnBeltTest() {
        Belt belt = belts.get(0);
        Vector2 beltPosition = belt.getPosition();
        player.setPosition(beltPosition);
        game.activateBelts(false);
        assertNotEquals(beltPosition, player.getPosition());
    }

    @Test
    public void playerMovesInSameDirectionAsBeltTest() {
        Belt belt = belts.get(0);
        Vector2 beltPosition = belt.getPosition();
        Direction beltDirection = belt.getDirection();
        Vector2 newPosition = game.getBoard().getNeighbourPosition(beltPosition, beltDirection);
        player.setPosition(beltPosition);
        game.activateBelts(false);
        assertEquals(newPosition, player.getPosition());
    }

    /**
     * Place player at belt below the corner belt, move belts so player is at the corner belt.
     * The corner belt should rotate player from NORTH to WEST.
     */
    @Test
    public void playerChangesDirectionWhenInACornerOfTheBeltTest() {
        // Found postition in Risky Exhange
        Vector2 fromSouthToNorthBeltPosition = new Vector2(1, 1);
        player.setPosition(fromSouthToNorthBeltPosition);
        player.setDirection(Direction.NORTH);
        // Move player onto corner belt
        game.activateBelts(false);
        // Turn player with corner belt
        game.activateBelts(false);
        assertEquals(Direction.WEST, player.getDirection());
    }

    @Test
    public void moveOneStepWhenOnBeltTest() {
        // Found postition in Risky Exhange. Belt goes east.
        Vector2 startBeltPosition = new Vector2(5, 5);
        player.setPosition(startBeltPosition);
        game.activateBelts(false);
        game.activateBelts(true);
        Vector2 beltMovedToPosition = new Vector2(6, 5);
        assertEquals(beltMovedToPosition, player.getPosition());
    }

    @Test
    public void moveTwoStepOnExpressBeltTest() {
        // Found position in Risky Exhange. Belt goes west
        Vector2 startBeltPosition = new Vector2(8, 6);
        player.setPosition(startBeltPosition);
        game.activateBelts(false);
        game.activateBelts(true);
        Vector2 beltMovedToPosition = new Vector2(6, 6);
        assertEquals(beltMovedToPosition, player.getPosition());
    }

}
