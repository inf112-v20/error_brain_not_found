package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
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
        this.board = new Board("assets/Risky_Exchange.tmx");
        this.player = new Player(new Vector2(0,0));
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
        board.addPlayer(0,0);
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

    /**
     * Test the check if a player is outside the board after a move, so
     * it can be respawned.
     *
     * When board.playerIsOutsideBoard(player) is implemented in Board it should return true
     * when the player moves outside.
     *
      */
    @Test
    public void whenPlayerIsOutsideOnTopOfBoardItIsDetectedTest() {
        player.setPosition(new Vector2(0, BOARD_HEIGHT-1));
        player.setDirection(Direction.NORTH);
        board.movePlayer(player);
        //TODO: make player is outside borders function in board
        //assertTrue(board.playerIsOutsideBoard(player));
    }

    @Test
    public void whenPlayerIsOutsideOnRightSideOfBoardItIsDetectedTest() {
        player.setPosition(new Vector2(BOARD_WIDTH-1, 0));
        player.setDirection(Direction.EAST);
        board.movePlayer(player);
        //TODO: make player is outside borders function in board
        //assertTrue(board.playerIsOutsideBoard(player));
    }

    @Test
    public void whenPlayerIsOutsideOnLeftSideOfBoardItIsDetectedTest() {
        player.setPosition(new Vector2(0, 0));
        player.setDirection(Direction.WEST);
        board.movePlayer(player);
        //TODO: make player is outside borders function in board
        //assertTrue(board.playerIsOutsideBoard(player));
    }

    @Test
    public void whenPlayerIsOutsideUnderTheBoardItIsDetectedTest() {
        player.setPosition(new Vector2(0, 0));
        player.setDirection(Direction.SOUTH);
        board.movePlayer(player);
        //TODO: make player is outside borders function in board
        //assertTrue(board.playerIsOutsideBoard(player));
    }


    @Test
    public void whenPlayerMovesOutsideBoardOnLeftSideItShouldBeAcceptedTest() {
        player.setPosition(new Vector2(0, 0));
        player.setDirection(Direction.WEST);
        assertTrue(board.canGo(player));
    }

    @Test
    public void whenPlayerMovesOutsideBoardOnRightSideItShouldBeAcceptedTest() {
        player.setPosition(new Vector2(BOARD_WIDTH - 1, 0));
        player.setDirection(Direction.EAST);
        //TODO: Make canGo accept player go outside board
        assertTrue(board.canGo(player));
    }

    @Test
    public void whenPlayerMovesOutsideBoardOnTopItShouldBeAcceptedTest() {
        player.setPosition(new Vector2(0, BOARD_HEIGHT-1));
        player.setDirection(Direction.NORTH);
        //TODO: Make canGo accept player go outside board
        assertTrue(board.canGo(player));
    }

    @Test
    public void whenPlayerMovesOutsideUnderBoardItShouldBeAcceptedTest() {
        player.setPosition(new Vector2(0, 0));
        player.setDirection(Direction.SOUTH);
        //TODO: Make canGo accept player go outside board
        assertTrue(board.canGo(player));
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




}
