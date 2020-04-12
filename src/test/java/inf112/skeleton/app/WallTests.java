package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.Board.Board;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.TileID;
import inf112.skeleton.app.objects.Player.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class WallTests {
    private Board board;
    private Player player;
    private Random random;
    private ArrayList<Vector2> allNorthWalls;
    private ArrayList<Vector2> allSouthWalls;
    private ArrayList<Vector2> allEastWalls;
    private ArrayList<Vector2> allWestWalls;

    @Before
    public void setUp() {
        //Mock OpenGL in order to use gdx.texture, gdx.tmxMapLoader etc without getting
        // nullpointerexception
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        int NUMBER_OF_PLAYERS_WHEN_STARTING_GAME = 2;
        this.board = new Board("assets/maps/Risky Exchange.tmx", NUMBER_OF_PLAYERS_WHEN_STARTING_GAME);
        this.player = new Player(new Vector2(0, 0), 1);
        random = new Random();
        allNorthWalls = new ArrayList<>();
        allSouthWalls = new ArrayList<>();
        allEastWalls = new ArrayList<>();
        allWestWalls = new ArrayList<>();
        putPositionsToWallsInLists();
    }

    /**
     *
     * @param position of tile
     * @return true if position is on tile before border
     */
    private boolean onEastBorder(Vector2 position) {
        return position.x >= board.getWidth()-1;
    }

    /**
     *
     * @param position of tile
     * @return true if position is on tile before border
     */
    private boolean onWestBorder(Vector2 position) {
        return position.x < 1;
    }

    /**
     *
     * @param position of tile
     * @return true if position is on tile before border
     */
    private boolean onSouthBorder(Vector2 position) {
        return position.y < 1;
    }

    /**
     *
     * @param position of tile
     * @return true if position is on tile before border
     */
    private boolean onNorthBorder(Vector2 position) {
        return position.y >= board.getHeight()-1;
    }

    /**
     * @param cell to check
     * @return true if cell only has north wall
     */
    private boolean isOnlyNorthWall(TiledMapTileLayer.Cell cell) {
        if (cell == null) {
            return false;
        }
        return cell.getTile().getId() == TileID.NORTH_WALL.getId();
    }

    /**
     * @param cell to check
     * @return true if cell only has south wall
     */
    private boolean isOnlySouthWall(TiledMapTileLayer.Cell cell) {
        if (cell == null) {
            return false;
        }
        return cell.getTile().getId() == TileID.SOUTH_WALL.getId();
    }

    /**
     * @param cell to check
     * @return true if cell only has east wall
     */
    private boolean isOnlyEastWall(TiledMapTileLayer.Cell cell) {
        if (cell == null) {
            return false;
        }
        return cell.getTile().getId() == TileID.EAST_WALL.getId();
    }

    /**
     * @param cell to check
     * @return true if cell only has west wall
     */
    private boolean isOnlyWestWall(TiledMapTileLayer.Cell cell) {
        if (cell == null) {
            return false;
        }
        return cell.getTile().getId() == TileID.WEST_WALL.getId();
    }

    /**
     * Give a random entry from given list
     *
     * @return a random position from the list given
     */
    private Vector2 getRandomWallPosition(ArrayList<Vector2> listOfWalls) {
        int randomIndex = random.nextInt(listOfWalls.size());
        return listOfWalls.get(randomIndex);
    }

    /**
     * Filter out walls like south-east, north-west etc. Is used
     * so that we can test that a player can move when there is not a wall in
     * front of the player, but on same tile.
     *
     * @param listOfWalls allNorthWalls
     * @return list of only north walls
     */
    private ArrayList<Vector2> getOnlyNorthWalls(ArrayList<Vector2> listOfWalls) {
        ArrayList<Vector2> northWalls = new ArrayList<>();
        for (Vector2 wallPos : listOfWalls) {
            TiledMapTileLayer.Cell cell = board.getWallLayer().getCell((int) wallPos.x, (int) wallPos.y);
            if (isOnlyNorthWall(cell)) {
                northWalls.add(wallPos);
            }
        }
        return northWalls;
    }

    /**
     * Filter out walls like south-east, north-west etc. Is used
     * so that we can test that a player can move when there is not a wall in
     * front of the player, but on same tile.
     *
     * @param listOfWalls allSouthWalls
     * @return list of only south walls
     */
    private ArrayList<Vector2> getOnlySouthWalls(ArrayList<Vector2> listOfWalls) {
        ArrayList<Vector2> southWalls = new ArrayList<>();
        for (Vector2 wallPos : listOfWalls) {
            TiledMapTileLayer.Cell cell = board.getWallLayer().getCell((int) wallPos.x, (int) wallPos.y);
            if (isOnlySouthWall(cell)) {
                southWalls.add(wallPos);
            }
        }
        return southWalls;
    }

    /**
     * Filter out walls like south-east, north-west etc. Is used
     * so that we can test that a player can move when there is not a wall in
     * front of the player, but on same tile.
     *
     * @param listOfWalls allEastWalls
     * @return list of only east walls
     */
    private ArrayList<Vector2> getOnlyEastWalls(ArrayList<Vector2> listOfWalls) {
        ArrayList<Vector2> eastWalls = new ArrayList<>();
        for (Vector2 wallPos : listOfWalls) {
            TiledMapTileLayer.Cell cell = board.getWallLayer().getCell((int) wallPos.x, (int) wallPos.y);
            if (isOnlyEastWall(cell)) {
                eastWalls.add(wallPos);
            }
        }
        return eastWalls;
    }

    /**
     * Filter out walls like south-east, north-west etc. Is used
     * so that we can test that a player can move when there is not a wall in
     * front of the player, but on same tile.
     *
     * @param listOfWalls allWestWalls
     * @return list of only west walls
     */
    private ArrayList<Vector2> getOnlyWestWalls(ArrayList<Vector2> listOfWalls) {
        ArrayList<Vector2> westWalls = new ArrayList<>();
        for (Vector2 wallPos : listOfWalls) {
            TiledMapTileLayer.Cell cell = board.getWallLayer().getCell((int) wallPos.x, (int) wallPos.y);
            if (isOnlyWestWall(cell)) {
                westWalls.add(wallPos);
            }
        }
        return westWalls;
    }

    /**
     * In order to test that a player can not move when facing a wall on a neighbour cell,
     * player needs to be placed on the cell next to the wall. Then this cell can not have a wall
     * facing towards the border, because then the player will be placed outside the board.
     *
     * @param listOfWalls a list of position to the walls
     * @return a list of position for the walls, where the walls aligning the borderline is excluded
     */
    private ArrayList<Vector2> filterWallsOnBorder(ArrayList<Vector2> listOfWalls) {
        ArrayList<Vector2> wallsCopy = (ArrayList<Vector2>) listOfWalls.clone();
        for (Vector2 wallPosition : listOfWalls) {
            TiledMapTileLayer.Cell wallCell = board.getWallLayer().getCell((int) wallPosition.x, (int) wallPosition.y);
            if (onEastBorder(wallPosition) && board.hasEastWall(wallCell)) {
                wallsCopy.remove(wallPosition);
            }
            if (onSouthBorder(wallPosition) && board.hasSouthWall(wallCell)) {
                wallsCopy.remove(wallPosition);
            }
            if (onWestBorder((wallPosition)) && board.hasWestWall(wallCell)) {
                wallsCopy.remove(wallPosition);
            }
            if (onNorthBorder(wallPosition) && board.hasNorthWall(wallCell)) {
                wallsCopy.remove(wallPosition);
            }
        }
        return wallsCopy;
    }

    /**
     * Put all position to the walls on board in lists.
     */
    private void putPositionsToWallsInLists() {
        TiledMapTileLayer wallLayer = board.getWallLayer();
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Vector2 pos = new Vector2(x, y);
                TiledMapTileLayer.Cell wall = wallLayer.getCell(x, y);
                if (board.hasSouthWall(wall)) {
                    allSouthWalls.add(pos);
                }
                if (board.hasNorthWall(wall)) {
                    allNorthWalls.add(pos);
                }
                if (board.hasEastWall(wall)) {
                    allEastWalls.add(pos);
                }
                if (board.hasWestWall(wall)) {
                    allWestWalls.add(pos);
                }
            }
        }
    }

    @Test
    public void thereAreNorthWallsOnBoardTest() {
        assertFalse(allNorthWalls.isEmpty());
    }

    @Test
    public void thereAreSouthWallsOnBoardTest() {
        assertFalse(allSouthWalls.isEmpty());
    }

    @Test
    public void thereAreEastWallsOnBoardTest() {
        assertFalse(allEastWalls.isEmpty());
    }

    @Test
    public void thereAreWestWallsOnBoardTest() {
        assertFalse(allWestWalls.isEmpty());
    }


    @Test
    public void playerIsOnCellWithNorthWallTest() {
        // Found position in Risky Exchange.tmx, North Wall has ID 31
        player.setPosition(new Vector2(2, 0));
        TiledMapTileLayer wallLayer = board.getWallLayer();
        TiledMapTileLayer.Cell playerCell = wallLayer.getCell((int) player.getPosition().x, (int) player.getPosition().y);
        assertTrue(board.hasNorthWall(playerCell));
    }

    @Test
    public void playerIsOnCellWithWestWallTest() {
        // Found position in Risky Exchange.tmx, SouthWest wall has ID 32
        player.setPosition(new Vector2(11, 7));
        TiledMapTileLayer wallLayer = board.getWallLayer();
        TiledMapTileLayer.Cell playerCell = wallLayer.getCell((int) player.getPosition().x, (int) player.getPosition().y);
        assertTrue(board.hasWestWall(playerCell));
    }

    @Test
    public void playerIsOnCellWithEastWallTestTest() {
        // Found position in Risky Exchange.tmx, East Wall ha ID 23
        player.setPosition(new Vector2(3, 2));
        TiledMapTileLayer wallLayer = board.getWallLayer();
        TiledMapTileLayer.Cell playerCell = wallLayer.getCell((int) player.getPosition().x, (int) player.getPosition().y);
        assertTrue(board.hasEastWall(playerCell));
    }

    @Test
    public void playerFacingRandomNorthWallCanNotGoTest() {
        // Test for some random walls on board
        for (int i = 0; i < 5; i++) {
            assertFalse(board.canGo(getRandomWallPosition(allNorthWalls), Direction.NORTH));
        }
    }

    @Test
    public void playerFacingRandomEastWallCanNotGoTest() {
        // Test for some random walls on board
        for (int i = 0; i < 5; i++) {
            assertFalse(board.canGo(getRandomWallPosition(allEastWalls), Direction.EAST));
        }
    }

    @Test
    public void playerFacingRandomSouthWallCanNotGoTest() {
        // Test for some random walls on board
        for (int i = 0; i < 5; i++) {
            assertFalse(board.canGo(getRandomWallPosition(allSouthWalls), Direction.SOUTH));
        }
    }

    @Test
    public void playerFacingRandomWestWallCanNotGoTest() {
        // Test for some random walls on board
        for (int i = 0; i < 5; i++) {
            assertFalse(board.canGo(getRandomWallPosition(allWestWalls), Direction.WEST));
        }
    }

    @Test
    public void playerFacingRandomNorthWallDoesNotMoveTest() {
        // Test for some random walls
        for (int i = 0; i < 5; i++) {
            player.setPosition(getRandomWallPosition(allNorthWalls));
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
            player.setPosition(getRandomWallPosition(allSouthWalls));
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
            player.setPosition(getRandomWallPosition(allEastWalls));
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
            player.setPosition(getRandomWallPosition(allWestWalls));
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
            player.setPosition(getRandomWallPosition(getOnlyNorthWalls(allNorthWalls)));
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
            player.setPosition(getRandomWallPosition(getOnlyWestWalls(allWestWalls)));
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
            player.setPosition(getRandomWallPosition(getOnlyEastWalls(allEastWalls)));
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
            player.setPosition(getRandomWallPosition(getOnlySouthWalls(allSouthWalls)));
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
     */
    @Test
    public void playerFacingRandomEastWallOnNeighbourCellCanNotMoveTest() {
        // Test for some random walls
        for (int i = 0; i < 5; i++) {
            Vector2 neighbourCellWithWall = getRandomWallPosition(filterWallsOnBorder(allEastWalls));
            int neighbourX = (int) neighbourCellWithWall.x;
            int neighbourY = (int) neighbourCellWithWall.y;
            Vector2 playerPosition = new Vector2(neighbourX + 1, neighbourY);
            player.setPosition(playerPosition);
            player.setDirection(Direction.WEST);
            board.movePlayer(player);
            assertEquals(playerPosition, player.getPosition());
        }
    }

    /**
     * See {@link #playerFacingRandomEastWallOnNeighbourCellCanNotMoveTest()}
     */
    @Test
    public void playerFacingRandomWestWallOnNeighbourCellCanNotMoveTest() {
        // Test for some random walls
        for (int i = 0; i < 5; i++) {
            Vector2 neighbourCellWithWall = getRandomWallPosition(filterWallsOnBorder(allWestWalls));
            int neighbourX = (int) neighbourCellWithWall.x;
            int neighbourY = (int) neighbourCellWithWall.y;
            Vector2 playerPosition = new Vector2(neighbourX - 1, neighbourY);
            player.setPosition(playerPosition);
            player.setDirection(Direction.EAST);
            board.movePlayer(player);
            assertEquals(playerPosition, player.getPosition());
        }
    }

    /**
     * See {@link #playerFacingRandomEastWallOnNeighbourCellCanNotMoveTest()}
     */
    @Test
    public void playerFacingRandomNorthWallOnNeighbourCellCanNotMoveTest() {
        // Test for some random walls
        for (int i = 0; i < 5; i++) {
            Vector2 neighbourCellWithWall = getRandomWallPosition(filterWallsOnBorder(allNorthWalls));
            int neighbourX = (int) neighbourCellWithWall.x;
            int neighbourY = (int) neighbourCellWithWall.y;
            Vector2 playerPosition = new Vector2(neighbourX, neighbourY+1);
            player.setPosition(playerPosition);
            player.setDirection(Direction.SOUTH);
            board.movePlayer(player);
            assertEquals(playerPosition, player.getPosition());
        }
    }

    /**
     * See {@link #playerFacingRandomEastWallOnNeighbourCellCanNotMoveTest()}
     */
    @Test
    public void playerFacingRandomSouthWallOnNeighbourCellCanNotMoveTest() {
        // Test for some random walls
        for (int i = 0; i < 5; i++) {
            Vector2 neighbourCellWithWall = getRandomWallPosition(filterWallsOnBorder(allSouthWalls));
            int neighbourX = (int) neighbourCellWithWall.x;
            int neighbourY = (int) neighbourCellWithWall.y;
            Vector2 playerPosition = new Vector2(neighbourX, neighbourY-1);
            player.setPosition(playerPosition);
            player.setDirection(Direction.NORTH);
            board.movePlayer(player);
            assertEquals(playerPosition, player.getPosition());
        }
    }



}
