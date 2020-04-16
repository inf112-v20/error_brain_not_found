package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PlayerTest {

    private Player player;
    private RallyGame game;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setUpGameWithoutConnection("assets/maps/Risky_Exchange.tmx", 0);
        Vector2 pos = new Vector2(0,0);
        player = new Player(pos, 1);
    }

    @Test
    public void positionGivenToPlayerIsSameAsPlayerGivesTest() {
        assertEquals(0, player.getPosition().x, 0.01);

    }

    @Test
    public void whenNewXCoordinateIsGivenPlayersCoordinatesChangedTest() {
        player.setPosition(new Vector2(1,0));
        assertEquals(1, player.getPosition().x, 0.01);
    }

    @Test
    public void whenNewYCoordinateIsGivenPlayersCoordinatesChangedTest() {
        player.setPosition(new Vector2(0, 1));
        assertEquals(1, player.getPosition().y, 0.01);
    }

    @Test
    public void whenNewPlayerIsMadeDirectionIsSetToEastTest() {
        assertEquals(Direction.EAST, player.getDirection());
    }

    @Test
    public void whenPlayersStartDirectionIsTurnedLeftItGivesNorthDirectionTest() {
        assertEquals(Direction.NORTH, player.getDirection().turnLeft());
    }

    @Test
    public void whenPlayersStartDirectionIsTurnedRightItGivesSouthDirectionTest() {
        assertEquals(Direction.SOUTH, player.getDirection().turnRight());
    }

    @Test
    public void startWithNoDamageTokensTest() {
        assertEquals(0, player.getDamageTokens());
    }

    @Test
    public void startWithThreeLivesTest() {
        assertEquals(3, player.getLifeTokens());
    }

    @Test
    public void takeDamageIncreasesDamageTokenTest() {
        int numberTokensBefore = player.getDamageTokens();
        player.handleDamage();
        assertEquals(numberTokensBefore+1, player.getDamageTokens());
    }

    @Test
    public void lifeDecreasedWhenCollectedTenDamageTokensTest() {
        Board board = game.getBoard();
        board.addPlayer(player);
        int livesBefore = player.getLifeTokens();
        for (int takeDamage = 1; takeDamage <= 10; takeDamage++) {
            player.handleDamage();
        }
        game.decreaseLives();
        assertEquals(livesBefore-1, player.getLifeTokens());
    }

}
