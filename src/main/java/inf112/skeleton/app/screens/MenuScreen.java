package inf112.skeleton.app.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.RallyGame;

import java.io.File;
import java.util.Objects;


public class MenuScreen extends StandardScreen {

    private float BUTTON_WIDTH;
    private float BUTTON_HEIGHT;
    private float BUTTON_X;
    private float START_BUTTON_Y;
    private float EXIT_BUTTON_Y;

    public final SelectBox<String> selectMap;
    private final Button startButton;
    private final Button exitButton;

    private final Texture background;

    private final Stage stage;
    public String select;

    public MenuScreen(RallyGame game) {
        super(game);

        BUTTON_WIDTH = (float) (viewport.getWorldWidth() / 4.0);
        BUTTON_HEIGHT = (float) (viewport.getWorldHeight() / 4.0);
        START_BUTTON_Y = (float) (viewport.getWorldHeight() / 2.0);
        EXIT_BUTTON_Y = (float) (viewport.getWorldHeight() / 2.0 - BUTTON_HEIGHT);
        BUTTON_X = (float) (viewport.getWorldWidth() / 2.0 - BUTTON_WIDTH / 2);

        stage = new Stage(viewport, batch);

        Skin skin = new Skin(Gdx.files.internal("assets/skin/glassy-ui.json"));

        background = new Texture("assets/images/GUI_Edited.jpg");

        selectMap = new SelectBox<>(skin);
        selectMap.setItems(getMaps());
        selectMap.setSelected("assets/maps/Risky Exchange.tmx");
        selectMap.setWidth(BUTTON_WIDTH * .87f);
        selectMap.setPosition(BUTTON_X - selectMap.getWidth(), START_BUTTON_Y - BUTTON_HEIGHT / 2);

        startButton = new TextButton("Start", skin);
        startButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.setPosition(BUTTON_X, START_BUTTON_Y);
        startButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setupGame("assets/maps/" + getBoard() + ".tmx");
                game.getScreen().dispose();
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        exitButton = new TextButton("Exit", skin);
        exitButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setPosition(BUTTON_X, EXIT_BUTTON_Y);
        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        stage.addActor(selectMap);
        stage.addActor(startButton);
        stage.addActor(exitButton);

        Gdx.input.setInputProcessor(stage);
    }

    private Array<String> getMaps() {
        Array<String> mapArray = new Array<>();
        File maps = new File("assets/maps");
        for (String fileType : Objects.requireNonNull(maps.list())) {
            if (fileType.endsWith(".tmx")) {
                mapArray.add(fileType.substring(0, fileType.length() - 4));
            }
        }
        return mapArray;
    }

    @Override
    public void render(float v) {
        renderSettings(v);

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.end();

        stage.act(v);
        stage.draw();
    }

    public String getBoard() {
        select = selectMap.getSelected();
        return select;
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        super.dispose();
    }
}
