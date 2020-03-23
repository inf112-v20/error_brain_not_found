package inf112.skeleton.app.objects;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Rotate;

public class Belt {

    private Vector2 position;

    private Direction direction;
    private int power;
    private Rotate rotate;

    public Belt(Direction direction, Rotate rotate, int power, Vector2 position){
        this.direction = direction;
        this.position = position;
        this.power = power;
        this.rotate = rotate;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getPower() {
        return power;
    }

    public Rotate getRotate() {
        return rotate;
    }
}
