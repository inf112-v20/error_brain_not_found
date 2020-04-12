package inf112.skeleton.app.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import inf112.skeleton.app.MenuScreenActors;
import inf112.skeleton.app.RallyGame;


public class MenuScreen extends StandardScreen {

    public MenuScreen(RallyGame game) {
        super(game);

        MenuScreenActors menuScreenActors = new MenuScreenActors();
        Image background = menuScreenActors.initializeBackground();
        SelectBox<String> selectMap = menuScreenActors.initializeSelectMap();
        ImageButton startButton = menuScreenActors.initializeStartButton(game, selectMap);
        ImageButton exitButton = menuScreenActors.initializeExitButton();

        stage.addActor(background);
        stage.addActor(selectMap);
        stage.addActor(startButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float v) {
        renderSettings(v);

        stage.act(v);
        stage.draw();
    }
}
