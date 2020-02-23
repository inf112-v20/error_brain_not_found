package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;


public class BoardTest {

    Board board;

    @Before
    public void setUp() {

        //Make a headless application in order to initialize the board. Does not show.
        //Mock OpenGL
        Gdx.gl = mock(GL20.class);
        new HeadlessApplication(new EmptyApplication());

        board = new Board("assets/Risky_Exchange.tmx");
    }


    @Test
    public void whenBoardIsInitializedMapIsNotNull() {
        assertNotNull(board.getMap());
    }
}
