package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.objects.Belt;
import inf112.skeleton.app.objects.Laser;
import inf112.skeleton.app.objects.RotatePad;
import inf112.skeleton.app.screens.GifScreen;
import inf112.skeleton.app.screens.LoadingScreen;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class RallyGame extends Game {

    public Board board;
    public Deck deck;
    public Player currentPlayer;
    public ArrayList<Player> players;
    public Semaphore waitForCards;
    public boolean playing;
    public Sound laserSound;
    public Player mainPlayer;

    public void create() {
        this.setScreen(new LoadingScreen(this));
        startMusic();
    }

    public void setupGame(String mapPath) {
        this.board = new Board(mapPath, 4);
        this.deck = new Deck();
        this.currentPlayer = board.getPlayer1();
        this.players = new ArrayList<>();
        this.players = board.getPlayers();
        this.mainPlayer = board.getPlayer1();

        this.waitForCards = new Semaphore(1);
        this.waitForCards.tryAcquire();
        this.playing = true;

        this.laserSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/LaserShot.mp3"));

        new Thread(this::doTurn).start();

        setInputProcessor();
        dealCards();
        selectCards();
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
                removeDeadPlayers();
                return super.keyDown(keycode);
            }
        });
    }

    public void startMusic() {
        Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/menu_music.mp3"));
        music.setLooping(true);
        music.setVolume(1f);
        music.play();
    }

    private void cardsReady() {
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
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                removeLasers();
            }
            removeDeadPlayers();
            dealCards();
            selectCards();

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
            playCard(player);
            // Wait 1 second for each player
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void playCard(Player player) {
        ProgramCard card = player.getSelectedCards().remove(0);
        System.out.println(player.toString() + " played " + card.toString());
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
        //laserSound.play();
    }

    public void activateRotatePads() {
        for (Player player : board.getPlayers()) {
            for (RotatePad rotatePad : board.rotatePads) {
                Vector2 playerPosition = player.getPosition();
                Vector2 rotePadPosition = rotatePad.getPosition();

                if (playerPosition.equals(rotePadPosition)) {
                    Rotate rotateDirection = rotatePad.getRotate();
                    Direction playerDirection = player.getDirection();

                    switch (rotateDirection) {
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
    public void activateBelts(boolean onlyExpress) {
        ArrayList<Belt> belts;

        if (onlyExpress){
            belts = board.expressBelts;
        } else {
            belts = board.belts;
        }

        for (Player player : board.getPlayers()) {
            for (Belt belt : belts){
                float positionX = player.getPosition().x;
                float positionY = player.getPosition().y;
                Direction direction = belt.getDirection();

                if (player.getPosition().equals(belt.getPosition())){

                    switch (direction){
                        case NORTH:
                            player.setPosition(new Vector2(positionX, positionY + 1));
                        case EAST:
                            player.setPosition(new Vector2(positionX + 1, positionY));
                        case WEST:
                            player.setPosition(new Vector2(positionX - 1, positionY));
                        case SOUTH:
                            player.setPosition(new Vector2(positionX, positionY - 1));
                        default:
                            System.out.println("There is something wrong her! ERROR");
                            return;
                    }
                }
            }
        }
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        this.screen.dispose();
    }

    public Board getBoard() {
        return this.board;
    }
}
