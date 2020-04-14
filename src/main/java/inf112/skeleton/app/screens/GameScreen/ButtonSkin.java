package inf112.skeleton.app.screens.GameScreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ButtonSkin {

    private final Skin buttonSkin;

    public ButtonSkin() {
        TextureAtlas atlas = new TextureAtlas();

        TextureRegion confirmReady = new TextureRegion(new Texture("assets/programCards/ConfirmButton.png"));
        TextureRegion confirmNotReady = new TextureRegion(new Texture("assets/programCards/ConfirmButtonNotReady.png"));
        TextureRegion ExitActive = new TextureRegion(new Texture("assets/programCards/Exit_Button_Active.png"));
        TextureRegion ExitInactive = new TextureRegion(new Texture("assets/programCards/Exit_Button_Inactive.png"));
        TextureRegion StartInactive = new TextureRegion(new Texture("assets/programCards/Start_Button.png"));
        TextureRegion StartActive = new TextureRegion(new Texture("assets/programCards/Start_Button_Active.png"));

        atlas.addRegion("Confirm ready", confirmReady);
        atlas.addRegion("Confirm not ready", confirmNotReady);
        atlas.addRegion("Exit active", ExitActive);
        atlas.addRegion("Exit inactive", ExitInactive);
        atlas.addRegion("Start active", StartActive);
        atlas.addRegion("Start inactive", StartInactive);

        this.buttonSkin = new Skin(atlas);
    }

    public Skin getSkins() {
        return buttonSkin;
    }
}
