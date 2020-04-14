package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.skeleton.app.RallyGame;

public class GameScreen extends StandardScreen {

    private final TiledMapRenderer mapRenderer;
    private final Texture lifeTokens;
    private final Texture damageTokens;
    private float tokensX;
    private float lifeTokensY;
    private float damageTokensY;
    private float tokensSize;

    public GameScreen(final RallyGame game) {
        super(game);

        lifeTokens = new Texture("assets/images/lifeToken.png");
        damageTokens = new Texture("assets/images/damageToken.png");
        super.camera.setToOrtho(false, game.board.getWidth() * 400, game.board.getHeight() * 400);
        this.mapRenderer = new OrthogonalTiledMapRenderer(game.getBoard().getMap());
        mapRenderer.setView(camera);
        updateTokens();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderSettings(v);

        updateTokens();
        mapRenderer.render();
        batch.begin();
        renderLifeTokens();
        renderDamageTokens();
        batch.end();
    }

    public void updateTokens() {
        tokensSize = camera.viewportHeight / 8;
        tokensX = 10;
        lifeTokensY = camera.viewportHeight - tokensSize;
        damageTokensY = lifeTokensY - tokensSize;
    }

    public void renderDamageTokens() {
        for (int i = 0; i < game.mainPlayer.getDamageTokens(); i++) {
            batch.draw(damageTokens, tokensX + i * tokensSize, damageTokensY, tokensSize, tokensSize);
        }
    }

    public void renderLifeTokens() {
        for (int i = 0; i < game.mainPlayer.getLifeTokens(); i++) {
            batch.draw(lifeTokens, tokensX + i * tokensSize, lifeTokensY, tokensSize, tokensSize);
        }
    }
}
