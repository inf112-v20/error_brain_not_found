package inf112.skeleton.app.objects;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;

public class Belt {

    private Vector2 position;

    private Direction direction;
    private int power;

    public Belt(Direction direction, int power, Vector2 position){
        this.direction = direction;
        this.position = position;
        this.power = power;
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

}
