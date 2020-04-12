package inf112.skeleton.app;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import inf112.skeleton.app.cards.ProgramCard;

public class ProgramCardButtons {

    private ProgramCardSkin cardSkin;
    private float cardWidth;
    private float cardHeight;
    private float ratio;

    public ProgramCardButtons() {
        cardSkin = new ProgramCardSkin();
        ratio = 242 / 173f;
        cardWidth = 240 / 3f;
        cardHeight = cardWidth * ratio;
    }

    public void initializeButtons(RallyGame game, Stage stage) {
        int idx = 0;
        System.out.println(game.mainPlayer.getAllCards().toString());
        for (int dy = 1; dy <= 3; dy++) {
            for (int dx = 0; dx <= 2; dx++) {
                ProgramCard card = game.mainPlayer.getAllCards().get(idx);
                ImageButton.ImageButtonStyle cardStyle = new ImageButton.ImageButtonStyle();
                cardStyle.up = cardSkin.getSkins().getDrawable(card.getName());
                ImageButton cardButton = new ImageButton(cardStyle);
                cardButton.setSize(cardWidth, cardHeight);
                cardButton.setPosition(720 + cardWidth * dx, game.getScreen().viewport.getWorldHeight() - cardHeight * dy);
                cardButton.addListener(new InputListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        System.out.println("Pressed " + card.getName() + " with priority " + card.getPriority());
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
}
