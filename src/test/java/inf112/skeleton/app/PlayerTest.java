package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    private Vector2 pos;
    private Player player;

    @Before
    public void setUp() {
        pos = new Vector2(0,0);
        player = new Player(pos);
    }

    @Test
    public void positionGivenToPlayerIsSameAsPlayerGives() {
        assertEquals(0, player.getPosition().x);

    }

    @Test
    public void whenNewXCoordinateIsGivenPlayersCoordinatesChanged() {
        player.setPosition(new Vector2(1,0));
        assertEquals(1, player.getPosition().x);
    }

    @Test
    public void whenNewYCoordinateIsGivenPlayersCoordinatesChanged() {
        player.setPosition(new Vector2(0, 1));
        assertEquals(1, player.getPosition().y);
    }

}
