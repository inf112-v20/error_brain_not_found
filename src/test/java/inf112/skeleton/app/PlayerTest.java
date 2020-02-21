package inf112.skeleton.app;

import inf112.skeleton.app.enums.Direction;
import com.badlogic.gdx.math.Vector2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        Vector2 pos = new Vector2(0,0);
        player = new Player(pos);
    }

    @Test
    public void positionGivenToPlayerIsSameAsPlayerGives() {
        assertEquals(0, player.getPosition().x, 0.01);

    }

    @Test
    public void whenNewXCoordinateIsGivenPlayersCoordinatesChanged() {
        player.setPosition(new Vector2(1,0));
        assertEquals(1, player.getPosition().x, 0.01);
    }

    @Test
    public void whenNewYCoordinateIsGivenPlayersCoordinatesChanged() {
        player.setPosition(new Vector2(0, 1));
        assertEquals(1, player.getPosition().y, 0.01);
    }

    @Test
    public void whenNewPlayerIsMadeDirectionIsSetToEast() {
        assertEquals(Direction.EAST, player.getDirection());
    }

    @Test
    public void whenPlayersStartDirectionIsTurnedLeftItGivesNorthDirection() {
        assertEquals(Direction.NORTH, player.getDirection().turnLeft());
    }

    @Test
    public void whenPlayersStartDirectionIsTurnedRightItGivesSouthDirection() {
        assertEquals(Direction.SOUTH, player.getDirection().turnRight());
    }

}
