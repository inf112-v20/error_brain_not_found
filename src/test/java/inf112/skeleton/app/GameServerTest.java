package inf112.skeleton.app;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.lan.Converter;
import inf112.skeleton.app.lan.GameServer;
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
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServerTest {

    private GameServer gameServer;
    private Converter converter;

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
    private MenuScreenActors menuScreenActors;

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
        this.converter = new Converter();
        gameServer.setServerSocket(serverSocket);
        gameServer.setConnectingToClients(true);
    }

    @Test
    public void clientsConnectUntilServerIsToldToCloseTest() {
        new Thread(gameServer::connect).start();
        // Make server wait for each new connection for 1 second, and stop connecting
        // after 2 seconds, gives 2 connected clients.
        gameServer.setWaitBetweenEachConnection(1100);
        gameServer.setConnectingToClientsTimeout(2000);
        assertEquals(2, gameServer.getNumberOfConnectedClients());
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
    public void sendPlayerNumberAndDeckWhenNewClientConnectedTest() {
        GameServer spyServer = spy(gameServer);
        spyServer.connect(1);
        verify(spyServer).sendPlayerNumberAndDeck(any(), anyInt(), any());
    }

    @Test
    public void sendMapToClientsAfterMapIsChosenTest() {
        GameServer spyServer = spy(gameServer);
        spyServer.setMapPath("My map path");
        spyServer.connect(1);
        verify(spyServer).sendToAll(converter.createMapPathMessage("My map path"));
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

    @Test
    public void maxNumberOfPlayersTest() {
        gameServer.connect(8);
        assertEquals(8, gameServer.getNumberOfConnectedClients());
    }


}
