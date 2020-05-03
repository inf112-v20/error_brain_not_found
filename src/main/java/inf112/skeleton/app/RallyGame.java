package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.lan.Converter;
import inf112.skeleton.app.lan.GameClientThread;
import inf112.skeleton.app.lan.GameServer;
import inf112.skeleton.app.lan.ServerThread;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
import inf112.skeleton.app.screens.gamescreen.GameScreen;
import inf112.skeleton.app.screens.ActorImages;
import inf112.skeleton.app.screens.LoadingScreen;
import inf112.skeleton.app.screens.menuscreen.MenuScreen;
import inf112.skeleton.app.screens.menuscreen.MenuScreenActors;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;

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
    public Music gameMusic;
    public Player mainPlayer;
    private int numberOfPlayers;
    private int myPlayerNumber;
    private ServerThread serverThread;
    private boolean isServer;
    private GameClientThread client;
    private Converter converter;

    public Skin textSkin;
    public Skin defaultSkin;
    public ActorImages actorImages;

    public static float volume = 0.5f;
    private String mapPath;

    public void create() {
        this.actorImages = new ActorImages();
        this.textSkin = new Skin(Gdx.files.internal("assets/skins/number-cruncher-ui.json"));
        this.defaultSkin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));
        this.setScreen(new LoadingScreen(this));
        startMusic();
    }

    /**
     * Set up game with given map
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

        sendMapToClients();

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
            System.out.println("Could not connect to " + hostIP + " on port " + portNumber);
        }
    }

    /**
     * Send all mainplayer's selected cards to {@link GameServer}.
     *
     * isServer needs to be false to send the cards.
     */
    public void sendSelectedCardsToServer() {
        if (!isServer) {
            for (Register register : mainPlayer.getRegisters().getRegisters()) {
                client.sendMessage(converter.convertToString(mainPlayer.getPlayerNr(), register.getProgramCard()));
            }
        }
    }

    public void fullscreen() {
        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(1280, 720);
        } else {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }
    }

    public Skin getActorImages() {
        return actorImages.getSkin();
    }

    public Skin getTextSkin() {
        return textSkin;
    }

    public Skin getDefaultSkin() {
        return defaultSkin;
    }

    public void setShouldPickCards(boolean shouldPickCards) {
        this.shouldPickCards = shouldPickCards;
    }

    public boolean shouldPickCards() {
        return shouldPickCards;
    }

    public void confirmCards() {
        if (!mainPlayer.getRegisters().hasRegistersWithoutCard()) {
            sendSelectedCardsToServer();
            if (isServer) {
                GameServer server = serverThread.getServer();
                server.setServerHasConfirmed(true);
                if (server.allClientsHaveSelectedCards()) {
                    server.setAllClientsHaveSelectedCards(false);
                    System.out.println("All clients selected cards");
                    server.sendSelectedCardsToAll();
                    server.sendToAll(Messages.START_TURN.toString());
                    cardsReady();
                }
            }
            setShouldPickCards(false);
        }
    }

    @Override
    public void setScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.dispose();
        }
        super.setScreen(screen);
    }

    public StandardScreen getScreen() {
        return (StandardScreen) super.getScreen();
    }

    public void muteMusic() {
        gameMusic.setVolume(gameMusic.getVolume() == 0 ? 0.5f : 0);
    }

    public void muteSounds() {
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
            System.out.println("Wait for cards");
            try {
                waitForCards.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Thread.interrupted()) {
                return;
            }
            System.out.println("Released");
            for (int cardNumber = 0; cardNumber < 5; cardNumber++) {

                System.out.println("Runde " + (cardNumber + 1));

                // All players play one card in the correct order
                allPlayersPlayCard(cardNumber);
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
            removeDeadPlayers();
            updateRegisters();

            ArrayList<ProgramCard> lockedCards = discardCards();
            if (isServer &&deck.deckSize() < numberOfDealtCards()) {
                serverThread.getServer().createAndSendDeckToAll(lockedCards);

            }
            discardCards();
            powerDown();
            dealCards();
            ((GameScreen) screen).updateCards();

            if (isServer) {
                serverThread.getServer().setAllClientsHaveSelectedCards(false);
            }
            setShouldPickCards(true);
            letClientsAndServerContinue();
            System.out.println("Continue talking");
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

    private int numberOfDealtCards() {
        int cards = 0;
        for (Player player : players) {
            cards += player.getProgramCardsDealt();
        }
        return cards;
    }

    private void updateRegisters() {
        for (Player player : players) {
            player.updateRegisters();
        }
    }

    private void pickUpFlags() {
        board.pickUpFlags();
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void dealCards() {
        for (Player player : players) {
            if (!player.isPoweredDown()) {
                player.drawCards(deck);
            }
        }
    }

    public ArrayList<ProgramCard> discardCards() {
        ArrayList<ProgramCard> lockedCards = new ArrayList<>();
        for (Player player : players) {
            lockedCards.addAll(player.discardCards(deck));
        }
        return lockedCards;
    }

    /**
     * Decrease life tokens to each player that has collected 10 damage tokens.
     * Reset damage tokens, remove player from board and discard all cards
     */
    public void decreaseLives() {
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

    public void removeDeadPlayers() {
        Player playerToRemove = null;
        for (Player player : players) {
            if (player.isDead()) {
                board.removePlayerFromBoard(player);
                playerToRemove = player;
                if (isServer) {
                    serverThread.getServer().remove(player.getPlayerNr());
                    serverThread.getServer().disconnect(player.getPlayerNr());
                }
            }
        }
        players.remove(playerToRemove);
    }

    public void respawnPlayers() {
        for (Player player : respawnPlayers) {
            players.add(player);
            board.respawn(player);
        }
        respawnPlayers.clear();
    }

    public void allPlayersPlayCard(int cardNumber) {
        ArrayList<Player> playerOrder = new ArrayList<>(players);
        // Add all players to order list, and remove players with no cards left
        playerOrder.removeIf(p -> !p.getRegisters().getRegister(cardNumber).hasCard());
        playerOrder.sort(new PlayerSorter(cardNumber));

        // Add all players to order list, and remove powered down players
        playerOrder.removeIf(Player::isPoweredDown);
        playerOrder.sort(new PlayerSorter(cardNumber));
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
                if (card.getDistance() == -1) {
                    board.movePlayer(player, true);
                } else {
                    for (int distance = 0; distance < card.getDistance(); distance++) {
                        board.movePlayer(player, false);
                        // Wait 250 ms for each move except last one
                        if (distance < card.getDistance() - 1) {
                            sleep(250);
                        }
                    }
                }
                break;
            default:
                break;
        }
        board.addPlayer(player);
        deck.addCardToDiscardPile(card);
    }

    public void removeLasers() {
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

    public void activateRotatePads() {
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

    private void updateBoard() {
        board.removePlayersFromBoard();
        updatePositionsAfterBeltPush();
        board.updateBoard();
    }

    public void validateBeltPushPos() {
        for (Player player : players) {
            for (Player otherPlayer : players) {
                if (player.getBeltPushPos() != null && !player.equals(otherPlayer) && player.getBeltPushPos().equals(otherPlayer.getBeltPushPos())) {
                    player.setBeltPushPos(null);
                    otherPlayer.setBeltPushPos(null);
                }
            }
        }
    }

    public void updatePositionsAfterBeltPush() {
        for (Player player : players) {
            if (player.getBeltPushPos() != null) {
                player.setPosition(player.getBeltPushPos());
                player.setBeltPushPos(null);
            }
        }
    }

    public void beltPush(Player player, Belt belt) {
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
     * Is used when {@link GameServer} has sent the stack to all players.
     * @param stack of cards for this game.
     */
    public void setDeck (Stack <ProgramCard> stack) {
        if (deck == null) {
            deck = new Deck();
        }
        this.deck.setDeck(stack);
    }

    public void activateRepairTiles() {
        for (Player player : players){
            for (Vector2 repairTilePos : board.getRepairTiles()){
                if (player.getPosition().equals(repairTilePos)){
                    player.resetDamageTokens();
                    player.setBackup(repairTilePos, player.getDirection());
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
        try {
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
        } catch (Exception ignored) {
        }
        try {
            gameMusic.dispose();
            laserSound.dispose();
            screen.dispose();
            board.dispose();
        } catch (Exception ignored) {
        }
    }

    /**
     * Finish the round in doTurn and then stop the thread.
     */
    public void quitPlaying() {
        this.playing = false;
    }

    /**
     * Create a new thread to send map to clients that are connectet to server.
     * If server have started {@link RallyGame#setupGame(String)} after client have connected,
     * client needs to get notified that server has chosen map and get the map from server.
     * If client joins after the host has picked a map, it will get the map right away.
     *
     * Send the map to all clients that are connected, until every client has a map.
     *
     *
     * isServer needs to be true to send the map
     */
    private void sendMapToClients() {
        if (isServer) {
            // If clients have connected before you started game, give them the map
            Thread sendMap = new Thread(() -> {
                while (!serverThread.getServer().allClientsHaveReceivedMap()) {
                    serverThread.getServer().sendMapPathToNewlyConnectedClients(mapPath);
                }
            });
            sendMap.start();
        }
    }

    public void powerDown() {
        for (Player player : players) {
            if (player.isPoweringDown()) {
                player.setPoweredDown(true);
                player.setPoweringDown(false);
            }
            if (player.isPoweredDown()) {
                player.resetDamageTokens();
            }
        }
    }
}
