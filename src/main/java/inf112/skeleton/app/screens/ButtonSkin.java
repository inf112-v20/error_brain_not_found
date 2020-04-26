package inf112.skeleton.app.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ButtonSkin {

    private final Skin buttonSkin;

    public ButtonSkin() {
        TextureAtlas atlas = new TextureAtlas();

        // TODO: Legg til "createGame", "createGameOver", "joinGame" and "joinGameOver"
        // TextureRegion createGame = new TextureRegion(new Texture("assets/images/CreateGame.png"));
        // TextureRegion createGameOver= new TextureRegion(new Texture("assets/images/CreateGameOver.png"));
        // TextureRegion joinGame = new TextureRegion(new Texture("assets/images/JoinGame.png"));
        // TextureRegion joinGameOver = new TextureRegion(new Texture("assets/images/JoinGameOver.png"));
        TextureRegion confirmReady = new TextureRegion(new Texture("assets/images/ConfirmButton.png"));
        TextureRegion confirmNotReady = new TextureRegion(new Texture("assets/images/ConfirmButtonNotReady.png"));
        TextureRegion Exit = new TextureRegion(new Texture("assets/images/Exit_Button_Active.png"));
        TextureRegion ExitOver = new TextureRegion(new Texture("assets/images/Exit_Button_Inactive.png"));
        TextureRegion Start = new TextureRegion(new Texture("assets/images/Start_Button_Active.png"));
        TextureRegion StartOver = new TextureRegion(new Texture("assets/images/Start_Button.png"));

        // atlas.addRegion("Create game", createGame);
        // atlas.addRegion("Create game over", createGameOver);
        // atlas.addRegion("Join game", joinGame);
        // atlas.addRegion("Join game over", joinGameOver);
        atlas.addRegion("Confirm ready", confirmReady);
        atlas.addRegion("Confirm not ready", confirmNotReady);
        atlas.addRegion("Exit", Exit);
        atlas.addRegion("Exit over", ExitOver);
        atlas.addRegion("Start", Start);
        atlas.addRegion("Start over", StartOver);

        this.buttonSkin = new Skin(atlas);
    }

    public Skin getSkins() {
        return buttonSkin;
    }
}
