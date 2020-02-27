package inf112.skeleton.app;

import inf112.skeleton.app.enums.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
    public void whenInitialisedRightValueIsGiven(){
        assertEquals(Direction.WEST,west);
        assertEquals(Direction.NORTH,north);
        assertEquals(Direction.EAST,east);
        assertEquals(Direction.SOUTH,south);

    }

    @Test
    public void whenWestIsTurnedLeftNewDirectionIsSouth() {
        assertEquals(Direction.SOUTH, west.turnLeft());
    }

    @Test
    public void whenNorthIsTurnedLeftNewDirectionIsWest() {
        assertEquals(Direction.WEST, north.turnLeft());
    }

    @Test
    public void whenEastIsTurnedLeftNewDirectionIsNorth() {
        assertEquals(Direction.NORTH, east.turnLeft());
    }

    @Test
    public void whenSouthIsTurnedLeftNewDirectionIsEast() {
        assertEquals(Direction.EAST, south.turnLeft());
    }

    @Test
    public void whenWestIsTurnedRightNewDirectionIsNorth() {
        assertEquals(Direction.NORTH, west.turnRight());
    }

    @Test
    public void whenNorthIsTurnedRightNewDirectionIsEast() {
        assertEquals(Direction.EAST, north.turnRight());
    }

    @Test
    public void whenEastIsTurnedRightNewDirectionIsSouth() {
        assertEquals(Direction.SOUTH, east.turnRight());
    }

    @Test
    public void whenSouthIsTurnedRightNewDirectionIsWest() {
        assertEquals(Direction.WEST, south.turnRight());
    }

    @Test
    public void whenNorthIsTurned360DegreesItWillBeNorthAfterwards() {
        assertEquals(Direction.NORTH, north.turnRight().turnRight().turnRight().turnRight());
    }

    @Test
    public void whenSouthIsTurned360DegreesItWillBeSouthAfterwards() {
        assertEquals(Direction.SOUTH, south.turnRight().turnRight().turnRight().turnRight());
    }

    @Test
    public void whenEastIsTurned360DegreesItWillBeEastAfterwards() {
        assertEquals(Direction.EAST, east.turnRight().turnRight().turnRight().turnRight());
    }

    @Test
    public void whenWestIsTurned360DegreesItWillBeWestAfterwards() {
        assertEquals(Direction.WEST, west.turnRight().turnRight().turnRight().turnRight());
    }

}
