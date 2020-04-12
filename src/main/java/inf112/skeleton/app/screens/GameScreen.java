package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import inf112.skeleton.app.Board;
import inf112.skeleton.app.Player;
import inf112.skeleton.app.ProgramCardButtons;
import inf112.skeleton.app.RallyGame;

public class GameScreen extends StandardScreen {

    private Skin skin;

    private TiledMapRenderer mapRenderer;
    private Image lifeTokens;
    private Image damageTokens;
    private ImageButton confirmButton;
    private Texture confirmProgramCardsButton = new Texture(Gdx.files.internal("assets/images/ConfirmButton.png"));
    private Texture confirmProgramCardsNotReadyButton = new Texture(Gdx.files.internal("assets/images/ConfirmButtonNotReady.png"));

    private float tokensX;
    private float lifeTokensY;
    private float damageTokensY;
    private float tokensSize;
    private Board board;
    private Player player;
    private Texture[] texture;
    private Texture cards;

    public GameScreen(final RallyGame game) {
        super(game);

        skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));

        // TODO: Confirmbutton
        makeConfirmButton();
        // TODO: Life tokens
        lifeTokens = new Image(new Texture("assets/images/lifeToken.png"));
        //lifeTokens.setSize();
        //lifeTokens.setPosition();
        // TODO: Damage tokens
        damageTokens = new Image(new Texture("assets/images/damageToken.png"));
        //damageTokens.setSize();
        //damageTokens.setPosition();
        // TODO: Cards
        ProgramCardButtons cards = new ProgramCardButtons();
        cards.initializeButtons(game, stage);
        // TODO: Map
        float unitScale = 45 / 300f;
        this.mapRenderer = new OrthogonalTiledMapRenderer(game.getBoard().getMap(), unitScale, batch);
        mapRenderer.setView(camera);

        stage.addActor(confirmButton);
        //stage.addActor(lifeTokens);
        //stage.addActor(damageTokens);
    }

    @Override
    public void render(float v) {
        renderSettings(v);
        mapRenderer.render();
        stage.act(v);
        stage.draw();
    }

    private void makeConfirmButton() {
        TextureAtlas atlas = new TextureAtlas();
        TextureRegion confirmTexture = new TextureRegion(new Texture("assets/images/ConfirmButtonNotReady.png"));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        atlas.addRegion("Confirm button", confirmTexture);
        Skin skin = new Skin(atlas);
        style.up = skin.getDrawable("Confirm button");
        confirmButton = new ImageButton(style);
        confirmButton.setSize(50, 50);
        confirmButton.setPosition(910, 0);
        confirmButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Pressed ready button");
                // TODO: Start runde
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }
}
