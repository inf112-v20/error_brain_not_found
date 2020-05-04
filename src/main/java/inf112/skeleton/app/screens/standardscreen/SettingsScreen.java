package inf112.skeleton.app.screens.standardscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.screens.gamescreen.GameScreen;
import inf112.skeleton.app.screens.menuscreen.MenuScreen;
import inf112.skeleton.app.screens.menuscreen.MenuScreenActors;

public class SettingsScreen extends StandardScreen {

    private Slider musicSlider;
    private Slider soundSlider;

    public float volume;

    public final float screenWidth;
    public final float screenHeight;

    public final float BUTTON_WIDTH;
    public final float BUTTON_HEIGHT;
    public final float BUTTON_X;
    public final float UPDATE_BUTTON_Y;
    public final float SAVE_BUTTON_Y;
    public final float TOGGLE_BUTTON_Y;



    public SettingsScreen(RallyGame game) {


        super(game);

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        BUTTON_WIDTH = (float) (screenWidth * 0.25);
        BUTTON_HEIGHT = (float) (screenHeight * 0.25);
        UPDATE_BUTTON_Y = (float) (screenHeight * 0.5 - BUTTON_HEIGHT *1.2);
        SAVE_BUTTON_Y = (float) (screenHeight * 0.5 - (BUTTON_HEIGHT *2));
        TOGGLE_BUTTON_Y = (float) (screenHeight * 0.5 - (BUTTON_HEIGHT ));
        BUTTON_X = (float) (screenWidth * 0.5 - BUTTON_WIDTH * 0.5);

        volume = 0.5f;

        initializeBackground();
        initializeMusicVolumeSlider();
        initializeMusicVolumeLabel();
        initializeSoundVolumeSlider();
        initializeSoundVolumeLabel();
        initializeUpdateSettings();
        initializeFullscreenButton();


    }
    public void initializeUpdateSettings(){
        ImageButton.ImageButtonStyle updateButtonStyle = new ImageButton.ImageButtonStyle();
        updateButtonStyle.up = game.actorImages.getSkin().getDrawable("Update settings");
        updateButtonStyle.over = game.actorImages.getSkin().getDrawable("Update settings over");

        ImageButton updateButton = new ImageButton(updateButtonStyle);
        updateButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        updateButton.setPosition(BUTTON_X, UPDATE_BUTTON_Y);
        updateButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.gameMusic.dispose();
                volume = musicSlider.getValue();
                game.setVolumeFromSlider(volume);
                game.startMusic();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(updateButton);
    }
    public void saveButton(){
        ImageButton.ImageButtonStyle saveButtonStyle = new ImageButton.ImageButtonStyle();
        saveButtonStyle.up = game.actorImages.getSkin().getDrawable("Save");
        saveButtonStyle.over = game.actorImages.getSkin().getDrawable("Save over");

        ImageButton saveButton = new ImageButton(saveButtonStyle);
        saveButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        saveButton.setPosition(BUTTON_X, UPDATE_BUTTON_Y);
        saveButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.gameMusic.dispose();
                volume = musicSlider.getValue();
                game.setVolumeFromSlider(volume);
                game.startMusic();
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(saveButton);

    }
    public void initializeFullscreenButton() {
        ImageButton.ImageButtonStyle toggleScreenStyle = new ImageButton.ImageButtonStyle();
        toggleScreenStyle.up = game.actorImages.getSkin().getDrawable("Screen Toggle");
        toggleScreenStyle.over = game.actorImages.getSkin().getDrawable("Screen Toggle over");

        ImageButton toggleButton = new ImageButton(toggleScreenStyle);
        toggleButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        toggleButton.setPosition(BUTTON_X, TOGGLE_BUTTON_Y);
        toggleButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.fullscreen();
            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(toggleButton);
    }

    public void initializeMusicVolumeSlider() {
        musicSlider = new Slider(0, 1, 0.01f, false, game.getDefaultSkin());
        musicSlider.setValue(volume);
        musicSlider.setPosition(camera.viewportWidth*0.2f, camera.viewportHeight*0.7f);
        musicSlider.setWidth(camera.viewportWidth*0.6f);
        stage.addActor(musicSlider);
    }

    public void initializeMusicVolumeLabel() {
        Label label = new Label("Music Volume", game.getTextSkin(), "title", Color.WHITE);
        label.setPosition(camera.viewportWidth*0.5f - label.getPrefWidth(), musicSlider.getY() + label.getPrefHeight());
        label.setFontScale(1.5f);
        stage.addActor(label);
    }

    public void initializeSoundVolumeSlider() {
        soundSlider = new Slider(0, 100, 1f, false, game.getDefaultSkin());
        soundSlider.setValue(0.5f);
        soundSlider.setPosition(camera.viewportWidth*0.2f, camera.viewportHeight*0.5f);
        soundSlider.setWidth(camera.viewportWidth*0.5f);
        stage.addActor(soundSlider);

    }

    public void initializeSoundVolumeLabel() {
        Label label = new Label("Sound Volume", game.getTextSkin(), "title", Color.WHITE);
        label.setPosition(camera.viewportWidth*0.5f - label.getPrefWidth(), soundSlider.getY() + label.getPrefHeight() );
        label.setFontScale(1.5f);
        stage.addActor(label);
    }


    public void initializeBackground() {
        Image background = new Image(game.getActorImages().getDrawable("Menu screen background"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        stage.addActor(background);
    }
}
