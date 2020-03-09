package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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

    @Before
    public void setUp() {
        //Mock OpenGL in order to use gdx.texture, gdx.tmxMapLoader etc without getting
        // nullpointerexception
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.board = new Board("assets/maps/Risky_Exchange.tmx", NUMBER_OF_PLAYERS_WHEN_STARTING_GAME);
        this.player = new Player(new Vector2(0,0), 1);
    }


    @Test
    public void whenBoardIsInitializedMapIsNotNullTest() {
        assertNotNull(board.getMap());
    }

    @Test
    public void whenBoardIsInitializedItHasCorrectNumberOfPlayersTest() {
        assertEquals(NUMBER_OF_PLAYERS_WHEN_STARTING_GAME, board.getPlayers().size());
    }

    @Test
    public void whenAPlayerIsAddedTheBoardHasIncrementedPlayersByOneTest() {
        board.addPlayer(player);
        assertEquals(NUMBER_OF_PLAYERS_WHEN_STARTING_GAME + 1, board.getPlayers().size());
    }

    @Test
    public void whenBoardIsInitBoardWidthIsTheSameAsExpectedTest() {
        assertEquals(BOARD_WIDTH, board.getWidth());
    }

    @Test
    public void whenBoardIsInitBoardHeightIsTheSameAsExpectedTest() {
        assertEquals(BOARD_HEIGHT, board.getHeight());
    }


    @Test
    public void whenPlayerIsOutsideOnTopOfBoardItIsDetectedTest() {
        player.setPosition(new Vector2(0, BOARD_HEIGHT));
        assertTrue(board.outsideBoard(player));
    }

    @Test
    public void whenPlayerIsOutsideOnRightSideOfBoardItIsDetectedTest() {
        player.setPosition(new Vector2(BOARD_WIDTH, 0));
        assertTrue(board.outsideBoard(player));
    }

    @Test
    public void whenPlayerIsOutsideOnLeftSideOfBoardItIsDetectedTest() {
        player.setPosition(new Vector2(-1, 0));
        assertTrue(board.outsideBoard(player));
    }

    @Test
    public void whenPlayerIsOutsideUnderTheBoardItIsDetectedTest() {
        player.setPosition(new Vector2(0, -1));
        assertTrue(board.outsideBoard(player));
    }

    @Test
    public void whenPlayerIsOutsideOfBoardPlayerIsRespawnedTest() {
        Vector2 outsideOfBoardPosition = new Vector2(-1, 0);
        player.setPosition(outsideOfBoardPosition);
        board.addPlayer(player);
        assertEquals(player.getPosition(), player.getBackupPosition());
    }


    @Test
    public void whenPlayerIsMovedPlayerHasChangedCoordinatesTest() {
        Vector2 startPosition = new Vector2(player.getPosition().x, player.getPosition().y);
        player.setDirection(Direction.EAST);
        board.movePlayer(player);
        assertNotEquals(startPosition, player.getPosition());
    }

    @Test
    public void whenPlayerIsMovedUpItHasMovedOneStepTest() {
        Vector2 startPosition = new Vector2(player.getPosition().x, player.getPosition().y);
        player.setDirection(Direction.NORTH);
        board.movePlayer(player);
        assertEquals((int) startPosition.y+1, (int) player.getPosition().y);
    }

    @Test
    public void whenPlayerIsOnCellWithNorthWallHasNorthWallShouldBeTrueTest() {
        // Found position in Risky_Exchange.tmx, North Wall has ID 31
        player.setPosition(new Vector2(2, 0));
        TiledMapTileLayer wallLayer = board.getWallLayer();
        TiledMapTileLayer.Cell playerCell = wallLayer.getCell((int) player.getPosition().x, (int) player.getPosition().y);
        assertTrue(board.hasNorthWall(playerCell));
    }

    @Test
    public void whenPlayerIsOnCellWithSouthWestWallHasSouthWallShouldBeTrueTest() {
        // Found position in Risky_Exchange.tmx, SouthWest wall has ID 32
        player.setPosition(new Vector2(11, 7));
        TiledMapTileLayer wallLayer = board.getWallLayer();
        TiledMapTileLayer.Cell playerCell = wallLayer.getCell((int) player.getPosition().x, (int) player.getPosition().y);
        assertTrue(board.hasWestWall(playerCell));
    }

    @Test
    public void whenPlayerIsOnCellWithEastWallHasEastWallShouldBeTrueTest() {
        // Found position in Risky_Exchange.tmx, East Wall ha ID 23
        player.setPosition(new Vector2(3, 2));
        TiledMapTileLayer wallLayer = board.getWallLayer();
        TiledMapTileLayer.Cell playerCell = wallLayer.getCell((int) player.getPosition().x, (int) player.getPosition().y);
        assertTrue(board.hasEastWall(playerCell));
    }

    @Test
    public void whenGivenPositionWithNorthWallAndNorthDirectionCanGoReturnsFalseTest() {
        // Found position in Risky_Exchange.tmx, North Wall has ID 31
        Vector2 tileWithNorthWallPosition = new Vector2(2, 0);
        assertFalse(board.canGo(tileWithNorthWallPosition, Direction.NORTH));
    }


}
