package inf112.skeleton.app;

import inf112.skeleton.app.LAN.GameServer;
import inf112.skeleton.app.enums.Messages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServerTest {

    private GameServer gameServer;

    @Mock
    ServerSocket serverSocket;

    @Mock
    Socket socket;

    @Mock
    RallyGame game;

    @Mock
    InputStream inputStream;

    @Mock
    OutputStream outputStream;

    @Before
    public void setUp() {
        try {
            when(serverSocket.accept()).thenReturn(socket);
            when(socket.getOutputStream()).thenReturn(outputStream);
            when(socket.getInputStream()).thenReturn(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gameServer = new GameServer(game);
        gameServer.setServerSocket(serverSocket);
    }

    @Test
    public void oneClientConnectedTest() {
        gameServer.connect(9000, 1);
        assertEquals(1, gameServer.getClients().size());
    }

    @Test
    public void twoClientConnectedTest() {
        gameServer.connect(9000, 2);
        assertEquals(2, gameServer.getClients().size());
    }

    @Test
    public void deckIsNotEmptyTest() {
        assertNotNull(gameServer.getDeck());
    }

    @Test
    public void sendStartValuesWhenNewClientConnectedTest() {
        gameServer.connect(9000, 1);
        // Deck is sent after numberofPlayers and playernumber
        assertEquals(Messages.DECK_END.toString(), gameServer.getClients().get(0).getLastSentMessage());
    }




}
