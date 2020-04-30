package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
    public static void main(String[] args) {

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Robo Rally by error_brain_not_found";
        cfg.width = 1280;
        cfg.height = 720;
        cfg.resizable = false;
        cfg.vSyncEnabled = true;
        //cfg.fullscreen = true;

        new LwjglApplication(new RallyGame(), cfg);
    }
}