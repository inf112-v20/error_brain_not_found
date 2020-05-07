package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Laser;
import inf112.skeleton.app.objects.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class LaserTest {

    private RallyGame game;
    private Board board;
    private ArrayList<Laser> lasers;
    private Player player;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setMapPath("assets/maps/Risky Exchange.tmx");
        this.game.setupGame();
        this.board = game.getBoard();
        this.lasers = board.getLasers();
        this.player = new Player(new Vector2(0,0), 5);
        board.addPlayer(player);
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
            Laser laser = lasers.get(0);
            Vector2 laserPosition = laser.getStartPosition();
            assertTrue(hasLaser(laserPosition));
        }
    }

    @Test
    public void playerBlockingLaserTest() {
        Laser laser = lasers.get(0);
        Vector2 blockingPosition = board.getNeighbourPosition(laser.getStartPosition(), laser.getDirection());
        player.setPosition(blockingPosition);
        game.fireLasers();
        Vector2 noLaserPosition = board.getNeighbourPosition(blockingPosition, laser.getDirection());
        assertFalse(hasLaser(noLaserPosition));
    }

    @Test
    public void playerTakesDamageWhenHitByLaserTest() {
        Laser laser = lasers.get(0);
        Vector2 hitPosition = board.getNeighbourPosition(laser.getStartPosition(), laser.getDirection());
        int damageBefore = player.getDamageTokens();
        player.setPosition(hitPosition);
        game.fireLasers();
        assertEquals(damageBefore+1, player.getDamageTokens());
    }

    @Test
    public void laserBlockedByAnotherPlayersDoesNotGiveDamageTest() {
        Laser laser = lasers.get(0);
        Vector2 hitPosition = board.getNeighbourPosition(laser.getStartPosition(), laser.getDirection());
        Player blockingPlayer = new Player(new Vector2(hitPosition), 6);
        board.addPlayer(blockingPlayer);
        Vector2 notHitPosition = board.getNeighbourPosition(hitPosition, laser.getDirection());
        player.setPosition(notHitPosition);
        int damageBefore = player.getDamageTokens();
        game.fireLasers();
        assertEquals(damageBefore, player.getDamageTokens());
    }

    @Test
    public void playerTakesDamageWhenOtherPlayerFiresAtPlayerTest() {
        Vector2 neighbourPosition = board.getNeighbourPosition(player.getPosition(), Direction.EAST);
        Player firePlayer = new Player(neighbourPosition, 6);
        firePlayer.setDirection(Direction.WEST);
        board.addPlayer(firePlayer);
        int damageBefore = player.getDamageTokens();
        game.firePlayerLaser();
        assertEquals(damageBefore+1, player.getDamageTokens());
    }

}
