package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.skeleton.app.Board;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.Player;
import inf112.skeleton.app.enums.Direction;

public class GameScreen implements Screen {

    private final RallyGame game;
    private final Board board;

    private OrthographicCamera camera;
    private TiledMapRenderer mapRenderer;

    public GameScreen(final RallyGame game) {
        camera = new OrthographicCamera();

        this.game = game;
        this.board = game.getBoard();

        camera.setToOrtho(false, board.getWidth() * 300, board.getHeight() * 300);

        this.mapRenderer = new OrthogonalTiledMapRenderer(game.getBoard().getMap());
        mapRenderer.setView(camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(v, v, v,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        mapRenderer.render();
        game.batch.end();
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
