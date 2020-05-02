package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import inf112.skeleton.app.LAN.GameServer;
import inf112.skeleton.app.LAN.GameServerThreads;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServerTest {

    private GameServerThreads server;

    @Mock
    Socket socket;

    @Mock
    RallyGame game;

    @Mock
    GameServer gameServer;

    @Mock
    InputStream input;

    @Mock
    OutputStream output;

    @Mock
    BufferedReader reader;

    @Mock
    Deck deck;

    @Before
    public void setUp() {

        when(deck.getDeck()).thenReturn(new Stack<>());
        try {
            when(socket.getInputStream()).thenReturn(input);
            when(socket.getOutputStream()).thenReturn(output);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.gameServer = new GameServer(game);
        this.server = new GameServerThreads(gameServer, game, socket, 2);
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
    public void clientAsksForMapThenServerSendsMap() {
        when(game.getMapPath()).thenReturn("My special map path");
        try {
            when(reader.readLine()).thenReturn(Messages.ASKING_FOR_MAP.toString()).thenReturn(Messages.STOP_THREAD.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
        waitForThread(server);
        assertEquals("My special map path", server.getLastSentMessage());
    }



}
