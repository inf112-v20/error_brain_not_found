package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import inf112.skeleton.app.Player;
import inf112.skeleton.app.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    private Position pos;
    private Player player;

    @Before
    public void setUp() {
        pos = new Position(0,0);
        player = new Player(pos);
    }

    @Test
    public void positionGivenToPlayerIsSameAsPlayerGives() {
        assertEquals(0, player.getPosition().getX());

    }

    @Test
    public void whenNewXCoordinateIsGivenPlayersCoordinatesChanged() {
        player.setPosition(new Position(1,0));
        assertEquals(1, player.getPosition().getX());
    }

    @Test
    public void whenNewYCoordinateIsGivenPlayersCoordinatesChanged() {
        player.setPosition(new Position(0, 1));
        assertEquals(1, player.getPosition().getY());
    }

}
