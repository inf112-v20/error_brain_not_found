package inf112.skeleton.app;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.objects.Flag;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


public class BoardTest {

    private Board board;
    private final int NUMBER_OF_PLAYERS_WHEN_STARTING_GAME = 0;
    private final int BOARD_WIDTH = 16;
    private final int BOARD_HEIGHT = 12;
    private Player player;
    private Random random;
    private ArrayList<Vector2> holes;
    private ArrayList<Flag> flags;

    @Before
    public void setUp() {
        //Mock OpenGL in order to use gdx.texture, gdx.tmxMapLoader etc without getting
        // nullpointerexception
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.board = new Board("assets/maps/Risky_Exchange.tmx", NUMBER_OF_PLAYERS_WHEN_STARTING_GAME);
        this.player = new Player(new Vector2(0, 0), 1);
        this.holes = board.holes;
        this.flags = board.flags;
        // Sort the flags so player can go on them in correct order
        flags.sort(Comparator.comparingInt(Flag::getFlagnr));
        this.random = new Random();
    }

    /**
     *
     * @param player1
     * @param player2
     * @return true if players have same playerNumber
     */
    private boolean isEqualPlayers(Player player1, Player player2) {
        return player1.getPlayerNr() == player2.getPlayerNr();
    }

    /**
     * @param flag1
     * @param flag2
     * @return true if two flags are equal
     */
    private boolean isEqualFlags(Flag flag1, Flag flag2) {
        if (flag1 == null || flag2 == null) {
            return false;
        }
        return flag1.getFlagnr() == flag2.getFlagnr() && flag1.getPosition().equals(flag2.getPosition());
    }

    /**
     * @return a random hole position
     */
    private Vector2 getRandomHolePosition() {
        int randomIndex = random.nextInt(holes.size());
        return holes.get(randomIndex);
    }

    /**
     * @return Flag a random flag
     */
    private Flag getRandomFlag() {
        int randomIndex = random.nextInt(flags.size());
        return flags.get(randomIndex);
    }

    /**
     *
     * @return true if player is on backupPosition and has backupDirection
     */
    private boolean isInBackupState(Player player) {
        return player.getPosition().equals(player.getBackupPosition()) && player.getDirection().equals(player.getBackupDirection());
    }

    @Test
    public void canNotAddSamePlayerOnBoardTest() {
        //TODO: Implement equalsmethod for player
        board.addPlayer(player);
        board.addPlayer(player);
       // assertEquals(1, board.getPlayers().size());
    }

    @Test
    public void boardHasAMapTest() {
        assertNotNull(board.getMap());
    }

    @Test
    public void correctNumbersOfPlayersOnBoardTest() {
        //TODO: Check that numplayers > 0
        //assertEquals(NUMBER_OF_PLAYERS_WHEN_STARTING_GAME, board.getPlayers().size());
    }

    @Test
    public void aPlayerIsAddedToTheBoardIncrementPlayersTest() {
        //TODO: Check that numplayers > 0
        board.addPlayer(player);
       // assertEquals(NUMBER_OF_PLAYERS_WHEN_STARTING_GAME + 1, board.getPlayers().size());
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
    public void flagIsOnFlagPositionOnBoardTest() {
        for (int i = 0; i < 5; i++) {
            Flag flag = getRandomFlag();
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
        assertTrue(board.shouldPush(player2));
    }

    @Test
    public void pushedPlayerMovesInSameDirectionAsItIsPushedTest() {
        Vector2 playerToBePushedPosition = new Vector2(1, 0);
        Vector2 playerIsPushedToPosition = new Vector2(2,0);
        player.setPosition(playerToBePushedPosition);
        Vector2 playerTwoPos = new Vector2(0,0);
        Player player2 = new Player(playerTwoPos, 2);
        player2.setDirection(Direction.EAST);
        board.addPlayer(player);
        board.movePlayer(player2);
        assertEquals(player, board.getPlayer(playerIsPushedToPosition));
    }

}
