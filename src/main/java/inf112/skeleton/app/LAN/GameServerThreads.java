package inf112.skeleton.app.LAN;


import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;

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
     * What the thread is doing when it is started.
     */
    public void run() {
        try {
            // Let the player know what the playernumber is
            sendMessage(this.playerNumber+"");
            sendMessage(this.numberOfPlayers+"");

            while (true) {
                String message = reader.readLine();
                if (message == null) {
                    break;
                }
                // Close client socket if client is leaving, decrease num of players..
                if (message.contains(Messages.QUIT.toString())) {
                    int playerNumber = Character.getNumericValue(message.charAt(0));
                    Player player = game.getBoard().getPlayer(playerNumber);
                    server.sendToAllExcept(player, "Player " + playerNumber + " is leaving...");
                    System.out.println("Player " + playerNumber + " is leaving...");
                    server.disconnect(playerNumber);
                    server.remove(playerNumber);
                    numberOfPlayers--;
                    return;
                }
                ProgramCard card = converter.convertToCardAndExtractPlayer(message);
                Player player = game.getBoard().getPlayer(converter.getPlayerNumber());
                addSelectedCard(player, card);
                if (allPlayersHaveSelectedCards()) {
                    server.sendSelectedCardsToAll();
                    startDoTurn();
                    waitForDoTurnToFinish();
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
     * Send given players dealt cards to this client
     * @param player
     */
    public void sendDealtCards(Player player) {
        for (ProgramCard card : player.getAllCards()) {
            sendMessage(converter.convertToString(player.getPlayerNr(), card));
        }
    }

    /**
     *
     * @return true if all players have selected their cards.
     */
    public boolean allPlayersHaveSelectedCards() {
        for (Player player : game.getBoard().getPlayers()) {
            if (player.getSelectedCards().size() < 5) {
                return false;
            }
        }
        return true;
    }

    /**
     * Send given players selected cards to this client
     * @param player
     */
    public void sendSelectedCards(Player player) {
        for (ProgramCard card : player.getSelectedCards()) {
            sendMessage(converter.convertToString(player.getPlayerNr(), card));
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
