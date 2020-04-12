package inf112.skeleton.app.LAN;

import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A server for handling connection between players.
 * @author Jenny
 */
public class GameServer {

    private ArrayList<GameServerThreads> clients;
    private RallyGame game;
    private HashMap<Integer, ArrayList<ProgramCard>> moves;
    private Converter converter;

    public GameServer(RallyGame game) {
        this.clients = new ArrayList<>();
        this.game = game;
        this.moves = new HashMap<>();
        this.converter = new Converter();
    }

    /**
     * Establish a connection at given portnumber, waiting for
     * number of clients to connect. Create a new thread for each client.
     * Close socket after connection.
     * @param port to open connection
     * @param numberOfClients how many clients allowed to connect before closing welcoming socket
     */
    public void connect(int port, int numberOfClients) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            // Connect to several clients
            int connected = 0;
            while (connected < numberOfClients) {
                Socket socket = serverSocket.accept();
                // Server is player 1
                int playerNumber = connected+2;
                GameServerThreads client = new GameServerThreads(this, game , socket, playerNumber, numberOfClients+1);
                System.out.println("I have connected to player" + playerNumber);
                client.start();
                clients.add(client);
                connected++;
            }
            System.out.println("Connected! :D");
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to all connecting clients.
     * @param message
     */
    public void sendToAll(String message) {
        for (GameServerThreads thread : clients) {
            thread.sendMessage(message);
        }
    }

    /**
     * Send a message to all clients except player specified.
     * @param player player not to send message to
     * @param message to send
     */
    public void sendToAllExcept(Player player, String message) {
        for (GameServerThreads thread : clients) {
            if (thread.getPlayerNumber() != player.getPlayerNr()) {
                thread.sendMessage(message);
            }
        }
    }

    /**
     * Disconnect this player from the server and close socket.
     * @param playerNumber
     */
    public void disconnect(int playerNumber) {
        for (GameServerThreads thread : clients) {
            if (thread.getPlayerNumber() == playerNumber) {
                thread.close();
            }
        }
    }

    /**
     * Remove a client from the list.
     * @param playerNumber number of player to remove.
     */
    public void remove(int playerNumber) {
        GameServerThreads playerToRemove = null;
        for (GameServerThreads thread : clients) {
            if (thread.getPlayerNumber() == playerNumber) {
                playerToRemove = thread;
            }
        }
        clients.remove(playerToRemove);
    }

    /**
     * Disconnect all players from server.
     */
    public void disconnectAll() {
        for (GameServerThreads thread : clients) {
            disconnect(thread.getPlayerNumber());
        }
    }

    /**
     * Register a new move for a player. If player already has registered move,
     * add it to list of cards belonging to the player.
     * @param playerNumber
     * @param card
     */
    public void putMove(int playerNumber, ProgramCard card) {
        if (moves.containsKey(playerNumber)) {
            moves.get(playerNumber).add(card);
        } else {
            ArrayList<ProgramCard> cards = new ArrayList<>();
            cards.add(card);
            moves.put(playerNumber, cards);
        }
    }

    /**
     * Check if all players have sent their program, consisting of 5 cards.
     * @return true if all players have registered their move.
     */
    public boolean gotAllMoves() {
        if (moves.size() < getNumberOfPlayers()) {
            return false;
        }
        boolean receivedAllCards = true;
        for (Map.Entry<Integer, ArrayList<ProgramCard>> move : moves.entrySet()) {
            ArrayList<ProgramCard> cards = move.getValue();
            if (cards.size() < 5) {
                receivedAllCards = false;
            }
        }
        return receivedAllCards;
    }

    public int getNumberOfPlayers() {
        return clients.size() + 1;
    }

    public void doAllMoves() {
        for (int i = 0; i < 4; i++) {
            allPlayOneMove();
        }
        moves = new HashMap<>();
    }

    /**
     * Play one move from the selected cards of all players, send this move
     * to all players.
     */
    public void allPlayOneMove() {
        System.out.println(moves);
        for (Map.Entry<Integer, ArrayList<ProgramCard>> move : moves.entrySet()) {
            int playerNumber = move.getKey();
            ArrayList<ProgramCard> cards = move.getValue();
            Player player = game.getBoard().getPlayer(playerNumber);
            ProgramCard playingCard = cards.remove(0);
            sendToAll(converter.convertToString(playerNumber, playingCard));
            game.playCard(player, playingCard);
        }
    }
}
