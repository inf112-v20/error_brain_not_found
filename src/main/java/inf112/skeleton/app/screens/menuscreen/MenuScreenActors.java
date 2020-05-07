package inf112.skeleton.app.screens.menuscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.gamescreen.GameScreen;
import inf112.skeleton.app.screens.standardscreen.SettingsScreen;

import java.io.File;
import java.net.InetAddress;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class MenuScreenActors {

    private final Stage stage;
    private final RallyGame game;

    public float screenWidth;
    public float screenHeight;

    public final float BUTTON_WIDTH;
    public final float BUTTON_HEIGHT;
    public final float BUTTON_X;
    public final float BUTTON_Y;
    public final float START_BUTTON_Y;
    public final float EXIT_BUTTON_Y;
    public final float SETTINGS_BUTTON_Y;

    public float TOP_BUTTON_Y;
    public float BOTTOM_BUTTON_Y;
    public float TEXT_INPUT_Y;
    public float CENTERED_BUTTON_X;
    public float LEFT_BUTTON_X;
    public float RIGHT_BUTTON_X;
    public float LABEL_Y;
    public float FONT_SCALE;

    private SelectBox<String> selectMap;
    private ImageButton startButton;
    private ImageButton createGameButton;
    private ImageButton joinGameButton;
    private TextField IPInput;
    private Label IPLabel;
    private Label errorLabel;
    private Label clientsConnectedLabel;
    private Semaphore waitForServerToSendStartValues = new Semaphore(1);
    private Semaphore waitForServerToSendMapPath = new Semaphore(1);

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


        TOP_BUTTON_Y = (float) (screenHeight * 0.5);
        BOTTOM_BUTTON_Y = (float) (screenHeight * 0.5 - BUTTON_HEIGHT);
        TEXT_INPUT_Y = (float) (screenHeight * 0.5 + BUTTON_HEIGHT);

        CENTERED_BUTTON_X = (float) (screenWidth * 0.5 - BUTTON_WIDTH * 0.5);
        LEFT_BUTTON_X = (float) (screenWidth * 0.5 - BUTTON_WIDTH);
        RIGHT_BUTTON_X = (float) (screenWidth * 0.5);

        LABEL_Y = screenHeight * (440f/540f);
        FONT_SCALE = screenWidth / 960f;

        try {
            waitForServerToSendStartValues.tryAcquire();
            waitForServerToSendMapPath.tryAcquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // BUTTONS
    public void initializeStartButton() {
        ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
        startButtonStyle.up = game.actorImages.getSkin().getDrawable("Start");
        startButtonStyle.over = game.actorImages.getSkin().getDrawable("Start over");

        startButton = new ImageButton(startButtonStyle);
        startButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        startButton.addListener(new InputListener() {
            /**
             *
             * Is activated only if {@link #touchDown(InputEvent, float, float, int, int)} is true
             */
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.getServer().setMapPath("assets/maps/" + selectMap.getSelected() + ".tmx");
                game.getServer().setConnectingToClients(false);
                game.setupGame();
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return game.getServer().getNumberOfConnectedClients() > 0;
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
    public void initializeSettingsButton() {
        ImageButton.ImageButtonStyle settingsButtonStyle = new ImageButton.ImageButtonStyle();
        settingsButtonStyle.up = game.actorImages.getSkin().getDrawable("Settings");
        settingsButtonStyle.over = game.actorImages.getSkin().getDrawable("Settings over");

        ImageButton settingsButton = new ImageButton(settingsButtonStyle);
        settingsButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        settingsButton.setPosition(LEFT_BUTTON_X, BOTTOM_BUTTON_Y);
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
                game.setIsServerToTrue();
                // Defaul port is 9000
                game.setUpHost(9000);
                toggleVisibilityCreateClick();
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
                    if (validIP(IPInput.getText())) {
                        if (setUpClient()) {
                            toggleVisibilityJoinSecondClick();
                            waitForGameSetUpAndStartGame();
                        } else {
                            updateErrorLabel(null);
                        }
                    } else {
                        updateErrorLabel(IPInput);
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

    // LABELS AND TEXTFIELD

    public void initializeIPInput() {
        IPInput = new TextField("", game.getDefaultSkin());
        IPInput.setMessageText("IP address");
        IPInput.setWidth(BUTTON_WIDTH * .87f);
        IPInput.setPosition(CENTERED_BUTTON_X + (BUTTON_WIDTH * .065f), TEXT_INPUT_Y);
        IPInput.setTextFieldFilter(new IPInputFilter());
        stage.addActor(IPInput);
    }

    public void initializeWaitForHostLabel() {
        Label waitForHost = new Label("Wait for host to start game", game.getTextSkin(), "button");
        waitForHost.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        waitForHost.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        waitForHost.setAlignment(Align.center);
        waitForHost.setFontScale(FONT_SCALE);
        stage.addActor(waitForHost);
    }

    public void initializeClientsConnectedLabel() {
        this.clientsConnectedLabel = new Label("0 clients connected", game.getTextSkin(), "button");
        clientsConnectedLabel.setPosition(screenWidth * 0.63f, LABEL_Y);
        clientsConnectedLabel.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        clientsConnectedLabel.setAlignment(Align.center);
        clientsConnectedLabel.setFontScale(FONT_SCALE * 0.8f);
        stage.addActor(clientsConnectedLabel);
    }

    public void initializeIPLabel() {
        String IP = "Unknown";
        try {
            IP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignored) {
        }
        IPLabel = new Label("IP: " + IP, game.getTextSkin(), "button");
        IPLabel.setPosition(screenWidth*0.072f, LABEL_Y);
        IPLabel.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        IPLabel.setAlignment(Align.center);
        IPLabel.setFontScale(FONT_SCALE * 0.8f);
        IPLabel.setVisible(false);
        stage.addActor(IPLabel);
    }

    public void updateClientsConnectedLabel() {
        int connected = getClientsConnected();
        clientsConnectedLabel.setText(connected + (connected == 1 ? " client": " clients") + " connected");
    }

    public void toggleVisibilityCreateClick() {
        initializeSelectMap();
        initializeStartButton();
        initializeIPLabel();
        initializeClientsConnectedLabel();
        createGameButton.setVisible(false);
        joinGameButton.setVisible(false);
        selectMap.setVisible(true);
        startButton.setVisible(true);
        IPLabel.setVisible(true);
    }

    public void toggleVisibilityJoinFirstClick() {
        createGameButton.setVisible(false);
        joinGameButton.setPosition(CENTERED_BUTTON_X, TOP_BUTTON_Y);
        initializeIPInput();
        initializeErrorLabel();
    }

    public void toggleVisibilityJoinSecondClick() {
        initializeWaitForHostLabel();
        joinGameButton.setVisible(false);
        IPInput.setVisible(false);
        errorLabel.setVisible(false);
    }

    public void initializeErrorLabel() {
        errorLabel = new Label("", game.getTextSkin(), "button");
        errorLabel.setPosition(CENTERED_BUTTON_X, LABEL_Y);
        errorLabel.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        errorLabel.setAlignment(Align.center);
        errorLabel.setFontScale(FONT_SCALE * 0.8f);
        stage.addActor(errorLabel);
    }

    public void updateErrorLabel(TextField textField) {
        if (textField.equals(IPInput)) {
            errorLabel.setText("Invalid IP address");
        } else {
            errorLabel.setText("Could not connect to " + IPInput.getText() + " on port 9000");
        }
        IPInput.setText("");
    }

    public boolean ipAddress(String ip) {
        for (char c : ip.toCharArray()) {
            if (!(Character.isDigit(c) || (c == '.'))) {
                return false;
            }
        }
        return true;
    }

    public boolean validIP(String ip) {
        return !"".equals(ip) && ("localhost".equals(ip.toLowerCase()) || ipAddress(ip));
    }

    // LAN STUFF

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
        Thread waitForGameSetupThread = new Thread(() -> {
            waitForGameSetup();
            Gdx.app.postRunnable(() -> {
                game.setupGame();
                game.setScreen(new GameScreen(game));
            });
        });
        waitForGameSetupThread.start();
    }

    public int getClientsConnected() {
        return game.getServer().getNumberOfConnectedClients();
    }
}

