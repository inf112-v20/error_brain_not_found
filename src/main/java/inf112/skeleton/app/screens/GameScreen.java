package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.skeleton.app.Player;
import inf112.skeleton.app.RallyGame;

public class GameScreen extends StandardScreen {

    private TiledMapRenderer mapRenderer;
    private Texture lifeTokens;
    private Texture damageTokens;
    private Player player;
    private int numberOfTokens;

    public GameScreen(final RallyGame game) {
        super(game);

        lifeTokens = new Texture("assets/images/lifeToken.png");
        super.camera.setToOrtho(false, game.board.getWidth() * 300, game.board.getHeight() * 300);
        this.mapRenderer = new OrthogonalTiledMapRenderer(game.getBoard().getMap());
        mapRenderer.setView(camera);
        //numberOfTokens = player.getLifeTokens();

        //game.loadTokens();
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderSettings(v);


      batch.begin();
        mapRenderer.render();

         //game.renderNewTokens(3);
        // renderTokens(numberOfTokens);
         //game.loadTokens();

        //System.out.println(player.getLifeTokens());
       // player.getLifeTokens();
        batch.end();

    }
    public void renderTokens(int tokens){
        //int numberOfLifeTokens = player.getLifeTokens();
        batch.end();  batch.begin();
        for (int i =1; i <= tokens; i++){
            batch.draw(lifeTokens,i*15,i*2);
        }

        //lifeTokens.draw(batch,gam);
    }
    public void loadTokens(){
        batch = new SpriteBatch();

    }
}
