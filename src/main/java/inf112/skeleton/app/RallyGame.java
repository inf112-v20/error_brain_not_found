package inf112.skeleton.app;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.LAN.Converter;
import inf112.skeleton.app.LAN.GameClientThread;
import inf112.skeleton.app.LAN.GameServer;
import inf112.skeleton.app.LAN.ServerThread;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.cards.Register;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.objects.Belt;
import inf112.skeleton.app.objects.Laser;
import inf112.skeleton.app.objects.RotatePad;
import inf112.skeleton.app.objects.player.Player;
import inf112.skeleton.app.objects.player.PlayerSorter;
import inf112.skeleton.app.screens.ButtonSkin;
import inf112.skeleton.app.screens.gamescreen.GameScreen;
import inf112.skeleton.app.screens.gifscreen.GifScreen;
import inf112.skeleton.app.screens.menuscreen.MenuScreen;
import inf112.skeleton.app.screens.menuscreen.MenuScreenActors;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class RallyGame extends Game {

    public Board board;
    public Deck deck;

    public ArrayList<Player> players;
    public ArrayList<Player> respawnPlayers;
    public Semaphore waitForCards;
    public boolean playing;
    public boolean shouldPickCards;
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

    public ButtonSkin buttonSkins;

    public static float volume = 0.5f;
    private String mapPath;

    public void create() {
        this.buttonSkins = new ButtonSkin();
        this.setScreen(new MenuScreen(this));
        startMusic();
    }

    /**
     * Set up game with a connection.
     *
     * @param mapPath
     */
    public void setupGame(String mapPath) {
        this.mapPath = mapPath;
        this.board = new Board(mapPath, this.numberOfPlayers);
        this.players = new ArrayList<>();
        this.players = board.getPlayers();
        this.mainPlayer = board.getPlayer(this.myPlayerNumber);
        this.respawnPlayers = new ArrayList<>();
        this.waitForCards = new Semaphore(1);
        this.waitForCards.tryAcquire();
        this.playing = true;
        this.converter = new Converter();
        this.shouldPickCards = true;

        this.laserSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/LaserShot.mp3"));

        new Thread(this::doTurn).start();

        // If clients connect after you start game, give them the map
        if (isServer) {
            Thread sendMap = new Thread(() -> {
                while (!serverThread.getServer().allClientsHaveReceivedMap()) {
                    serverThread.getServer().sendMapPathToNewlyConnectedClients(mapPath);
                }
            });
            sendMap.start();
        }

        setInputProcessor();

        dealCards();
    }

    /**
     * Set up a host for this game.
     * @param portNumber on port
     * @param numberOfPlayers in this game (including host)
     */
    public void setUpHost(int portNumber, int numberOfPlayers) {
        this.isServer = true;
        this.myPlayerNumber = 1;
        this.numberOfPlayers = numberOfPlayers;
        this.serverThread = new ServerThread(this, numberOfPlayers, portNumber);
        serverThread.start();
    }

    /**
     * Try to establish a connection with host IP on port portNumber. If no connection
     * can be made game ends.
     *
     * @param hostIP     IP to connect to
     * @param portNumber to establish connection with
     */
    public void setUpClient(String hostIP, int portNumber) {
        try {
            Socket clientSocket = new Socket(hostIP, portNumber);
            System.out.println("I am a client, connected to " + hostIP + " port " + portNumber);
            // Create new thread for speaking to server
            this.client = new GameClientThread(this, clientSocket);
            client.start();
        } catch (UnknownHostException e) {
            System.out.println("Did not find host.");
            Gdx.app.exit();
        } catch (IOException e) {
            System.out.println("Could not connect to " + hostIP + " on port " + portNumber + " Quit.");
            Gdx.app.exit();
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
                    muteMusic();
                    muteSounds();
                }

                else if (keycode == Input.Keys.D) {

                    System.out.println("Pressed d");
                        dealCards();

                } else if (keycode == Input.Keys.SPACE) {
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
                return super.keyDown(keycode);
            }
        });
    }

    /**
     * Give all mainplayer's selected cards to the server.
     */
    public void sendSelectedCards() {
        if (!isServer) {
            for (Register register : mainPlayer.getRegisters().getRegisters()) {
                client.sendMessage(converter.convertToString(mainPlayer.getPlayerNr(), register.getProgramCard()));
            }
        }
    }

    public void setShouldPickCards(boolean shouldPickCards) {
        this.shouldPickCards = shouldPickCards;
    }

    public boolean shouldPickCards() {
        return shouldPickCards;
    }

    public void confirmCards() {
        if (!mainPlayer.getRegisters().hasRegistersWithoutCard()) {
            sendSelectedCards();
            if (isServer) {
                GameServer server = serverThread.getServer();
                if (server.allClientsHaveSelectedCards()) {
                    System.out.println("All clients selected cards");
                    server.sendSelectedCardsToAll();
                    server.sendToAll(Messages.START_TURN.toString());
                    cardsReady();
                }
            }
        }
    }

    @Override
    public void setScreen (Screen screen){
        if (this.screen != null) {
            this.screen.dispose();
        }
        super.setScreen(screen);
    }

    public StandardScreen getScreen () {
        return (StandardScreen) super.getScreen();
    }

    public void muteMusic () {
        gameMusic.setVolume(gameMusic.getVolume() == 0 ? 0.5f : 0);
    }

    public void muteSounds () {
        volume = volume == 0 ? 0.5f : 0;
    }
    public void loadMusic() {
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/menu_music.mp3"));
    }

    public void startMusic() {
        loadMusic();
        gameMusic.setVolume(0.5f);
        gameMusic.setLooping(true);
        gameMusic.play();
    }

    public void cardsReady () {
        waitForCards.release();
    }

    /**
    1. Deal the Program cards.
    2. Arrange your Program cards face down among your
       five registers.
    3. Announce intent to power down or continue running
       NEXT turn.
    4. Complete each register in order:
            A. Reveal Program Cards
            B. Robots Move
            C. board Elements Move (Gears, Express belt, normal belt)
                1. Express conveyor belts move 1 space in the direction of the arrows.
                2. Express conveyor belts and normal conveyor belts move 1 space in the
                   direction of the arrows.
                3. Pushers push if active.
                4. Gears rotate 90° in the direction of the arrows.
            D. Lasers Fire (player, then board)
            E. Touch Checkpoints (Flag, Repair)
    5. Clean up any end-of-turn effects
    */
    public void doTurn () {

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

                // All players play one card in the correct order
                allPlayersPlayCard(i);
                sleep(250);

                // Express belts move 1
                activateBelts(true);
                sleep(250);

                decreaseLives();

                // All belts move 1
                activateBelts(false);
                sleep(250);

                // Rotate pads rotate
                activateRotatePads();
                sleep(250);

                // Fire lasers for 250 ms
                firePlayerLaser();
                sleep(250);
                removeLasers();
                sleep(500);

                decreaseLives();
                if (!board.getLasers().isEmpty()) {
                    fireLasers();
                    sleep(250);
                    removeLasers();
                    sleep(500);

                    decreaseLives();
                }

                activateRepairTiles();
                sleep(250);

                pickUpFlags();
                sleep(500);

                sleep(1000);
            }
            if (!respawnPlayers.isEmpty()) {
                respawnPlayers();
            }
            updateRegisters();
            discardCards();
            // Dealcards draw 9 cards, so deck needs to be larger than 9
            if (deck.deckSize() < 9) {
                if (isServer) {
                    serverThread.getServer().createAndSendDeckToAll();
                }
            }

            dealCards();

            ((GameScreen) screen).updateCards();
            letClientsAndServerContinue();
            sendSelectedCards();
        }
    }

    /**
     * After playing cards, let server and clients exhange more cards.
     */
    public void letClientsAndServerContinue () {
        if (!isServer) {
            client.continueListening();
        } else {
            serverThread.getServer().continueAll();
            setShouldPickCards(true);
        }
    }

    private void updateRegisters() {
        for (Player player : players) {
            player.updateRegisters();
        }
    }

    private void pickUpFlags () {
        board.pickUpFlags();
    }

    private void sleep ( int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void dealCards () {
        for (Player player : players) {
            System.out.print("Player  " + player.getPlayerNr() + " draw cards");
            player.drawCards(deck);
        }
    }

    public void discardCards () {
        for (Player player : players) {
            player.discardCards(deck);
        }
    }

    /**
     * Decrease life tokens to each player that has collected 10 damage tokens.
     * Reset damage tokens, remove player from board and discard all cards
     */
    public void decreaseLives () {
        ArrayList<Player> removedPlayers = new ArrayList<>();
        for (Player player : players) {
            if (player.getDamageTokens() >= 10 || board.getBoardLogic().outsideBoard(player, board)) {
                player.decrementLifeTokens();
                player.resetDamageTokens();
                board.removePlayerFromBoard(player);
                removedPlayers.add(player);
            }
        }
        if (!removedPlayers.isEmpty()) {
            players.removeAll(removedPlayers);
            respawnPlayers.addAll(removedPlayers);
        }
    }

    public void respawnPlayers () {
        for (Player player : respawnPlayers) {
            if (!player.isDead()) {
                players.add(player);
                board.respawn(player);
            }
        }
        respawnPlayers.clear();
    }
    public void allPlayersPlayCard(int cardNumber) {
        ArrayList<Player> playerOrder = new ArrayList<>(players);
        // Add all players to order list, and remove players with no cards left
        // playerOrder.removeIf(p -> p.getSelectedCards().isEmpty());
        playerOrder.sort(new PlayerSorter());

        for (Player player : playerOrder) {
            playCard(player, cardNumber);
            // Wait 1 second for each player
            sleep(500);
            //decreaseLives();
            sleep(500);
        }
    }

    public void playCard(Player player, int cardNumber) {
        player.setBeltPushDir(null);
        ProgramCard card = player.getRegisters().getCard(cardNumber);
        System.out.println(player.toString() + " played " + card.toString());
        switch (card.getRotate()) {
            case RIGHT:
                player.setDirection(player.getDirection().turnRight());
                break;
            case LEFT:
                player.setDirection(player.getDirection().turnLeft());
                break;
            case UTURN:
                player.setDirection(player.getDirection().turnAround());
                break;
            case NONE:
                for (int i = 0; i < card.getDistance(); i++) {
                    board.movePlayer(player);
                    // Wait 300 ms for each move except last one
                    if (i < card.getDistance() - 1) {
                        sleep(300);
                    }
                }
                break;
            default:
                break;
        }
        board.addPlayer(player);
        deck.addCardToDiscardPile(card);
    }

    public void setWinScreen () {
        setScreen(new GifScreen(this));
    }

    public void removeLasers () {
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                board.getLaserLayer().setCell(x, y, null);
            }
        }
    }

    public void firePlayerLaser() {
        if (!players.isEmpty()) {
            for (Player player : players) {
                player.fire(this);
            }
            laserSound.play(volume);
        }
    }

    public void fireLasers() {
        if (!board.getLasers().isEmpty()) {
            for (Laser laser : board.getLasers()) {
                laser.fire(this);
            }
            laserSound.play(volume);
        }
    }

    public void activateRotatePads () {
        for (Player player : board.getPlayers()) {
            for (RotatePad pad : board.getRotatePads()) {
                Vector2 playerPosition = player.getPosition();
                Vector2 padPosition = pad.getPosition();

                if (playerPosition.equals(padPosition)) {
                    pad.rotate(player);
                    board.addPlayer(player);
                    sleep(500);
                }
            }
        }
    }

    /**
     * <p>
     * Activate the belts on the map, so they pushes the player in the direction of the belt.
     * </p>
     *
     * @param onlyExpress if true then the pool of belts should be set to expressBelts
     */
    public void activateBelts(boolean onlyExpress) {
        ArrayList<Belt> belts = onlyExpress ? board.getExpressBelts() : board.getBelts();
        for (Player player : board.getPlayers()) {
            for (Belt belt : belts) {
                if (player.getPosition().equals(belt.getPosition())) {
                    beltPush(player, belt);
                }
            }
        }
        validateBeltPushPos();
        updateBoard();
    }

    private void updateBoard () {
        board.removePlayersFromBoard();
        updatePositionsAfterBeltPush();
        board.updateBoard();
    }

    public void validateBeltPushPos () {
        for (Player player : players) {
            for (Player otherPlayer : players) {
                if (player.getBeltPushPos() != null && !player.equals(otherPlayer) && player.getBeltPushPos().equals(otherPlayer.getBeltPushPos())) {
                    player.setBeltPushPos(null);
                    otherPlayer.setBeltPushPos(null);
                }
            }
        }
    }

    public void updatePositionsAfterBeltPush () {
        for (Player player : players) {
            if (player.getBeltPushPos() != null) {
                player.setPosition(player.getBeltPushPos());
                player.setBeltPushPos(null);
            }
        }
    }

    public void beltPush (Player player, Belt belt){
        Direction lastPush = player.getBeltPushDir();
        Direction beltDirection = belt.getDirection();
        if (lastPush != null) {
            switch (lastPush) {
                case NORTH:
                    setPlayerDirectionAfterBeltPush(player, beltDirection, Direction.EAST, Direction.WEST);
                    break;
                case SOUTH:
                    setPlayerDirectionAfterBeltPush(player, beltDirection, Direction.WEST, Direction.EAST);
                    break;
                case EAST:
                    setPlayerDirectionAfterBeltPush(player, beltDirection, Direction.SOUTH, Direction.NORTH);
                    break;
                case WEST:
                    setPlayerDirectionAfterBeltPush(player, beltDirection, Direction.NORTH, Direction.SOUTH);
                    break;
                default:
                    break;
            }
        }
        player.setBeltPushDir(beltDirection);
        player.setBeltPushPos(board.getNeighbourPosition(player.getPosition(), beltDirection));
    }

    public void setPlayerDirectionAfterBeltPush (Player player, Direction beltDirection, Direction
    turnRight, Direction leftTurn){
        if (beltDirection.equals(turnRight)) {
            player.setDirection(player.getDirection().turnRight());
        } else if (beltDirection.equals(leftTurn)) {
            player.setDirection(player.getDirection().turnLeft());
        }
    }

    public Board getBoard () {
        return this.board;
    }

    /**
     * Is used when server has sent the stack to all players.
     * @param stack of cards for this game.
     */
    public void setDeck (Stack <ProgramCard> stack) {
        if (deck != null) {
            this.deck.setDeck(stack);
        } else {
            deck = new Deck();
            deck.setDeck(stack);
        }
    }

    public void activateRepairTiles() {
        for (Player player : players){
            for (Vector2 repairTilePos : board.getRepairTiles()){
                if (player.getPosition().equals(repairTilePos)){
                    player.resetDamageTokens();
                }
            }
        }
    }

    public void setPlayerNumber ( int playerNumber){
        this.myPlayerNumber = playerNumber;
    }

    public void setNumberOfPlayers ( int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * Let game know it hosts the game
     */
    public void setIsServerToTrue() {
        this.isServer = true;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getMapPath() {
        return this.mapPath;
    }

    public GameClientThread getClient() {
        return client;
    }

    /**
     * Realese actor
     */
    public void setWaitForServerToSendStartValuesToRelease() {
        StandardScreen screen = getScreen();
        if (screen instanceof MenuScreen) {
            MenuScreenActors actors = ((MenuScreen) screen).getActors();
            actors.waitForServerToSendStartValues.release();
        }
    }

    public void setWaitForServerToSendMapPathToRelease() {
        StandardScreen screen = getScreen();
        if (screen instanceof MenuScreen) {
            MenuScreenActors actors = ((MenuScreen) screen).getActors();
            actors.waitForServerToSendMapPath.release();
        }
    }

    /**
     * Close sockets on exit.
     */
    public void dispose () {
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
        try {
            gameMusic.dispose();
            laserSound.dispose();
            screen.dispose();
            board.dispose();
        } catch (Exception ignored) {
        }
    }
}
