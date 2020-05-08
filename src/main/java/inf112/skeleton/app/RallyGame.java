package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import inf112.skeleton.app.board.Board;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.lan.Converter;
import inf112.skeleton.app.lan.GameClientThread;
import inf112.skeleton.app.lan.GameServer;
import inf112.skeleton.app.lan.ServerThread;
import inf112.skeleton.app.objects.Belt;
import inf112.skeleton.app.objects.Flag;
import inf112.skeleton.app.objects.Laser;
import inf112.skeleton.app.objects.RotatePad;
import inf112.skeleton.app.objects.player.Player;
import inf112.skeleton.app.objects.player.PlayerSorter;
import inf112.skeleton.app.screens.ActorImages;
import inf112.skeleton.app.screens.gamescreen.GameScreen;
import inf112.skeleton.app.screens.gifscreen.GifScreen;
import inf112.skeleton.app.screens.menuscreen.MenuScreen;
import inf112.skeleton.app.screens.menuscreen.MenuScreenActors;
import inf112.skeleton.app.screens.standardscreen.SettingsScreen;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class RallyGame extends Game {

    public Board board;
    public Deck deck;
    public Screen lastScreen;

    public ArrayList<Player> players;
    public ArrayList<Player> respawnPlayers;
    public boolean playing;
    public boolean waitingForCards;
    public boolean waitingForPowerUp;

    public Sound walledLaserSound;
    public Sound robotLaserSound;
    public Sound firstBeltStartUp;
    public Sound secondBeltStartUp;
    public Sound repairRobotSound;
    public Sound hitByLaser;
    public Sound robotDestroyed;

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


    public static float soundVolume = 0.5f;
    private String mapPath;
    public Semaphore waitForPowerUp;
    private ArrayList<Player> poweredDownPlayers;
    private Semaphore waitForCards;

    public void create() {
        this.actorImages = new ActorImages();
        this.textSkin = new Skin(Gdx.files.internal("assets/skins/number-cruncher-ui.json"));
        this.defaultSkin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));
        this.setScreen(new MenuScreen(this));
        startMusic();
    }

    /**
     * Set up game with given map
     *
     */
    public void setupGame() {
        this.board = new Board(mapPath);
        this.deck = new Deck();
        this.players = makePlayersAndAddToBoard(this.numberOfPlayers);
        this.respawnPlayers = new ArrayList<>();
        this.poweredDownPlayers = new ArrayList<>();
        this.waitForPowerUp = new Semaphore(1);
        this.waitForPowerUp.tryAcquire();
        this.waitForCards = new Semaphore(1);
        this.waitForCards.tryAcquire();
        this.playing = true;
        this.converter = new Converter();
        this.waitingForCards = true;
        this.waitingForPowerUp = false;

        this.walledLaserSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/LaserShot.mp3"));
        this.robotLaserSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/laser.mp3"));
        this.firstBeltStartUp = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/firstBeltStartUp.mp3"));
        this.secondBeltStartUp = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/secondBeltStartUp.mp3"));
        this.repairRobotSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/Repair.mp3"));
        this.hitByLaser = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/LaserHit.mp3"));
        this.robotDestroyed = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/Destroyed.mp3"));


        new Thread(this::doTurn).start();

        dealCards();
    }

    public ArrayList<Player> makePlayersAndAddToBoard(int numberOfPlayers) {
        ArrayList<Player> players = new ArrayList<>();
        String[] colors = new String[]{"blue", "brown", "dark green", "light green", "pink", "purple", "red", "yellow"};
        for (int playerNumber = 1; playerNumber <= numberOfPlayers; playerNumber++) {
            Vector2 startPos = board.getStartPosition(playerNumber);
            Player player = new Player(startPos, playerNumber, colors[playerNumber - 1]);
            if (this.myPlayerNumber == playerNumber) {
                this.mainPlayer = player;
            }
            players.add(player);
            board.addPlayer(player);
        }
        return players;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        board.removePlayerFromBoard(player);
        board.addPlayer(player);
    }

    /**
     * Set up a host for this game.
     * @param portNumber on port
     */
    public void setUpHost(int portNumber) {
        this.isServer = true;
        this.myPlayerNumber = 1;
        this.serverThread = new ServerThread(this, portNumber);
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
            for (ProgramCard card : mainPlayer.getRegisters().getCards()) {
                client.sendMessage(converter.convertToString(mainPlayer.getPlayerNumber(), card));
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

    public void setWaitingForCards(boolean waitingForCards) {
        this.waitingForCards = waitingForCards;
    }

    public boolean isWaitingForCards() {
        return waitingForCards;
    }

    public boolean isWaitingForPowerUp() {
        return waitingForPowerUp;
    }

    public void setWaitingForPowerUp(boolean waitingForPowerUp) {
        this.waitingForPowerUp = waitingForPowerUp;
    }

    /**
     * Called when confirm button in {@link inf112.skeleton.app.screens.gamescreen.GameScreenActors} is pressed.
     */
    public void confirm() {
        setWaitingForPowerUp(false);
        if (waitingForCards) {
            setWaitingForCards(false);
            if (mainPlayer.getPowerDownNextRound()) {
                mainPlayer.setPoweringDown(true);
                mainPlayer.setPowerDownNextRound(false);
                sendPoweringDownMessage();
            }
            System.out.println("Sending cards");
            sendSelectedCardsToServer();
            if (isServer) {
                GameServer server = serverThread.getServer();
                server.setServerHasConfirmed(true);
                if (server.allClientsHaveSelectedCardsOrIsPoweredDown()) {
                    server.setServerHasConfirmed(false);
                    server.setAllClientsHaveSelectedCardsOrIsPoweredDown(false);
                    System.out.println("All clients selected cards");
                    server.sendSelectedCardsToAll();
                    server.sendToAll(Messages.START_TURN.toString());
                    startTurn();
                }
            }
        } else {
            if (mainPlayer.getPowerUpNextRound()) {
                mainPlayer.setPoweredDown(false);
                mainPlayer.setPowerUpNextRound(false);
                removePoweredDownPlayer(mainPlayer);
                sendPowerUpMessage();
            } else {
                sendContinuePowerDownMessage();
            }
            if (isServer) {
                GameServer server = serverThread.getServer();
                server.setServerHasConfirmed(true);
                if (server.allPoweredDownClientsHaveConfirmed() || serverIsOnlyOneInPowerDown()) {
                    server.setAllPoweredDownClientsHaveConfirmed(false);
                    server.setServerHasConfirmed(false);
                    System.out.println("All clients confirmed powered up");
                    server.sendToAll(Messages.CONTINUE_TURN.toString());
                    continueTurn();
                }
            }
        }
    }

    public void displayMessage(String message) {
        ((GameScreen) screen).actors.displayMessage(message);
    }

    public void displayPlayersPoweringDown() {
        StringBuilder stringBuilder = new StringBuilder("Player ");
        for (Player player : players) {
            if (player.isPoweringDown()) {
                stringBuilder.append(player.getPlayerNumber()).append(", ");
            }
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 2);
        stringBuilder.append("is powering down");
        displayMessage(stringBuilder.toString());
    }

    public void displayPlayerStats() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Player player : players) {
            stringBuilder.append(player.toString())
                    .append(": ")
                    .append(player.getDamageTokens())
                    .append(" damage token")
                    .append(player.getDamageTokens() > 1 ? "s, " : ", ")
                    .append(player.getLifeTokens())
                    .append(" life token")
                    .append(player.getLifeTokens() > 1 ? "s" : "")
                         .append(" --- ");
        }
        stringBuilder.delete(stringBuilder.length() - 6, stringBuilder.length() - 1);
        displayMessage(stringBuilder.toString());
    }

    public boolean readyToConfirm() {
        return isWaitingForCards() ?
                !mainPlayer.getRegisters().hasRegistersWithoutCard() : isWaitingForPowerUp();
    }

    public void returnToLastScreen() {
        if (this.screen instanceof SettingsScreen) {
            if (lastScreen instanceof GameScreen) {
                setScreen(new GameScreen(this));
            } else if (lastScreen instanceof MenuScreen) {
                setScreen(lastScreen);
            }
        } else {
            setScreen(new SettingsScreen(this));
        }
    }

    @Override
    public void setScreen(Screen screen) {
        if (this.screen != null && !(this.screen instanceof MenuScreen)) {
            this.screen.dispose();
        }
        if (!(screen instanceof SettingsScreen)) {
            this.lastScreen = screen;
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
        soundVolume = soundVolume == 0 ? 0.5f : 0;
    }

    public void loadMusic() {
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/menu_music.mp3"));
    }

    public void startMusic() {
        loadMusic();
        gameMusic.setVolume(soundVolume);
        gameMusic.setLooping(true);
        gameMusic.play();
    }

    public float getSoundVolume(){
        return soundVolume;
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
            waitForCards();
            if (Thread.interrupted()) { return; }
            for (int cardNumber = 0; cardNumber < 5; cardNumber++) {
                System.out.println("Runde " + (cardNumber + 1));
                allPlayersPlayCard(cardNumber);
                activateBeltsAndRotatePads();
                fireAllLasers();
                updateBackupAndPickUpFlagsAndRepair(false);
                if (someoneWon()) {
                    endGame();
                    return;
                }
                sleep(1000);
            }
            updateBackupAndPickUpFlagsAndRepair(true);
            if (!respawnPlayers.isEmpty()) {
                respawnPlayers();
            }
            removeDeadPlayers();
            updateRegisters();
            discardCardsAndServerCreatesNewDeckIfEmpty();
            if (!poweredDownPlayers.isEmpty()) {
                getPowerUpOrDownConfirm();
            }
            resetConfirmPowerUp();
            powerDown();
            dealCards();
            displayPlayerStats();
            ((GameScreen) screen).updateCards();
            serverResetConfirms();
            setWaitingForCards(!mainPlayer.isPoweredDown());
            setWaitingForPowerUp(!mainPlayer.isPoweredDown());
            letClientsAndServerContinue();
            serverGetReadyForNextRound();
        }
    }

    /**
     * End game if a player has picked up all flags
     */
    public boolean someoneWon() {
        for (Player player : players) {
            if (player.hasAllFlags(board.getFlags().size())) {
                return true;
            }
        }
        return false;
    }
    /**
     * Discard all players cards using {@link #discardCards()}.
     *
     * If isServer and there is not enough cards left in deck, create a new deck,
     * and remove the discarded cards from the new deck.
     */
    public void discardCardsAndServerCreatesNewDeckIfEmpty() {
        ArrayList<ProgramCard> lockedCards = discardCards();
        if (isServer && deck.deckSize() < numberOfDealtCards()) {
            serverThread.getServer().createAndSendDeckToAll(lockedCards);
        }
    }

    /**
     * First fire player lasers and then wall lasers. Wall lasers are only
     * activated if there are any wall lasers on the board. Make thread sleep after each methodcall.
     *
     * {@link #decreaseLives()} is invoked after both {@link #firePlayerLaser()} and {@link #fireLasers()}
     */
    private void fireAllLasers() {
        firePlayerLaser();
        sleep(350);
        removeLasers();
        sleep(600);
        decreaseLives();
        if (!board.getLasers().isEmpty()) {
            fireLasers();
            sleep(350);
            removeLasers();
            sleep(600);
            decreaseLives();
        }
    }

    /**
     * Move express belts one and then all belts one. Turn rotatepads.
     * Make doTurn thread sleep after each movement.
     *
     * {@link #decreaseLives()} at the end
     */
    public void activateBeltsAndRotatePads() {
      if (!board.getExpressBelts().isEmpty()) {
                    activateBelts(true);
                    sleep(1300);
                }
        decreaseLives();
        activateBelts(false);
        sleep(800);
        activateRotatePads();
        sleep(250);
        decreaseLives();
    }

    /**
     * If isServer is true, update the {@link GameServer#setServerHasConfirmed(boolean)}.
     *
     * Both client and server continue talking, and wait to continue
     * gameloop until everyone have confirmed.
     */
    public void getPowerUpOrDownConfirm() {
        if (isServer) {
            serverThread.getServer().setServerHasConfirmed(!mainPlayer.isPoweredDown());
        }
        System.out.println("Wait for power up");
        letClientsAndServerContinue();
        setWaitingForPowerUp(mainPlayer.isPoweredDown());
        waitForPowerUp();
        System.out.println("Continue game loop");
    }

    /**
     * After each round, the server needs to reset the confirms from server and clients so that
     * if will wait for new confirms before starting next rounds.
     *
     * isServer needs to be true
     */
    public void serverResetConfirms() {
        if (isServer) {
            serverThread.getServer().setAllClientsHaveSelectedCardsOrIsPoweredDown(false);
            serverThread.getServer().setServerHasConfirmed(false);
        }
    }

    /**
     *
     * isServer needs to be true
     *
     * If {@link #everyOneIsPoweredDown()} is true, then send a message to all players
     * telling them to start the next turn immediately.
     *
     * If {@link #serverIsOnlyOneInPowerDown()} or server is in power down, when server gets the last message in
     * {@link inf112.skeleton.app.lan.GameServerThreads} from another player the server have confirmed
     * {@link GameServer#setServerHasConfirmed(boolean)} to true beforehand so that it will send out start turn messages.
     *
     * If {@link #everyOneExceptServerIsPoweredDown()} then server should not wait for any messages, and
     * when server then confirms its cards the turn should start. Therefore it needs to set
     * {@link GameServer#setAllClientsHaveSelectedCardsOrIsPoweredDown(boolean)} to true so that it can send start turn
     * messages when pressing confirm button.
     */
    public void serverGetReadyForNextRound() {
        if (isServer) {
            if (everyOneIsPoweredDown()) {
                serverThread.getServer().sendToAll(Messages.START_TURN.toString());
                startTurn();
            } else if (serverIsOnlyOneInPowerDown()) {
                serverThread.getServer().setServerHasConfirmed(true);
            } else if (getPoweredDownRobots().contains(mainPlayer)) {
                serverThread.getServer().setServerHasConfirmed(true);
            } else if (everyOneExceptServerIsPoweredDown()) {
                serverThread.getServer().setAllClientsHaveSelectedCardsOrIsPoweredDown(true);
            }
        }
    }

    /**
     *
     * @return True if only player is powered up
     */
    private boolean everyOneExceptServerIsPoweredDown() {
        Player hostPlayer = board.getPlayer(1);
        return getPoweredDownRobots().size() == (players.size()-1) && !getPoweredDownRobots().contains(hostPlayer);
    }

    /**
     *
     * @return True if every player in the game is in power down
     */
    private boolean everyOneIsPoweredDown() {
        return getPoweredDownRobots().size() == players.size();
    }

    public void waitForCards() {
        try {
            waitForCards.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startTurn() {
        waitForCards.release();
    }

    public void continueTurn() {
        waitForPowerUp.release();
    }

    /**
     * Wait for everyone to either power up or remain powered down
     */
    public void waitForPowerUp() {
        try {
            waitForPowerUp.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * After playing cards, let server and clients exchange more cards.
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

            if (player.getDamageTokens() >= 10 || board.outsideBoard(player)) {

                robotDestroyed.play(soundVolume);
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
        for (Player player : players) {
            if (player.isDead()) {
                board.removePlayerFromBoard(player);
                players.remove(player);
                if (isServer) {
                    serverThread.getServer().remove(player.getPlayerNumber());
                    serverThread.getServer().disconnect(player.getPlayerNumber());
                }
            }
        }
    }

    public void respawnPlayers() {
        for (Player player : respawnPlayers) {
            players.add(player);
            board.respawn(player);
        }
        respawnPlayers.clear();
    }

    /**
     * All players move their card. doTurn thread sleeps at the end and between moves.
     * @param cardNumber
     */
    public void allPlayersPlayCard(int cardNumber) {
        ArrayList<Player> playerOrder = new ArrayList<>(players);
        // Add all players to order list, and remove players with no cards left or powered down
        playerOrder.removeIf(Player::isPoweredDown);
        playerOrder.removeIf(p -> !p.getRegisters().getRegister(cardNumber).hasCard());
        playerOrder.sort(new PlayerSorter(cardNumber));
        for (Player player : playerOrder) {
            playCard(player, cardNumber);
            // Wait 1 second for each player
            sleep(500);
            // Decrease lives in case player is pushed outside board
            decreaseLives();
            sleep(500);
        }
        sleep(250);
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

    public void setWinScreen() {
        setScreen(new GifScreen(this));
    }

    public void removeLasers() {
        for (int y = 0; y < board.getBoardHeight(); y++) {
            for (int x = 0; x < board.getBoardWidth(); x++) {
                board.getLaserLayer().setCell(x, y, null);
            }
        }
    }

    public void firePlayerLaser() {
        if (!players.isEmpty()) {
            for (Player player : players) {
                player.fire(this);
            }
            robotLaserSound.play(soundVolume);
        }
    }

    public void fireLasers() {
        if (!board.getLasers().isEmpty()) {
            for (Laser laser : board.getLasers()) {
                laser.fire(this);
            }
            walledLaserSound.play(soundVolume);
        }
    }

    public void activateRotatePads() {
        for (Player player : players) {
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
        for (Player player : players) {
            for (Belt belt : belts) {
                if (player.getPosition().equals(belt.getPosition())) {
                    beltPush(player, belt);
                }
            }
        }
        if (!onlyExpress){
            firstBeltStartUp.play(soundVolume);
        }
        else {
            secondBeltStartUp.play(soundVolume);
        }
        sleep(500);
        validateBeltPushPos();
        updatePositionsAfterBeltPush();
        updateBoardAfterBeltPush();
    }

    private void updateBoardAfterBeltPush() {
        board.removePlayersFromBoard();
        board.updateBoard();
    }

    public void validateBeltPushPos() {
        player:
        for (Player player : players) {
            for (Player otherPlayer : players) {
                if (player.getBeltPushPos() != null && !player.equals(otherPlayer)) {
                    if (player.getBeltPushPos().equals(otherPlayer.getBeltPushPos())) {
                        player.setBeltPushPos(null);
                        otherPlayer.setBeltPushPos(null);
                        continue player;
                    }
                    if (board.shouldPush(player, player.getBeltPushDir()) &&
                            otherPlayer.getBeltPushPos() == null &&
                            !board.canPush(otherPlayer, player.getBeltPushDir())) {
                        player.setBeltPushPos(null);
                        continue player;
                    }
                }
            }
        }
    }

    public void updatePositionsAfterBeltPush() {
        for (Player player : players) {
            if (player.getBeltPushPos() != null) {
                Direction dir = player.getBeltPushDir();
                if (board.shouldPush(player, dir)) {
                    Player enemyPlayer = board.getPlayer(board.getNeighbourPosition(player.getPosition(), dir));
                    if (board.canPush(enemyPlayer, dir)) {
                        board.pushPlayer(enemyPlayer, dir);
                    }
                }
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

    public void endGame() {
        setScreen(new GifScreen(this));
    }

    /**
     * Activate the repair keys and pick up flags. Thread sleeps between each method call
     */
    public void updateBackupAndPickUpFlagsAndRepair(boolean repair) {
        for (Player player : players) {
            for (Vector2 repairTilePos : board.getRepairTiles()) {
                if (player.getPosition().equals(repairTilePos)) {
                    player.setBackup(repairTilePos, player.getDirection());
                    if (repair) {
                        player.decrementDamageTokens();
                    }
                }
            }
            for (Flag flag : board.getFlags()) {
                if (player.getPosition().equals(flag.getPosition())) {
                    player.setBackup(flag.getPosition(), player.getDirection());
                    player.tryToPickUpFlag(flag);
                    if (repair) {
                        player.decrementDamageTokens();
                    }
                }
            }
            repairRobotSound.play(soundVolume);
            sleep(500);
        }
    }

    public void setPlayerNumber ( int playerNumber){
        this.myPlayerNumber = playerNumber;
    }

    public void setNumberOfPlayers ( int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
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
     * @return MenuScreenActors if the current screen is a {@link MenuScreen}. Null otherwise;
     */
    public MenuScreenActors getMenuScreenActors() {
        StandardScreen screen = getScreen();
        if (screen instanceof MenuScreen) {
            return ((MenuScreen) screen).getActors();
        }
        return null;
    }

    /**
     * Close sockets on exit.
     */
    public void dispose () {
        try {
            // Tell server you are leaving. Close your socket.
            if (!isServer) {
                this.client.sendMessage(converter.createQuitMessage(this.myPlayerNumber));
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
        } catch (Exception ignored) {
        }
        try {
            walledLaserSound.dispose();
            repairRobotSound.dispose();
            firstBeltStartUp.dispose();
            secondBeltStartUp.dispose();
            robotDestroyed.dispose();
            robotLaserSound.dispose();
        } catch (Exception ignored) {
        }
        try {

            screen.dispose();
        } catch (Exception ignored) {
        }
        try {
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
     * If a player has pressed powerDown button the round before, player is in powerDown.
     */
    public void powerDown() {
        for (Player player : players) {
            if (player.isPoweringDown()) {
                player.setPoweredDown(true);
                player.setPoweringDown(false);
                poweredDownPlayers.add(player);
            }
            if (player.isPoweredDown()) {
                player.resetDamageTokens();
            }
        }
    }

    /**
     * Let the other players know you have powered up.
     */
    public void sendPowerUpMessage() {
        if (isServer) {
            serverThread.getServer().sendToAll(converter.createMessageFromPlayer(mainPlayer.getPlayerNumber(), Messages.POWER_UP));
        } else {
            client.sendMessage(converter.createMessageFromPlayer(mainPlayer.getPlayerNumber(), Messages.POWER_UP));
        }
    }

    /**
     * Let the other players know you are powering down.
     */
    public void sendPoweringDownMessage() {
        if (isServer) {
            serverThread.getServer().sendToAll(converter.createMessageFromPlayer(mainPlayer.getPlayerNumber(), Messages.POWERING_DOWN));
        } else {
            client.sendMessage(converter.createMessageFromPlayer(mainPlayer.getPlayerNumber(), Messages.POWERING_DOWN));
        }
    }

    public boolean serverIsOnlyOneInPowerDown() {
        for (Player player : players) {
            if (player.getPlayerNumber() != 1 && player.isPoweredDown() && !player.hasConfirmedPowerUpOrContinuePowerDown()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Let the other players know that you are continueing the power down.
     */
    public void sendContinuePowerDownMessage () {
        if (isServer) {
            serverThread.getServer().setServerHasConfirmed(true);
        } else {
            client.sendMessage(converter.createMessageFromPlayer(mainPlayer.getPlayerNumber(), Messages.CONTINUE_POWER_DOWN));
        }
    }

    public void setMainPlayer(Player mainPlayer) {
        this.mainPlayer = mainPlayer;
    }

    public void setClient(GameClientThread client) {
        this.client = client;
    }

    public void setServerThread(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    public void setIsServer(boolean isServer) {
        this.isServer = isServer;
    }

    /**
     * Add a player to power down.
     *
     * @param player who is powered down
     */
    public void addPoweredDownPlayer(Player player) {
        this.poweredDownPlayers.add(player);
    }

    /**
     * Remove a powered down player (player is powered up)
     * @param player who is powered up
     */
    public void removePoweredDownPlayer(Player player) {
        this.poweredDownPlayers.remove(player);
    }

    public ArrayList<Player> getPoweredDownRobots() {
        return this.poweredDownPlayers;
    }

    public void resetConfirmPowerUp() {
        players.forEach(player -> player.setConfirmedPowerUpOrContinuePowerDown(false));
    }

    public void setPlayers(Player... players) {
        this.players = new ArrayList<>(Arrays.asList(players));
    }

    public GameServer getServer() {
        if (isServer) {
            return serverThread.getServer();
        }
        return null;
    }

    public boolean isServer() {
        return isServer;
    }
}
