package inf112.skeleton.app.screens.standardscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import inf112.skeleton.app.RallyGame;

public class SettingsScreen extends StandardScreen {

    private Slider musicSlider;
    private Slider soundSlider;

    public SettingsScreen(RallyGame game) {
        super(game);
        initializeBackground();
        initializeMusicVolumeSlider();
        initializeMusicVolumeLabel();
        initializeSoundVolumeSlider();
        initializeSoundVolumeLabel();
    }
    public void updateSettings(){

    }
    public void saveButton(){

    }

    public void initializeMusicVolumeSlider() {
        musicSlider = new Slider(0, 1, 0.01f, false, game.getDefaultSkin());
        musicSlider.setValue(game.gameMusic.getVolume());
        musicSlider.setPosition(camera.viewportWidth*0.2f, camera.viewportHeight*0.7f);
        musicSlider.setWidth(camera.viewportWidth*0.6f);
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.gameMusic.setVolume(musicSlider.getValue());
            }
        });
        stage.addActor(musicSlider);
    }

    public void initializeMusicVolumeLabel() {
        Label label = new Label("Music Volume", game.getTextSkin(), "title", Color.WHITE);
        label.setPosition(camera.viewportWidth*0.5f - label.getPrefWidth(), musicSlider.getY() + label.getPrefHeight());
        label.setFontScale(1.5f);
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
        Label label = new Label("Sound Volume", game.getTextSkin(), "title", Color.WHITE);
        label.setPosition(camera.viewportWidth*0.5f - label.getPrefWidth(), soundSlider.getY() + label.getPrefHeight() );
        label.setFontScale(1.5f);
        stage.addActor(label);
    }

    public void initializeFullscreenButton() {
        game.fullscreen();
        // Lag ny knapp, når man trykker skal den kalle på game.fullscreen()
    }

    public void initializeBackground() {
        Image background = new Image(game.getActorImages().getDrawable("Menu screen background"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        stage.addActor(background);
    }
}
