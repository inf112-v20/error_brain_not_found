package inf112.skeleton.app.LAN;


import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private RallyGame game;
    private Converter converter;

    public GameServerThreads(GameServer server, RallyGame game, Socket client, int playerNumber, int numberOfPlayers) {
        this.client = client;
        this.playerNumber = playerNumber;
        this.numberOfPlayers = numberOfPlayers;
        this.server = server;
        this.game = game;
        this.converter = new Converter();

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
                // Close client socket if client is leaving, decrease num of players..
                if (message.equals("quit")) {
                    Player player = game.getBoard().getPlayer(playerNumber);
                    server.sendToAllExcept(player, "Player " + playerNumber + " is leaving...");
                    System.out.println("Player " + playerNumber + " is leaving...");
                    server.disconnect(playerNumber);
                    server.remove(playerNumber);
                    System.out.println("Disconnected player");
                    numberOfPlayers--;
                    return;
                }
                ProgramCard card = converter.convertToCardAndExtractPlayer(message);
                int playerNumber = converter.getPlayerNumber();
                server.putMove(playerNumber, card);
                System.out.println("Moves: "+server.gotAllMoves() + " Numplay; " + numberOfPlayers);
                // Wait for all clients to send their card.
                if (server.gotAllMoves()) {
                    System.out.println("Doing moves");
                    server.doAllMoves();
                }

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
