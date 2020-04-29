package inf112.skeleton.app.screens.standardscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import inf112.skeleton.app.RallyGame;

public class SettingsInput extends InputAdapter {

    private final RallyGame game;

    public SettingsInput(RallyGame game) {
        this.game = game;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                break;
            case Input.Keys.M:
                game.muteMusic();
                game.muteSounds();
                break;
            /*case Input.Keys.S:

                break;*/
            default:
                // Fuck u Codacy
        }
        return false;
    }
}
