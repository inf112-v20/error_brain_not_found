package inf112.skeleton.app;


import java.io.*;
import java.net.Socket;

/**
 * Make a thread for each client connecting.
 * @author Jenny
 */
public class GameServerThreads extends Thread {

    private Socket client;
    private int playerNumber;
    private int numberOfPlayers;
    private GameServer server;

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
                InputStream input = client.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                String message = reader.readLine();
                if (message == null) {
                    break;
                }
                // Close client socket.
                if (message.equals("quit")) {
                    System.out.println("Player " + playerNumber + " is leaving...");
                    server.disconnect(playerNumber);
                    System.out.println("Disconnected player");
                    return;
                }
                System.out.print(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void close() {
        try {
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
