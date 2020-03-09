package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
    public static void main(String[] args) {

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "robo-rally";
        cfg.width = 960;
        cfg.height = 540;
        //cfg.fullscreen = true;

        new LwjglApplication(new RallyGame(), cfg);
    }
}