package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class BoardTest {

    private Board board;
    private final int NUMBER_OF_PLAYERS_WHEN_STARTING_GAME = 2;
    private final int BOARD_WIDTH = 16;
    private final int BOARD_HEIGHT = 12;
    private Player player;

    //Make a headless application in order to initialize the board. Does not show.
    private HeadlessApplication app = new HeadlessApplication(new EmptyApplication());

    @Before
    public void setUp() {
        //Mock OpenGL in order to use gdx.texture, gdx.tmxMapLoader etc without getting
        // nullpointerexception
        Gdx.gl = mock(GL20.class);

        this.board = new Board("assets/Risky_Exchange.tmx");
        this.player = new Player(new Vector2(0,0));
    }


    @Test
    public void whenBoardIsInitializedMapIsNotNull() {
        assertNotNull(board.getMap());
    }

    @Test
    public void whenBoardIsInitializedItHasCorrectNumberOfPlayers() {
        assertEquals(NUMBER_OF_PLAYERS_WHEN_STARTING_GAME, board.getPlayers().size());
    }

    @Test
    public void whenAPlayerIsAddedTheBoardHasIncrementedPlayersByOne() {
        board.addPlayer(0,0);
        assertEquals(NUMBER_OF_PLAYERS_WHEN_STARTING_GAME + 1, board.getPlayers().size());
    }

    @Test
    public void whenBoardIsInitBoardWidthIsTheSameAsExpected() {
        assertEquals(BOARD_WIDTH, board.getWidth());
    }

    @Test
    public void whenBoardIsInitBoardHeightIsTheSameAsExpected() {
        assertEquals(BOARD_HEIGHT, board.getHeight());
    }

    /**
     * Test the check if a player is outside the board after a move, so
     * it can be respawned.
      */
    @Test
    public void whenPlayerIsOutsideOnTopOfBoardItIsDetected() {
        player.setPosition(new Vector2(0, BOARD_HEIGHT-1));
        player.setDirection(Direction.NORTH);
        board.movePlayer(player);
        assertTrue(board.playerIsOutsideBoard(player));
    }
}
