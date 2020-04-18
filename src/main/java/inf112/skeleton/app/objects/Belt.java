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

    private final Rotate rotation;
    private final Vector2 position;

    private final Direction direction;
    private final int power;

    public Belt(Direction direction, Rotate rotation, int power, Vector2 position) {
        this.direction = direction;
        this.position = position;
        this.rotation = rotation;
        this.power = power;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public Rotate getRotation() {
        return rotation;
    }

    public int getPower() {
        return power;
    }
}
