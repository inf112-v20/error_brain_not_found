package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionTest {

    private Vector2 pos;

    @Before
    public void setUp() {
        pos = new Vector2(0,0);
    }

    @Test
    public void whenXIsChangedYIsNotChanged() {
        pos.x = 1;
        assertEquals(0, pos.y, 0.01);
    }

    @Test
    public void whenYIsChangedXIsNotChanged() {
        pos.y = 1;
        assertEquals(0, pos.x, 0.01);
    }
}
