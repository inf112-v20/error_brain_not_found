package inf112.skeleton.app.screens.menuscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.gamescreen.GameScreen;
import inf112.skeleton.app.screens.standardscreen.SettingsScreen;

import java.io.File;
import java.util.Objects;

public class MenuScreenActors {

    private final Stage stage;
    private final RallyGame game;

    public final float screenWidth;
    public final float screenHeight;

    public final float BUTTON_WIDTH;
    public final float BUTTON_HEIGHT;
    public final float BUTTON_X;
    public final float BUTTON_Y;
    public final float START_BUTTON_Y;
    public final float EXIT_BUTTON_Y;
    public final float SETTINGS_BUTTON_Y;

    public SelectBox<String> selectMap;

    public MenuScreenActors(RallyGame game, Stage stage) {
        this.game = game;
        this.stage = stage;

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        BUTTON_WIDTH = (float) (screenWidth * 0.25);
        BUTTON_HEIGHT = (float) (screenHeight * 0.25);
        START_BUTTON_Y = (float) (screenHeight * 0.5);
        EXIT_BUTTON_Y = (float) (screenHeight * 0.5 - BUTTON_HEIGHT);
        SETTINGS_BUTTON_Y = (float) (screenHeight * 0.5 - (BUTTON_HEIGHT *2));
        BUTTON_X = (BUTTON_WIDTH * 1);
        BUTTON_Y = (BUTTON_WIDTH * 2);

    }


    public void initializeStartButton() {
        ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
        startButtonStyle.up = game.actorImages.getSkin().getDrawable("Start");
        startButtonStyle.over = game.actorImages.getSkin().getDrawable("Start over");

        ImageButton startButton = new ImageButton(startButtonStyle);
        startButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.setPosition(BUTTON_X, START_BUTTON_Y);
        startButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setupGame("assets/maps/" + selectMap.getSelected() + ".tmx");
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(startButton);
    }

    public void initializeExitButton() {
        ImageButton.ImageButtonStyle exitButtonStyle = new ImageButton.ImageButtonStyle();
        exitButtonStyle.up = game.actorImages.getSkin().getDrawable("Exit");
        exitButtonStyle.over = game.actorImages.getSkin().getDrawable("Exit over");

        ImageButton exitButton = new ImageButton(exitButtonStyle);
        exitButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setPosition(BUTTON_Y, EXIT_BUTTON_Y);
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
        stage.addActor(exitButton);
    }
    public void initializeSettingsButton() {
        ImageButton.ImageButtonStyle settingsButtonStyle = new ImageButton.ImageButtonStyle();
        settingsButtonStyle.up = game.actorImages.getSkin().getDrawable("Settings");
        settingsButtonStyle.over = game.actorImages.getSkin().getDrawable("Settings over");

        ImageButton settingsButton = new ImageButton(settingsButtonStyle);
        settingsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton.setPosition(BUTTON_X, EXIT_BUTTON_Y);
        settingsButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new SettingsScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(settingsButton);
    }

    public void initializeSelectMap() {
        selectMap = new SelectBox<>(game.getDefaultSkin());
        selectMap.setItems(getMaps());
        selectMap.setSelected("assets/maps/Risky Exchange.tmx");
        selectMap.setWidth(BUTTON_WIDTH * .87f);
        selectMap.setPosition(BUTTON_Y, (float) (START_BUTTON_Y + BUTTON_HEIGHT / 1.5));
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

    public void initializeBackground() {
        Image background = new Image(game.getActorImages().getDrawable("Menu screen background"));
        background.setSize(screenWidth, screenHeight);
        stage.addActor(background);
    }
}
