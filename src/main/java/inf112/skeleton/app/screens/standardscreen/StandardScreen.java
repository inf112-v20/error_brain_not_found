package inf112.skeleton.app.screens.standardscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import inf112.skeleton.app.RallyGame;

public abstract class StandardScreen implements Screen {
    public final RallyGame game;
    public final OrthographicCamera camera;
    public final SpriteBatch batch;
    public final Stage stage;
    public final FitViewport viewport;
    private final InputMultiplexer inputMultiplexer;
    protected float elapsed;

    public StandardScreen(final RallyGame game) {
        this.game = game;

        this.camera = new OrthographicCamera();

        this.viewport = new FitViewport(Gdx.graphics.getHeight() * (16 / 9f), Gdx.graphics.getHeight(), camera);
        this.viewport.apply(true);

        this.batch = new SpriteBatch();
        this.batch.setProjectionMatrix(camera.combined);

        this.stage = new Stage(viewport, batch);

        this.inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new SettingsInput(game));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(v, v, v, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        elapsed += Gdx.graphics.getDeltaTime();

        stage.act(v);
        stage.draw();
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
        stage.dispose();
        batch.dispose();
    }
}
