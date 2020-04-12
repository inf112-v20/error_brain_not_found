package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.screens.GameScreen;

import java.io.File;
import java.util.Objects;

public class MenuScreenActors {

    public MenuButtonSkin buttonSkin;
    public Skin skin;

    public float screenWidth;
    public float screenHeight;

    public float BUTTON_WIDTH;
    public float BUTTON_HEIGHT;
    public float BUTTON_X;
    public float START_BUTTON_Y;
    public float EXIT_BUTTON_Y;

    public MenuScreenActors() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        BUTTON_WIDTH = (float) (screenWidth * 0.25);
        BUTTON_HEIGHT = (float) (screenHeight * 0.25);
        START_BUTTON_Y = (float) (screenHeight * 0.5);
        EXIT_BUTTON_Y = (float) (screenHeight * 0.5 - BUTTON_HEIGHT);
        BUTTON_X = (float) (screenWidth * 0.5 - BUTTON_WIDTH * 0.5);

        buttonSkin = new MenuButtonSkin();
        skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));
    }

    public ImageButton initializeStartButton(RallyGame game, SelectBox<String> selectMap) {
        ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
        startButtonStyle.up = buttonSkin.menuButtonSkin.getDrawable("Start button");
        startButtonStyle.over = buttonSkin.menuButtonSkin.getDrawable("Start button over");

        ImageButton startButton = new ImageButton(startButtonStyle);
        startButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.setPosition(BUTTON_X, START_BUTTON_Y);
        startButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setupGame("assets/maps/" + selectMap.getSelected() + ".tmx");
                game.getScreen().dispose();
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        return startButton;
    }

    public ImageButton initializeExitButton() {
        ImageButton.ImageButtonStyle exitButtonStyle = new ImageButton.ImageButtonStyle();
        exitButtonStyle.up = buttonSkin.menuButtonSkin.getDrawable("Exit button");
        exitButtonStyle.over = buttonSkin.menuButtonSkin.getDrawable("Exit button over");

        ImageButton exitButton = new ImageButton(exitButtonStyle);
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
        return exitButton;
    }

    public SelectBox<String> initializeSelectMap() {
        SelectBox<String> selectMap = new SelectBox<>(skin);
        selectMap.setItems(getMaps());
        selectMap.setSelected("assets/maps/Risky Exchange.tmx");
        selectMap.setWidth(BUTTON_WIDTH * .87f);
        selectMap.setPosition(BUTTON_X - selectMap.getWidth(), START_BUTTON_Y - BUTTON_HEIGHT / 2);
        return selectMap;
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

    public Image initializeBackground() {
        Image background = new Image(new Texture("assets/images/GUI_Edited.jpg"));
        background.setSize(screenWidth, screenHeight);
        return background;
    }
}
