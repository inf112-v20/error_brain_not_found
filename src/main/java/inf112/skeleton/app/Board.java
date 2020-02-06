package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class Board implements ApplicationListener {

    Player player;
    Texture texture;
    SpriteBatch batch;
    Sprite playerSprite;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;

    TiledMap tiledMap;
    TiledMapTileLayer boardLayer;

    public void makePlayer(int x, int y) {
        player = new Player(new Position(x, y));
        playerSprite = new Sprite(texture);
    }

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();

        tiledMap = new TmxMapLoader().load("src/main/java/inf112/skeleton/app/images/kart.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        boardLayer = (TiledMapTileLayer) tiledMap.getLayers().get("board");
        texture = new Texture("src/main/java/inf112/skeleton/app/images/arrow.png");
        batch = new SpriteBatch();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            int x = (int) Math.floor(Gdx.input.getX()/32.0)*32;
            int y = (int) Math.floor((Gdx.graphics.getHeight() - Gdx.input.getY())/32.0)*32;

            if (player == null) { makePlayer(x/32, y/32); }
            player.getPosition().setPosition(x/32, y/32);
            playerSprite.setPosition(x, y);
        }

        if (playerSprite != null) {
            batch.begin();
            playerSprite.draw(batch);
            batch.end();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}