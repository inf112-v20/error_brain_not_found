package inf112.skeleton.app.LAN;

import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;
import org.lwjgl.Sys;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
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
    private boolean receivedCards;
    private Stack<ProgramCard> stack;

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

        // Get playernum and numberOfPlayers
        this.myPlayerNumber = Integer.parseInt(getMessage());
        this.numberOfPlayers = Integer.parseInt(getMessage());
        game.setPlayerNumber(myPlayerNumber);
        game.setNumberOfPlayers(numberOfPlayers);

        game.gotInitializedValues();

        while (true) {

            String message = getMessage();
            if (message == null) {
                break;
            }
            if (message.equals(Messages.HOST_LEAVES.toString())) {
                System.out.println(message);
                close();
                return;
            }
            // If another player leaves.
            else if (message.contains(Messages.QUIT.toString())) {
                int playerNumber = Character.getNumericValue(message.charAt(0));
                System.out.println("Player " + playerNumber + " left game.");
            }
            else if (message.equals(Messages.DECK_BEGIN.toString())) {
                    receivedCards = false;
                    stack = new Stack<>();
            }
            else if (message.equals(Messages.DECK_END.toString())) {
                    receivedCards = true;
                    game.setDeck(stack);
            }
            else if (!receivedCards) {
                ProgramCard card = converter.convertToCard(message);
                stack.add(card);
            } else {
                ProgramCard card = converter.convertToCardAndExtractPlayer(message);
                Player player = game.getBoard().getPlayer(converter.getPlayerNumber());
                if (allPlayersHaveSelectedCards()){
                    startDoTurn();
                    waitForDoTurnToFinish();
                } else {
                    player.addSelectedCard(card);
                }
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
     * Send a message to server.
     * @param message message to be sent
     */
    public void sendMessage(String message) {
        writer.println(message);
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
