package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Laser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class LaserTest {

    private RallyGame game;
    private Board board;
    private Laser laser;
    private ArrayList<Laser> lasers;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setupGame("assets/maps/Risky_Exchange.tmx");
        this.board = game.getBoard();
        this.laser = new Laser(0,0, Direction.NORTH);
        this.lasers = board.lasers;
    }

    @Test
    public void thereAreLasersOnBoardTest() {
        assertFalse(lasers.isEmpty());
    }


}
