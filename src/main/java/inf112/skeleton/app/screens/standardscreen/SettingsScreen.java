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
        initializeSoundVolumeSlider();
        initializeSoundVolumeLabel();
    }

    public void initializeMusicVolumeSlider() {
        musicSlider = new Slider(0, 1, 0.01f, false, game.getDefaultSkin());

        musicSlider.setValue(0.6f);
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
