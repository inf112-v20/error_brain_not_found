package inf112.skeleton.app.LAN;

import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private RallyGame game;
    private Converter converter;
    private HashMap<Object, Object> moves;

    public GameClientThread(RallyGame game, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.game = game;
        this.converter = new Converter();
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
        while (true) {
            String message = getMessage();
            if (message == null) {
                break;
            }
            // If host leaves
            if (message.equals("Host is leaving.. ")) {
                System.out.println(message);
                close();
                return;
            }
            // If another player leaves.
            if (message.contains("Player")) {
                System.out.println(message);
            } else {
                System.out.println(message);

                System.out.println(message);
                ProgramCard card = converter.convertToCardAndExtractPlayer(message);
                int playerNumber = converter.getPlayerNumber();
                Player player = game.getBoard().getPlayer(playerNumber);
                game.playCard(player, card);
            }
        }
    }

    /**
     *
     * @return message from this socket. Close socket if error.
     */
    public String getMessage() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            try {
                // Close socket if exception
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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
            System.out.println("Closed socket " + myPlayerNumber + " clientside.");
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
