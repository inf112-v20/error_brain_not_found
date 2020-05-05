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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServerTest {

    private GameServerThreads server;
    private Player player2;
    private String cardString;
    private Player player1;
    private ProgramCard card;
    private ArrayList<Player> threePlayers;

    @Mock
    private Socket socket;

    @Mock
    private GameServer gameServer;

    @Mock
    private RallyGame game;

    @Mock
    private InputStream input;

    @Mock
    private OutputStream output;

    @Mock
    private BufferedReader reader;

    @Mock
    private Deck deck;

    @Mock
    private Board board;


    @Before
    public void setUp() {
        when(deck.getDeck()).thenReturn(new Stack<>());
        try {
            when(socket.getInputStream()).thenReturn(input);
            when(socket.getOutputStream()).thenReturn(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Converter converter = new Converter();
        this.server = new GameServerThreads(gameServer, game, socket, 2);
        this.player1 = new Player(new Vector2(0, 0), 1);
        this.player2 = new Player(new Vector2(0, 1), 2);
        Player player3 = new Player(new Vector2(0, 2), 3);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1, player2));
        this.threePlayers = new ArrayList<>(Arrays.asList(player1, player2, player3));
        this.card = new ProgramCard(10, 2, Rotate.NONE, "Move 2");
        this.cardString = converter.convertToString(2, card);

        when(game.getBoard()).thenReturn(board);
        when(board.getPlayers()).thenReturn(players);
        when(board.getPlayer(1)).thenReturn(player1);
        when(board.getPlayer(2)).thenReturn(player2);
        when(board.getPlayer(3)).thenReturn(player3);

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
    public void sentCardByPlayer2GoesToPlayer2RegisterTest() {
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
        assertTrue(server.allClientsHaveSelectedCardsOrInPowerDown());
    }

    @Test
    public void player2SendPowerDownMessageTest() {
        try {
            when(reader.readLine()).thenReturn("2"+Messages.POWERING_DOWN.toString()).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        assertTrue(player2.isPoweredDown());
    }

    @Test
    public void letPlayer3KnowPlayer2IsPoweredDownTest() {
        when(board.getPlayers()).thenReturn(threePlayers);
        try {
            when(reader.readLine()).thenReturn("2"+Messages.POWERING_DOWN.toString()).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        verify(gameServer).sendToAllExcept(player2, "2"+Messages.POWERING_DOWN.toString());
    }

    @Test
    public void doNotWaitForPoweredDownPlayersToSendCardTest() {
        try {
            when(reader.readLine()).thenReturn("2"+Messages.POWERING_DOWN.toString()).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        assertTrue(server.allClientsHaveSelectedCardsOrInPowerDown());
    }

    @Test
    public void allPlayersHaveSelectedCardsServerSendsStartTurnMessageTest() {
        player1.setSelectedCards(card, card, card, card, card);
        // Send 5 cards from player 2
        try {
            when(gameServer.serverHasConfirmed()).thenReturn(true);
            when(reader.readLine()).thenReturn(cardString, cardString, cardString, cardString, cardString).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Do not wait for doTurn to finish
        server.continueListening();
        server.start();
        waitForThread(server);
        verify(gameServer).sendToAll(Messages.START_TURN.toString());
    }

    @Test
    public void doNotSendCardsFromPlayersInPowerDownTest() {
        player2.setPoweredDown(true);
        server.sendMessage("This should be last message");
        server.sendSelectedCards(player2);
        assertEquals("This should be last message", server.getLastSentMessage());
    }

    @Test
    public void hostHasPoweredDownAndAllCardsReceivedStartTurnTest() {
        player1.setPoweredDown(true);
        try {
            when(gameServer.serverHasConfirmed()).thenReturn(true);
            when(reader.readLine()).thenReturn(cardString, cardString, cardString, cardString, cardString).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Do not wait for doTurn to finish
        server.continueListening();
        server.start();
        waitForThread(server);
        verify(gameServer).sendToAll(Messages.START_TURN.toString());
    }

    @Test
    public void player2SendsPowerUpMessageTest() {
        player2.setPoweredDown(true);
        try {
            when(reader.readLine()).thenReturn("2"+Messages.POWER_UP.toString()).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        assertFalse(player2.isPoweredDown());
    }

    @Test
    public void player2SendsPowerUpMessageIsRemovedFromPoweredDownPlayersTest() {
        try {
            when(reader.readLine()).thenReturn("2"+Messages.POWER_UP.toString()).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        verify(game).removePoweredDownPlayer(player2);
    }

    @Test
    public void serverBroadcastsReceivedPowerUpMessageTest() {
        try {
            when(reader.readLine()).thenReturn("2"+Messages.POWER_UP.toString()).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        verify(gameServer).sendToAllExcept(player2, "2"+Messages.POWER_UP.toString());
    }



    @Test
    public void sendMessageToAllWhenPlayerContinuesPowerDownTest() {
        try {
            when(reader.readLine())
                    .thenReturn("2"+Messages.CONTINUE_POWER_DOWN.toString())
                    .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        verify(gameServer).sendToAllExcept(player2, "2"+Messages.CONTINUE_POWER_DOWN.toString());
    }



}
