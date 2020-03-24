package inf112.skeleton.app.objects;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;
import inf112.skeleton.app.enums.Rotate;

/**
 * <p>
 *     A object to mimic a belt in the game.
 * </p>
 *
 * <p>
 * Since the belts can har two different {@link Rotate}, the {@link Direction} of the {@link inf112.skeleton.app.Player} should be set to the
 * direction of the belt. Should also push in that direction.</p>
 */
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
