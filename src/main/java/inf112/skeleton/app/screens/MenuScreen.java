package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.RallyGame;

import java.io.File;
import java.util.Objects;


public class MenuScreen implements Screen {

    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 150;
    private static final int START_BUTTON_Y = 250;
    private static final int EXIT_BUTTON_Y = 100;

    private RallyGame game;
    public SelectBox<String> selectMap;

    private Texture startButtonInactive;
    private Texture startButtonActive;
    private Texture exitButtonInactive;
    private Texture exitButtonActive;

    private OrthographicCamera camera;
    private Texture background;
    private Stage stage;
    public String select;

    public MenuScreen(RallyGame game) {
        this.game = game;
        stage = new Stage ();
        Skin skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));

        background = new Texture("assets/images/GUI_Edited.jpg");

        startButtonActive = new Texture("assets/images/Start_Button.png");
        startButtonInactive = new Texture("assets/images/Start_Button_Active.png");
        exitButtonInactive = new Texture("assets/images/Exit_Button_Active.png");
        exitButtonActive= new Texture("assets/images/Exit_Button_Inactive.png");


        selectMap = new SelectBox<>(skin);

        selectMap.setItems(getMaps());
        selectMap.setSelected("assets/maps/Risky_Exchange.tmx");
        selectMap.setWidth(BUTTON_WIDTH*.87f);
        selectMap.setPosition((100), START_BUTTON_Y - selectMap.getHeight()*2);

        Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/sound/menu_music.mp3"));
        music.setLooping(true);
        music.setVolume(1f);
        music.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(selectMap);
    }

    private Array<String> getMaps() {
        Array<String> mapArray = new Array<>();
        File maps = new File("assets/maps");
        for (String fileType : Objects.requireNonNull(maps.list())){
            if (fileType.endsWith(".tmx")){
                mapArray.add(fileType.substring(0, fileType.length()-4));
            }
        }
        System.out.println(mapArray);

        return mapArray;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        render(0);
    }
    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);

        int x = (int) (camera.viewportWidth /2 - BUTTON_WIDTH/2);
        if (Gdx.input.getX() < x + BUTTON_WIDTH && Gdx.input.getX() > x && camera.viewportHeight - Gdx.input.getY() < EXIT_BUTTON_Y + BUTTON_HEIGHT && camera.viewportHeight - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.batch.draw(exitButtonActive,x,EXIT_BUTTON_Y,BUTTON_WIDTH,BUTTON_HEIGHT);
            if (Gdx.input.isTouched()){
                Gdx.app.exit();
            }
        }
        else game.batch.draw(exitButtonInactive,x,EXIT_BUTTON_Y,BUTTON_WIDTH,BUTTON_HEIGHT);

        x = (int) (camera.viewportWidth / 2 - BUTTON_WIDTH /2);
        if (Gdx.input.getX() < x + BUTTON_WIDTH && Gdx.input.getX() > x && camera.viewportHeight - Gdx.input.getY() < START_BUTTON_Y + BUTTON_HEIGHT && camera.viewportHeight - Gdx.input.getY() > START_BUTTON_Y) {
            game.batch.draw(startButtonActive,x,START_BUTTON_Y,BUTTON_WIDTH,BUTTON_HEIGHT);
            if (Gdx.input.isTouched()){
                game.setupGame("assets/maps/"+getBoard()+".tmx");
                game.setScreen(new GameScreen(game));
                this.dispose();
            }
        }
        else { game.batch.draw(startButtonInactive,x, START_BUTTON_Y,BUTTON_WIDTH,BUTTON_HEIGHT);}

        game.batch.end();
        stage.act(v);
        stage.draw();
    }

    public String getBoard(){
        select = selectMap.getSelected();
        return select;
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
        stage.dispose();
        background.dispose();
    }

}
