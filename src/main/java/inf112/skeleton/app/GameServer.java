package inf112.skeleton.app;


import java.io.*;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A server for handling connection between players
 */
public class GameServer {

    private int numberOfClients;
    private ServerSocket serverSocket;
    private ArrayList<ClientThreads> clients;

    public GameServer(int numberOfClients) {
        this.numberOfClients = numberOfClients;
        this.clients = new ArrayList<>();
    }

    public void connect(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            //this.sockets = new ArrayList<>();
            // Connect to several clients
            int connected = 0;
            while (true) {
                Socket socket = serverSocket.accept();
                // Server is player 1
                int playerNumber = connected +2;
                ClientThreads client = new ClientThreads(socket, playerNumber);
                client.start();
                clients.add(client);
                connected++;
                if (connected == this.numberOfClients) {
                    System.out.print("Connected! :)");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to all connectiong clients.
     * @param message
     */
    public void sendToAll(String message) {
        System.out.println("Trying to broadcast.");
        for (ClientThreads thread : clients) {
            System.out.println("Thread: "+thread.getPlayerNumber());
            thread.sendMessage(message);
        }
    }


    /**
     * Close all connecting sockets.
     */
    public void closeAll() {
        for (ClientThreads thread : clients) {
            thread.close();
        }
    }



}
