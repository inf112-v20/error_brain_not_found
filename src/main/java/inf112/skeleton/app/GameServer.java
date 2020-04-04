package inf112.skeleton.app;


import com.badlogic.gdx.Game;
import sun.lwawt.macosx.CThreading;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A server for handling connection between players.
 * @author Jenny
 */
public class GameServer {

    private ServerSocket serverSocket;
    private ArrayList<GameServerThreads> clients;

    public GameServer() {
        this.clients = new ArrayList<>();
    }

    /**
     * Establish a connection at given portnumber, waiting for
     * number of clients to connect. Create a new thread for each client.
     * Close socket after connection.
     * @param port to open connection
     * @param numberOfClients how many clients allowed to connect before closing welcoming socket
     */
    public void connect(int port, int numberOfClients) {
        try {
            this.serverSocket = new ServerSocket(port);
            // Connect to several clients
            int connected = 0;
            while (connected < numberOfClients) {
                Socket socket = serverSocket.accept();
                // Server is player 1
                int playerNumber = connected+2;
                GameServerThreads client = new GameServerThreads(this, socket, playerNumber, numberOfClients+1);
                System.out.println("I have connected to player" + playerNumber);
                client.start();
                clients.add(client);
                connected++;
            }
            System.out.println("Connected! :D");
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to all connecting clients.
     * @param message
     */
    public void sendToAll(String message) {
        for (GameServerThreads thread : clients) {
            thread.sendMessage(message);
        }
    }

    /**
     * Send a message to all clients except player specified.
     * @param playerNr player not to send message to
     * @param message to send
     */
    public void sendToAllExcept(int playerNr, String message) {
        for (GameServerThreads thread : clients) {
            if (thread.getPlayerNumber() != playerNr) {
                thread.sendMessage(message);
            }
        }
    }

    /**
     * Disconnect this player from the server. Stop the thread,
     * and close socket.
     * @param playerNumber
     */
    public void disconnect(int playerNumber) {
        GameServerThreads threadToBeRemoved = null;
        for (GameServerThreads thread : clients) {
            if (thread.getPlayerNumber() == playerNumber) {
                threadToBeRemoved = thread;
                threadToBeRemoved.close();
                System.out.print("Closed socket to " + playerNumber);
            }
        }
        clients.remove(threadToBeRemoved);
    }

    /**
     * Disconnect all players from server.
     */
    public void disconnectAll() {
        for (GameServerThreads thread : clients) {
            disconnect(thread.getPlayerNumber());
        }
    }
}
