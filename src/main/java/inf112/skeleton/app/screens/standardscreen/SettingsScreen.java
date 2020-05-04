package inf112.skeleton.app.screens.standardscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import inf112.skeleton.app.RallyGame;

public class SettingsScreen extends StandardScreen {

    private Slider slider;

    public SettingsScreen(RallyGame game) {
        super(game);
        initializeBackground();
        initializeMusicVolumeSlider();
        initializeMusicVolumeLabel();
    }

    public void initializeMusicVolumeSlider() {
        slider = new Slider(0, 100, 1f, false, game.getDefaultSkin());
        slider.setValue(50f);
        slider.setPosition(camera.viewportWidth*0.2f, camera.viewportHeight*0.7f);
        slider.setWidth(camera.viewportWidth*0.6f);
        stage.addActor(slider);
    }

    public void initializeMusicVolumeLabel() {
        Label label = new Label("Music Volume", game.getTextSkin(), "title", Color.WHITE);
        label.setPosition(camera.viewportWidth*0.5f - label.getPrefWidth(), slider.getY() + label.getPrefHeight());
        label.setFontScale(1.5f);
        stage.addActor(label);
    }

    public void initializeSoundVolumeSlider() {

    }

    public void initializeSoundVolumeLabel() {

    }

    public void initializeFullscreenButton() {
        // Lag ny knapp, når man trykker skal den kallle på game.fullscreen()
    }

    public void initializeBackground() {
        Image background = new Image(game.getActorImages().getDrawable("Menu screen background"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        stage.addActor(background);
    }
}
