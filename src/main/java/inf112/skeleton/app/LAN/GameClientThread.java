package inf112.skeleton.app.LAN;

import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * Own thread for a client so client can get continous updates from server.
 * @author Jenny
 */
public class GameClientThread extends Thread {

    private Socket clientSocket;
    private int myPlayerNumber;
    private int numberOfPlayers;
    private InputStream input;
    private OutputStream output;
    private BufferedReader reader;
    private PrintWriter writer;
    private RallyGame game;
    private Converter converter;
    private Semaphore continueListening;
    private boolean haveSelectedCards;

    public GameClientThread(RallyGame game, Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.game = game;
        this.converter = new Converter();
        this.continueListening = new Semaphore(1);
        continueListening.tryAcquire();
        try {
            this.input = this.clientSocket.getInputStream();
            this.reader = new BufferedReader(new InputStreamReader(input));
            this.output = this.clientSocket.getOutputStream();
            this.writer = new PrintWriter(output, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Listen for incoming messages from server when thread is started.
     */
    @Override
    public void run() {
        while (true) {
            String message = getMessage();
            if (message == null) {
                break;
            }
            else if (message.equals(Messages.HOST_LEAVES.toString())) {
                System.out.println(message);
                close();
                return;
            }
            // If another player leaves.
            else if (message.contains(Messages.QUIT.toString())) {
                int playerNumber = Character.getNumericValue(message.charAt(0));
                System.out.println("Player " + playerNumber + " left game.");
            } else {
                ProgramCard card = converter.convertToCardAndExtractPlayer(message);
                Player player = game.getBoard().getPlayer(converter.getPlayerNumber());
                if (!allPlayersHaveReceivedCards() && !haveSelectedCards) {
                    player.addDealtCard(card);
                    System.out.println(allPlayersHaveReceivedCards());
                    System.out.println("Added dealt card ");
                } else {
                    System.out.println("Done receiving dealt cards");
                    for (Player play : game.getBoard().getPlayers()) {
                        System.out.println("Player " + play.getPlayerNr() + " "+play.getSelectedCards());
                    }
                    player.addSelectedCard(card);
                    if (allPlayersHaveSelectedCards()) {
                        startDoTurn();
                        waitForDoTurnToFinish();
                    }
                }
            }
        }
    }

    public void haveSelectedCards() {
        haveSelectedCards = true;
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
     *
     * @return true if all players have got their cards.
     */
    public boolean allPlayersHaveReceivedCards() {
        for (Player player : game.getBoard().getPlayers()) {
            if (player.getAllCards().size() < 9) {
                System.out.println("Player " + player.getPlayerNr() + " has " + player.getAllCards().size() + " dealt cards.");
                return false;
            }
        }
        return true;
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
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Read the playerNumber and numberOfPlayers that server has given to this player.
     */
    public void storeInitializationValuesFromSocket() {
        this.myPlayerNumber = Integer.parseInt(getMessage());
        this.numberOfPlayers = Integer.parseInt(getMessage());
    }

    /**
     * Send a message to server.
     * @param message message to be sent
     */
    public void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * @return playernumber assigned to this client by server
     */
    public int getMyPlayerNumber() {
        return this.myPlayerNumber;
    }

    /**
     *
     * @return numberOfPlayers in this game
     */
    public int getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    /**
     * Close the socket.
     */
    public void close() {
        try {
            this.clientSocket.close();
            System.out.println(Messages.CLOSED.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a string to send if you are exiting game.
     * @param playerNumber
     * @return string for quitting game
     */
    public String createQuitMessage(int playerNumber) {
        return playerNumber + Messages.QUIT.toString();
    }

}
