package inf112.skeleton.app;


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
     * @param port
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
                GameServerThreads client = new GameServerThreads(socket, playerNumber, numberOfClients+1);
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
        System.out.println("Trying to broadcast.");
        for (GameServerThreads thread : clients) {
            System.out.println("Thread: "+thread.getPlayerNumber());
            thread.sendMessage(message);
        }
    }

    /**
     * Close all connecting sockets.
     */
    public void closeAll() {
        for (GameServerThreads thread : clients) {
            if (thread != null) {
                thread.close();
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
