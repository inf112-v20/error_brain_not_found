package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import inf112.skeleton.app.RallyGame;

public abstract class StandardScreen implements Screen {
    protected final RallyGame game;
    protected final OrthographicCamera camera;
    protected final SpriteBatch batch;
    protected final Texture background;

    public StandardScreen(final RallyGame game) {
        this(game, null);
    }

    public StandardScreen(final RallyGame game, final Texture background) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.background = background;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    public void renderSettings(float v) {
        Gdx.gl.glClearColor(v, v, v, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
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
        batch.dispose();
        if (background != null) {
            background.dispose();
        }
    }
}
