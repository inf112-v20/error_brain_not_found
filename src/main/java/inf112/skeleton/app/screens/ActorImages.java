package inf112.skeleton.app.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ActorImages {

    private final Skin skin;

    public ActorImages() {
        TextureAtlas atlas = new TextureAtlas();

        TextureRegion confirmReady = new TextureRegion(new Texture("assets/images/ConfirmButton.png"));
        TextureRegion confirmNotReady = new TextureRegion(new Texture("assets/images/ConfirmButtonNotReady.png"));
        TextureRegion Exit = new TextureRegion(new Texture("assets/images/Exit button active.png"));
        TextureRegion ExitOver = new TextureRegion(new Texture("assets/images/Exit button inactive.png"));
        TextureRegion Start = new TextureRegion(new Texture("assets/images/Start button active.png"));
        TextureRegion StartOver = new TextureRegion(new Texture("assets/images/Start button inactive.png"));
        TextureRegion Settings = new TextureRegion(new Texture("assets/images/Settings Active.png"));
        TextureRegion SettingsOver = new TextureRegion(new Texture("assets/images/Settings Inactive.png"));
        TextureRegion powerDownActive = new TextureRegion(new Texture("assets/images/Power Down Active.png"));
        TextureRegion powerDownInactive = new TextureRegion(new Texture("assets/images/Power Down Inactive.png"));
        TextureRegion powerUpActive = new TextureRegion(new Texture("assets/images/Power Up Active.png"));
        TextureRegion powerUpInactive = new TextureRegion(new Texture("assets/images/Power Up Inactive.png"));
        TextureRegion poweringDown = new TextureRegion(new Texture("assets/images/Powering Down.png"));

        TextureRegion damageToken = new TextureRegion(new Texture("assets/images/damageToken.png"));
        TextureRegion lifeToken = new TextureRegion(new Texture("assets/images/lifeToken.png"));

        TextureRegion loadingScreenBackground = new TextureRegion(new Texture("assets/images/RoboRallyMenuScreen.png"));
        TextureRegion menuScreenBackground = new TextureRegion(new Texture("assets/images/GUI_Edited.jpg"));
        TextureRegion youWinBackground = new TextureRegion(new Texture("assets/images/YouWin.png"));

        atlas.addRegion("Confirm ready", confirmReady);
        atlas.addRegion("Confirm not ready", confirmNotReady);
        atlas.addRegion("Exit", Exit);
        atlas.addRegion("Exit over", ExitOver);
        atlas.addRegion("Start", Start);
        atlas.addRegion("Start over", StartOver);
        atlas.addRegion("Settings", Settings);
        atlas.addRegion("Settings over", SettingsOver);
        atlas.addRegion("Power down active", powerDownActive);
        atlas.addRegion("Power down inactive", powerDownInactive);
        atlas.addRegion("Power up active", powerUpActive);
        atlas.addRegion("Power up inactive", powerUpInactive);
        atlas.addRegion("Powering down", poweringDown);
        atlas.addRegion("Loading screen background", loadingScreenBackground);
        atlas.addRegion("Menu screen background", menuScreenBackground);
        atlas.addRegion("You win background", youWinBackground);
        atlas.addRegion("Damage token", damageToken);
        atlas.addRegion("Life token", lifeToken);

        this.skin = new Skin(atlas);
    }

    public Skin getSkin() {
        return skin;
    }
}
