package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.lan.Converter;
import inf112.skeleton.app.lan.GameClientThread;
import inf112.skeleton.app.lan.NotProgramCardException;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.lan.PlayerAndProgramCard;
import inf112.skeleton.app.objects.player.Player;
import inf112.skeleton.app.screens.menuscreen.MenuScreenActors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.net.Socket;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientTest {

    private GameClientThread client;
    private Converter converter;
    private ProgramCard programcard;

    @Mock
    private Socket socket;

    @Mock
    private OutputStream outputStream;

    @Mock
    private RallyGame game;

    @Mock
    private Board board;

    @Mock
    private BufferedReader reader;

    @Mock
    MenuScreenActors menuScreenActors;

    @Mock
    private InputStream inputStream;
    private Player player1;

    @Before
    public void setUp() {
        try {
            when(socket.getInputStream()).thenReturn(inputStream);
            when(socket.getOutputStream()).thenReturn(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.client = new GameClientThread(game, socket);
        this.converter = new Converter();
        this.programcard = new ProgramCard(10, 2, Rotate.NONE, "Move 2");
        this.player1 = new Player(new Vector2(0,0), 1);
        when(game.getBoard()).thenReturn(board);
        when(board.getPlayer(1)).thenReturn(player1);
        when(game.getMenuScreenActors()).thenReturn(menuScreenActors);
        // Reader is decided in each test
        client.setReader(reader);
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
    public void clientGetCorrectPlayerNumberTest() {
        try {
            when(reader.readLine()).thenReturn(converter.createPlayerNumberMessage(3))
            .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
        waitForThread(client);
        verify(game).setPlayerNumber(3);
    }

    @Test
    public void clientGetCorrectNumberOfPlayersTest() {
        try {
            when(reader.readLine()).thenReturn(converter.createNumberOfPlayersMessage(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.getStartValues();
        assertEquals(4, client.getNumberOfPlayers());
    }

    @Test
    public void whenReceivingProgramCardFromServerCorrectPlayerIsExtractedTest() {
        String programCardAsString = converter.convertToString(1, programcard);
        try {
            when(reader.readLine())
                    .thenReturn(programCardAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.getStartValues();
        String programCardAndPlayer = client.getMessage();
        try {
            PlayerAndProgramCard playerAndCard = converter.getSentCardFromPlayer(programCardAndPlayer);
            assertEquals(1, playerAndCard.getPlayerNumber());
        } catch (NotProgramCardException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void onlyAddingCardsToDeckWhenReceivingDeckIsTrueTest() {
        String programCardString = converter.convertToString(programcard);
        Player player = new Player(new Vector2(0,0), 1);
        String programCardNotBelongingToDeck = converter.convertToString(1, programcard);
        try {
            when(reader.readLine())
                    .thenReturn(Messages.DECK_BEGIN.toString())
                    .thenReturn(programCardString)
                    .thenReturn(Messages.DECK_END.toString())
                    .thenReturn(programCardNotBelongingToDeck)
                    .thenReturn(Messages.STOP_THREAD.toString());
            when(board.getPlayer(1)).thenReturn(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
        waitForThread(client);
        assertEquals(1, client.getStackOfDeck().size());
    }

    @Test
    public void receivingCorrectMapTest() {
        try {
            when(reader.readLine())
                    .thenReturn(converter.createMapPathMessage("assets/maps/Risky Exchange.tmx"))
                    .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
        waitForThread(client);
        verify(game).setMapPath("assets/maps/Risky Exchange.tmx");
    }

    @Test
    public void playerOneSendsPoweringDownTest() {
        try {
            when(reader.readLine())
                    .thenReturn(converter.createMessageFromPlayer(1, Messages.POWERING_DOWN))
                    .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
        waitForThread(client);
        assertTrue(player1.isPoweringDown());
    }

    @Test
    public void playerOneSendsPowerUpTest() {
        try {
            when(reader.readLine())
                    .thenReturn(converter.createMessageFromPlayer(1, Messages.POWER_UP))
                    .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
        waitForThread(client);
        assertFalse(player1.isPoweredDown());
    }

    @Test
    public void playerOneSendsPowerDownIsRegisteredTest() {
        try {
            when(reader.readLine())
                    .thenReturn(converter.createMessageFromPlayer(1, Messages.POWERING_DOWN))
                    .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
        waitForThread(client);
        assertTrue(player1.isPoweringDown());
    }

    @Test
    public void playerOneSendsPowerUpIsRemovedFromPoweredDownInGameTest() {
        try {
            when(reader.readLine())
                    .thenReturn(converter.createMessageFromPlayer(1, Messages.POWER_UP))
                    .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
        waitForThread(client);
        verify(game).removePoweredDownPlayer(player1);
    }

    @Test
    public void continueTurnWhenServerSendsContinueMessage() {
        try {
            when(reader.readLine())
                    .thenReturn(Messages.CONTINUE_TURN.toString())
                    .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Do not wait for doTurn to finish
        client.continueListening();
        client.start();
        waitForThread(client);
        verify(game).continueTurn();
    }

    @Test
    public void client2SendsQuitMessageTest() {
        try {
            when(reader.readLine())
                    .thenReturn(converter.createQuitMessage(2))
                    .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Do not wait for doTurn to finish
        client.continueListening();
        client.start();
        waitForThread(client);
        verify(game).quitPlaying();
    }

}
