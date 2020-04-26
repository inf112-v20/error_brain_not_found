package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.objects.Laser;
import inf112.skeleton.app.objects.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class LaserTest {

    private RallyGame game;
    private Board board;
    private ArrayList<Laser> lasers;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setUpGameWithoutConnection("assets/maps/Risky Exchange.tmx", 0);
        this.board = game.getBoard();
        this.lasers = board.lasers;
    }

    /**
     * @param lasers on board
     * @return random laser laser from list given
     */
    private Laser getRandomLaser(ArrayList<Laser> lasers) {
        Random random = new Random();
        int randomIndex = random.nextInt(lasers.size());
        return lasers.get(randomIndex);
    }

    /**
     * @param position to check for laser
     * @return true if cell in this position from laserLayer has a laser on this cell.
     */
    private boolean hasLaser(Vector2 position) {
        TiledMapTileLayer laserLayer = board.getLaserLayer();
        TiledMapTileLayer.Cell cell = laserLayer.getCell((int) position.x, (int) position.y);
        return cell != null;
    }

    @Test
    public void thereAreLasersOnBoardTest() {
        assertFalse(lasers.isEmpty());
    }

    @Test
    public void lasersFiredHasLasersInStartPositionTest() {
        game.fireLasers();
        for (int i = 0; i < 3; i++) {
            Laser laser = getRandomLaser(lasers);
            Vector2 laserPosition = laser.getStartPosition();
            assertTrue(hasLaser(laserPosition));
        }
    }

    @Test
    public void playerBlockingLaserTest() {
        for (int i = 0; i < 3; i++) {
            Laser laser = getRandomLaser(lasers);
            Vector2 blockingPosition = board.getNeighbourPosition(laser.getStartPosition(), laser.getDirection());
            //TODO: Let RallyGame take in number of players as arg
            Player blockingPlayer = new Player(blockingPosition, 5);
            board.addPlayer(blockingPlayer);
            game.fireLasers();
            Vector2 noLaserPosition = board.getNeighbourPosition(blockingPosition, laser.getDirection());
            assertFalse(hasLaser(noLaserPosition));
        }
    }

}
