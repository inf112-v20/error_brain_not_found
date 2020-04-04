package inf112.skeleton.app;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;

/**
 * Own thread for a client so client can get continous updates from server.
 * @author Jenny
 */
public class GameClientThread extends Thread {

    private Socket clientSocket;
    private int myPlayerNumber;
    private int numberOfPlayers;
    private InputStream input;
    private OutputStream output;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean isClosed;

    public GameClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.input = this.clientSocket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input));
            this.output = this.clientSocket.getOutputStream();
            this.writer = new PrintWriter(output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listen for incoming messages from server when thread is started.
     */
    @Override
    public void run() {
        System.out.println("Im still running");
        while (!isClosed) {
            System.out.println("Im still not closed running");
            String message = getMessage();
            if (message == null) {
                break;
            }
            System.out.print(message);
        }
    }

    /**
     *
     * @return message from this socket.
     */
    public String getMessage() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Read the playerNumber and numberOfPlayers that server has given to this player.
     */
    public void storeInitializationValuesFromSocket() {
        this.myPlayerNumber = Integer.parseInt(getMessage());
        System.out.println("From server, my playernumber: "+myPlayerNumber);
        this.numberOfPlayers = Integer.parseInt(getMessage());
        System.out.println("From server, numberofplayers "+numberOfPlayers);
    }

    /**
     * Send a message to server.
     * @param message message to be sent
     */
    public void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * @return playernumber assigned to this client by server
     */
    public int getMyPlayerNumber() {
        return this.myPlayerNumber;
    }

    /**
     *
     * @return numberOfPlayers in this game
     */
    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    /**
     * Close the socket.
     */
    public void close() {
        try {
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setClosed() {
        this.isClosed = true;
    }
}
