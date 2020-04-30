package inf112.skeleton.app.LAN;

import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.cards.Register;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.objects.player.Player;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

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
    private Semaphore continueListening;

    public GameServerThreads(GameServer server, RallyGame game, Socket client, int playerNumber, int numberOfPlayers) {
        this.client = client;
        this.playerNumber = playerNumber;
        this.numberOfPlayers = numberOfPlayers;
        this.server = server;
        this.game = game;
        this.converter = new Converter();
        this.continueListening = new Semaphore(1);
        continueListening.tryAcquire();

        try {
            input = client.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        reader = new BufferedReader(new InputStreamReader(input));

    }

    /**
     * Receive messages from client. If none of the messages match, then it is a {@link ProgramCard}.
     * The player who sent this Programcard will have it added to its {@link inf112.skeleton.app.cards.Registers}.
     *
     * When all clients have sent their cards, the {@link GameServer} will be notified, and it will wait until
     * the host confirm its cards before sending cards to the clients and start the turn. If a client is the last
     * one to confirm its cards, then {@link #allPlayersHaveSelectedCards()} will be true and server will send all
     * cards to players and tell them to start the turn.
     */
    public void run() {
        try {
            while (true) {
                String message = getMessage();
                if (message == null) {
                    break;
                }
                if (message.equals(Messages.ASKING_FOR_MAP.toString())) {
                    sendMap();
                }
                else if (message.contains(Messages.QUIT.toString())) {
                    int playerNumber = getPlayerNumberFromMessage(message);
                    endConnectionWithPlayerAndTellOtherPlayersThatThisPlayerLeft(game.getBoard().getPlayer(playerNumber));
                    game.quitPlaying();
                    return;
                } else {
                    ProgramCard card = converter.convertToCardAndExtractPlayer(message);
                    Player player = game.getBoard().getPlayer(converter.getPlayerNumber());
                    addSelectedCard(player, card);
                    if (allClientsHaveSelectedCards()) {
                        server.setAllClientsHaveSelectedCards(true);
                    }
                    if (allPlayersHaveSelectedCards()) {
                        server.sendSelectedCardsToAll();
                        server.sendToAll(Messages.START_TURN.toString());
                        startDoTurn();
                        waitForDoTurnToFinish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * When host has called on {@link RallyGame#setupGame(String)} (pressed Start), the host have
     * picked a map and it will be sent to client. If host has not pressed Start yet, it will not send the map.
     */
    private void sendMap() {
        if (game.getMapPath() != null) {
            sendMessage(Messages.HERE_IS_MAP.toString());
            sendMessage(game.getMapPath());
            System.out.println("Sent map to client");
        }
    }

    /**
     *
     * @param player to end connection with
     */
    private void endConnectionWithPlayerAndTellOtherPlayersThatThisPlayerLeft(Player player) {
        server.sendToAllExcept(player, playerNumber + Messages.QUIT.toString());
        System.out.println("Player " + playerNumber + " is leaving...");
        server.disconnect(playerNumber);
        server.remove(playerNumber);
        numberOfPlayers--;
    }

    /**
     * Playernumber is always first in the message.
     *
     * @param message
     * @return the playerNumber for the player sending this message
     */
    private int getPlayerNumberFromMessage(String message) {
        return Character.getNumericValue(message.charAt(0));
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
                client.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * @return true if all clients have selected cards. Server has playernr 1
     */
    private boolean allClientsHaveSelectedCards() {
        for (Player player : game.getBoard().getPlayers()) {
            if (player.getPlayerNr() != 1) {
                if (player.getRegisters().hasRegistersWithoutCard()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Wait for doTurn to realease in game.
     */
    private void waitForDoTurnToFinish() {
        try {
            continueListening.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tell game that cards are ready, doTurn can begin.
     */
    private void startDoTurn() {
        game.cardsReady();
    }

    /**
     * Let client continue loop
     */
    public void continueListening() {
        continueListening.release();
    }


    /**
     * Add a card to players program.
     * @param player
     * @param card
     */
    public void addSelectedCard(Player player, ProgramCard card) {
        player.addSelectedCard(card);
    }

    /**
     *
     * @return true if all players have selected their cards.
     */
    public boolean allPlayersHaveSelectedCards() {
        for (Player player : game.getBoard().getPlayers()) {
            if (player.getRegisters().hasRegistersWithoutCard()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Send given player's selected cards to this client
     * @param player
     */
    public void sendSelectedCards(Player player) {
        for (Register register : player.getRegisters().getRegisters()) {
            sendMessage(converter.convertToString(player.getPlayerNr(), register.getProgramCard()));
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
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
