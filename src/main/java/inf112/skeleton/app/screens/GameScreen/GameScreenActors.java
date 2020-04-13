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

    private final RallyGame game;
    private final Stage stage;
    private ProgramCardSkin cardSkin;
    private float cardWidth;
    private float cardHeight;
    private float ratio;
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
        for (int y = 60; y <= 156; y += 48) {
            for (int x = 720; x <= 840; x += 60) {
                newDamageToken(x, y, 48, 48);
            }
        }
    }

    public void newDamageToken(int x, int y, int width, int height) {
        Image token = new Image(new Texture("assets/images/damageToken.png"));
        token.setSize(width, height);
        token.setPosition(x, y);
        damageTokens.add(token);
        stage.addActor(token);
    }

    public void initializeLifeTokens() {
        for (int x = 720; x <= 840; x += 60) {
            newLifeToken(x, 0, 60, 60);
        }
    }

    public void newLifeToken(int x, int y, int width, int height) {
        Image token = new Image(new Texture("assets/images/lifeToken.png"));
        token.setSize(width, height);
        token.setPosition(x, y);
        lifeTokens.add(token);
        stage.addActor(token);
    }
}
