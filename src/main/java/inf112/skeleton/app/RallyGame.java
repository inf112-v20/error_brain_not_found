package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.Game;
import inf112.skeleton.app.screens.GameScreen;

public class RallyGame extends Game {

    public Board board;
    public SpriteBatch batch;

    public void create() {
        this.batch = new SpriteBatch();
        this.board = new Board("assets/kart.tmx");
        this.setScreen(new GameScreen(this));
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
