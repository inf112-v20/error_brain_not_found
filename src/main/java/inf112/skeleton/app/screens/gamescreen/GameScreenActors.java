package inf112.skeleton.app.screens.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.cards.Register;

import java.util.ArrayList;

public class GameScreenActors {

    public final float mapHeight = 12;
    public final float mapWidth = 16;
    public final float programCardRatio = 0.72f;
    private float labelFontScale;
    public float screenWidth;
    public float screenHeight;
    public float mapRightPx;
    private final ArrayList<Label> numberLabels;
    private final ArrayList<Label> cardPriorities;
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
    public Skin skin;

    public GameScreenActors(RallyGame game, Stage stage) {
        this.game = game;
        this.stage = stage;
        this.skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));
        programCardButtons = new ArrayList<>();
        damageTokens = new ArrayList<>();
        lifeTokens = new ArrayList<>();
        numberLabels = new ArrayList<>();
        cardPriorities = new ArrayList<>();

        cardSkin = new ProgramCardSkin();

        screenWidth = game.getScreen().viewport.getScreenWidth();
        screenHeight = game.getScreen().viewport.getScreenHeight();
        mapRightPx = (screenHeight / mapHeight) * mapWidth;

        programCardWidth = (screenWidth - mapRightPx) / 3f;
        programCardHeight = programCardWidth / programCardRatio;
        lifeTokenSize = (screenWidth - mapRightPx) / 4f;
        confirmButtonSize = lifeTokenSize;
        damageTokenSize = (screenWidth - mapRightPx) / 5f;
    }

    public void updateButtons() {
        updateConfirm();
        updateLifeTokens();
        updateDamageTokens();
        updateCards();
    }

    // PROGRAM CARD BUTTONS

    public void initializeProgramCardButtons() {
        int idx = 0;
        for (int dy = 1; dy <= 3; dy++) {
            for (int dx = 0; dx <= 2; dx++) {
                ProgramCard card = game.mainPlayer.getCardsOnHand().get(idx);
                ImageButton.ImageButtonStyle cardStyle = new ImageButton.ImageButtonStyle();
                cardStyle.up = cardSkin.getSkins().getDrawable(card.getName());
                ImageButton cardButton = new ImageButton(cardStyle);
                cardButton.setSize(programCardWidth, programCardHeight);
                cardButton.setPosition(mapRightPx + programCardWidth * dx, screenHeight - programCardHeight * dy * 1.18f);
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
                updateCardNumbers();
                System.out.println(game.mainPlayer.getRegisters());
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
    }

    public void updateCards() {
        for (int buttonIndex = 0; buttonIndex < 9; buttonIndex++) {
            if (buttonIndex < game.mainPlayer.getCardsOnHand().size()) {
                ProgramCard card = game.mainPlayer.getCardsOnHand().get(buttonIndex);
                addCardToButton(card, buttonIndex);
                drawPriority(card);
            }
        }
    }

    public void addCardToButton(ProgramCard card, int buttonIndex) {
        ImageButton cardButton = programCardButtons.get(buttonIndex);
        cardButton.getStyle().up = cardSkin.getSkins().getDrawable(card.getName());
        setCardButtonInputListener(card, cardButton);
        cardButton.setVisible(true);
    }

    // CONFIRM BUTTON

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

    public void updateConfirm() {
        if (!game.mainPlayer.getRegisters().hasRegistersWithoutCard()) {
            confirmButton.getStyle().up = game.buttonSkins.getSkins().getDrawable("Confirm ready");
        } else {
            confirmButton.getStyle().up = game.buttonSkins.getSkins().getDrawable("Confirm not ready");
        }
    }

    // DAMAGE TOKEN IMAGE

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

    public void updateDamageTokens() {
        for (int i = 0; i < 10; i++) {
            damageTokens.get(i).setVisible(i < game.mainPlayer.getDamageTokens());
        }
    }

    // LIFE TOKEN IMAGE

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

    public void updateLifeTokens() {
        for (int i = 0; i < 3; i++) {
            lifeTokens.get(i).setVisible(i < game.mainPlayer.getLifeTokens());
        }
    }

    // PRIORITY LABEL

    public void initializePriorityLabels() {

        //  Skin john =new Skin(Gdx.files.internal("skins/number-cruncher-ui.atlas"));
        for (ImageButton button : programCardButtons) {
            Label cardPriority = new Label("", skin);
            float height = programCardHeight * .18f;
            float x = button.getX();
            float y = button.getY() + programCardHeight + height / 2;
            cardPriority.setWidth(programCardWidth);
            cardPriority.setPosition(x, y);
            cardPriority.setFontScale(labelFontScale);
            cardPriority.setAlignment(Align.center);
            stage.addActor(cardPriority);
            cardPriorities.add(cardPriority);
        }
    }

    public void drawPriority(ProgramCard card) {
        int index = game.mainPlayer.getCardsOnHand().indexOf(card);
        Label label = cardPriorities.get(index);
        label.setText((card.getPriority()));
    }

    // NUMBER LABELS

    public void initializeNumberLabels() {
        for (ImageButton button : programCardButtons) {
            Label numberLabel = new Label("", skin);
            float x = button.getX() + programCardWidth * .1f;
            float y = button.getY() + programCardHeight * .2f;
            numberLabel.setPosition(x, y);
            numberLabel.setFontScale(labelFontScale * 1.8f);
            numberLabel.setVisible(false);
            stage.addActor(numberLabel);
            numberLabels.add(numberLabel);
        }
    }

    public void drawNumberOnCard(Register register) {
        ProgramCard card = register.getProgramCard();
        int index = game.mainPlayer.getCardsOnHand().indexOf(card);
        Label label = numberLabels.get(index);
        label.setText(register.getRegisterNumber() + 1);
        label.setVisible(true);
    }

    public void removeNumberFromCard() {
        for (Label label : numberLabels) {
            label.setText("");
            label.setVisible(false);
        }
    }

    public void updateCardNumbers() {
        removeNumberFromCard();
        for (Register register : game.mainPlayer.getRegisters().getRegisters()) {
            if (register.hasCard()) {
                drawNumberOnCard(register);
            }
        }
    }
}
