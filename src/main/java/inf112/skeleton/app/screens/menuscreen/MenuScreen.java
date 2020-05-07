package inf112.skeleton.app.screens.menuscreen;

import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;


public class MenuScreen extends StandardScreen {

    private MenuScreenActors actors;

    public MenuScreen(RallyGame game) {
        super(game);

        actors = new MenuScreenActors(game, stage);
        actors.initializeBackground();
        actors.initializeExitButton();
        actors.initializeCreateGame();
        actors.initializeJoinGame();
    }

    public MenuScreenActors getActors() {
        return actors;
    }

    @Override
    public void render(float v) {
        super.render(v);
        if (game.isServer()) {
            actors.updateClientsConnectedLabel();
        }
    }
}
