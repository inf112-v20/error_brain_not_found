package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.RallyGame;

public abstract class StandardScreen implements Screen {
    protected final RallyGame game;
    protected final OrthographicCamera camera;
    protected final SpriteBatch batch;

    public StandardScreen(final RallyGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false);
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

        camera.setToOrtho(false);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void resize(int i, int i1) {
        // Empty functions gives bad code quality
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
