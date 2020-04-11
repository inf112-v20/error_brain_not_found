package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import inf112.skeleton.app.RallyGame;

public abstract class StandardScreen implements Screen {
    public final RallyGame game;
    public final OrthographicCamera camera;
    public final SpriteBatch batch;
    public final FitViewport viewport;

    public StandardScreen(final RallyGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        this.viewport.apply(true);
    }

    @Override
    public void show() {
        // Empty functions gives bad code quality
    }

    @Override
    public void render(float v) {
        // Empty functions gives bad code quality
    }

    public void renderSettings(float v) {
        Gdx.gl.glClearColor(v, v, v, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Empty functions gives bad code quality
    }

    @Override
    public void resume() {
        // Empty functions gives bad code quality
    }

    @Override
    public void hide() {
        // Empty functions gives bad code quality
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
