package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import inf112.skeleton.app.RallyGame;

public class MenuScreen implements Screen {

    private static final int START_BUTTON_WIDTH = 250;
    private static final int START_BUTTON_HEIGHT = 150;
    private static final int START_BUTTON_Y = 230;
    private static final int EXIT_BUTTON_WIDTH = 250;
    private static final int EXIT_BUTTON_HEIGHT = 150;
    private static final int EXIT_BUTTON_Y = 100;

    private final RallyGame game;

    Texture startButtonInactive;
    Texture startButtonActive;
    Texture exitButtonInactive;
    Texture exitButtonActive;

    private OrthographicCamera camera;
    private Texture background;
    private Music music;

    public MenuScreen(RallyGame game) {
        this.game = game;

        background = new Texture("assets/images/GUI_Edited.jpg");
        startButtonActive = new Texture("assets/images/Start_Button.png");
        startButtonInactive = new Texture("assets/images/Start_Button_Active.png");
        exitButtonInactive = new Texture("assets/images/Exit_Button_Active.png");
        exitButtonActive= new Texture("assets/images/Exit_Button_Inactive.png");

        music = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/menu_music.mp3"));
        music.setLooping(true);
        music.setVolume(1f);
        music.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
    @Override
    public void show() {
    }
    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.begin();
        game.batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);

        int x = (int) (camera.viewportWidth /2 - EXIT_BUTTON_WIDTH/2);
        if (Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && camera.viewportHeight - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && camera.viewportHeight - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.batch.draw(exitButtonActive,x,EXIT_BUTTON_Y,EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()){
                Gdx.app.exit();
            }
        }
        else game.batch.draw(exitButtonInactive,x,EXIT_BUTTON_Y,EXIT_BUTTON_WIDTH,EXIT_BUTTON_HEIGHT);

        x = (int) (camera.viewportWidth / 2 - START_BUTTON_WIDTH /2);
        if (Gdx.input.getX() < x + START_BUTTON_WIDTH && Gdx.input.getX() > x && camera.viewportHeight - Gdx.input.getY() < START_BUTTON_Y + START_BUTTON_HEIGHT && camera.viewportHeight - Gdx.input.getY() > START_BUTTON_Y) {
            game.batch.draw(startButtonActive,x,START_BUTTON_Y,START_BUTTON_WIDTH,START_BUTTON_HEIGHT);
            if (Gdx.input.isTouched()){
                game.setupGame("assets/maps/Risky_Exchange.tmx");
                game.setScreen(new GameScreen(game));
                this.dispose();
            }
        }
        else { game.batch.draw(startButtonInactive,x, START_BUTTON_Y,START_BUTTON_WIDTH,START_BUTTON_HEIGHT);}

        game.batch.end();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        background.dispose();

    }

}
