package inf112.skeleton.app.screens;

import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.skeleton.app.RallyGame;

public class GameScreen extends StandardScreen {

    private TiledMapRenderer mapRenderer;

    public GameScreen(final RallyGame game) {
        super(game);

        super.camera.setToOrtho(false, game.board.getWidth() * 300, game.board.getHeight() * 300);
        this.mapRenderer = new OrthogonalTiledMapRenderer(game.getBoard().getMap());
        mapRenderer.setView(camera);
    }

    @Override
    public void render(float v) {
        renderSettings(v);

        batch.begin();
        mapRenderer.render();
        batch.end();
    }
}
