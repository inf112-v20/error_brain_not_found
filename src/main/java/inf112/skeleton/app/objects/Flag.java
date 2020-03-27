package inf112.skeleton.app.objects;

import com.badlogic.gdx.math.Vector2;

public class Flag {
    private final int flagnr;

    private final Vector2 position;

    public Flag(int flagnr, int x, int y) {
        this.flagnr = flagnr;
        this.position = new Vector2(x, y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getFlagnr() {
        return flagnr;
    }
}
