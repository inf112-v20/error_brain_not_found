package inf112.skeleton.app.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;

public class YouWinScreen extends StandardScreen {

    public YouWinScreen(final RallyGame game) {
        super(game);

        Image background = new Image(game.getActorImages().getDrawable("You win background"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        stage.addActor(background);
    }
}
