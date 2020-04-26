package inf112.skeleton.app.screens.menuscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.gamescreen.GameScreen;

import java.io.File;
import java.util.Objects;

public class MenuScreenActors {

    private final Stage stage;
    private final RallyGame game;
    public Skin skin;

    public float screenWidth;
    public float screenHeight;

    public float BUTTON_WIDTH;
    public float BUTTON_HEIGHT;
    public float TEXT_INPUT_WIDTH;
    public float TOP_BUTTON_Y;
    public float BOTTOM_BUTTON_Y;
    public float TEXT_INPUT_Y;
    public float CENTERED_BUTTON_X;
    public float LEFT_BUTTON_X;
    public float RIGHT_BUTTON_X;

    private SelectBox<String> selectMap;
    private ImageButton startButton;
    private ImageButton exitButton;
    private ImageButton createGameButton;
    private ImageButton joinGameButton;
    private TextField IPInput;
    private TextField portInput;
    private TextField numOfPlayers;

    public MenuScreenActors(RallyGame game, Stage stage) {
        this.game = game;
        this.stage = stage;

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        BUTTON_WIDTH = (float) (screenWidth * 0.25);
        BUTTON_HEIGHT = (float) (screenHeight * 0.25);

        TOP_BUTTON_Y = (float) (screenHeight * 0.5);
        BOTTOM_BUTTON_Y = (float) (screenHeight * 0.5 - BUTTON_HEIGHT);
        TEXT_INPUT_Y = (float) (screenHeight * 0.5 + BUTTON_HEIGHT);

        CENTERED_BUTTON_X = (float) (screenWidth * 0.5 - BUTTON_WIDTH * 0.5);
        LEFT_BUTTON_X = (float) (screenWidth * 0.5 - BUTTON_WIDTH);
        RIGHT_BUTTON_X = (float) (screenWidth * 0.5);

        skin = new Skin(Gdx.files.internal("assets/skins/uiskin.json"));
    }

    public void initializeStartButton() {
        ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
        startButtonStyle.up = game.buttonSkins.getSkins().getDrawable("Start");
        startButtonStyle.over = game.buttonSkins.getSkins().getDrawable("Start over");

        startButton = new ImageButton(startButtonStyle);
        startButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
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
        startButton.setVisible(false);
        stage.addActor(startButton);
    }

    public void initializeExitButton() {
        ImageButton.ImageButtonStyle exitButtonStyle = new ImageButton.ImageButtonStyle();
        exitButtonStyle.up = game.buttonSkins.getSkins().getDrawable("Exit");
        exitButtonStyle.over = game.buttonSkins.getSkins().getDrawable("Exit over");

        exitButton = new ImageButton(exitButtonStyle);
        exitButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.setPosition(CENTERED_BUTTON_X, BOTTOM_BUTTON_Y);
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

    public void initializeSelectMap() {
        selectMap = new SelectBox<>(skin);
        selectMap.setItems(getMaps());
        selectMap.setSelected("Risky Exchange");
        selectMap.setWidth(BUTTON_WIDTH * .87f);
        selectMap.setPosition(screenWidth / 2f - selectMap.getWidth() / 2f, TEXT_INPUT_Y);
        selectMap.setVisible(false);
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
        Image background = new Image(new Texture("assets/images/GUI_Edited.jpg"));
        background.setSize(screenWidth, screenHeight);
        stage.addActor(background);
    }

    public void initializeCreateGame() {
        // TODO: Bytt til "Create game" og "Create game over"
        ImageButton.ImageButtonStyle createGameButtonStyle = new ImageButton.ImageButtonStyle();
        createGameButtonStyle.up = game.buttonSkins.getSkins().getDrawable("Start");
        createGameButtonStyle.over = game.buttonSkins.getSkins().getDrawable("Start over");

        createGameButton = new ImageButton(createGameButtonStyle);
        createGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        createGameButton.setPosition(LEFT_BUTTON_X, TOP_BUTTON_Y);
        createGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Create game
                System.out.println("Created game for " + numOfPlayers.getText() + " players with IP " + IPInput.getText() + " on port " + portInput.getText());
                changeButtonVisibility();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(createGameButton);
    }

    public void initializeJoinGame() {
        ImageButton.ImageButtonStyle joinGameButtonStyle = new ImageButton.ImageButtonStyle();
        // TODO: Bytt til "Join game" og "Join game over"
        joinGameButtonStyle.up = game.buttonSkins.getSkins().getDrawable("Exit");
        joinGameButtonStyle.over = game.buttonSkins.getSkins().getDrawable("Exit over");

        joinGameButton = new ImageButton(joinGameButtonStyle);
        joinGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        joinGameButton.setPosition(RIGHT_BUTTON_X, TOP_BUTTON_Y);
        joinGameButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // Join game
                System.out.println("Joined game for " + numOfPlayers.getText() + " players with IP " + IPInput.getText() + " on port " + portInput.getText());
                changeButtonVisibility();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(joinGameButton);
    }

    public void initializeIPInput() {
        IPInput = new TextField("", skin);
        IPInput.setMessageText("IP address");
        IPInput.setWidth(BUTTON_WIDTH * .87f);
        IPInput.setPosition(screenWidth / 2f - BUTTON_WIDTH / 2f - IPInput.getWidth(), TEXT_INPUT_Y);
        stage.addActor(IPInput);
    }

    public void initializePortInput() {
        portInput = new TextField("", skin);
        portInput.setMessageText("Port number");
        portInput.setWidth(BUTTON_WIDTH * .87f);
        portInput.setPosition(screenWidth / 2f - portInput.getWidth() / 2f, TEXT_INPUT_Y);
        stage.addActor(portInput);
    }

    public void initializeNumOfPlayersIput() {
        numOfPlayers = new TextField("", skin);
        numOfPlayers.setMessageText("Number of players");
        numOfPlayers.setWidth(BUTTON_WIDTH * .87f);
        numOfPlayers.setPosition(screenWidth / 2f + BUTTON_WIDTH / 2f, TEXT_INPUT_Y);
        stage.addActor(numOfPlayers);
    }

    public void changeButtonVisibility() {
        createGameButton.setVisible(false);
        joinGameButton.setVisible(false);
        portInput.setVisible(false);
        IPInput.setVisible(false);
        numOfPlayers.setVisible(false);
        startButton.setVisible(true);
        exitButton.setVisible(true);
        selectMap.setVisible(true);
    }
}
