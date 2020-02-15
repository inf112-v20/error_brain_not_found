package inf112.skeleton.app;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import inf112.skeleton.app.enums.Direction;

public class Main {
    public static void main(String[] args) {

        System.out.println(Direction.EAST.turnLeft());

        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "robo-rally";
        cfg.width = 500;
        cfg.height = 500;

        new LwjglApplication(new RallyGame(), cfg);
    }
}