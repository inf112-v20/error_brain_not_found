package inf112.skeleton.app.screens.menuscreen;

import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;


public class MenuScreen extends StandardScreen {

    private MenuScreenActors actors;

    public MenuScreen(RallyGame game) {
        super(game);

        actors = new MenuScreenActors(game, stage);
        actors.initializeBackground();
        actors.initializeSelectMap();
        actors.initializeStartButton();
        actors.initializeExitButton();
        actors.initializeCreateGame();
        actors.initializeJoinGame();
        actors.initializeIPInput();
        actors.initializeWaitForHostLabel();
        actors.initializeIPLabel();
        actors.initializeInvalidInputLabel();
        actors.initializeWaitForClientsLabel();
    }

    public MenuScreenActors getActors() {
        return actors;
    }
}
