package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class GameTest {

    private RallyGame game;
    private Player player;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setupGame("assets/maps/Risky_Exchange.tmx");
        Board board = game.getBoard();

        // Already 4 players on board.
        //TODO: Let setupGame take in playerNumber as arg
        player = new Player(new Vector2(0, 0), 5);
        board.addPlayer(player);
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
        assertTrue(player.isInBackupState());
    }

    @Test
    public void deadPlayerAreRemovedFromGameTest() {
        // Kill player
        for (int livesTaken = 1; livesTaken <= 3; livesTaken++) {
            player.decrementLifeTokens();
        }
        game.removeDeadPlayers();
        assertFalse(game.players.contains(player));
    }
}
