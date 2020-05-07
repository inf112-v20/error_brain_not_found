package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.lan.GameServer;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.objects.player.Player;
import inf112.skeleton.app.screens.menuscreen.MenuScreenActors;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServerTest {

    private GameServer gameServer;

    @Mock
    private ServerSocket serverSocket;

    @Mock
    private Socket socket;

    @Mock
    private RallyGame game;

    @Mock
    private InputStream inputStream;

    @Mock
    private OutputStream outputStream;

    @Mock
    MenuScreenActors menuScreenActors;

    @Before
    public void setUp() {
        try {
            when(serverSocket.accept()).thenReturn(socket);
            when(socket.getOutputStream()).thenReturn(outputStream);
            when(socket.getInputStream()).thenReturn(inputStream);
            when(game.getMenuScreenActors()).thenReturn(menuScreenActors);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.gameServer = new GameServer(game);
        gameServer.setServerSocket(serverSocket);
        gameServer.setConnectingToClients(true);
    }

    @Test
    public void clientsConnectUntilServerIsToldToCloseTest() {
        new Thread(gameServer::connect).start();
        // Make server wait for each new connection for 1 second, and stop connecting
        // after 2 seconds, gives 2 connected clients.
        gameServer.setWaitBetweenEachConnection(1000);
        gameServer.setConnectingToClientsTimeout(2000);
        assertEquals(2, gameServer.getClients().size());
    }

    @Test
    public void serverStopsConnectionAfterMaxNumberOfConnectionsIsMadeTest() {
        gameServer.connect(2);
        try {
            verify(serverSocket).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deckIsNotEmptyTest() {
        assertNotNull(gameServer.getDeck());
    }

    @Test
    public void sendStartValuesWhenNewClientConnectedTest() {
        gameServer.connect(1);
        // Deck is sent after numberofPlayers and playernumber
        assertEquals(Messages.DECK_END.toString(), gameServer.getClients().get(0).getLastSentMessage());
    }

    @Test
    public void hostHasPickedMapConnectingClientGetsMapTest() {
        when(game.getMapPath()).thenReturn("My special map path");
        gameServer.connect(1);
        assertEquals("My special map path", gameServer.getClients().get(0).getLastSentMessage());
    }

    @Test
    public void sendToAllExceptPlayerNumberTwoTest() {
        gameServer.connect( 3);
        gameServer.sendToAll("Hello");
        gameServer.sendToAllExcept(new Player(new Vector2(0,0), 2), "Hello again");
        assertEquals("Hello", gameServer.getClients().get(0).getLastSentMessage());
    }

    @Test
    public void removePlayerFromServerTest() {
        gameServer.connect(2);
        gameServer.remove(2);
        assertEquals(1, gameServer.getClients().size());
    }


}
