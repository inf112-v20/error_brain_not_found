package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MenuButtonSkin {
    public Skin menuButtonSkin;

    public MenuButtonSkin() {
        TextureAtlas atlas = new TextureAtlas();

        TextureRegion StartButton = new TextureRegion(new Texture("assets/images/Start_Button.png"));
        TextureRegion StartButtonActive = new TextureRegion(new Texture("assets/images/Start_Button_Active.png"));
        TextureRegion ExitButtonInactive = new TextureRegion(new Texture("assets/images/Exit_Button_Inactive.png"));
        TextureRegion ExitButtonActive = new TextureRegion(new Texture("assets/images/Exit_Button_Active.png"));

        atlas.addRegion("Start button over", StartButton);
        atlas.addRegion("Start button", StartButtonActive);
        atlas.addRegion("Exit button over", ExitButtonInactive);
        atlas.addRegion("Exit button", ExitButtonActive);

        this.menuButtonSkin = new Skin(atlas);
    }
}
