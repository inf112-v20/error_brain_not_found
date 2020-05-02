package inf112.skeleton.app.screens.menuscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.gamescreen.GameScreen;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class MenuScreenActors {

    private final Stage stage;
    private final RallyGame game;
    public Skin skin;

    public float screenWidth;
    public float screenHeight;

    public float BUTTON_WIDTH;
    public float BUTTON_HEIGHT;
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
    private Label waitForHost;
    private Label IPLabel;
    public Semaphore waitForServerToSendStartValues = new Semaphore(1);
    public Semaphore waitForServerToSendMapPath = new Semaphore(1);
    public Thread waitForGameSetupThread;

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

        try {
            waitForServerToSendStartValues.tryAcquire();
            waitForServerToSendMapPath.tryAcquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ImageButton.ImageButtonStyle createGameButtonStyle = new ImageButton.ImageButtonStyle();
        createGameButtonStyle.up = game.buttonSkins.getSkins().getDrawable("Create game");
        createGameButtonStyle.over = game.buttonSkins.getSkins().getDrawable("Create game over");

        createGameButton = new ImageButton(createGameButtonStyle);
        createGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        createGameButton.setPosition(LEFT_BUTTON_X, TOP_BUTTON_Y);
        createGameButton.addListener(new InputListener() {

            /**
             * If {@link #touchDown(InputEvent, float, float, int, int)} is true and player have chosen
             * to create game, a host is started.
             */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setIsServerToTrue();
                if (joinGameButton.isVisible()) {
                    toggleVisibilityCreateFirstClick();
                } else {
                    toggleVisibilityCreateSecondClick();
                    game.setUpHost(Integer.parseInt(portInput.getText()), Integer.parseInt(numOfPlayers.getText()));
                }
            }

            /**
             *
             * @return False if input is required and no input is given. True otherwise.
             */
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (joinGameButton.isVisible()) {
                    return true;
                }
                String port = portInput.getText();
                String numberOfPlayer = numOfPlayers.getText();
                if (port.equals("") && numberOfPlayer.equals("")) {
                    portInput.setMessageText("Need to give a portnumber. :) ");
                    numOfPlayers.setMessageText("Need to give number of players.");
                    return false;
                }
                if (port.equals("")) {
                    portInput.setMessageText("Need to give a portnumber. :) ");
                    return false;
                } else if (numberOfPlayer.equals("")) {
                    numOfPlayers.setMessageText("Need to give number of players.");
                    return false;
                }
                if (!isNumber(port)) {
                    portInput.setMessageText("Port can only have digits.");
                }
                if (!portInValidRange(Integer.parseInt(port))) {
                    portInput.setMessageText("Give number between 1024 and 49151");
                }
                return true;
            }
        });
        stage.addActor(createGameButton);
    }

    public void initializeJoinGame() {
        ImageButton.ImageButtonStyle joinGameButtonStyle = new ImageButton.ImageButtonStyle();
        joinGameButtonStyle.up = game.buttonSkins.getSkins().getDrawable("Join game");
        joinGameButtonStyle.over = game.buttonSkins.getSkins().getDrawable("Join game over");

        joinGameButton = new ImageButton(joinGameButtonStyle);
        joinGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        joinGameButton.setPosition(RIGHT_BUTTON_X, TOP_BUTTON_Y);
        joinGameButton.addListener(new InputListener() {

            /**
             * If {@link #touchDown(InputEvent, float, float, int, int)} is true, player have chosen
             * to join a game that is being hosted, then a client will connect to the host.
             */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (createGameButton.isVisible()) {
                    toggleVisibilityJoinFirstClick();
                } else {
                    if (setUpClient()) {
                        toggleVisibilityJoinSecondClick();
                        waitForGameSetUpAndStartGame();
                    }
                }
            }

            /**
             *
             * @return False if input is required and no input is given. True otherwise.
             */
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (createGameButton.isVisible()) {
                    return true;
                }
                String ip = IPInput.getText();
                String port = portInput.getText();
                if (ip.equals("") && port.equals("")) {
                    portInput.setMessageText("Need to give portnumber.");
                    IPInput.setMessageText("Need to give IP address to host.");
                    return false;
                }
                if (ip.equals("")) {
                    IPInput.setMessageText("Need to give IP address to host. :)");
                    return false;
                }
                if (port.equals("")) {
                    portInput.setMessageText("Need to give portnumber..");
                    return false;
                }
                if (!isNumber(port)) {
                    portInput.setMessageText("Port can only have digits.");
                    return false;
                }
                if (!portInValidRange(Integer.parseInt(port))) {
                    portInput.setMessageText("Give number between 1024 and 49151");
                    return false;
                }
                return true;
            }
        });
        stage.addActor(joinGameButton);
    }

    /**
     *
     * @param string
     * @return True if string is an int
     */
    public boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param port to check
     * @return True if portnumber is registered but not well-known
     */
    public boolean portInValidRange(int port) {
        return port >= 1024 &&port <= 49151;
    }

    public void initializeIPInput() {
        IPInput = new TextField("", skin);
        IPInput.setMessageText("IP address");
        IPInput.setWidth(BUTTON_WIDTH * .87f);
        IPInput.setPosition(screenWidth / 2f + BUTTON_WIDTH * .13f, TEXT_INPUT_Y);
        IPInput.setVisible(false);
        stage.addActor(IPInput);
    }

    public void initializePortInput() {
        portInput = new TextField("", skin);
        portInput.setMessageText("Port number");
        portInput.setWidth(BUTTON_WIDTH * .87f);
        portInput.setPosition(screenWidth / 2f - BUTTON_WIDTH, TEXT_INPUT_Y);
        portInput.setVisible(false);
        stage.addActor(portInput);
    }

    public void initializeNumOfPlayersInput() {
        numOfPlayers = new TextField("", skin);
        numOfPlayers.setMessageText("Number of players");
        numOfPlayers.setWidth(BUTTON_WIDTH * .87f);
        numOfPlayers.setPosition(screenWidth / 2f + BUTTON_WIDTH * .13f, TEXT_INPUT_Y);
        numOfPlayers.setVisible(false);
        stage.addActor(numOfPlayers);
    }

    public void initializeWaitForHostLabel() {
        waitForHost = new Label("Wait for host to start game", skin);
        waitForHost.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        waitForHost.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        waitForHost.setAlignment(Align.center);
        waitForHost.setFontScale(2);
        waitForHost.setVisible(false);
        stage.addActor(waitForHost);
    }

    public void toggleVisibilityCreateFirstClick() {
        joinGameButton.setVisible(false);
        createGameButton.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        portInput.setVisible(true);
        numOfPlayers.setVisible(true);
    }

    public void toggleVisibilityCreateSecondClick() {
        createGameButton.setVisible(false);
        portInput.setVisible(false);
        numOfPlayers.setVisible(false);
        selectMap.setVisible(true);
        startButton.setVisible(true);
        IPLabel.setVisible(true);
    }

    public void toggleVisibilityJoinFirstClick() {
        createGameButton.setVisible(false);
        joinGameButton.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        portInput.setVisible(true);
        IPInput.setVisible(true);
    }

    public void toggleVisibilityJoinSecondClick() {
        joinGameButton.setVisible(false);
        portInput.setVisible(false);
        IPInput.setVisible(false);
        waitForHost.setVisible(true);
    }

    public void initializeIPLabel() {
        String IP = "Unknown";
        try {
            IP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        IPLabel = new Label("Your IP: " + IP, skin);
        IPLabel.setPosition(CENTERED_BUTTON_X, TEXT_INPUT_Y + selectMap.getHeight());
        IPLabel.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        IPLabel.setFontScale(1.5f);
        IPLabel.setVisible(false);
        stage.addActor(IPLabel);
    }

    /**
     *
     * Setting up new client.
     *
     * @return true if a client has been made
     */
    public boolean setUpClient() {
        game.setUpClient(IPInput.getText(), Integer.parseInt(portInput.getText()));
        if (game.getClient() == null) {
            return false;
        }
        System.out.println("Made client");
        return true;
    }

    /**
     * Wait for server to sent initial values so you can create game.
     */
    public void waitForGameSetup() {
        try {
            waitForServerToSendStartValues.acquire();
            waitForServerToSendMapPath.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Game path when setup: " + game.getMapPath());
    }

    /**
     * Create own thread to wait for {@link #waitForGameSetup()} to get startValues
     * from server, so that "waiting for server to start" picture can render.
     */
    public void waitForGameSetUpAndStartGame() {
        waitForGameSetupThread = new Thread(() -> {
            waitForGameSetup();
            Gdx.app.postRunnable(() -> {
                game.setupGame(game.getMapPath());
                game.setScreen(new GameScreen(game));
            });
        });
        waitForGameSetupThread.start();
    }
}
