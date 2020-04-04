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
    private BufferedReader reader;

    public GameClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            this.input = clientSocket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listen for incoming messages from server.
     */
    public void run() {
        while (true) {
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

}
