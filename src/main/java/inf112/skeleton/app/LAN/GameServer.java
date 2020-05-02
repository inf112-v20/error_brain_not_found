package inf112.skeleton.app.LAN;

import com.badlogic.gdx.utils.ByteArray;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.objects.player.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * A server for handling connection between players.
 * @author Jenny
 */
public class GameServer {

    private ArrayList<GameServerThreads> clients;
    private RallyGame game;
    private Converter converter;
    private boolean allClientsHaveSelectedCards;
    private Deck deck;
    private HashMap<GameServerThreads, Boolean> haveSentMapPath;
    private boolean serverHasConfirmed;
    private ServerSocket serverSocket;

    public GameServer(RallyGame game) {
        this.clients = new ArrayList<>();
        this.haveSentMapPath = new HashMap<>();
        this.game = game;
        this.converter = new Converter();
        this.deck = new Deck();
        deck.shuffleDeck();
        game.setDeck(deck.getDeck());
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
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
            if (this.serverSocket == null) {
                this.serverSocket = new ServerSocket(port);
            }
            // Connect to several clients
            int connected = 0;
            while (connected < numberOfClients) {
                Socket socket = serverSocket.accept();
                // Server is player 1
                int playerNumber = connected+2;
                GameServerThreads client = new GameServerThreads(this, game, socket, playerNumber);
                System.out.println("I have connected to player" + playerNumber);
                client.start();
                sendStartValues(client, numberOfClients+1, playerNumber, this.deck);
                if (game.getMapPath() == null) {
                    haveSentMapPath.put(client, false);
                } else {
                    sendMapPath(client, game.getMapPath());
                    haveSentMapPath.put(client, true);
                }
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
     * Send values so client can start game:
     * Playernumber, number of players and a deck.
     * @param client
     */
    public void sendStartValues(GameServerThreads client, int numberOfPlayers, int playerNumber, Deck deck) {
        client.sendMessage(playerNumber+"");
        client.sendMessage(numberOfPlayers+"");
        sendDeck(client, deck);
    }

    /**
     * When the host calls on {@link RallyGame#setupGame(String)} (starts the game) after a client has connected,
     * it needs to let the client know it has chosen a map.
     * @param mapPath
     */
    public void sendMapPathToNewlyConnectedClients(String mapPath) {
        for (Map.Entry<GameServerThreads, Boolean> entry : haveSentMapPath.entrySet()) {
            Boolean haveSentMap = entry.getValue();
            if (!haveSentMap) {
                GameServerThreads client = entry.getKey();
                System.out.println("Have sent map to client");
                sendMapPath(client, mapPath);
                entry.setValue(true);
            }
        }
    }

    /**
     * Send the map to a client.
     */
    public void sendMapPath(GameServerThreads client, String map) {
        client.sendMessage(Messages.HERE_IS_MAP.toString());
        client.sendMessage(map);
    }

    /**
     * Create a new deck, but remove the locked cards.
     * Update deck in {@link RallyGame} and send this deck to the other players.
     *
     * The player hosting the game sends all of the players locked cards.
     */
    public void createAndSendDeckToAll(ArrayList<ProgramCard> lockedCards) {
        this.deck = new Deck();
        deck.removeCards(lockedCards);
        deck.shuffleDeck();
        game.setDeck(deck.getDeck());
        sendDeckToAll(deck);
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
     * Send program to all clients.
     */
    public void sendSelectedCardsToAll() {
        for (GameServerThreads client : clients) {
            for (Player player : game.getBoard().getPlayers()) {
                client.sendSelectedCards(player);
            }
        }
    }

    /**
     * Let all serverthreads continue loop
     */
    public void continueAll() {
        for (GameServerThreads client : clients) {
            client.continueListening();
        }
    }

    /**
     * Send deck to client
     * @param client to send to
     * @param deck to send
     */
    public void sendDeck(GameServerThreads client, Deck deck) {
        Stack<ProgramCard> stack = deck.getDeck();
        Iterator iter = stack.iterator();
        client.sendMessage(Messages.DECK_BEGIN.toString());
        while (iter.hasNext()) {
            ProgramCard card = (ProgramCard) iter.next();
            client.sendMessage(converter.convertToString(card));
        }
        client.sendMessage(Messages.DECK_END.toString());
    }

    /**
     * Send deck to all players.
     * @param deck to send
     */
    public void sendDeckToAll(Deck deck) {
        for (GameServerThreads client : clients) {
            sendDeck(client, deck);
        }
    }

    /**
     * Tell if all clients have selected their cards or not.
     * @param allClientsHaveSelectedCards true if all have selected cards
     */
    public void setAllClientsHaveSelectedCards(boolean allClientsHaveSelectedCards) {
        this.allClientsHaveSelectedCards = allClientsHaveSelectedCards;
    }

    /**
     *
     * @return true if all clients have selected cards
     */
    public boolean allClientsHaveSelectedCards() {
        return allClientsHaveSelectedCards;
    }

    public boolean allClientsHaveReceivedMap() {
        for (Map.Entry<GameServerThreads, Boolean> entry : haveSentMapPath.entrySet()) {
            Boolean haveSentMap = entry.getValue();
            if (!haveSentMap) {
                return false;
            }
        }
        return true;
    }

    public void setServerHasConfirmed(boolean serverHasConfirmed) {
        this.serverHasConfirmed = serverHasConfirmed;
    }

    public boolean serverHasConfirmed() {
        return serverHasConfirmed;
    }

    public ArrayList<GameServerThreads> getClients() {
        return clients;
    }

    public Deck getDeck() {
        return deck;
    }
}
