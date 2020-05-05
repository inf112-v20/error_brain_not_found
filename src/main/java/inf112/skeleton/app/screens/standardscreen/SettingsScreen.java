package inf112.skeleton.app.screens.standardscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import inf112.skeleton.app.RallyGame;

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
        initializeFullscreenButton();
        initializeBackButton();


    }
    public void initializeBackButton(){
        ImageButton.ImageButtonStyle backButtonStyle = new ImageButton.ImageButtonStyle();
        backButtonStyle.up = game.actorImages.getSkin().getDrawable("Back");
        backButtonStyle.over = game.actorImages.getSkin().getDrawable("Back over");

        ImageButton backButton = new ImageButton(backButtonStyle);
        backButton.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton.setPosition(BUTTON_X, SAVE_BUTTON_Y);
        backButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.returnToLastScreen();
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(backButton);

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
        musicSlider.setValue(0.5f);
        musicSlider.setPosition(camera.viewportWidth*0.2f, camera.viewportHeight*0.7f);
        musicSlider.setWidth(camera.viewportWidth*0.5f);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.gameMusic.setVolume(musicSlider.getValue());
            }
        });
        stage.addActor(musicSlider);
    }

    public void initializeMusicVolumeLabel() {
        Label label = new Label("Music Volume", game.getTextSkin(), "button", Color.WHITE);
        label.setPosition(camera.viewportWidth*0.5f - label.getPrefWidth()*1.3f, musicSlider.getY() + label.getPrefHeight()*0.7f);
        label.setFontScale(0.5f);
        stage.addActor(label);
    }

    public void initializeSoundVolumeSlider() {
        soundSlider = new Slider(0, 1, 0.01f, false, game.getDefaultSkin());
        soundSlider.setValue(RallyGame.soundVolume);
        soundSlider.setPosition(camera.viewportWidth*0.2f, camera.viewportHeight*0.5f);
        soundSlider.setWidth(camera.viewportWidth*0.5f);
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                RallyGame.soundVolume = soundSlider.getValue();
            }
        });
        stage.addActor(soundSlider);

    }

    public void initializeSoundVolumeLabel() {
        Label label = new Label("Sound Volume", game.getTextSkin(), "button", Color.WHITE);
        label.setPosition(camera.viewportWidth*0.5f - label.getPrefWidth()*1.23f, soundSlider.getY() + label.getPrefHeight()*0.7f );
        label.setFontScale(0.5f);
        stage.addActor(label);
    }


    public void initializeBackground() {
        Image background = new Image(game.getActorImages().getDrawable("Menu screen background"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        stage.addActor(background);
    }
}
