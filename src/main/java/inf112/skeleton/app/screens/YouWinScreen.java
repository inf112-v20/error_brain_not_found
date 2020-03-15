package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import inf112.skeleton.app.RallyGame;

public class YouWinScreen extends StandardScreen {

    private Texture background;

    public YouWinScreen(final RallyGame game) {
        super(game);

        background = new Texture("assets/images/YouWin.png");
    }

    @Override
    public void render(float v) {
        renderSettings(v);

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        super.dispose();
    }
}
