package inf112.skeleton.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.screens.GifScreen;
import inf112.skeleton.app.screens.LoadingScreen;

public class RallyGame extends Game {

    public Board board;
    public SpriteBatch batch;
    public Deck deck;
    public Player currentPlayer;

    public void create() {
        this.batch = new SpriteBatch();
        this.setScreen(new LoadingScreen(this));
    }

    public void setupGame(String mapPath) {
        this.board = new Board(mapPath, 4);
        this.currentPlayer = board.getPlayer1();
        this.deck = new Deck();
        setInputProcessor();
    }

    public void setInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                Player player = board.getPlayer1();
                if (keycode == Input.Keys.RIGHT) {
                    player.setDirection(Direction.EAST);
                    board.movePlayer(player);
                } else if (keycode == Input.Keys.LEFT) {
                    player.setDirection(Direction.WEST);
                    board.movePlayer(player);
                } else if (keycode == Input.Keys.UP) {
                    player.setDirection(Direction.NORTH);
                    board.movePlayer(player);
                } else if (keycode == Input.Keys.DOWN) {
                    player.setDirection(Direction.SOUTH);
                    board.movePlayer(player);
                } else if (keycode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                }
                if (currentPlayer.hasAllFlags(board.getFlags().size())) {
                    setWinScreen();
                }
                return super.keyDown(keycode);
            }
        });
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

    public Board getBoard() {
        return this.board;
    }
}
