package inf112.skeleton.app.objects;

import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.objects.Player.Player;

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

    public void rotate(Player player) {
        switch (rotate) {
            case LEFT:
                player.setDirection(player.getDirection().turnLeft());
                break;
            case RIGHT:
                player.setDirection(player.getDirection().turnRight());
                break;
            case UTURN:
                player.setDirection(player.getDirection().turnAround());
                break;
            default:
                // Will never happen
        }
    }
}
