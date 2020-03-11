package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.screens.GifScreen;
import inf112.skeleton.app.screens.LoadingScreen;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class RallyGame extends Game {

    public Board board;
    public SpriteBatch batch;
    public Deck deck;
    public Player currentPlayer;
    public ArrayList<Player> players;
    public Semaphore waitForCards;
    public boolean playing;

    public void create() {
        this.batch = new SpriteBatch();
        this.board = new Board(this, "assets/maps/Risky_Exchange.tmx", 4);
        this.setScreen(new LoadingScreen(this));
        this.deck = new Deck();
        this.players = new ArrayList<>();
        this.currentPlayer = board.getPlayer1();
        this.waitForCards = new Semaphore(1);
        waitForCards.tryAcquire();
        this.playing = true;
        new Thread(this::doTurn).start();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                Player player = board.getPlayer1();
                if (keycode == Input.Keys.RIGHT) {
                    player.setDirection(Direction.EAST);
                } else if (keycode == Input.Keys.LEFT) {
                    player.setDirection(Direction.WEST);
                } else if (keycode == Input.Keys.UP) {
                    player.setDirection(Direction.NORTH);
                } else if (keycode == Input.Keys.DOWN) {
                    player.setDirection(Direction.SOUTH);
                } else if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                } else if (keycode == Input.Keys.SPACE) {
                    cardsReady();
                    return super.keyDown(keycode);
                } else {
                    return super.keyDown(keycode);
                }
                board.movePlayer(player);
                if (player.hasAllFlags(board.getFlags().size())) {
                    setWinScreen();
                }
                return super.keyDown(keycode);
            }
        });
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
            if (Thread.interrupted()) { return; }
            for (ProgramCard card : currentPlayer.getProgramCards()) {
                switch (card.getRotate()) {
                    case RIGHT:
                        currentPlayer.setDirection(currentPlayer.getDirection().turnRight());
                        board.rotatePlayer(currentPlayer);
                        break;
                    case LEFT:
                        currentPlayer.setDirection(currentPlayer.getDirection().turnLeft());
                        board.rotatePlayer(currentPlayer);
                        break;
                    case UTURN:
                        currentPlayer.setDirection(currentPlayer.getDirection().turnAround());
                        board.rotatePlayer(currentPlayer);
                        break;
                    case NONE:
                        for (int i = 0; i < card.getDistance(); i++) {
                            board.movePlayer(currentPlayer);
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    default:
                        break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setWinScreen() {
        this.setScreen(new GifScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();

    }

    /**
     * @return list of all players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return this.board;
    }
}
