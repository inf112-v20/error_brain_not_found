package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.TileID;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class BoardTest {

    private Board board;
    private final int NUMBER_OF_PLAYERS_WHEN_STARTING_GAME = 2;
    private final int BOARD_WIDTH = 16;
    private final int BOARD_HEIGHT = 12;
    private Player player;
    private Random random;
    private ArrayList<Vector2> northWalls;
    private ArrayList<Vector2> southWalls;
    private ArrayList<Vector2> eastWalls;
    private ArrayList<Vector2> westWalls;

    @Before
    public void setUp() {
        //Mock OpenGL in order to use gdx.texture, gdx.tmxMapLoader etc without getting
        // nullpointerexception
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.board = new Board("assets/maps/Risky_Exchange.tmx", NUMBER_OF_PLAYERS_WHEN_STARTING_GAME);
        this.player = new Player(new Vector2(0,0), 1);
        random = new Random();
        northWalls = new ArrayList<>();
        southWalls = new ArrayList<>();
        eastWalls = new ArrayList<>();
        westWalls = new ArrayList<>();
        putPositionsToWallsInLists();
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
    public void aPlayerIsAddedToTheBoardIncrementPlayersTest() {
        board.addPlayer(player);
        assertEquals(NUMBER_OF_PLAYERS_WHEN_STARTING_GAME + 1, board.getPlayers().size());
    }

    @Test
    public void boardInitializedWithCorrectWidthTest() {
        assertEquals(BOARD_WIDTH, board.getWidth());
    }

    @Test
    public void boardInitializedWithCorrectHeightTest() {
        assertEquals(BOARD_HEIGHT, board.getHeight());
    }


    @Test
    public void playerIsOutsideOfUpperBorderTest() {
        player.setPosition(new Vector2(0, BOARD_HEIGHT));
        assertTrue(board.outsideBoard(player));
    }

    @Test
    public void playerIsOutsideOfRightBorderTest() {
        player.setPosition(new Vector2(BOARD_WIDTH, 0));
        assertTrue(board.outsideBoard(player));
    }

    @Test
    public void playerIsOutsideOfLeftBorderTest() {
        player.setPosition(new Vector2(-1, 0));
        assertTrue(board.outsideBoard(player));
    }

    @Test
    public void playerIsOutsideOfUnderBorderTest() {
        player.setPosition(new Vector2(0, -1));
        assertTrue(board.outsideBoard(player));
    }

    @Test
    public void playerOutsideBoardPlayerIsRespawnedTest() {
        Vector2 outsideOfBoardPosition = new Vector2(-1, 0);
        player.setPosition(outsideOfBoardPosition);
        board.addPlayer(player);
        assertEquals(player.getPosition(), player.getBackupPosition());
    }


    @Test
    public void playerHasMovedThenPlayerHasChangedCoordinatesTest() {
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
    public void playerIsOnCellWithNorthWallTest() {
        // Found position in Risky_Exchange.tmx, North Wall has ID 31
        player.setPosition(new Vector2(2, 0));
        TiledMapTileLayer wallLayer = board.getWallLayer();
        TiledMapTileLayer.Cell playerCell = wallLayer.getCell((int) player.getPosition().x, (int) player.getPosition().y);
        assertTrue(board.hasNorthWall(playerCell));
    }

    @Test
    public void playerIsOnCellWithWestWallTest() {
        // Found position in Risky_Exchange.tmx, SouthWest wall has ID 32
        player.setPosition(new Vector2(11, 7));
        TiledMapTileLayer wallLayer = board.getWallLayer();
        TiledMapTileLayer.Cell playerCell = wallLayer.getCell((int) player.getPosition().x, (int) player.getPosition().y);
        assertTrue(board.hasWestWall(playerCell));
    }

    @Test
    public void playerIsOnCellWithEastWallTestTest() {
        // Found position in Risky_Exchange.tmx, East Wall ha ID 23
        player.setPosition(new Vector2(3, 2));
        TiledMapTileLayer wallLayer = board.getWallLayer();
        TiledMapTileLayer.Cell playerCell = wallLayer.getCell((int) player.getPosition().x, (int) player.getPosition().y);
        assertTrue(board.hasEastWall(playerCell));
    }

    @Test
    public void playerFacingRandomNorthWallCanNotGoTest() {
        // Test for some random walls on board
        for (int i = 0; i < 5; i++) {
            assertFalse(board.canGo(getRandomNorthWallPosition(), Direction.NORTH));
        }
    }

    @Test
    public void playerFacingRandomEastWallCanNotGoTest() {
        // Test for some random walls on board
        for (int i = 0; i < 5; i++) {
            assertFalse(board.canGo(getRandomEastWallPosition(), Direction.EAST));
        }
    }

    @Test
    public void playerFacingRandomSouthWallCanNotGoTest() {
        // Test for some random walls on board
        for (int i = 0; i < 5; i++) {
            assertFalse(board.canGo(getRandomSouthWallPosition(), Direction.SOUTH));
        }
    }

    @Test
    public void playerFacingRandomWestWallCanNotGoTest() {
        // Test for some random walls on board
        for (int i = 0; i < 5; i++) {
            assertFalse(board.canGo(getRandomWestWallPosition(), Direction.WEST));
        }
    }

    @Test
    public void playerFacingRandomNorthWallDoesNotMoveTest() {
        // Test for some random walls
        for (int i = 0; i < 5; i++) {
            player.setPosition(getRandomNorthWallPosition());
            player.setDirection(Direction.NORTH);
            Vector2 posBefore = new Vector2((int) player.getPosition().x, (int) player.getPosition().y);
            board.movePlayer(player);
            assertEquals(posBefore, player.getPosition());
        }
    }

    @Test
    public void playerFacingRandomSouthWallDoesNotMoveTest() {
        // Test for some random walls
        for (int i = 0; i < 5; i++) {
            player.setPosition(getRandomSouthWallPosition());
            player.setDirection(Direction.SOUTH);
            Vector2 posBefore = new Vector2((int) player.getPosition().x, (int) player.getPosition().y);
            board.movePlayer(player);
            assertEquals(posBefore, player.getPosition());
        }
    }

    @Test
    public void playerFacingRandomEastWallDoesNotMoveTest() {
        // Test for several random walls
        for (int i = 0; i < 5; i++) {
            player.setPosition(getRandomEastWallPosition());
            player.setDirection(Direction.EAST);
            Vector2 posBefore = new Vector2((int) player.getPosition().x, (int) player.getPosition().y);
            board.movePlayer(player);
            assertEquals(posBefore, player.getPosition());
        }
    }

    @Test
    public void playerFacingRandomWestWallDoesNotMoveTest() {
        // Test for several random walls
        for (int i = 0; i < 5; i++) {
            player.setPosition(getRandomWestWallPosition());
            player.setDirection(Direction.WEST);
            Vector2 posBefore = new Vector2((int) player.getPosition().x, (int) player.getPosition().y);
            board.movePlayer(player);
            assertEquals(posBefore, player.getPosition());
        }
    }

    @Test
    public void playerIsNotFacingRandomNorthWallButOnSameTileAsWallThenPlayerCanMoveTest() {
        // Test for several random walls
        for (int i = 0; i < 5; i++) {
            player.setPosition(getRandomNorthWallPosition());
            player.setDirection(Direction.WEST);
            Vector2 posBefore = new Vector2((int) player.getPosition().x, (int) player.getPosition().y);
            board.movePlayer(player);
            assertNotEquals(posBefore, player.getPosition());
        }
    }

    @Test
    public void playerIsNotFacingRandomWestWallButOnSameTileAsWallThenPlayerCanMoveTest() {
        // Test for several random walls
        for (int i = 0; i < 5; i++) {
            player.setPosition(getRandomWestWallPosition());
            player.setDirection(Direction.EAST);
            Vector2 posBefore = new Vector2((int) player.getPosition().x, (int) player.getPosition().y);
            board.movePlayer(player);
            assertNotEquals(posBefore, player.getPosition());
        }
    }

    @Test
    public void playerIsNotFacingRandomEastWallButOnSameTileAsWallThenPlayerCanMoveTest() {
        // Test for several random walls
        for (int i = 0; i < 5; i++) {
            player.setPosition(getRandomEastWallPosition());
            player.setDirection(Direction.SOUTH);
            Vector2 posBefore = new Vector2((int) player.getPosition().x, (int) player.getPosition().y);
            board.movePlayer(player);
            assertNotEquals(posBefore, player.getPosition());
        }
    }

    @Test
    public void playerIsNotFacingRandomSouthWallButOnSameTileAsWallThenPlayerCanMoveTest() {
        // Test for several random walls
        for (int i = 0; i < 5; i++) {
            player.setPosition(getRandomSouthWallPosition());
            player.setDirection(Direction.NORTH);
            Vector2 posBefore = new Vector2((int) player.getPosition().x, (int) player.getPosition().y);
            board.movePlayer(player);
            assertNotEquals(posBefore, player.getPosition());
        }
    }

    /**
     * A player can be on tile with no walls but still
     * have a neighbouring tile with wall pointing against
     * player. So when a player tries to go on this tile, it should
     * be blocked even though it has no wall on the tile player is standing
     * on.
     *
     * Here the scenario is:
     *
     *    ----------
     *    |    |<-P|
     *    |        |
     *    |        |
     *    |________|
     *
     */
    @Test
    public void playerFacingRandomWallOnNeighbourCellCanNotMoveTest() {
        // Test for some random walls
        for (int i = 0; i < 5; i++) {
            Vector2 neighbourCellWithWall = getRandomEastWallPosition();
            int neighbourX = (int) neighbourCellWithWall.x;
            int neighbourY = (int) neighbourCellWithWall.y;
            // If wall is on border east side, player can not
            // get on the cell next to it
            while (neighbourX >= BOARD_WIDTH -1) {
                neighbourCellWithWall = getRandomEastWallPosition();
                neighbourX = (int) neighbourCellWithWall.x;
            }
            Vector2 playerPosition = new Vector2(neighbourX + 1, neighbourY);
            player.setPosition(playerPosition);
            player.setDirection(Direction.WEST);
            board.movePlayer(player);
            assertEquals(playerPosition, player.getPosition());
        }
    }

    /**
     * @param cell
     * @return true if cell only has north wall
     */
    private boolean isNorthWall(TiledMapTileLayer.Cell cell) {
        if (cell == null) {
            return false;
        }
        return cell.getTile().getId() == TileID.NORTH_WALL.getId();
    }

    /**
     * @param cell
     * @return true if cell only has south wall
     */
    private boolean isSouthWall(TiledMapTileLayer.Cell cell) {
        if (cell == null) {
            return false;
        }
        return cell.getTile().getId() == TileID.SOUTH_WALL.getId();
    }

    /**
     * @param cell
     * @return true if cell only has east wall
     */
    private boolean isEastWall(TiledMapTileLayer.Cell cell) {
        if (cell == null) {
            return false;
        }
        return cell.getTile().getId() == TileID.EAST_WALL.getId();
    }

    /**
     * @param cell
     * @return true if cell only has west wall
     */
    private boolean isWestWall(TiledMapTileLayer.Cell cell) {
        if (cell == null) {
            return false;
        }
        return cell.getTile().getId() == TileID.WEST_WALL.getId();
    }

    /**
     * Get the position of a random north wall so that player can be placed on this position.
     *
     * @return position of random north wall on board.
     */
    private Vector2 getRandomWestWallPosition() {
        int randomIndex = random.nextInt(westWalls.size());
        return westWalls.get(randomIndex);
    }

    /**
     * Get the position of a random north wall so that player can be placed on this position.
     *
     * @return position of random north wall on board.
     */
    private Vector2 getRandomNorthWallPosition() {
        int randomIndex = random.nextInt(northWalls.size());
        return northWalls.get(randomIndex);
    }

    /**
     * Get the position of a random south wall so that player can be placed on this position.
     *
     * @return position of random south wall on board.
     */
    private Vector2 getRandomSouthWallPosition() {
        int randomIndex = random.nextInt(southWalls.size());
        return southWalls.get(randomIndex);
    }

    /**
     * Get the position of a random east wall so that player can be placed on this position.
     *
     * @return position of random east wall on board.
     */
    private Vector2 getRandomEastWallPosition() {
        int randomIndex = random.nextInt(eastWalls.size());
        return eastWalls.get(randomIndex);
    }

    /**
     * Put all position to the walls on board in lists.
     */
    private void putPositionsToWallsInLists() {
        TiledMapTileLayer wallLayer = board.getWallLayer();
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                Vector2 pos = new Vector2(x, y);
                TiledMapTileLayer.Cell cell = wallLayer.getCell(x, y);
                System.out.print(cell);
                System.out.print(TileID.SOUTH_WALL.getId());
                if (isSouthWall(cell)) {
                    southWalls.add(pos);
                }
                if (isNorthWall(cell)) {
                    northWalls.add(pos);
                }
                if (isEastWall(cell)) {
                    eastWalls.add(pos);
                }
                if (isWestWall(cell)) {
                    westWalls.add(pos);
                }
            }
        }
    }

}
