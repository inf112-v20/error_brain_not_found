package inf112.skeleton.app.gameObjects;

import com.badlogic.gdx.math.Vector2;

public class Flag {
    private final int flagnr;

    private Vector2 position;

    public Flag(int flagnr, Vector2 position) {
        this.flagnr = flagnr;
        this.position = position;
    }


    public Flag(int flagnr, int x, int y) {
        this.flagnr = flagnr;
        this.position = new Vector2(x, y);
    }


}
