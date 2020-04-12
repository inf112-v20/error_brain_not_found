package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import inf112.skeleton.app.RallyGame;

public class LoadingScreen extends StandardScreen {

    private final Image background;

    public LoadingScreen(final RallyGame game) {
        super(game);
        background = new Image(new Texture("assets/images/RoboRallyMenuScreen.png"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        stage.addActor(background);
    }

    @Override
    public void render(float v) {
        renderSettings(v);

        stage.act(v);
        stage.draw();

        if (Gdx.input.isTouched()) {
            game.dispose();
            game.setScreen(new MenuScreen(game));
        }
    }
}
