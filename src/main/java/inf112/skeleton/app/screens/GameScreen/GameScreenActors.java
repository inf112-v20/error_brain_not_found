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

import java.util.ArrayList;

public class GameScreenActors {

    public final float tilePx = 300;
    public final float mapHeight = 12;
    public final float mapWidth = 16;
    public final float programCardRatio = 0.72f;
    public float screenWidth;
    public float screenHeight;
    public float mapScale;
    public float mapRightPx;
    public float programCardWidth;
    public float programCardHeight;
    public float confirmButtonSize;
    public float damageTokenSize;
    public float lifeTokenSize;

    private final RallyGame game;
    private final Stage stage;
    private ProgramCardSkin cardSkin;
    private ArrayList<ImageButton> programCardButtons;
    private ArrayList<Image> damageTokens;
    private ArrayList<Image> lifeTokens;

    public GameScreenActors(RallyGame game, Stage stage) {
        this.game = game;
        this.stage = stage;
        programCardButtons = new ArrayList<>();
        damageTokens = new ArrayList<>();
        lifeTokens = new ArrayList<>();

        cardSkin = new ProgramCardSkin();

        screenWidth = game.getScreen().viewport.getScreenWidth();
        screenHeight = game.getScreen().viewport.getScreenHeight();
        mapRightPx = (screenHeight / mapHeight) * mapWidth;
        programCardWidth = (screenWidth - mapRightPx) / 3;
        programCardHeight = programCardWidth / programCardRatio;
        lifeTokenSize = (screenWidth - mapRightPx) / 4;
        confirmButtonSize = lifeTokenSize;
        damageTokenSize = lifeTokenSize * 0.8f;
    }

    public void initializeProgramCardButtons() {
        int idx = 0;
        for (int dy = 1; dy <= 3; dy++) {
            for (int dx = 0; dx <= 2; dx++) {
                ProgramCard card = game.currentPlayer.getAllCards().get(idx);
                ImageButton.ImageButtonStyle cardStyle = new ImageButton.ImageButtonStyle();
                cardStyle.up = cardSkin.getSkins().getDrawable(card.getName());
                ImageButton cardButton = new ImageButton(cardStyle);
                cardButton.setSize(programCardWidth, programCardHeight);
                cardButton.setPosition(mapRightPx + programCardWidth * dx, screenHeight - programCardHeight * dy);
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
                programCardButtons.add(cardButton);
                stage.addActor(cardButton);
                idx++;
            }
        }
    }

    public void initializeConfirmButton() {
        TextureAtlas atlas = new TextureAtlas();
        TextureRegion confirmTexture = new TextureRegion(new Texture("assets/images/ConfirmButton.png"));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        atlas.addRegion("Confirm button", confirmTexture);
        Skin skin = new Skin(atlas);
        style.up = skin.getDrawable("Confirm button");
        ImageButton confirmButton = new ImageButton(style);
        confirmButton.setSize(confirmButtonSize, confirmButtonSize);
        confirmButton.setPosition(screenWidth - confirmButtonSize, 0);
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
        for (float y = lifeTokenSize; y < lifeTokenSize + 3 * damageTokenSize; y += damageTokenSize) {
            for (float x = mapRightPx; x < mapRightPx + 3 * damageTokenSize; x += damageTokenSize) {
                newDamageToken(x, y);
            }
        }
    }

    public void newDamageToken(float x, float y) {
        Image token = new Image(new Texture("assets/images/damageToken.png"));
        token.setSize(damageTokenSize, damageTokenSize);
        token.setPosition(x, y);
        damageTokens.add(token);
        stage.addActor(token);
    }

    public void initializeLifeTokens() {
        for (float x = mapRightPx; x < mapRightPx + 3 * lifeTokenSize; x += lifeTokenSize) {
            newLifeToken(x, 0);
        }
    }

    public void newLifeToken(float x, float y) {
        Image token = new Image(new Texture("assets/images/lifeToken.png"));
        token.setSize(lifeTokenSize, lifeTokenSize);
        token.setPosition(x, y);
        lifeTokens.add(token);
        stage.addActor(token);
    }
}
