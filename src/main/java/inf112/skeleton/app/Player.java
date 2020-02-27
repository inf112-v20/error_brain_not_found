package inf112.skeleton.app;


import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.enums.Direction;

public class Player {

    private final int playerNr;
    private Vector2 backupPosition;
    private Vector2 position;
    private Direction direction;

    public Player(Vector2 position, int playerNr) {
        this.position = position;
        this.direction = Direction.EAST;
        this.playerNr = playerNr;
        setBackupPosition(position);
    }

    /**
     * Set new backup position
     * @param backupPosition respawn position when damaged
     */
    public void setBackupPosition(Vector2 backupPosition) {
        if (this.backupPosition == null) {
            this.backupPosition = new Vector2(backupPosition.x, backupPosition.y);
        } else {
            this.backupPosition.set(backupPosition.x, backupPosition.y);
        }
    }

    /**
     * @return backup position
     */
    public Vector2 getBackupPosition() {
        return backupPosition;
    }

    /**
     * @return number of player
     */
    public int getPlayerNr() {return playerNr; }

    /**
     * @return the {@link Vector2} to the player
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Set's the position to the player
     */
    public void setPosition(Vector2 pos) {
        this.position.set(pos.x, pos.y);
    }

    /**
     * @return the direction the player are facing
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Set's the direction the player are suppose to face
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
