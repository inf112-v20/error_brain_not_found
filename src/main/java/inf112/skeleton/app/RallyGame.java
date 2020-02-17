package inf112.skeleton.app;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import inf112.skeleton.app.cards.Deck;
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
    }

    public void render() {
        super.render();
    }

    public void dispose() { batch.dispose(); }

    public Board getBoard() {
        return this.board;
    }
}
