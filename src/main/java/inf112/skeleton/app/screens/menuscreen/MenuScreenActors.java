package inf112.skeleton.app.screens.menuscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.gamescreen.GameScreen;

import java.io.File;
import java.net.InetAddress;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class MenuScreenActors {

    private final Stage stage;
    private final RallyGame game;

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
    private ImageButton createGameButton;
    private ImageButton joinGameButton;
    private TextField IPInput;
    private TextField portInput;
    private TextField numOfPlayers;
    private Label waitForHost;
    private Label IPLabel;
    private Label invalidInputLabel;
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

        try {
            waitForServerToSendStartValues.tryAcquire();
            waitForServerToSendMapPath.tryAcquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initializeStartButton() {
        ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
        startButtonStyle.up = game.actorImages.getSkin().getDrawable("Start");
        startButtonStyle.over = game.actorImages.getSkin().getDrawable("Start over");

        startButton = new ImageButton(startButtonStyle);
        startButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        startButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (isValidInputPortAndNumberOfPlayers(portInput.getText(), numOfPlayers.getText())) {
                    game.setupGame("assets/maps/" + selectMap.getSelected() + ".tmx");
                    game.setScreen(new GameScreen(game));
                }
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
        exitButtonStyle.up = game.actorImages.getSkin().getDrawable("Exit");
        exitButtonStyle.over = game.actorImages.getSkin().getDrawable("Exit over");

        ImageButton exitButton = new ImageButton(exitButtonStyle);
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
        selectMap = new SelectBox<>(game.getDefaultSkin());
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
        Image background = new Image(game.getActorImages().getDrawable("Menu screen background"));
        background.setSize(screenWidth, screenHeight);
        stage.addActor(background);
    }

    public void initializeCreateGame() {
        ImageButton.ImageButtonStyle createGameButtonStyle = new ImageButton.ImageButtonStyle();
        createGameButtonStyle.up = game.actorImages.getSkin().getDrawable("Create game");
        createGameButtonStyle.over = game.actorImages.getSkin().getDrawable("Create game over");

        createGameButton = new ImageButton(createGameButtonStyle);
        createGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        createGameButton.setPosition(LEFT_BUTTON_X, TOP_BUTTON_Y);
        createGameButton.addListener(new InputListener() {

            /**
             * Start a host if input is valid.
             */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (joinGameButton.isVisible()) {
                    toggleVisibilityCreateFirstClick();
                } else {
                    if (isValidInputPortAndNumberOfPlayers(portInput.getText(), numOfPlayers.getText())) {
                        toggleVisibilityCreateSecondClick();
                        game.setIsServerToTrue();
                        game.setUpHost(Integer.parseInt(portInput.getText()), Integer.parseInt(numOfPlayers.getText()));
                    }  else {
                        updateInvalidInputLabel();
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(createGameButton);
    }

    /**
     *
     * @param port
     * @param numberOfPlayers
     * @return true og port and numberOfPlayers are not empty and in valid range
     */
    public boolean isValidInputPortAndNumberOfPlayers(String port, String numberOfPlayers) {
        return (!"".equals(port) && !"".equals(numberOfPlayers) && isNumber(port) && isNumber(numberOfPlayers) &&
                portInValidRange(Integer.parseInt(port)) && numberOfPlayersInValidRange(Integer.parseInt(numberOfPlayers)));
    }

    /**
     *
     * @param numberOfPLayers
     * @return True if players between 2 and 8.
     */
    private boolean numberOfPlayersInValidRange(int numberOfPLayers) {
        return numberOfPLayers >= 2 && numberOfPLayers <= 8;
    }

    public void initializeJoinGame() {
        ImageButton.ImageButtonStyle joinGameButtonStyle = new ImageButton.ImageButtonStyle();
        joinGameButtonStyle.up = game.actorImages.getSkin().getDrawable("Join game");
        joinGameButtonStyle.over = game.actorImages.getSkin().getDrawable("Join game over");

        joinGameButton = new ImageButton(joinGameButtonStyle);
        joinGameButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        joinGameButton.setPosition(RIGHT_BUTTON_X, TOP_BUTTON_Y);
        joinGameButton.addListener(new InputListener() {

            /**
             * Create a client if input is valid.
             */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (createGameButton.isVisible()) {
                    toggleVisibilityJoinFirstClick();
                } else {
                    if (isValidInputPortAndIP(portInput.getText(), IPInput.getText())) {
                        if (setUpClient()) {
                            toggleVisibilityJoinSecondClick();
                            waitForGameSetUpAndStartGame();
                        } else {
                            invalidInputLabel.setText("Could not connect to " + IPInput.getText() + " on port " + portInput.getText());
                        }
                    } else {
                        updateInvalidInputLabel();
                    }
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(joinGameButton);
    }

    /**
     *
     * @param port
     * @param ip
     * @return True if port and ip is not empty, port is a number and within range.
     */
    public boolean isValidInputPortAndIP(String port, String ip) {
        return !"".equals(ip) && !"".equals(port) && isNumber(port) && portInValidRange(Integer.parseInt(port));
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
        IPInput = new TextField("", game.getDefaultSkin());
        IPInput.setMessageText("IP address");
        IPInput.setWidth(BUTTON_WIDTH * .87f);
        IPInput.setPosition(screenWidth / 2f + BUTTON_WIDTH * .13f, TEXT_INPUT_Y);
        IPInput.setVisible(false);
        IPInput.setTextFieldFilter(new IPInputFilter());
        stage.addActor(IPInput);
    }

    public void initializePortInput() {
        portInput = new TextField("", game.getDefaultSkin());
        portInput.setMessageText("Port number");
        portInput.setWidth(BUTTON_WIDTH * .87f);
        portInput.setPosition(screenWidth / 2f - BUTTON_WIDTH, TEXT_INPUT_Y);
        portInput.setVisible(false);
        portInput.setTextFieldFilter(new PortInputFilter());
        stage.addActor(portInput);
    }

    public void initializeNumOfPlayersInput() {
        numOfPlayers = new TextField("", game.getDefaultSkin());
        numOfPlayers.setMessageText("Number of players");
        numOfPlayers.setWidth(BUTTON_WIDTH * .87f);
        numOfPlayers.setPosition(screenWidth / 2f + BUTTON_WIDTH * .13f, TEXT_INPUT_Y);
        numOfPlayers.setVisible(false);
        numOfPlayers.setTextFieldFilter(new NumOfPlayersInputFilter());
        stage.addActor(numOfPlayers);
    }

    public void initializeWaitForHostLabel() {
        waitForHost = new Label("Wait for host to start game", game.getDefaultSkin());
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
        invalidInputLabel.setVisible(false);
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
        invalidInputLabel.setVisible(false);
    }

    public void initializeIPLabel() {
        String IP = "Unknown";
        try {
            IP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignored) {
        }
        IPLabel = new Label("Your IP: " + IP, game.getDefaultSkin());
        IPLabel.setPosition(CENTERED_BUTTON_X, TEXT_INPUT_Y + selectMap.getHeight());
        IPLabel.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        IPLabel.setAlignment(Align.center);
        IPLabel.setFontScale(1.5f);
        IPLabel.setVisible(false);
        stage.addActor(IPLabel);
    }

    public void initializeInvalidInputLabel() {
        invalidInputLabel = new Label("", game.getDefaultSkin());
        invalidInputLabel.setPosition(CENTERED_BUTTON_X, TEXT_INPUT_Y + selectMap.getHeight());
        invalidInputLabel.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        invalidInputLabel.setAlignment(Align.center);
        invalidInputLabel.setFontScale(1.5f);
        stage.addActor(invalidInputLabel);
    }

    public void updateInvalidInputLabel() {
        if (portInput.getText().equals("") || !portInValidRange(Integer.parseInt(portInput.getText()))) {
            invalidInputLabel.setText("Invalid port number, should be between 1024 and 49151");
            portInput.setText("");
        } else if (numOfPlayers.getText().equals("") || !numberOfPlayersInValidRange(Integer.parseInt(numOfPlayers.getText()))) {
            invalidInputLabel.setText("Number of players should be between 2 and 8");
            numOfPlayers.setText("");
        } else {
            invalidInputLabel.setText("Invalid IP number");
            IPInput.setText("");
        }
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
