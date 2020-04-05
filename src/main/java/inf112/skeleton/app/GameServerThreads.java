package inf112.skeleton.app;


import org.lwjgl.Sys;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * Make a thread for each client connecting.
 * @author Jenny
 */
public class GameServerThreads extends Thread {

    private Socket client;
    private int playerNumber;
    private int numberOfPlayers;
    private GameServer server;
    private InputStream input;
    private BufferedReader reader;

    public GameServerThreads(GameServer server, Socket client, int playerNumber, int numberOfPlayers) {
        this.client = client;
        this.playerNumber = playerNumber;
        this.numberOfPlayers = numberOfPlayers;
        this.server = server;
    }

    /**
     * What the thread is doing when it is started.
     */
    public void run() {
        try {
            // Let the player know what the playernumber is
            sendMessage(this.playerNumber+"");
            System.out.println("Server has sent playernum");
            // Get incoming messages
            sendMessage(this.numberOfPlayers+"");
            System.out.println("Server has sent numplayers");

            while (true) {
                input = client.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                String message = reader.readLine();
                if (message == null) {
                    break;
                }
                int player = Integer.parseInt(String.valueOf(message.charAt(0)));
                // Close client socket if client is leaving.
                if (message.equals("quit")) {
                    server.sendToAllExcept(playerNumber, "Player " + playerNumber + " is leaving...");
                    System.out.println("Player " + playerNumber + " is leaving...");
                    server.disconnect(playerNumber);
                    server.remove(playerNumber);
                    System.out.println("Disconnected player");
                    return;
                }
                System.out.print(message);
                server.sendToAllExcept(player, message);
            }
        } catch (IOException e) {
            // Close socket if exception
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Send a message to this client.
     * @param message
     */
    public void sendMessage(String message) {
        try {
            OutputStream output = client.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(message);
        } catch (IOException e) {
            System.out.println("Closing socket from sendmsg");
            try {
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void close() {
        try {
            System.out.println("Closed socket " + playerNumber + " serverside.");
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
