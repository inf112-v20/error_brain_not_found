package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.Board.Board;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.objects.Laser;
import inf112.skeleton.app.objects.Player.Player;
import inf112.skeleton.app.objects.Player.PlayerSorter;
import inf112.skeleton.app.objects.RotatePad;
import inf112.skeleton.app.screens.ButtonSkin;
import inf112.skeleton.app.screens.GifScreen.GifScreen;
import inf112.skeleton.app.screens.MenuScreen.MenuScreen;
import inf112.skeleton.app.screens.StandardScreen.StandardScreen;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class RallyGame extends Game {

    public Board board;
    public Deck deck;

    public ArrayList<Player> players;
    public Semaphore waitForCards;
    public boolean playing;
    public Sound laserSound;
    public Music gameMusic;
    public Player mainPlayer;

    public ButtonSkin buttonSkins;

    public float volume;
    public boolean muted;

    public void create() {
        this.buttonSkins = new ButtonSkin();
        this.setScreen(new MenuScreen(this));
    }

    public void setupGame(String mapPath) {
        this.board = new Board(mapPath, 1);
        this.deck = new Deck();
        this.players = new ArrayList<>();
        this.players = board.getPlayers();
        this.mainPlayer = board.getPlayer1();

        this.waitForCards = new Semaphore(1);
        this.waitForCards.tryAcquire();
        this.playing = true;

        this.laserSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/LaserShot.mp3"));

        new Thread(this::doTurn).start();

        dealCards();
    }

    public void confirmCards() {
        if (mainPlayer.getSelectedCards().size() == 5) {
            cardsReady();
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

    public void mute() {
        if (muted) {
            volume = 0.5f;
            muted = false;
        } else {
            volume = 0f;
            muted = true;
        }
        gameMusic.setVolume(volume);
    }

    public void loadMusic() {
        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/menu_music.mp3"));
    }

    public void startMusic() {
        loadMusic();
        gameMusic.setVolume(0.5f);
        gameMusic.play();
    }

    private void cardsReady() {
        waitForCards.release();
    }

    public void doTurn() {
        /*
        1. Deal the Program cards.
        2. Arrange your Program cards face down among your
           five registers.
        3. Announce intent to power down or continue running
           NEXT turn.
        4. Complete each register in order:
                A. Reveal Program Cards
                B. Robots Move
                C. Board Elements Move (Gears, Express belt, normal belt)
                D. Lasers Fire (Player, then board)
                E. Touch Checkpoints (Flag, Repair)
        5. Clean up any end-of-turn effects
        */
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
                sleep(500);
                activateRotatePads();
                sleep(500);
                fireLasers();
                sleep(300);
                removeLasers();
                sleep(1000);
            }
            removeDeadPlayers();
            dealCards();
        }
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
       // for (Player player : players) { player.selectCards(); }

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
            sleep(1000);
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
                        sleep(500);
                    }
                }
                break;
            default:
                break;
        }
        deck.addCardToDiscardPile(card);
    }

    public void setWinScreen() {
        setScreen(new GifScreen(this));
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
        laserSound.play(0.2f);
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

    public Board getBoard() {
        return this.board;
    }
}
