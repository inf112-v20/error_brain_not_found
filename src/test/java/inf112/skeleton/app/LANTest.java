package inf112.skeleton.app;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.LAN.Converter;
import inf112.skeleton.app.LAN.GameClientThread;
import inf112.skeleton.app.LAN.NotProgramCardException;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.objects.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.net.Socket;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class LANTest {

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
    private InputStream inputStream;

    @Before
    public void setUp() {
        initMocks(this);
        try {
            when(socket.getInputStream()).thenReturn(inputStream);
            when(socket.getOutputStream()).thenReturn(outputStream);
            when(game.getBoard()).thenReturn(board);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.client = new GameClientThread(game, socket);
        this.converter = new Converter();
        this.programcard = new ProgramCard(10, 2, Rotate.NONE, "Move 2");
        // Reader is decided in each test
        client.setReader(reader);
    }

    /**
     *
     * @param s string
     * @return ByteArrayInputStream to send to InputStream
     */
    private ByteArrayInputStream setInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes());
    }

    @Test
    public void clientGetCorrectPlayerNumberTest() {
        try {
            when(reader.readLine()).thenReturn("3", "4");
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.getStartValues();
        assertEquals(3, client.getMyPlayerNumber());
    }

    @Test
    public void clientGetCorrectNumberOfPlayersTest() {
        try {
            when(reader.readLine()).thenReturn("3", "4");
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
                    .thenReturn("3")
                    .thenReturn("4")
                    .thenReturn(programCardAsString);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.getStartValues();
        String programCardAndPlayer = client.getMessage();
        try {
            ProgramCard card = converter.convertToCardAndExtractPlayer(programCardAndPlayer);
        } catch (NotProgramCardException e) {
            e.printStackTrace();
        }
        assertEquals(1, converter.getPlayerNumber());
    }

    @Test
    public void onlyAddingCardsToDeckWhenReceivingDeckIsTrueTest() {
        String programCardString = converter.convertToString(programcard);
        Player player = new Player(new Vector2(0,0), 1);
        String programCardNotBelongingToDeck = converter.convertToString(1, programcard);
        try {
            when(reader.readLine())
                    .thenReturn("3", "4")
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
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1, client.getStackOfDeck().size());
    }

    @Test
    public void receivingCorrectMapTest() {
        try {
            when(reader.readLine())
                    .thenReturn("3", "4")
                    .thenReturn(Messages.HERE_IS_MAP.toString())
                    .thenReturn("assets/maps/Risky Exchange.tmx")
                    .thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.start();
        try {
            client.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String mapPath = client.getMap();
        assertEquals("assets/maps/Risky Exchange.tmx", mapPath);
    }

}
