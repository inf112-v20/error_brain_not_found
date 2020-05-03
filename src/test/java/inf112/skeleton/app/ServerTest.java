package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.lan.Converter;
import inf112.skeleton.app.lan.GameServer;
import inf112.skeleton.app.lan.GameServerThreads;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.objects.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServerTest {

    private GameServerThreads server;
    private Converter converter;
    private ArrayList<Player> players;
    private Player player1;
    private Player player2;
    private ProgramCard card;
    private String cardString;

    @Mock
    Socket socket;

    @Mock
    GameServer gameServer;

    @Mock
    RallyGame game;

    @Mock
    InputStream input;

    @Mock
    OutputStream output;

    @Mock
    BufferedReader reader;

    @Mock
    Deck deck;

    @Mock
    Board board;

    @Before
    public void setUp() {
        when(deck.getDeck()).thenReturn(new Stack<>());
        try {
            when(socket.getInputStream()).thenReturn(input);
            when(socket.getOutputStream()).thenReturn(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.server = new GameServerThreads(gameServer, game, socket, 2);
        this.converter = new Converter();
        this.player1 = new Player(new Vector2(0,0), 1);
        this.player2 = new Player(new Vector2(0, 1), 2);
        this.players = new ArrayList<>(Arrays.asList(player1, player2));
        this.card = new ProgramCard(10, 2, Rotate.NONE, "Move 2");
        this.cardString = converter.convertToString(2, card);

        when(game.getBoard()).thenReturn(board);
        when(board.getPlayers()).thenReturn(players);
        when(board.getPlayer(2)).thenReturn(player2);

        // Reader decided for each test
        server.setReader(reader);
    }

    /**
     * Wait for thread to finish
     * @param thread
     */
    private void waitForThread(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sentCardByPlayerTwoGoesToPlayerTwoRegisterTest() {
        // Send card from player 2
        try {
            when(reader.readLine()).thenReturn(cardString).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        assertEquals(1, player2.getRegisters().getCards().size());
    }

    @Test
    public void allClientsHaveSentCardsTest() {
        // Send 5 cards from player 2
        try {
            when(reader.readLine()).thenReturn(cardString, cardString, cardString, cardString, cardString).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        assertTrue(server.allClientsHaveSelectedCards());
    }

}
