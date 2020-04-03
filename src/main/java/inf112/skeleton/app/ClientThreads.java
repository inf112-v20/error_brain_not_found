package inf112.skeleton.app;


import java.io.*;
import java.net.Socket;

/**
 * Make a thread for each client connecting
 */
public class ClientThreads extends Thread {

    private Socket client;
    private int playerNumber;

    public ClientThreads(Socket client, int playerNumber) {
        this.client = client;
        this.playerNumber = playerNumber;
    }

    @Override
    public void run() {
        try {

            // Let the player know what the playernumber is
            OutputStream output = client.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(playerNumber);

            // Get incoming messages
            InputStream input = client.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            System.out.print(reader.readLine());
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


    /**
     * Close this socket.
     *
     */
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
