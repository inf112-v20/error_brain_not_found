package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;


public class BoardTest {

    private Board board;
    private final int NUMBER_OF_PLAYERS_WHEN_STARTING_GAME = 2;

    //Make a headless application in order to initialize the board. Does not show.
    private HeadlessApplication app = new HeadlessApplication(new EmptyApplication());

    @Before
    public void setUp() {
        //Mock OpenGL
        Gdx.gl = mock(GL20.class);

        this.board = new Board("assets/Risky_Exchange.tmx");
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
}
