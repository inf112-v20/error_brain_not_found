package inf112.skeleton.app.screens.menuscreen;

import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;


public class MenuScreen extends StandardScreen {

    public MenuScreen(RallyGame game) {
        super(game);

        MenuScreenActors actors = new MenuScreenActors(game, stage);
        actors.initializeBackground();
        actors.initializeSelectMap();
        actors.initializeStartButton();
        //actors.initializePreferenceButton();
        actors.initializeExitButton();
    }
}
