package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import inf112.skeleton.app.cards.Deck;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.screens.MenuScreen;

public class RallyGame extends Game {

    public Board board;
    public SpriteBatch batch;
    public Deck deck;

    public void create() {
        this.batch = new SpriteBatch();
        this.board = new Board("assets/kart.tmx");
        this.setScreen(new MenuScreen(this));
        this.deck = new Deck();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                Player player = board.getPlayers().get(0);
                if (keycode == Input.Keys.RIGHT) {
                    player.setDirection(Direction.EAST);
                }
                if (keycode == Input.Keys.LEFT) {
                    player.setDirection(Direction.WEST);
                }
                if (keycode == Input.Keys.UP) {
                    player.setDirection(Direction.NORTH);
                }
                if (keycode == Input.Keys.DOWN) {
                    player.setDirection(Direction.SOUTH);
                }
                board.movePlayer(player);
                return super.keyDown(keycode);
            }
        });
    }

    public void render() {
        super.render();
    }

    public void dispose() { batch.dispose(); }

    public Board getBoard() {
        return this.board;
    }
}
