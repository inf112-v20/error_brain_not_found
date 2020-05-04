package inf112.skeleton.app.screens.standardscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import inf112.skeleton.app.RallyGame;

public class SettingsScreen extends StandardScreen {

    private Slider musicSlider;
    private Slider soundSlider;

    public SettingsScreen(RallyGame game) {
        super(game);
        initializeBackground();
        initializeMusicVolumeSlider();
        initializeMusicVolumeLabel();
    }

    public void initializeMusicVolumeSlider() {
        musicSlider = new Slider(0, 100, 1f, false, game.getDefaultSkin());

        game.sliderVolume(musicSlider.getValue());
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
        soundSlider = new Slider(0, 100, 0.9f, false, game.getDefaultSkin());
        soundSlider.setValue(50f);
        soundSlider.setPosition(camera.viewportWidth*0.1f, camera.viewportHeight*0.6f);
        soundSlider.setWidth(camera.viewportWidth*0.5f);
        stage.addActor(soundSlider);

    }

    public void initializeSoundVolumeLabel() {

    }

    public void initializeFullscreenButton() {
        game.fullscreen();
        // Lag ny knapp, når man trykker skal den kallle på game.fullscreen()
    }

    public void initializeBackground() {
        Image background = new Image(game.getActorImages().getDrawable("Menu screen background"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        stage.addActor(background);
    }
}
