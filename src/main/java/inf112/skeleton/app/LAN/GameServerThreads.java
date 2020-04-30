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

    public void run() {
        try {
            while (true) {
                String message = getMessage();
                System.out.println("From client: "+message);
                if (message == null) {
                    break;
                }
                if (message.equals(Messages.ASKING_FOR_MAP.toString())) {
                    System.out.println("Client asked for map, map is " + game.getMapPath());
                    if (game.getMapPath() != null) {
                        sendMessage(Messages.HERE_IS_MAP.toString());
                        sendMessage(game.getMapPath());
                        System.out.println("Sent map to client");
                    }
                }
                // Close client socket if client is leaving, decrease num of players..
                else if (message.contains(Messages.QUIT.toString())) {
                    int playerNumber = Character.getNumericValue(message.charAt(0));
                    Player player = game.getBoard().getPlayer(playerNumber);
                    server.sendToAllExcept(player, playerNumber + Messages.QUIT.toString());
                    System.out.println("Player " + playerNumber + " is leaving...");
                    server.disconnect(playerNumber);
                    server.remove(playerNumber);
                    numberOfPlayers--;
                    return;
                } else {
                    ProgramCard card = converter.convertToCardAndExtractPlayer(message);
                    Player player = game.getBoard().getPlayer(converter.getPlayerNumber());
                    addSelectedCard(player, card);
                    if (allClientsHaveSelectedCards()) {
                        System.out.print("All clients have selected cards");
                        server.setAllClientsHaveSelectedCards(true);
                    }
                    if (allPlayersHaveSelectedCards()) {
                        System.out.println("Do turn");
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
