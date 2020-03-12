package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import inf112.skeleton.app.RallyGame;

public class MenuScreen extends StandardScreen {

    private static final int START_BUTTON_WIDTH = 250;
    private static final int START_BUTTON_HEIGHT = 150;
    private static final int START_BUTTON_Y = 230;
    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 150;
    private static final int EXIT_BUTTON_Y = 100;

    Texture startButtonInactive;
    Texture startButtonActive;
    Texture exitButtonInactive;
    Texture exitButtonActive;

    public MenuScreen(RallyGame game) {
        super(game, new Texture("assets/images/GUI_Edited.jpg"));

        startButtonActive = new Texture("assets/images/Start_Button.png");
        startButtonInactive = new Texture("assets/images/Start_Button_Active.png");
        exitButtonInactive = new Texture("assets/images/Exit_Button_Active.png");
        exitButtonActive= new Texture("assets/images/Exit_Button_Inactive.png");
    }

    @Override
    public void render(float v) {
        renderSettings(v);

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);

        int x = (int) (camera.viewportWidth / 2 - EXIT_BUTTON_WIDTH / 2);
        if (Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && camera.viewportHeight - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && camera.viewportHeight - Gdx.input.getY() > EXIT_BUTTON_Y) {
            batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                this.dispose();
                Gdx.app.exit();
            }
        } else batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

        x = (int) (camera.viewportWidth / 2 - START_BUTTON_WIDTH /2);
        if (Gdx.input.getX() < x + START_BUTTON_WIDTH && Gdx.input.getX() > x && camera.viewportHeight - Gdx.input.getY() < START_BUTTON_Y + START_BUTTON_HEIGHT && camera.viewportHeight - Gdx.input.getY() > START_BUTTON_Y) {
            batch.draw(startButtonActive, x, START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                this.dispose();
                game.setScreen(new GameScreen(game));
            }
        } else {
            batch.draw(startButtonInactive, x, START_BUTTON_Y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
        }

        batch.end();
    }
}
