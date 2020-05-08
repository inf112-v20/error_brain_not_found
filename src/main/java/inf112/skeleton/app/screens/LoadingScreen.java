package inf112.skeleton.app.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.menuscreen.MenuScreen;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;


public class LoadingScreen extends StandardScreen {


    public LoadingScreen(final RallyGame game) {
        super(game);

        Image background = new Image(game.getActorImages().getDrawable("Loading screen background"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        background.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.getScreen().dispose();
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
