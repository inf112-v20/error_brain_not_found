package inf112.skeleton.app.objects;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Rotate;

public class RotatePad {
    private final Rotate rotate;
    private final Vector2 position;

    public RotatePad(Rotate rotate, Vector2 vector) {
        this.rotate = rotate;
        this.position = vector;
    }

    public Rotate getRotate() {
        return rotate;
    }


    public Vector2 getPosition() {
        return position;
    }
}
