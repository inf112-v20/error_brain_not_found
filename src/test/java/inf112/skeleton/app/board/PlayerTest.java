package inf112.skeleton.app.board;

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
        Position pos = new Position(0,0);
        Player player = new Player(pos);
    }

    @Test
    public void positionGivenToPlayerIsSameAsPlayerGives() {
        assertEquals(0, player.getPosition().getX());

    }

}
