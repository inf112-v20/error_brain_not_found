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

    private float BUTTON_WIDTH;
    private float BUTTON_HEIGHT;
    private float BUTTON_X;
    private float START_BUTTON_Y;
    private float EXIT_BUTTON_Y;

    public SelectBox<String> selectMap;

    private Texture background;
    private Texture startButtonInactive;
    private Texture startButtonActive;
    private Texture exitButtonInactive;
    private Texture exitButtonActive;

    private Stage stage;
    public String select;

    public MenuScreen(RallyGame game) {
        super(game);
        stage = new Stage();
        Skin skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));

        updateButtons();

        background = new Texture("assets/images/GUI_Edited.jpg");
        startButtonActive = new Texture("assets/images/Start_Button.png");
        startButtonInactive = new Texture("assets/images/Start_Button_Active.png");
        exitButtonInactive = new Texture("assets/images/Exit_Button_Active.png");
        exitButtonActive = new Texture("assets/images/Exit_Button_Inactive.png");

        selectMap = new SelectBox<>(skin);
        selectMap.setItems(getMaps());
        selectMap.setSelected("assets/maps/Risky_Exchange.tmx");
        selectMap.setWidth(BUTTON_WIDTH * .87f);
        selectMap.setPosition(BUTTON_X - selectMap.getWidth(), START_BUTTON_Y - BUTTON_HEIGHT / 2);

        stage.addActor(selectMap);
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

    public void updateButtons() {
        BUTTON_WIDTH = (float) (Gdx.graphics.getWidth() / 4.0);
        BUTTON_HEIGHT = (float) (Gdx.graphics.getHeight() / 4.0);
        START_BUTTON_Y = (float) (Gdx.graphics.getHeight() / 2.0);
        EXIT_BUTTON_Y = (float) (Gdx.graphics.getHeight() / 2.0 - BUTTON_HEIGHT);
        BUTTON_X = (float) (Gdx.graphics.getWidth() / 2.0 - BUTTON_WIDTH / 2);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        render(0);
    }

    @Override
    public void render(float v) {
        renderSettings(v);
        updateButtons();

        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);

        if (mouseOnExit()) {
            batch.draw(exitButtonActive, BUTTON_X, EXIT_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                Gdx.app.exit();
            }
        } else {
            batch.draw(exitButtonInactive, BUTTON_X, EXIT_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        }

        if (mouseOnStart()) {
            batch.draw(startButtonActive, BUTTON_X, START_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
            if (Gdx.input.isTouched()) {
                game.setupGame("assets/maps/" + getBoard() + ".tmx");
                this.dispose();
                game.setScreen(new GameScreen(game));
            }
        } else {
            batch.draw(startButtonInactive, BUTTON_X, START_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        }

        batch.end();
        stage.act(v);
        stage.draw();
    }

    public String getBoard() {
        select = selectMap.getSelected();
        return select;
    }

    private boolean mouseOnButton(float buttonY) {
        return Gdx.input.getX() < BUTTON_X + BUTTON_WIDTH &&
                Gdx.input.getX() > BUTTON_X &&
                camera.viewportHeight - Gdx.input.getY() < buttonY + BUTTON_HEIGHT &&
                camera.viewportHeight - Gdx.input.getY() > buttonY;
    }

    private boolean mouseOnExit() {
        return mouseOnButton(EXIT_BUTTON_Y);
    }

    private boolean mouseOnStart() {
        return mouseOnButton(START_BUTTON_Y);
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
        super.dispose();
    }
}
