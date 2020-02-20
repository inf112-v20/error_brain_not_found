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
        west = west.turnLeft();
        assertEquals(Direction.SOUTH, west);
    }

    @Test
    public void turningLeftTest() {
        west = west.turnLeft();
        north = north.turnLeft();
        east = east.turnLeft();
        south = south.turnLeft();

        assertEquals(Direction.SOUTH, west);
        assertEquals(Direction.WEST, north);
        assertEquals(Direction.NORTH, east);
        assertEquals(Direction.EAST, south);
    }

    @Test
    public void turningRightTest() {
        west = west.turnRight();
        north = north.turnRight();
        east = east.turnRight();
        south = south.turnRight();

        assertEquals(Direction.NORTH, west);
        assertEquals(Direction.EAST, north);
        assertEquals(Direction.SOUTH, east);
        assertEquals(Direction.WEST, south);
    }
}
