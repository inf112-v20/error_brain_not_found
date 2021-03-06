package inf112.skeleton.app.screens.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.standardscreen.StandardScreen;

public class GameScreen extends StandardScreen {

    private final TiledMapRenderer mapRenderer;
    public final GameScreenActors actors;

    public GameScreen(final RallyGame game) {
        super(game);

        actors = new GameScreenActors(game, stage);
        actors.initializeProgramCardButtons();
        actors.initializeConfirmButton();
        actors.initializeLifeTokens();
        actors.initializeLifeTokenLabel();
        actors.initializeDamageTokens();
        actors.initializeDamageTokenLabel();
        actors.initializeNumberLabels();
        actors.initializePriorityLabels();
        actors.initializeLockedLabels();
        actors.initializePowerDownButton();
        actors.initializeInfoLabel();

        float tilePx = 300f;
        float unitScale = ((float) Gdx.graphics.getHeight() / game.getBoard().getBoardHeight()) / tilePx;

        this.mapRenderer = new OrthogonalTiledMapRenderer(game.getBoard().getTiledMap(), unitScale, batch);
        this.mapRenderer.setView(camera);
    }

    @Override
    public void render(float v) {
        actors.updateButtons();
        super.render(v);
        mapRenderer.render();
    }

    public void updateCards() {
        actors.updateCards();
    }
}
