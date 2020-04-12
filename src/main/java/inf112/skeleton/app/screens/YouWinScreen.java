package inf112.skeleton.app.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.StandardScreen.StandardScreen;

public class YouWinScreen extends StandardScreen {

    public YouWinScreen(final RallyGame game) {
        super(game);

        Image background = new Image(new Texture("assets/images/YouWin.png"));
        stage.addActor(background);
    }
}
