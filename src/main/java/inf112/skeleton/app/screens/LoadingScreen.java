package inf112.skeleton.app.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.MenuScreen.MenuScreen;
import inf112.skeleton.app.screens.StandardScreen.StandardScreen;


public class LoadingScreen extends StandardScreen {


    public LoadingScreen(final RallyGame game) {
        super(game);

        Image background = new Image(new Texture("assets/images/RoboRallyMenuScreen.png"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        background.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new MenuScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(background);
    }
}
