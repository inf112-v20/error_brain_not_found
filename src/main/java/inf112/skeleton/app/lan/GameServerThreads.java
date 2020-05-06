package inf112.skeleton.app.lan;

import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.cards.Register;
import inf112.skeleton.app.cards.Registers;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.objects.player.Player;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;

/**
 * Make a thread for each client connecting.
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
     * The player who sent this Programcard will have it added to its {@link Registers}.
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
                    endConnectionWithPlayerAndTellOtherPlayersThatThisPlayerLeft(getPlayerFromMessage(message));
                    game.quitPlaying();
                    return;
                }
                if (converter.isMessageFromAnotherPlayer(message)) {
                    int playerNumber = converter.getPlayerNumberFromMessage(message);
                    String messageFromPlayer = converter.getMessageFromPlayer(message);
                    Player player = game.getBoard().getPlayer(playerNumber);
                    if (messageFromPlayer.equals(Messages.POWERING_DOWN.toString())) {
                        player.setPoweringDown(true);
                        server.sendToAllExcept(player, message);
                    }
                    else if (messageFromPlayer.equals(Messages.CONTINUE_POWER_DOWN.toString())) {
                        player.setConfirmedPowerUp(true);
                        if (allPoweredDownClientsHaveConfirmed()) {
                            if (server.serverHasConfirmed()) {
                                server.setAllPoweredDownClientsHaveConfirmed(false);
                                server.setServerHasConfirmed(false);
                                server.sendToAll(Messages.CONTINUE_TURN.toString());
                                game.continueTurn();
                                waitForTurnToFinish();
                            } else {
                                server.setAllPoweredDownClientsHaveConfirmed(true);
                            }
                        }
                    }
                    else if (messageFromPlayer.equals(Messages.POWER_UP.toString())) {
                        player.setPoweredDown(false);
                        player.setConfirmedPowerUp(true);
                        game.removePoweredDownPlayer(player);
                        server.sendToAllExcept(player, message);
                        if (allPoweredDownClientsHaveConfirmed()) {
                            if (server.serverHasConfirmed()) {
                                server.setAllPoweredDownClientsHaveConfirmed(false);
                                server.setServerHasConfirmed(false);
                                server.sendToAll(Messages.CONTINUE_TURN.toString());
                                game.continueTurn();
                                waitForTurnToFinish();
                            } else {
                                server.setAllPoweredDownClientsHaveConfirmed(true);
                            }
                        }
                    }
                    else {
                        PlayerAndProgramCard playerAndCard = converter.getSentCardFromPlayer(message);
                        ProgramCard card = playerAndCard.getProgramCard();
                        addSelectedCard(player, card);
                        if (allPlayersHaveSelectedCardsOrInPowerDown() && server.serverHasConfirmed()) {
                            server.sendSelectedCardsToAll();
                            server.sendToAll(Messages.START_TURN.toString());
                            server.setServerHasConfirmed(false);
                            game.startTurn();
                            waitForTurnToFinish();
                        }
                        if (allClientsHaveSelectedCardsOrInPowerDown()) {
                            server.setAllClientsHaveSelectedCardsOrIsPoweredDown(true);
                        }
                    }
                }
            }
        } catch (NotProgramCardException e) {
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
    public void waitForTurnToFinish() {
        try {
            continueListening.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Let server continue loop
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

    public boolean allPoweredDownClientsHaveConfirmed() {
        for (Player player : game.getPoweredDownRobots()) {
            if (player.getPlayerNr() != 1 && !player.hasConfirmedPowerUp()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return Player from message
     */
    public Player getPlayerFromMessage(String message) {
        int playerNumber = converter.getPlayerNumberFromMessage(message);
        return game.getBoard().getPlayer(playerNumber);
    }
}
