package inf112.skeleton.app.screens.gamescreen;

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

    public final float mapHeight = 12;
    public final float mapWidth = 16;
    public final float programCardRatio = 0.72f;
    public float screenWidth;
    public float screenHeight;
    public float mapRightPx;
    public float programCardWidth;
    public float programCardHeight;
    public float confirmButtonSize;
    public float damageTokenSize;
    public float lifeTokenSize;

    private ImageButton confirmButton;

    private final RallyGame game;
    private final Stage stage;
    private final ProgramCardSkin cardSkin;
    private final ArrayList<ImageButton> programCardButtons;
    private final ArrayList<Image> damageTokens;
    private final ArrayList<Image> lifeTokens;

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
        damageTokenSize = (screenWidth - mapRightPx) / 5;
    }

    public void initializeProgramCardButtons() {
        int idx = 0;
        for (int dy = 1; dy <= 3; dy++) {
            for (int dx = 0; dx <= 2; dx++) {
                ProgramCard card = game.mainPlayer.getAllCards().get(idx);
                ImageButton.ImageButtonStyle cardStyle = new ImageButton.ImageButtonStyle();
               // System.out.println("CARD: "+card.getName() + " " + card);
                cardStyle.up = cardSkin.getSkins().getDrawable(card.getName());
                ImageButton cardButton = new ImageButton(cardStyle);
                cardButton.setSize(programCardWidth, programCardHeight);
                cardButton.setPosition(mapRightPx + programCardWidth * dx, screenHeight - programCardHeight * dy);
                setCardButtonInputListener(card, cardButton);
                programCardButtons.add(cardButton);
                stage.addActor(cardButton);
                idx++;
            }
        }
    }

    private void setCardButtonInputListener(ProgramCard card, ImageButton cardButton) {
        cardButton.clearListeners();
        cardButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.mainPlayer.selectCard(card);
                System.out.println(game.mainPlayer.getSelectedCards());
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    public void initializeConfirmButton() {
        TextureAtlas atlas = new TextureAtlas();
        TextureRegion confirmTexture = new TextureRegion(new Texture("assets/images/ConfirmButton.png"));
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        atlas.addRegion("Confirm button", confirmTexture);
        Skin skin = new Skin(atlas);
        style.up = skin.getDrawable("Confirm button");
        confirmButton = new ImageButton(style);
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
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                float x = mapRightPx + j * damageTokenSize;
                float y = lifeTokenSize + i * damageTokenSize;
                newDamageToken(x, y);
            }
        }
    }

    public void newDamageToken(double x, double y) {
        Image token = new Image(new Texture("assets/images/damageToken.png"));
        token.setSize(damageTokenSize, damageTokenSize);
        token.setPosition((float) x, (float) y);
        damageTokens.add(token);
        stage.addActor(token);
    }

    public void initializeLifeTokens() {
        for (int i = 0; i < 3; i++) {
            float x = mapRightPx + i * lifeTokenSize;
            newLifeToken(x, 0);
        }
    }

    public void newLifeToken(double x, double y) {
        Image token = new Image(new Texture("assets/images/lifeToken.png"));
        token.setSize(lifeTokenSize, lifeTokenSize);
        token.setPosition((float) x, (float) y);
        lifeTokens.add(token);
        stage.addActor(token);
    }

    public void updateButtons() {
        updateConfirm();
        updateLifeTokens();
        updateDamageTokens();
        if (game.haveReceivedDeck()) {
            updateCards();
        }
    }

    public void updateConfirm() {
        if (game.mainPlayer.getSelectedCards().size() == 5) {
            confirmButton.getStyle().up = game.buttonSkins.getSkins().getDrawable("Confirm ready");
        } else {
            confirmButton.getStyle().up = game.buttonSkins.getSkins().getDrawable("Confirm not ready");
        }
    }

    public void updateLifeTokens() {
        for (int i = 0; i < 3; i++) {
            lifeTokens.get(i).setVisible(i < game.mainPlayer.getLifeTokens());
        }
    }

    public void updateDamageTokens() {
        for (int i = 0; i < 10; i++) {
            damageTokens.get(i).setVisible(i < game.mainPlayer.getDamageTokens());
        }
    }

    public void updateCards() {
        if (game.mainPlayer.getAllCards().size() < 9) {
            return;
        }
        for (int i = 0; i < 9; i++) {
            ProgramCard card = game.mainPlayer.getAllCards().get(i);
            ImageButton cardButton = programCardButtons.get(i);
            cardButton.getStyle().up = cardSkin.getSkins().getDrawable(card.getName());
            setCardButtonInputListener(card, cardButton);
        }
    }
}
