package inf112.skeleton.app.LAN;

import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.objects.player.Player;

import java.io.*;
import java.net.Socket;
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
    private Stack<ProgramCard> stack;
    private boolean receivingDeck;
    private boolean receivingMap;


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
     * Listen for messages from server. If no messages match, it is a {@link ProgramCard} and should
     * be added to the player is belongs to (it is discarded if it is yours, since you have selected cards)
     *
     */
    @Override
    public void run() {

        getStartValues();

        while (true) {
            String message = getMessage();
            if (message == null) {
                break;
            }
            if (message.equals(Messages.HOST_LEAVES.toString())) {
                System.out.println("Host left game.");
                game.quitPlaying();
                close();
                return;
            } else if (message.equals(Messages.HERE_IS_MAP.toString())){
                receivingMap = true;
            } else if (message.contains(Messages.QUIT.toString())) {
                int playerNumber = getPlayerNumberFromMessage(message);
                System.out.println("Player " + playerNumber + " left game.");
            } else if (message.equals(Messages.DECK_BEGIN.toString())) {
                stack = new Stack<>();
                receivingDeck = true;
            } else if (message.equals(Messages.DECK_END.toString())){
                receivingDeck = false;
                game.setDeck(stack);
                System.out.println("Received deck.");
                game.setWaitForServerToSendStartValuesToRelease();
            } else if (message.equals(Messages.START_TURN.toString())) {
                startDoTurn();
                waitForTurnToFinish();
            } else if (receivingDeck) {
                ProgramCard card = converter.convertToCard(message);
                stack.add(card);
            } else if (receivingMap) {
                game.setMapPath(message);
                game.setWaitForServerToSendMapPathToRelease();
                receivingMap = false;
                System.out.println("Got map");
            } else {
                ProgramCard card = converter.convertToCardAndExtractPlayer(message);
                // Your player have already selected cards
                if (myPlayerNumber != converter.getPlayerNumber()) {
                    Player player = game.getBoard().getPlayer(converter.getPlayerNumber());
                    player.addSelectedCard(card);
                }
            }
        }
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
     * Wait for server to send your playernumber and how many players are in the game.
     * Give the values to {@link RallyGame} so it can be initialized.
     */
    private void getStartValues() {
        this.myPlayerNumber = Integer.parseInt(getMessage());
        this.numberOfPlayers = Integer.parseInt(getMessage());
        game.setPlayerNumber(myPlayerNumber);
        game.setNumberOfPlayers(numberOfPlayers);
    }

    /**
     * Ask server to send you the map.
     */
    public void askForMap() {
        sendMessage(Messages.ASKING_FOR_MAP.toString());
        System.out.print("Asking for map....");
    }

    /**
     * Wait for doTurn to realease in game.
     */
    private void waitForTurnToFinish() {
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
