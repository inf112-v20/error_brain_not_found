package inf112.skeleton.app;

import inf112.skeleton.app.enums.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DirectionTest {

    private Direction west;
    private Direction north;
    private Direction east;
    private Direction south;

    @Before
    public void setUp() {
        west = Direction.WEST;
        north = Direction.NORTH;
        east = Direction.EAST;
        south = Direction.SOUTH;
    }

    @Test
    public void whenInitialisedRightValueIsGivenTest(){
        assertEquals(Direction.WEST,west);
        assertEquals(Direction.NORTH,north);
        assertEquals(Direction.EAST,east);
        assertEquals(Direction.SOUTH,south);

    }

    @Test
    public void whenWestIsTurnedLeftNewDirectionIsSouthTest() {
        assertEquals(Direction.SOUTH, west.turnLeft());
    }

    @Test
    public void whenNorthIsTurnedLeftNewDirectionIsWestTest() {
        assertEquals(Direction.WEST, north.turnLeft());
    }

    @Test
    public void whenEastIsTurnedLeftNewDirectionIsNorthTest() {
        assertEquals(Direction.NORTH, east.turnLeft());
    }

    @Test
    public void whenSouthIsTurnedLeftNewDirectionIsEastTest() {
        assertEquals(Direction.EAST, south.turnLeft());
    }

    @Test
    public void whenWestIsTurnedRightNewDirectionIsNorthTest() {
        assertEquals(Direction.NORTH, west.turnRight());
    }

    @Test
    public void whenNorthIsTurnedRightNewDirectionIsEastTest() {
        assertEquals(Direction.EAST, north.turnRight());
    }

    @Test
    public void whenEastIsTurnedRightNewDirectionIsSouthTest() {
        assertEquals(Direction.SOUTH, east.turnRight());
    }

    @Test
    public void whenSouthIsTurnedRightNewDirectionIsWestTest() {
        assertEquals(Direction.WEST, south.turnRight());
    }

    @Test
    public void whenNorthIsTurned360DegreesItWillBeNorthAfterwardsTest() {
        assertEquals(Direction.NORTH, north.turnRight().turnRight().turnRight().turnRight());
    }

    @Test
    public void whenSouthIsTurned360DegreesItWillBeSouthAfterwardsTest() {
        assertEquals(Direction.SOUTH, south.turnRight().turnRight().turnRight().turnRight());
    }

    @Test
    public void whenEastIsTurned360DegreesItWillBeEastAfterwardsTest() {
        assertEquals(Direction.EAST, east.turnRight().turnRight().turnRight().turnRight());
    }

    @Test
    public void whenWestIsTurned360DegreesItWillBeWestAfterwardsTest() {
        assertEquals(Direction.WEST, west.turnRight().turnRight().turnRight().turnRight());
    }

}
