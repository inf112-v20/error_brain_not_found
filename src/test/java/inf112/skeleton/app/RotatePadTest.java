package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.objects.RotatePad;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class RotatePadTest {

    private RallyGame game;
    private Board board;
    private ArrayList<RotatePad> rotatePads;
    private Player player;
    private ArrayList<RotatePad> leftRotatePads;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setupGame("assets/maps/Risky_Exchange.tmx");
        this.board = game.getBoard();
        this.rotatePads = board.rotatePads;

        // Already 4 players on board.
        //TODO: Let setupGame take in playerNumber as arg
        player = new Player(new Vector2(0,0), 5);
        board.addPlayer(player);
    }

    /**
     * @return a random rotatePad from the list given
     */
    private RotatePad getRandomRotatePad(ArrayList<RotatePad> rotatePads) {
        Random random = new Random();
        int randomIndex = random.nextInt(rotatePads.size());
        return rotatePads.get(randomIndex);
    }

    /**
     * Get only left rotate pads.
     * @param rotatePads
     * @return list of left rotate pads
     */
    private ArrayList<RotatePad> getLeftRotatePads(ArrayList<RotatePad> rotatePads) {
        ArrayList<RotatePad> leftRotatePads = new ArrayList<>();
        for (RotatePad pad : rotatePads) {
            if (pad.getRotate() == Rotate.LEFT) {
                leftRotatePads.add(pad);
            }
        }
        return leftRotatePads;
    }

    @Test
    public void onlyRotateWhenPadIsActivatedTest() {
        RotatePad pad = getRandomRotatePad(rotatePads);
        player.setDirection(Direction.EAST);
        player.setPosition(pad.getPosition());
        assertEquals(Direction.EAST, player.getDirection());
    }
    @Test
    public void playerOnPadRotatesTest() {
        RotatePad pad = getRandomRotatePad(getLeftRotatePads(rotatePads));
        Vector2 padPosition = pad.getPosition();
        player.setPosition(padPosition);
        player.setDirection(Direction.EAST);
        game.activateRotatePads();
        assertEquals(Direction.NORTH, player.getDirection());
    }


}
