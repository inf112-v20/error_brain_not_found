package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Flag;
import inf112.skeleton.app.objects.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class BoardTest {

    private Board board;
    private BoardLogic boardLogic;
    private final int NUMBER_OF_PLAYERS_WHEN_STARTING_GAME = 0;
    private final int BOARD_WIDTH = 16;
    private final int BOARD_HEIGHT = 12;
    private Player player;
    private ArrayList<Vector2> holes;
    private Vector2 startPosition;
    private ArrayList<Flag> flags;

    @Before
    public void setUp() {
        //Mock OpenGL in order to use gdx.texture, gdx.tmxMapLoader etc without getting
        // nullpointerexception
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.board = new Board("assets/maps/Risky Exchange.tmx", NUMBER_OF_PLAYERS_WHEN_STARTING_GAME);
        this.boardLogic = new BoardLogic();
        // Random position
        this.startPosition = new Vector2(5, 5);
        this.player = new Player(startPosition, 1);
        this.holes = board.getHoles();
        this.flags = board.getFlags();
        // Sort the flags so player can go on them in correct order
        flags.sort(Comparator.comparingInt(Flag::getFlagnr));
    }

    /**
     * @param flag1 first flag
     * @param flag2 second flag
     * @return true if two flags are equal
     */
    private boolean isEqualFlags(Flag flag1, Flag flag2) {
        if (flag1 == null || flag2 == null) {
            return false;
        }
        return flag1.getFlagnr() == flag2.getFlagnr() && flag1.getPosition().equals(flag2.getPosition());
    }

    /*
     *
     * @return true if player is on backupPosition and has backupDirection
     */
    private boolean isInBackupState(Player player) {
        return player.getPosition().equals(player.getBackupPosition()) && player.getDirection().equals(player.getBackupDirection());

    }

    @Test
    public void canNotAddSamePlayerOnBoardTest() {
        board.addPlayer(player);
        board.addPlayer(player);
        assertEquals(1, board.getPlayers().size());
    }

    @Test
    public void boardHasAMapTest() {
        assertNotNull(board.getTiledMap());
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
        assertTrue(board.getBoardLogic().outsideBoard(player, board));
    }

    @Test
    public void playerIsOutsideOfRightBorderTest() {
        player.setPosition(new Vector2(BOARD_WIDTH, 0));
        assertTrue(board.getBoardLogic().outsideBoard(player, board));
    }

    @Test
    public void playerIsOutsideOfLeftBorderTest() {
        player.setPosition(new Vector2(-1, 0));
        assertTrue(board.getBoardLogic().outsideBoard(player, board));
    }

    @Test
    public void playerIsUnderBorderTest() {
        player.setPosition(new Vector2(0, -1));
        assertTrue(board.getBoardLogic().outsideBoard(player, board));
    }

    @Test
    public void playerOutsideBoardIsRespawnedTest() {
        Vector2 outsideOfBoardPosition = new Vector2(-1, 0);
        player.setPosition(outsideOfBoardPosition);
        board.addPlayer(player);
        board.respawnPlayers();
        assertTrue(player.isInBackupState());
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
        assertEquals((int) startPosition.y + 1, (int) player.getPosition().y);
    }

    @Test
    public void thereAreHolesOnBoardTest() {
        assertFalse(holes.isEmpty());
    }

    @Test
    public void playerOnHoleIsOutsideBoardTest() {
        // Choose some random holes
        for (int i = 0; i < 5; i++) {
            Vector2 holePosition = holes.get(0);
            player.setPosition(holePosition);
            assertTrue(board.getBoardLogic().outsideBoard(player, board));
        }
    }

    @Test
    public void playerOnHoleIsRespawnedTest() {
        // Choose some random holes
        for (int i = 0; i < 5; i++) {
            Vector2 holePosition = holes.get(0);
            player.setPosition(holePosition);
            board.addPlayer(player);
            board.respawnPlayers();
            assertTrue(player.isInBackupState());
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

    @Test
    public void flagIsOnFlagPositionOnBoardTest() {
        for (int i = 0; i < 5; i++) {
            Flag flag = flags.get(0);
            Vector2 flagPosition = flag.getPosition();
            assertTrue(board.hasFlag(flagPosition));
        }
    }

    @Test
    public void playerPicksUpFirstFlagTest() {
        Flag flag = flags.get(0);
        Vector2 flagPosition = flag.getPosition();
        player.setPosition(flagPosition);
        board.pickUpFlag(player);
        assertTrue(isEqualFlags(flag, player.getFlagsCollected().get(0)));
    }

    @Test
    public void canNotPickUpFlagNumberTwoBeforeFlagNumberOneTest() {
        // Get flag nr. 2
        Flag flag = flags.get(1);
        Vector2 flagPosition = flag.getPosition();
        player.setPosition(flagPosition);
        board.pickUpFlag(player);
        assertEquals(0, player.getFlagsCollected().size());
    }

    @Test
    public void canNotPickUpFlagNumberThreeBeforeFlagNumberTwoTest() {
        Flag firstFlag = flags.get(0);
        player.setPosition(firstFlag.getPosition());
        board.pickUpFlag(player);
        Flag thirdFlag = flags.get(2);
        player.setPosition(thirdFlag.getPosition());
        board.pickUpFlag(player);
        assertEquals(1, player.getFlagsCollected().size());
    }

    @Test
    public void playerMovesOnFlagTest() {
        Flag flag = flags.get(0);
        Vector2 flagPosition = flag.getPosition();
        Vector2 playerPosition = new Vector2(flagPosition.x -1, flagPosition.y);
        player.setPosition(playerPosition);
        player.setDirection(Direction.EAST);
        board.movePlayer(player);
        board.pickUpFlags();
        assertTrue(isEqualFlags(flag, player.getFlagsCollected().get(0)));
    }

    @Test
    public void pickingUpAllFlagsInIncreasingOrderYouWinTest() {
        // Visit flags 1, 2, and 3
        for (int flagNumber = 1; flagNumber <=3; flagNumber++) {
            Flag flag = flags.get(flagNumber-1);
            Vector2 flagPosition = flag.getPosition();
            player.setPosition(flagPosition);
            board.pickUpFlag(player);
        }
        assertTrue(player.hasAllFlags(3));
    }

    @Test
    public void facingNeighbourPlayerShouldPushTest()   {
        Vector2 playerToBePushedPosition = new Vector2(0,0);
        player.setPosition(playerToBePushedPosition);
        Vector2 playerTwoPos = new Vector2(1, 0);
        Player player2 = new Player(playerTwoPos, 2);
        player2.setDirection(Direction.WEST);
        board.addPlayer(player);
        assertTrue(boardLogic.shouldPush(player2, board));
    }

    @Test
    public void pushedPlayerIsReplacedByPlayerThatIsPushingTest() {
        Vector2 playerToBePushedPosition = new Vector2(1, 0);
        player.setPosition(playerToBePushedPosition);
        Vector2 playerTwoPos = new Vector2(0,0);
        Player player2 = new Player(playerTwoPos, 2);
        player2.setDirection(Direction.EAST);
        board.addPlayer(player);
        board.movePlayer(player2);
        assertEquals(player2, board.getPlayer(playerToBePushedPosition));
    }

    @Test
    public void pushedPlayerMovesInSameDirectionAsItIsPushedTest() {
        Vector2 playerToBePushedPosition = new Vector2(1, 0);
        Vector2 playerIsPushedToPosition = new Vector2(2,0);
        player.setPosition(playerToBePushedPosition);
        // Set different direction then it is pushed in
        player.setDirection(Direction.NORTH);
        Vector2 playerTwoPos = new Vector2(0,0);
        Player player2 = new Player(playerTwoPos, 2);
        player2.setDirection(Direction.EAST);
        board.addPlayer(player);
        board.movePlayer(player2);
        assertEquals(player, board.getPlayer(playerIsPushedToPosition));
    }

    @Test
    public void pushingTwoPlayersInSameDirectionTest() {
        // line 6 from bottom up has no walls in Risky-Exchange:
        Vector2 playerToBePushedPositionOne = new Vector2(2, 6);
        Vector2 playerToBePushedPositionTwo = new Vector2(3, 6);
        Vector2 playerPushingPosition = new Vector2(4, 6);
        Vector2 positionToBePushedTo = new Vector2(1, 6);
        Player player2 = new Player(playerToBePushedPositionOne, 2);
        Player player3 = new Player(playerToBePushedPositionTwo, 3);
        // Random directions
        player2.setDirection(Direction.EAST);
        player3.setDirection(Direction.SOUTH);
        player.setPosition(playerPushingPosition);
        player.setDirection(Direction.WEST);
        board.addPlayer(player2);
        board.addPlayer(player3);
        board.movePlayer(player);
        assertEquals(player2, board.getPlayer(positionToBePushedTo));
    }

    @Test
    public void wallStopsPushingTest() {
        // Found wall in Risky Exhange
        Vector2 northWallPosition = new Vector2(0, 5);
        Player player2 = new Player(northWallPosition, 2);
        player2.setDirection(Direction.WEST);
        Vector2 playerPushingPosition = new Vector2(0,4);
        player.setPosition(playerPushingPosition);
        player.setDirection(Direction.NORTH);
        board.addPlayer(player2);
        board.addPlayer(player);
        board.movePlayer(player);
        assertEquals(player2, board.getPlayer(northWallPosition));
    }

    @Test
    public void wallStopsPushingSeveralPlayersTest() {
        // Found wall in Risky Exhange
        Vector2 northWallPosition = new Vector2(0, 5);
        Vector2 middlePosition = new Vector2(0, 4);
        Player playerToBeStoppedByWall = new Player(northWallPosition, 2);
        Player playerInMiddle = new Player(middlePosition, 3);
        playerToBeStoppedByWall.setDirection(Direction.WEST);
        playerInMiddle.setDirection(Direction.SOUTH);
        Vector2 playerPushingPosition = new Vector2(0,3);
        player.setPosition(playerPushingPosition);
        player.setDirection(Direction.NORTH);
        board.addPlayer(playerToBeStoppedByWall);
        board.addPlayer(playerInMiddle);
        board.addPlayer(player);
        board.movePlayer(player);
        assertEquals(playerToBeStoppedByWall, board.getPlayer(northWallPosition));
    }

}
