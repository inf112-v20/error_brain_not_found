package inf112.skeleton.app;


import inf112.skeleton.app.enums.Direction;
import jdk.nashorn.internal.objects.NativeFloat32Array;
import org.lwjgl.Sys;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
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

    public GameServerThreads(GameServer server, RallyGame game, Socket client, int playerNumber, int numberOfPlayers) {
        this.client = client;
        this.playerNumber = playerNumber;
        this.numberOfPlayers = numberOfPlayers;
        this.server = server;
        this.moves = new ArrayList<>();
        this.game = game;
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
                int playerNumber = Character.getNumericValue(message.charAt(0));
                Player player = game.getBoard().getPlayer(playerNumber);
                Direction direction = getDirection(message);
                int steps = getSteps(message);
                game.movePlayer(player, direction, steps);
                //int playerNumber = Character.getNumericValue(message.charAt(0));
                //game.movePlayer(playerNumber, message);
                System.out.print(message);

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
