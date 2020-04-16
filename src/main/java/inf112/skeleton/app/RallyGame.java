package inf112.skeleton.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.LAN.ServerThread;
import inf112.skeleton.app.LAN.Converter;
import inf112.skeleton.app.LAN.GameClientThread;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.objects.Laser;
import inf112.skeleton.app.objects.RotatePad;
import inf112.skeleton.app.screens.GifScreen;
import inf112.skeleton.app.screens.MenuScreen;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class RallyGame extends Game {

    public Board board;
    public Deck deck;
    public ArrayList<Player> players;
    public Semaphore waitForCards;
    public boolean playing;
    public Sound laserSound;
    public static Music gameMusic;

    public Player mainPlayer;
    private int numberOfPlayers;
    private int myPlayerNumber;
    private ServerThread serverThread;
    private boolean isServer;
    private GameClientThread client;
    private ProgramCard card;
    private Converter converter;
    private Semaphore waitForInitializeValues;

    public static float volume = 0.2f;
    public boolean unMute = true;

    public void create() {
        //TODO: Delete LoadingScreen if not used
        this.setScreen(new MenuScreen(this));
        startMusic();
        this.waitForInitializeValues = new Semaphore(1);
        this.waitForInitializeValues.tryAcquire();
    }

    public void setupGame(String mapPath) {

        setUpConnection();

        this.board = new Board(mapPath, this.numberOfPlayers);
        this.deck = new Deck();
        this.players = new ArrayList<>();
        this.players = board.getPlayers();
        this.mainPlayer = board.getPlayer(this.myPlayerNumber);
        this.waitForCards = new Semaphore(1);
        this.waitForCards.tryAcquire();
        this.playing = true;

        this.converter = new Converter();

        this.laserSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/LaserShot.mp3"));

        new Thread(this::doTurn).start();

        setInputProcessor();
        //dealCards();
       // selectCards();
    }

    public void setUpConnection() {
        // Try to create a client socket.
        try {
            Socket clientSocket = new Socket("localhost", 9000);
            System.out.println("I am a client :)");

            // Create new thread for speaking to server
            this.client = new GameClientThread(this, clientSocket);
            client.start();
            try {
                waitForInitializeValues.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            System.out.println("Did not find host.");
        } catch (IOException e) {
            this.isServer = true;
            this.myPlayerNumber = 1;
            System.out.println("How many players?");
            Scanner scanner = new Scanner(System.in);
            //TODO: Create check for number
            this.numberOfPlayers = scanner.nextInt();
            this.serverThread = new ServerThread(this, this.numberOfPlayers);
            serverThread.start();
            }
    }

    public void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (mainPlayer.isDead()) {
                    return false;
                }

                removeLasers();

                if (keycode == Input.Keys.RIGHT) {
                    mainPlayer.setDirection(Direction.EAST);
                    board.movePlayer(mainPlayer);
                } else if (keycode == Input.Keys.LEFT) {
                    mainPlayer.setDirection(Direction.WEST);
                    board.movePlayer(mainPlayer);
                } else if (keycode == Input.Keys.UP) {
                    mainPlayer.setDirection(Direction.NORTH);
                    board.movePlayer(mainPlayer);
                } else if (keycode == Input.Keys.DOWN) {
                    mainPlayer.setDirection(Direction.SOUTH);
                    board.movePlayer(mainPlayer);
                } else if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                } else if (keycode == Input.Keys.M) {
                    mute();
                    muteMusic();
                }
                // Send deck to all players.
                else if (keycode == Input.Keys.D) {
                    if (isServer) {
                        if (serverThread.getServer().allClientsAreConnected()) {
                            System.out.println("Dealing deck");
                            deck = new Deck();
                            deck.shuffleDeck();
                            dealCards();
                            serverThread.getServer().sendDeckToAll(deck);
                            System.out.println("Dealt cards");
                        } else {
                            System.out.println("Need to wait for players to connect before dealing cards.");
                        }
                    }
                }
                else if(keycode == Input.Keys.L) {
                    setUpConnection();
                }
                else if (keycode == Input.Keys.S) {
                    dealCards();
                    if (mainPlayer.getAllCards().size() >= 9) {
                        mainPlayer.selectCards();
                    } else {
                        System.out.println("Need to get cards first.");
                    }
                    System.out.println("Your program is: " + mainPlayer.getSelectedCards());
                    sendSelectedCards();
                }
                else if (keycode == Input.Keys.SPACE) {
                    cardsReady();
                    return super.keyDown(keycode);
                } else {
                    return super.keyDown(keycode);
                }

                if (mainPlayer.hasAllFlags(board.getFlags().size())) {
                    setWinScreen();
                }
                board.respawnPlayers();
                fireLasers();
                decreaseLives();
                removeDeadPlayers();
                return super.keyDown(keycode);
            }
        });
    }

    /**
     * Wait for server to send playernumber and number of players
     */
    public void gotInitializedValues() {
        waitForInitializeValues.release();
    }

    /**
     * Give all mainplayer's selected cards to the server.
     */
    public void sendSelectedCards() {
        if (!isServer) {
            for (ProgramCard card : mainPlayer.getSelectedCards()) {
                client.sendMessage(converter.convertToString(mainPlayer.getPlayerNr(), card));
            }
        }
    }

    public void mute(){
        if (unMute){
            volume = 0f;
            unMute = false;

        }
        else {
            volume = 0.5f;
            unMute = true;
        }
    }

    public  void loadMusic() {
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/menu_music.mp3"));
    }

    public  void muteMusic() {
       if (!unMute){
        gameMusic.setVolume(volume);}

       gameMusic.setVolume(volume);

    }

    public void startMusic() {
        loadMusic();
        gameMusic.setVolume(0.3f);
        gameMusic.play();

    }

    public void cardsReady() {
        waitForCards.release();
    }

    public void doTurn() {

        // TODO: Alle velger kort
        // TODO: Første kort spilles for alle i riktig rekkefølge
        // TODO: Gears roterer
        // TODO: Express belt flytter én
        // TODO: Express belt og vanlig belt flytter én
        // TODO: Spiller skyter
        // TODO: Laser skyter
        while (playing) {
            try {
                waitForCards.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Thread.interrupted()) {
                return;
            }
            for (int i = 0; i < 5; i++) {
                System.out.println("Runde " + (i + 1));
                allPlayersPlayCard();
                activateRotatePads();

                fireLasers();
                board.respawnPlayers();
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                removeLasers();
            }
            removeDeadPlayers();
            dealCards();
            mainPlayer.selectCards();
            System.out.println(mainPlayer.getSelectedCards());
            System.out.println("Your program is: " + mainPlayer.getSelectedCards());
            letClientsAndServerContinue();
            sendSelectedCards();
        }
    }

    /**
     * After playing cards, let server and clients exhange more cards.
     */
    public void letClientsAndServerContinue() {
        if (!isServer) {
            client.continueListening();
        } else {
            serverThread.getServer().continueAll();
        }
    }

    /**
     * Decrease lifetokens to each player that has collected 10 damagetokens.
     * Reset damagetokens and respawn player.
     */
    public void decreaseLives() {
        for (Player player : players) {
            if (player.getDamageTokens() >= 10) {
                player.decrementLifeTokens();
                player.resetDamageTokens();
                board.respawn(player);
            }
        }
    }

    public void selectCards() {
        for (Player player : players) {
            player.selectCards();
        }
    }

    public void dealCards() {
        for (Player player : players) {
            player.drawCards(deck);
        }
    }

    public void removeDeadPlayers() {
        ArrayList<Player> deadPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.isDead()) {
                board.removePlayerFromBoard(player);
                deadPlayers.add(player);
            }
        }
        players.removeAll(deadPlayers);
    }

    public void allPlayersPlayCard() {
        ArrayList<Player> playerOrder = new ArrayList<>(players);
        // Add all players to order list, and remove players with no cards left
        playerOrder.removeIf(p -> p.getSelectedCards().isEmpty());
        playerOrder.sort(new PlayerSorter());

        for (Player player : playerOrder) {
            playCard(player, nextCard(player));
            // Wait 1 second for each player
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get new card for player. When hand is empty new cards are given.
     * @param player
     * @return the next card player is going to play
     */
    public ProgramCard nextCard(Player player) {
        if (player.getSelectedCards().isEmpty()) {
            System.out.println("Getting new cards...");
            mainPlayer.drawCards(deck);
            mainPlayer.selectCards();
        }
        this.card = player.getSelectedCards().remove(0);
        return card;
    }

    /**
     * Move player according to its playerCard.
     * @param player
     * @param card
     */
    public void playCard(Player player, ProgramCard card) {
        switch (card.getRotate()) {
            case RIGHT:
                player.setDirection(player.getDirection().turnRight());
                board.rotatePlayer(player);
                break;
            case LEFT:
                player.setDirection(player.getDirection().turnLeft());
                board.rotatePlayer(player);
                break;
            case UTURN:
                player.setDirection(player.getDirection().turnAround());
                board.rotatePlayer(player);
                break;
            case NONE:
                for (int i = 0; i < card.getDistance(); i++) {
                    board.movePlayer(player);
                    // Wait 500 ms for each move except last one
                    if (i < card.getDistance() - 1) {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            default:
                break;
        }
        deck.addCardToDiscardPile(card);
    }

    public void setWinScreen() {
        this.dispose();
        this.setScreen(new GifScreen(this));
    }

    public void removeLasers() {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                board.laserLayer.setCell(x, y, null);
            }
        }
    }

    public void fireLasers() {
        for (Laser laser : board.lasers) {
            laser.fire(this);
        }
        laserSound.play();
    }

    public void activateRotatePads(){
        for(Player player : board.getPlayers()){
            for(RotatePad rotatePad : board.rotatePads){
                Vector2 playerPosition = player.getPosition();
                Vector2 rotePadPosition = rotatePad.getPosition();

                if(playerPosition.equals(rotePadPosition)){
                    Rotate rotateDirection = rotatePad.getRotate();
                    Direction playerDirection = player.getDirection();

                    switch (rotateDirection){
                        case LEFT:
                            player.setDirection(playerDirection.turnLeft());
                            break;
                        case RIGHT:
                            player.setDirection(playerDirection.turnRight());
                            break;
                        case UTURN:
                            player.setDirection(playerDirection.turnAround());
                            break;
                        default:
                            // Will never happen
                    }
                }
            }
        }
    }

    /**
     * Close sockets on exit.
     */
    public void dispose() {
        // Tell server you are leaving. Close your socket.
        if (!isServer) {
            this.client.sendMessage(this.client.createQuitMessage(this.myPlayerNumber));
            this.client.close();
            this.client.sendMessage(Messages.CLOSED.toString());
        }
        // Close all sockets in serverthread
        if (isServer) {
            this.serverThread.getServer().sendToAll(Messages.HOST_LEAVES.toString());
            this.serverThread.getServer().disconnectAll();
            System.out.println(Messages.CLOSED.toString());
        }
        this.screen.dispose();
    }

    public Board getBoard() {
        return this.board;
    }

    /**
     * Is used when server has sent the stack to all players.
     * @param stack of cards for this game.
     */
    public void setDeck(Stack<ProgramCard> stack) {
        this.deck.setDeck(stack);
    }

    public void setPlayerNumber(int playerNumber) {
        this.myPlayerNumber = playerNumber;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}
