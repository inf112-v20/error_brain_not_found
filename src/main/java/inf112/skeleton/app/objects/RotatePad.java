package inf112.skeleton.app.objects;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Rotate;

public class RotatePad {
    private Rotate rotate;
    private Vector2 position;

    public RotatePad(Rotate rotate, Vector2 vector){
        this.rotate = rotate;
        this.position = vector;
    }

    public Rotate getRotate() {
        return rotate;
    }

<<<<<<< HEAD
    public Vector2 getPosition() {return position;}
=======
    public Vector2 getPosition() {
        return position;
    }
>>>>>>> develop
}
