package inf112.skeleton.app.lan;

import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.objects.player.Player;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * A server for handling connection between players.
 */
public class GameServer {

    private ArrayList<GameServerThreads> clients;
    private RallyGame game;
    private Converter converter;
    private boolean allClientsHaveSelectedCardsOrIsPoweredDown;
    private Deck deck;
    private HashMap<GameServerThreads, Boolean> haveSentMapPath;
    private boolean serverHasConfirmed;
    private ServerSocket serverSocket;
    private boolean allPoweredDownClientsHaveConfirmed;
    private boolean connectingToClients;
    private int waitForNextConnectionMilliSeconds;
    private String mapPath;
    private int numberOfPlayers;

    public GameServer(RallyGame game) {
        this.clients = new ArrayList<>();
        this.haveSentMapPath = new HashMap<>();
        this.game = game;
        this.converter = new Converter();
        this.deck = new Deck();
        deck.shuffleDeck();
        game.setDeck(deck.getDeck());
        this.connectingToClients = true;
    }

    /**
     * Set the socket for incoming connections.
     *
     * @param serverSocket
     */
    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Create a serversocket using given portnumber
     *
     * @param portNumber
     */
    public void createServerSocket(int portNumber) {
        try {
            this.serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establish a connection waiting for number of clients to connect. Create a new thread for each client.
     * If no serverSocket is made by {@link #setServerSocket(ServerSocket)} or {@link #createServerSocket(int)} then
     * a default serversocket will be made on port 9000. Close socket after connection.
     *
     * @param maxNumberOfClients how many clients allowed to connect before closing welcoming socket
     */
    public void connect(int maxNumberOfClients) {
        try {
            if (this.serverSocket == null) {
                createServerSocket(9000);
            }
            // Connect to several clients
            int connected = 0;
            while (connectingToClients) {
                if (connected >= maxNumberOfClients) {
                    break;
                }
                System.out.println("Waiting for client to connect");
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Got timeout or client connected");
                    // Server is player 1
                    int playerNumber = connected+2;
                    GameServerThreads client = new GameServerThreads(this, game, socket, playerNumber);
                    System.out.println("I have connected to player" + playerNumber);
                    client.start();
                    sendPlayerNumberAndDeck(client, playerNumber, this.deck);
                    clients.add(client);
                    connected++;
                    if (waitForNextConnectionMilliSeconds != 0) {
                        waitForNextConnection(waitForNextConnectionMilliSeconds);
                    }
                } catch (SocketException error) {
                    System.out.println("Closed socket");
                }
            }
            this.numberOfPlayers = getConnectedClients()+1;
            game.setNumberOfPlayers(numberOfPlayers);
            game.getMenuScreenActors().allClientsHaveConnected();
            sendToAll(Messages.SHOW_SCREEN.toString());
            System.out.println("Connected! :D");
            sendToAll(converter.createNumberOfPlayersMessage(numberOfPlayers));
            if (mapPath!=null) {
                sendToAll(converter.createMapPathMessage(mapPath));
            }
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        connect(7);
    }

    /**
     * @param client to send to
     */
    public void sendPlayerNumberAndDeck(GameServerThreads client, int playerNumber, Deck deck) {
        client.sendMessage(converter.createPlayerNumberMessage(playerNumber));
        sendDeck(client, deck);
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
     * @param allClientsHaveSelectedCardsOrIsPoweredDown true if all have selected cards
     */
    public void setAllClientsHaveSelectedCardsOrIsPoweredDown(boolean allClientsHaveSelectedCardsOrIsPoweredDown) {
        this.allClientsHaveSelectedCardsOrIsPoweredDown = allClientsHaveSelectedCardsOrIsPoweredDown;
    }

    /**
     *
     * @return true if all clients have selected cards
     */
    public boolean allClientsHaveSelectedCardsOrIsPoweredDown() {
        return allClientsHaveSelectedCardsOrIsPoweredDown;
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

    public boolean allPoweredDownClientsHaveConfirmed() {
        return this.allPoweredDownClientsHaveConfirmed;
    }

    public void setAllPoweredDownClientsHaveConfirmed(boolean allPoweredDownClientsHaveConfirmed) {
        this.allPoweredDownClientsHaveConfirmed = allPoweredDownClientsHaveConfirmed;
    }

    /**
     * Stop while loop and close serversockt if false.
     * @param connectingToClients
     */
    public void setConnectingToClients(boolean connectingToClients) {
        this.connectingToClients = connectingToClients;
        if (connectingToClients == false) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getStopConnectingToClients() {
        return this.connectingToClients;
    }

    /***
     *
     * @param milliseconds time before connection is ended
     */
    public void setConnectingToClientsTimeout(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setConnectingToClients(false);
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***
     * Make the socket wait before listening for new client.
     *
     * @param milliseconds
     */
    public void setWaitBetweenEachConnection(int milliseconds) {
        this.waitForNextConnectionMilliSeconds = milliseconds;
    }

    /**
     * Wait given milliseconds before accepting new connection.
     * @param milliseconds
     */
    public void waitForNextConnection(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * When server ends connection the number of clients are given.
     *
     *
     * @return numberOfClients if server has ended connection, -1 if still connecting.
     */
    public int getConnectedClients() {
        if (!connectingToClients) {
            return clients.size();
        }
        return -1;
    }

    /**
     * When server has pressed start, mappath should be set.
     * Set mappath in game.
     * @param mapPath
     */
    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
        game.setMapPath(mapPath);
    }

}
