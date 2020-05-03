package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.objects.player.Player;
import inf112.skeleton.app.objects.player.PlayerSorter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ProgramCardTest {

    private Deck deck;
    private RallyGame game;
    private Player player;
    private ProgramCard right;
    private ProgramCard left;
    private ProgramCard uturn;
    private ProgramCard moveOne;
    private ProgramCard moveTwo;

    @Before
    public void setUp() {
        Gdx.gl = mock(GL20.class);
        //Make a headless application in order to initialize the board. Does not show.
        new HeadlessApplication(new EmptyApplication());
        this.game = new RallyGame();
        this.game.setupGame("assets/maps/Risky Exchange.tmx");
        Deck deck = new Deck();
        game.setDeck(deck.getDeck());
        player = new Player(new Vector2(0, 0), 1);
        player.setDirection(Direction.EAST);
        Board board = game.getBoard();
        board.addPlayer(player);
        this.deck = new Deck();
        this.left = new ProgramCard(10, 0, Rotate.LEFT, "left");
        this.right = new ProgramCard(10, 0, Rotate.RIGHT, "right");
        this.uturn = new ProgramCard(10, 0, Rotate.UTURN, "uturn");
        this.moveOne = new ProgramCard(10, 1, Rotate.NONE, "move 1");
        this.moveTwo = new ProgramCard(10, 2, Rotate.NONE, "move 2");
    }

    @Test
    public void playerGetNineCardsWhenDrawingCardsTest() {
        player.drawCards(deck);
        assertEquals(9, player.getCardsOnHand().size());
    }

    @Test
    public void canChooseFiveCardsFromDrawnCardsTest() {
        player.drawCards(deck);
        player.selectCards();
        assertEquals(5, player.getRegisters().getNumberOfCardsSelected());
    }

    @Test
    public void playingUturnCardTest() {
        player.setSelectedCards(uturn);
        game.playCard(player, 0);
        assertEquals(Direction.WEST, player.getDirection());
    }

    @Test
    public void playingRightRotateCardTest() {
        player.setSelectedCards(right);
        game.playCard(player, 0);
        assertEquals(Direction.SOUTH, player.getDirection());
    }

    @Test
    public void playingLeftRotateCardTest() {
        player.setSelectedCards(left);
        game.playCard(player, 0);
        assertEquals(Direction.NORTH, player.getDirection());
    }

    @Test
    public void playingMoveOneStepCardTest() {
        player.setSelectedCards(moveOne);
        Vector2 beforePosition = player.getPosition();
        Vector2 afterPosition = new Vector2(beforePosition.x + 1, beforePosition.y);
        game.playCard(player, 0);
        assertEquals(afterPosition, player.getPosition());
    }

    @Test
    public void firstRotateThenMoveToCorrectPositionTest() {
        player.setSelectedCards(left, moveOne);
        Vector2 beforePosition = player.getPosition();
        // player is rotated left and therefore player will go up instead of to the right
        Vector2 afterPosition = new Vector2(beforePosition.x, beforePosition.y + 1);
        game.playCard(player, 0);
        game.playCard(player, 1);
        assertEquals(afterPosition, player.getPosition());
    }

    @Test
    public void firstMoveThenRotateToCorrectRotationTest() {
        player.setSelectedCards(moveTwo, right);
        game.playCard(player, 0);
        game.playCard(player, 1);
        assertEquals(Direction.SOUTH, player.getDirection());
    }

    @Test
    public void prioritizedCardsArePlayedFirstTest() {
        Player player2 = new Player(new Vector2(0, 1), 6);
        ProgramCard highPrioCard = new ProgramCard(10, 0, Rotate.LEFT, "left");
        ProgramCard lowPrioCard = new ProgramCard(1, 0, Rotate.RIGHT, "right");
        ArrayList<Player> players = new ArrayList<>();
        player.setSelectedCards(lowPrioCard);
        player2.setSelectedCards(highPrioCard);
        players.add(player);
        players.add(player2);
        players.sort(new PlayerSorter(0));
        assertEquals(player2, players.get(0));
    }



    /**
     * Starting at east, a sequence of right, left, left, uturn, right, should give west..
     */

    @Test
    public void sequenceOfRotateCardsTest() {
        player.setSelectedCards(right, left, left, uturn, right);
        for (int playedCards = 0; playedCards <= 4; playedCards++) {
            game.playCard(player, playedCards);
        }
        assertEquals(Direction.WEST, player.getDirection());
    }



    /**
     * Starting at (0,0) east, move one, rotate left, move two, rotate right, move one should give
     * (2, 2)
     */
    @Test
    public void sequenceOfCardsTest() {
        player.setSelectedCards(moveOne, left, moveTwo, right, moveOne);
        Vector2 afterPosition = new Vector2(2, 2);
        for (int playedCards = 0; playedCards <= 4; playedCards++) {
            game.playCard(player, playedCards);
        }
        assertEquals(afterPosition, player.getPosition());
    }




}
