package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.assertEquals;
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

    @Test
    public void lifeDecreasedWhenCollectedTenDamageTokensTest() {
        int livesBefore = player.getLifeTokens();
        for (int takeDamage = 1; takeDamage <= 10; takeDamage++) {
            player.handleDamage();
        }
        game.decreaseLives();
        assertEquals(livesBefore-1, player.getLifeTokens());
    }
}
