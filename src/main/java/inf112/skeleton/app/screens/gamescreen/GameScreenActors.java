package inf112.skeleton.app.screens.gamescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private final float labelFontScale;
    public float screenWidth;
    public float screenHeight;
    public float mapRightPx;
    private final ArrayList<Label> numberLabels;
    private final ArrayList<Label> cardPriorities;
    private final ArrayList<Label> lockedLabels;
    public float programCardWidth;
    public float programCardHeight;
    public float confirmButtonSize;
    public float damageTokenSize;
    public float lifeTokenSize;
    public boolean canPickCards;

    private ImageButton confirmButton;

    private final RallyGame game;
    private final Stage stage;
    private final ProgramCardSkin cardSkin;
    private final ArrayList<ImageButton> programCardButtons;
    private final ArrayList<Image> damageTokens;
    private final ArrayList<Image> lifeTokens;
    public Skin skin;
    public Skin numberSkin;
    public BitmapFont font;

    public GameScreenActors(RallyGame game, Stage stage) {
        this.game = game;
        this.stage = stage;
        this.skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));
        this.numberSkin = new Skin(Gdx.files.internal("assets/skins/number-cruncher-ui.json"));
        programCardButtons = new ArrayList<>();
        damageTokens = new ArrayList<>();
        lifeTokens = new ArrayList<>();
        numberLabels = new ArrayList<>();
        cardPriorities = new ArrayList<>();
        lockedLabels = new ArrayList<>();

        cardSkin = new ProgramCardSkin();

        screenWidth = game.getScreen().viewport.getScreenWidth();
        screenHeight = game.getScreen().viewport.getScreenHeight();
        mapRightPx = (screenHeight / mapHeight) * mapWidth;

        programCardWidth = (screenWidth - mapRightPx) / 3f;
        programCardHeight = programCardWidth / programCardRatio;
        lifeTokenSize = (screenWidth - mapRightPx) / 4f;
        confirmButtonSize = lifeTokenSize;
        damageTokenSize = (screenWidth - mapRightPx) / 5f;

        labelFontScale = screenWidth / 960f;

        canPickCards = true;
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
                if (game.shouldPickCards()) {
                    game.mainPlayer.selectCard(card);
                    updateCardNumbers();
                    System.out.println(game.mainPlayer.getRegisters());
                }
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
            } else {
                hideCardButton(buttonIndex);
            }
        }
        moveLockedCards();
    }

    public void addCardToButton(ProgramCard card, int buttonIndex) {
        ImageButton cardButton = programCardButtons.get(buttonIndex);
        cardButton.getStyle().up = cardSkin.getSkins().getDrawable(card.getName());
        setCardButtonInputListener(card, cardButton);
        cardButton.setVisible(true);
    }

    public void moveLockedCards() {
        for (int i = 4; i >= 0; i--) {
            Register register = game.mainPlayer.getRegisters().getRegister(i);
            if (!register.isOpen()) {
                ProgramCard card = register.getProgramCard();
                addCardToButton(card, i + 4);
            }
        }
    }

    public void hideCardButton(int buttonIndex) {
        programCardButtons.get(buttonIndex).setVisible(false);
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
                if (game.shouldPickCards()) {
                    game.setShouldPickCards(false);
                    game.confirmCards();
                }
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
        for (ImageButton button : programCardButtons) {
            Label cardPriority = new Label("", numberSkin);
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
            Label numberLabel = new Label("", numberSkin);
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
            } else {
                return;
            }
        }
    }

    // LOCKED LABELS

    public void initializeLockedLabels() {
        for (ImageButton button : programCardButtons) {
            Label lockedLabel = new Label("LOCKED", numberSkin);
            lockedLabel.setWidth(programCardWidth);
            float x = button.getX();
            float y = button.getY() + programCardHeight * .1f;
            lockedLabel.setPosition(x, y);
            lockedLabel.setFontScale(labelFontScale);
            lockedLabel.setAlignment(Align.center);
            lockedLabel.setVisible(false);
            stage.addActor(lockedLabel);
            lockedLabels.add(lockedLabel);
        }
    }

    public void lockCard(int buttonIndex) {
        lockedLabels.get(buttonIndex).setVisible(true);
    }

    public void unlockAllCards() {
        for (Label label : lockedLabels) {
            label.setVisible(false);
        }
    }
}
