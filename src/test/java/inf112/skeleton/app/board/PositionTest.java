package inf112.skeleton.app.board;

import inf112.skeleton.app.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionTest {

    private Position pos;

    @Before
    public void setUp() {
        pos = new Position(0,0);
    }

    @Test
    public void whenXIsChangedYIsNotChanged() {
        pos.setX(1);
        assertEquals(0, pos.getY());
    }

    @Test
    public void whenYIsChangedXIsNotChanged() {
        pos.setY(1);
        assertEquals(0, pos.getX());
    }
}
