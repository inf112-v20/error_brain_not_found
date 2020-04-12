package inf112.skeleton.app.LAN;


import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

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
    private ArrayList<String> moves;
    private RallyGame game;
    private Converter converter;

    public GameServerThreads(GameServer server, RallyGame game, Socket client, int playerNumber, int numberOfPlayers) {
        this.client = client;
        this.playerNumber = playerNumber;
        this.numberOfPlayers = numberOfPlayers;
        this.server = server;
        this.moves = new ArrayList<>();
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
                ProgramCard card = converter.convertToCardAndExtractPlayer(message);
                int playerNumber = converter.getPlayerNumber();
                Player player = game.getBoard().getPlayer(playerNumber);
                System.out.println(message);
                game.playCard(player, card);
                System.out.println("Played" + message + "from " +player.getPlayerNr());
                //int playerNumber = Character.getNumericValue(message.charAt(0));
                //game.movePlayer(playerNumber, message);

                //int playerNumber = Character.getNumericValue(message.charAt(0));
                //game.movePlayer(playerNumber, message);

                //game.movePlayer(player, message);
                // Close client socket if client is leaving.
                if (message.equals("quit")) {
                    server.sendToAllExcept(player, "Player " + playerNumber + " is leaving...");
                    System.out.println("Player " + playerNumber + " is leaving...");
                    server.disconnect(playerNumber);
                    server.remove(playerNumber);
                    System.out.println("Disconnected player");
                    return;
                }
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
 * Get number of steps player is supposed to move.
 * @param message
 * @return int steps
 */
private int getSteps(String message) {
        return Character.getNumericValue(message.charAt(3));
        }

/**
 * Get the direction that the player is moving to.
 * @param message
 * @return direction to player
 */
public Direction getDirection(String message) {
        if (message.contains("east")) {
        return Direction.EAST;
        }
        if (message.contains("west")) {
        return Direction.WEST;
        }
        if (message.contains("south")) {
        return Direction.SOUTH;
        }
        if (message.contains("north")) {
        return Direction.NORTH;
        }
        return null;
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
