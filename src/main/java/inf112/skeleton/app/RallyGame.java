package inf112.skeleton.app;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;
import inf112.skeleton.app.screens.GameScreen;
import inf112.skeleton.app.screens.MenuScreen;

import java.awt.*;

public class RallyGame extends Game {

    public Board board;
    public SpriteBatch batch;
    public BitmapFont font;

    public void create() {
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.board = new Board("assets/kart.tmx");
        this.setScreen(new MenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() { batch.dispose(); }

    public Board getBoard() {
        return this.board;
    }
}
