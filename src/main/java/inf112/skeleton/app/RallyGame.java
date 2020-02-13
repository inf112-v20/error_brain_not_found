package inf112.skeleton.app;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.Game;

public class RallyGame extends Game {

    public Board board;

    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private TiledMapTileLayer.Cell player;
    private Screen screen;

    public RallyGame(){
        this.board = new Board();
    }

    @Override
    public void create() {
        float GDX_GRAPHICS_WIDTH = Gdx.graphics.getWidth();
        float GDX_GRAPHICS_HEIGHT = Gdx.graphics.getHeight();
        TiledMap tiledMap = new TmxMapLoader().load("src/main/java/inf112/skeleton/app/assets/kart.tmx");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, GDX_GRAPHICS_WIDTH, GDX_GRAPHICS_HEIGHT);
        camera.update();

        //tiledMap = new TmxMapLoader().load("src/main/java/inf112/skeleton/app/assets/kart.tmx");
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        Texture textureArrow = new Texture("src/main/java/inf112/skeleton/app/assets/arrow.png");

    }


    @Override
    public void resize(int width, int height) {

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

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }
}
