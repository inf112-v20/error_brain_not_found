package inf112.skeleton.app.screens.GameScreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;

public class GameScreenActors {

    private final RallyGame game;
    private final Stage stage;
    private ProgramCardSkin cardSkin;
    private float cardWidth;
    private float cardHeight;
    private float ratio;

    public GameScreenActors(RallyGame game, Stage stage) {
        this.game = game;
        this.stage = stage;

        cardSkin = new ProgramCardSkin();
        ratio = 242 / 173f;
        cardWidth = 240 / 3f;
        cardHeight = cardWidth * ratio;
    }

    public void initializeProgramCardButtons() {
        int idx = 0;
        for (int dy = 1; dy <= 3; dy++) {
            for (int dx = 0; dx <= 2; dx++) {
                ProgramCard card = game.currentPlayer.getAllCards().get(idx);
                ImageButton.ImageButtonStyle cardStyle = new ImageButton.ImageButtonStyle();
                cardStyle.up = cardSkin.getSkins().getDrawable(card.getName());
                ImageButton cardButton = new ImageButton(cardStyle);
                cardButton.setSize(cardWidth, cardHeight);
                cardButton.setPosition(720 + cardWidth * dx, game.getScreen().viewport.getWorldHeight() - cardHeight * dy);
                cardButton.addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        game.currentPlayer.selectCard(card);
                    }

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        return true;
                    }
                });
                stage.addActor(cardButton);
                idx++;
            }
        }
    }

    public void initializeConfirmButton() {
        TextureAtlas atlas = new TextureAtlas();
        TextureRegion confirmTexture = new TextureRegion(new Texture("assets/images/ConfirmButtonNotReady.png"));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        atlas.addRegion("Confirm button", confirmTexture);
        Skin skin = new Skin(atlas);
        style.up = skin.getDrawable("Confirm button");
        ImageButton confirmButton = new ImageButton(style);
        confirmButton.setSize(60, 60);
        confirmButton.setPosition(900, 0);
        confirmButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.confirmCards();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(confirmButton);
    }

    public void initializeDamageTokens() {
        Image damageToken1 = new Image(new Texture("assets/images/damageToken.png"));
        damageToken1.setSize(48, 48);
        damageToken1.setPosition(720, 60);
        stage.addActor(damageToken1);

        Image damageToken2 = new Image(new Texture("assets/images/damageToken.png"));
        damageToken2.setSize(48, 48);
        damageToken2.setPosition(780, 60);
        stage.addActor(damageToken2);

        Image damageToken3 = new Image(new Texture("assets/images/damageToken.png"));
        damageToken3.setSize(48, 48);
        damageToken3.setPosition(840, 60);
        stage.addActor(damageToken3);

        Image damageToken4 = new Image(new Texture("assets/images/damageToken.png"));
        damageToken4.setSize(48, 48);
        damageToken4.setPosition(720, 108);
        stage.addActor(damageToken4);

        Image damageToken5 = new Image(new Texture("assets/images/damageToken.png"));
        damageToken5.setSize(48, 48);
        damageToken5.setPosition(780, 108);
        stage.addActor(damageToken5);

        Image damageToken6 = new Image(new Texture("assets/images/damageToken.png"));
        damageToken6.setSize(48, 48);
        damageToken6.setPosition(840, 108);
        stage.addActor(damageToken6);

        Image damageToken7 = new Image(new Texture("assets/images/damageToken.png"));
        damageToken7.setSize(48, 48);
        damageToken7.setPosition(720, 156);
        stage.addActor(damageToken7);

        Image damageToken8 = new Image(new Texture("assets/images/damageToken.png"));
        damageToken8.setSize(48, 48);
        damageToken8.setPosition(780, 156);
        stage.addActor(damageToken8);

        Image damageToken9 = new Image(new Texture("assets/images/damageToken.png"));
        damageToken9.setSize(48, 48);
        damageToken9.setPosition(840, 156);
        stage.addActor(damageToken9);

    }

    public void initializeLifeTokens() {
        Image lifeToken1 = new Image(new Texture("assets/images/lifeToken.png"));
        lifeToken1.setSize(60, 60);
        lifeToken1.setPosition(720, 0);
        stage.addActor(lifeToken1);

        Image lifeToken2 = new Image(new Texture("assets/images/lifeToken.png"));
        lifeToken2.setSize(60, 60);
        lifeToken2.setPosition(780, 0);
        stage.addActor(lifeToken2);

        Image lifeToken3 = new Image(new Texture("assets/images/lifeToken.png"));
        lifeToken3.setSize(60, 60);
        lifeToken3.setPosition(840, 0);
        stage.addActor(lifeToken3);
    }
}
