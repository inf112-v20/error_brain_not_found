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
    private TextField numOfPlayers;
    private Label waitForHost;
    private Label IPLabel;
    private Label invalidInputLabel;
    private Semaphore waitForServerToSendStartValues = new Semaphore(1);
    private Semaphore waitForServerToSendMapPath = new Semaphore(1);
    private Thread waitForGameSetupThread;
    private Semaphore waitForAllClientsToConnect = new Semaphore(1);
    private Thread waitForAllClients;
    private Label waitForClients;
    private int clientsConnected;
    private Semaphore waitForServerToSendShowGameScreen = new Semaphore(1);

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
            waitForAllClientsToConnect.tryAcquire();
            waitForServerToSendShowGameScreen.tryAcquire();
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
                if (isValidNumberOfPlayers(numOfPlayers.getText())) {
                    game.getServer().setMapPath("assets/maps/" + selectMap.getSelected() + ".tmx");
                    game.getServer().setConnectingToClients(false);
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
                    //if (isValidNumberOfPlayers(numOfPlayers.getText())) {
                        game.setIsServerToTrue();
                        // Defaul port is 9000
                        game.setUpHost(9000);
                        //waitForClients.setVisible(true);
                        createGameButton.setVisible(false);
                        numOfPlayers.setVisible(false);
                        startButton.setVisible(true);
                        //waitForAllClientsToConnectBeforeStartingGame();
                   // }  else {
                        //updateInvalidInputLabel(numOfPlayers);
                   // }
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
     * @param numberOfPlayers
     * @return true og port and numberOfPlayers are not empty and in valid range
     */
    public boolean isValidNumberOfPlayers(String numberOfPlayers) {
        return (!"".equals(numberOfPlayers)&& isNumber(numberOfPlayers)&& numberOfPlayersInValidRange(Integer.parseInt(numberOfPlayers)));
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
                    if (!"".equals(IPInput.getText())) {
                        if (setUpClient()) {
                            toggleVisibilityJoinSecondClick();
                            waitForGameSetUpAndStartGame();
                        } else {
                            updateInvalidInputLabel(null);
                        }
                    } else {
                        updateInvalidInputLabel(IPInput);
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
        IPInput.setPosition(screenWidth / 2f - (BUTTON_WIDTH * .87f ) / 2f, TEXT_INPUT_Y);
        IPInput.setVisible(false);
        IPInput.setTextFieldFilter(new IPInputFilter());
        stage.addActor(IPInput);
    }

    public void initializeNumOfPlayersInput() {
        numOfPlayers = new TextField("", game.getDefaultSkin());
        numOfPlayers.setMessageText("Number of players");
        numOfPlayers.setWidth(BUTTON_WIDTH * .87f);
        numOfPlayers.setPosition(screenWidth / 2f - (BUTTON_WIDTH * .87f ) / 2f, TEXT_INPUT_Y);
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

    public void initializeWaitForClientsLabel() {
        waitForClients = new Label("Wait for clients to connect...", game.getDefaultSkin());
        waitForClients.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        waitForClients.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        waitForClients.setAlignment(Align.center);
        waitForClients.setFontScale(2);
        waitForClients.setVisible(false);
        stage.addActor(waitForClients);
    }

    public void toggleVisibilityCreateFirstClick() {
        joinGameButton.setVisible(false);
        createGameButton.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        numOfPlayers.setVisible(true);
    }

    public void toggleVisibilityCreateSecondClick() {
        createGameButton.setVisible(false);
        numOfPlayers.setVisible(false);
        selectMap.setVisible(true);
        startButton.setVisible(true);
        IPLabel.setVisible(true);
        invalidInputLabel.setVisible(false);
    }

    public void toggleVisibilityJoinFirstClick() {
        createGameButton.setVisible(false);
        joinGameButton.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        IPInput.setVisible(true);
    }

    public void toggleVisibilityJoinSecondClick() {
        joinGameButton.setVisible(false);
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

    public void updateInvalidInputLabel(TextField textField) {
        if (textField == numOfPlayers) {
            invalidInputLabel.setText("Number of players should be between 2 and 8");
            numOfPlayers.setText("");
        } else if (textField == IPInput) {
            invalidInputLabel.setText("Invalid IP number");
            IPInput.setText("");
        } else {
            invalidInputLabel.setText("Could not connect to " + IPInput.getText() + " on port 9000");
            IPInput.setText("");
        }
    }

    /**
     * Release {@link #waitForGameSetup()} so that game can start.
     */
    public void haveReceivedMapPath() {
        waitForServerToSendMapPath.release();
    }

    /**
     * Release {@link #waitForGameSetup()} so that we can receive the mapPath
     */
    public void haveReceivedStartValues() {
        waitForServerToSendStartValues.release();
    }

    /**
     *
     * Setting up new client on default port 9000
     *
     * @return true if a client has been made
     */
    public boolean setUpClient() {
        game.setUpClient(IPInput.getText(), 9000);
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
            waitForServerToSendShowGameScreen.acquire();
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

    /**
     * Create own thread to wait for {@link #waitForAllClientsToConnect()} so all clients have connected
     * before starting game screen.
     */
    public void waitForAllClientsToConnectBeforeStartingGame() {
        waitForAllClients = new Thread(() -> {
            waitForAllClientsToConnect();
            toggleVisibilityCreateSecondClick();
            waitForClients.setVisible(false);
        });
        waitForAllClients.start();
    }

    public void waitForAllClientsToConnect() {
        try {
            waitForAllClientsToConnect.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Release {@link #waitForAllClientsToConnect()} so that tha game can begin.
     */
    public void allClientsHaveConnected() {
        waitForAllClientsToConnect.release();
    }

    /**
     * Release Semaphore waitForServerToSendShowGameScreen {@link #waitForGameSetup()} can be released.
     */
    public void showGameScreen() {
        waitForServerToSendShowGameScreen.release();
        System.out.println("Released screen");
    }
}

