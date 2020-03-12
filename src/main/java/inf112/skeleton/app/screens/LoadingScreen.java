package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import inf112.skeleton.app.RallyGame;

public class LoadingScreen extends StandardScreen {

    public LoadingScreen(final RallyGame game) {
        super(game, new Texture("assets/images/RoboRallyMenuScreen.png"));
    }

    @Override
    public void render(float v) {
        renderSettings(v);

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new MenuScreen(game));
            this.dispose();
        }
    }
}
