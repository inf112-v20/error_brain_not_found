package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
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
    private ArrayList<Vector2> holes;
    private Vector2 startPosition;

    @Before
    public void setUp() {
        //Mock OpenGL in order to use gdx.texture, gdx.tmxMapLoader etc without getting
        // nullpointerexception
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.board = new Board("assets/maps/Risky_Exchange.tmx", NUMBER_OF_PLAYERS_WHEN_STARTING_GAME);
        // Random position
        this.startPosition = new Vector2(5,5);
        this.player = new Player(startPosition, 1);
        this.holes = board.holes;
    }

    /**
     *
     * @return a random hole position
     */
    private Vector2 getRandomHolePosition() {
        Random random = new Random();
        int randomIndex = random.nextInt(holes.size());
        return holes.get(randomIndex);
    }

    /**
     *
     * @return true if player is on backupPosition and has backupDirection
     */
    private boolean isInBackupState(Player player) {
        return player.getPosition().equals(player.getBackupPosition()) && player.getDirection().equals(player.getBackupDirection());
    }

    @Test
    public void boardHasAMapTest() {
        assertNotNull(board.getMap());
    }

    @Test
    public void correctNumbersOfPlayersOnBoardTest() {
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
    public void playerIsUnderBorderTest() {
        player.setPosition(new Vector2(0, -1));
        assertTrue(board.outsideBoard(player));
    }

    @Test
    public void playerOutsideBoardIsRespawnedTest() {
        Vector2 outsideOfBoardPosition = new Vector2(-1, 0);
        player.setPosition(outsideOfBoardPosition);
        board.addPlayer(player);
        assertTrue(isInBackupState(player));
    }


    @Test
    public void movedPlayerHasChangedCoordinatesTest() {
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
    public void thereAreHolesOnBoardTest() {
        assertFalse(holes.isEmpty());
    }

    @Test
    public void playerOnRandomHoleIsOutsideBoardTest() {
        // Choose some random holes
        for (int i = 0; i < 5; i++) {
            Vector2 holePosition = getRandomHolePosition();
            player.setPosition(holePosition);
            assertTrue(board.outsideBoard(player));
        }
    }
    @Test
    public void playerOnRandomHoleIsRespawnedTest() {
        // Choose some random holes
        for (int i = 0; i < 5; i++) {
            Vector2 holePosition = getRandomHolePosition();
            player.setPosition(holePosition);
            board.addPlayer(player);
            assertTrue(isInBackupState(player));
        }
    }

    @Test
    public void getWestNeighbourPositionTest() {
        Vector2 neighbourPosition = new Vector2(startPosition.x -1, startPosition.y);
        assertEquals(neighbourPosition, board.getNeighbourPosition(startPosition, Direction.WEST));
    }

    @Test
    public void getEastNeighbourPositionTest() {
        Vector2 neighbourPosition = new Vector2(startPosition.x +1, startPosition.y);
        assertEquals(neighbourPosition, board.getNeighbourPosition(startPosition, Direction.EAST));
    }

    @Test
    public void getSouthNeighbourPositionTest() {
        Vector2 neighbourPosition = new Vector2(startPosition.x, startPosition.y - 1);
        assertEquals(neighbourPosition, board.getNeighbourPosition(startPosition, Direction.SOUTH));
    }

    @Test
    public void getNorthNeighbourPositionTest() {
        Vector2 neighbourPosition = new Vector2(startPosition.x, startPosition.y + 1);
        assertEquals(neighbourPosition, board.getNeighbourPosition(startPosition, Direction.NORTH));
    }

}
