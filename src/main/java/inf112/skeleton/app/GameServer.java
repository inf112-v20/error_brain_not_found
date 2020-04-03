package inf112.skeleton.app;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A server for handling connection between players
 */
public class GameServer {

    private int numberOfClients;
    private ServerSocket serverSocket;
    private ArrayList<GameServerThreads> clients;

    public GameServer(int numberOfClients) {
        this.numberOfClients = numberOfClients;
        this.clients = new ArrayList<>();
    }

    public void connect(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            // Connect to several clients
            int connected = 0;
            while (connected < this.numberOfClients) {
                Socket socket = serverSocket.accept();
                // Server is player 1
                System.out.println();
                int playerNumber = connected+2;
                GameServerThreads client = new GameServerThreads(socket, playerNumber);
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
