package inf112.skeleton.app.lan;

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

    private Socket serverSideSocket;
    private int playerNumber;
    private GameServer server;
    private PrintWriter writer;
    private BufferedReader reader;
    private RallyGame game;
    private Converter converter;
    private Semaphore continueListening;
    private String sentMessage;

    public GameServerThreads(GameServer server, RallyGame game, Socket serverSideSocket, int playerNumber) {
        this.serverSideSocket = serverSideSocket;
        this.playerNumber = playerNumber;
        this.server = server;
        this.game = game;
        this.converter = new Converter();
        this.continueListening = new Semaphore(1);
        continueListening.tryAcquire();
        try {
            reader = new BufferedReader(new InputStreamReader(serverSideSocket.getInputStream()));
            writer = new PrintWriter(serverSideSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive messages from client. If none of the messages match, then it is a {@link ProgramCard}.
     * The player who sent this Programcard will have it added to its {@link inf112.skeleton.app.cards.Registers}.
     *
     * When all clients have sent their cards, the {@link GameServer} will be notified, and it will wait until
     * the host confirm its cards before sending cards to the clients and start the turn. If a client is the last
     * one to confirm its cards, then {@link #allPlayersHaveSelectedCardsOrInPowerDown()} will be true and server will send all
     * cards to players and tell them to start the turn.
     */
    public void run() {
        try {
            while (true) {
                String message = getMessage();
                if (message == null) {
                    break;
                }
                if (message.equals(Messages.STOP_THREAD.toString())) {
                    return;
                }
                if (message.contains(Messages.QUIT.toString())) {
                    int playerNumber = getPlayerNumberFromMessage(message);
                    endConnectionWithPlayerAndTellOtherPlayersThatThisPlayerLeft(game.getBoard().getPlayer(playerNumber));
                    game.quitPlaying();
                    return;
                }
                else if (message.contains(Messages.POWER_DOWN.toString())) {
                    int playerNumber = getPlayerNumberFromMessage(message);
                    Player player = game.getBoard().getPlayer(playerNumber);
                    player.setPoweredDown(true);
                    server.sendToAllExcept(player, message);
                    if (allClientsHaveSelectedCardsOrInPowerDown()) {
                        server.setAllClientsHaveSelectedCards(true);
                    }
                }
                else {
                    PlayerAndProgramCard playerAndCard = converter.convertToCardAndExtractPlayer(message);
                    ProgramCard card = playerAndCard.getProgramCard();
                    int playerNumber = playerAndCard.getPlayerNumber();
                    Player player = game.getBoard().getPlayer(playerNumber);
                    addSelectedCard(player, card);
                    if (allPlayersHaveSelectedCardsOrInPowerDown() && server.serverHasConfirmed()) {
                        server.sendSelectedCardsToAll();
                        server.sendToAll(Messages.START_TURN.toString());
                        server.setServerHasConfirmed(false);
                        startDoTurn();
                        waitForDoTurnToFinish();
                    }
                    if (allClientsHaveSelectedCardsOrInPowerDown()) {
                        server.setAllClientsHaveSelectedCards(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                serverSideSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * @return true if all clients have selected cards or in power down. Server has playernr 1
     */
    public boolean allClientsHaveSelectedCardsOrInPowerDown() {
        for (Player player : game.getBoard().getPlayers()) {
            if (player.getPlayerNr() != 1 && (player.getRegisters().hasRegistersWithoutCard() && !player.isPoweredDown())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Wait for doTurn to realease in game.
     */
    public void waitForDoTurnToFinish() {
        try {
            continueListening.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tell game that cards are ready, doTurn can begin.
     */
    public void startDoTurn() {
        game.continueGameLoop();
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
    public boolean allPlayersHaveSelectedCardsOrInPowerDown() {
        for (Player player : game.getBoard().getPlayers()) {
            if (player.getRegisters().hasRegistersWithoutCard() && !player.isPoweredDown()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Send given player's selected cards to this client. Player in
     * power down do not send out their cards.
     * @param player
     */
    public void sendSelectedCards(Player player) {
        if (!player.isPoweredDown()) {
            for (Register register : player.getRegisters().getRegisters()) {
                sendMessage(converter.convertToString(player.getPlayerNr(), register.getProgramCard()));
            }
        }
    }

    /**
     * Send a message to this client.
     * @param message
     */
    public void sendMessage(String message) {
        this.sentMessage = message;
        writer.println(message);

    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void close() {
        try {
            this.serverSideSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     *
     * @return the last sent message to the client
     */
    public String getLastSentMessage() {
        return sentMessage;
    }
}
