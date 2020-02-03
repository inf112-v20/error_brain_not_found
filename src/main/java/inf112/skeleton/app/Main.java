package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        /*
        For later
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "robo-rally";
        cfg.width = 256;
        cfg.height = 256;

        new LwjglApplication(new Board(), cfg);
    }
}