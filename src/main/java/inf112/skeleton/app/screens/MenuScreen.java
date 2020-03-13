package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.RallyGame;

import java.io.File;
import java.util.Objects;


public class MenuScreen extends StandardScreen {

    private static final int BUTTON_WIDTH = 250;
    private static final int BUTTON_HEIGHT = 150;
    private static final int START_BUTTON_Y = 250;
    private static final int EXIT_BUTTON_Y = 100;

    public SelectBox<String> selectMap;

    private Texture startButtonInactive;
    private Texture startButtonActive;
    private Texture exitButtonInactive;
    private Texture exitButtonActive;

    private Stage stage;
    public String select;

    public MenuScreen(RallyGame game) {
        super(game, new Texture("assets/images/GUI_Edited.jpg"));
        stage = new Stage ();
        Skin skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));

        startButtonActive = new Texture("assets/images/Start_Button.png");
        startButtonInactive = new Texture("assets/images/Start_Button_Active.png");
        exitButtonInactive = new Texture("assets/images/Exit_Button_Active.png");
        exitButtonActive= new Texture("assets/images/Exit_Button_Inactive.png");


        selectMap = new SelectBox<>(skin);

        selectMap.setItems(getMaps());
        selectMap.setSelected("assets/maps/Risky_Exchange.tmx");
        selectMap.setWidth(BUTTON_WIDTH*.87f);
        selectMap.setPosition((100), START_BUTTON_Y - selectMap.getHeight()*2);

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
        renderSettings(v);

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);

        int x = (int) (camera.viewportWidth /2 - BUTTON_WIDTH/2);
        if (Gdx.input.getX() < x + BUTTON_WIDTH && Gdx.input.getX() > x && camera.viewportHeight - Gdx.input.getY() < EXIT_BUTTON_Y + BUTTON_HEIGHT && camera.viewportHeight - Gdx.input.getY() > EXIT_BUTTON_Y) {
            batch.draw(exitButtonActive,x,EXIT_BUTTON_Y,BUTTON_WIDTH,BUTTON_HEIGHT);
            if (Gdx.input.isTouched()){
                Gdx.app.exit();
            }
        }
        else batch.draw(exitButtonInactive,x,EXIT_BUTTON_Y,BUTTON_WIDTH,BUTTON_HEIGHT);

        x = (int) (camera.viewportWidth / 2 - BUTTON_WIDTH /2);
        if (Gdx.input.getX() < x + BUTTON_WIDTH && Gdx.input.getX() > x && camera.viewportHeight - Gdx.input.getY() < START_BUTTON_Y + BUTTON_HEIGHT && camera.viewportHeight - Gdx.input.getY() > START_BUTTON_Y) {
            batch.draw(startButtonActive,x,START_BUTTON_Y,BUTTON_WIDTH,BUTTON_HEIGHT);
            if (Gdx.input.isTouched()){
                this.dispose();
                game.setupGame("assets/maps/"+getBoard()+".tmx");
                game.setScreen(new GameScreen(game));
            }
        }
        else { batch.draw(startButtonInactive,x, START_BUTTON_Y,BUTTON_WIDTH,BUTTON_HEIGHT);}

        batch.end();
        stage.act(v);
        stage.draw();
    }

    public String getBoard(){
        select = selectMap.getSelected();
        return select;
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
}
