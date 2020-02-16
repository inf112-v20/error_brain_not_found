package inf112.skeleton.app;


import inf112.skeleton.app.enums.Direction;

public class Player {

    private Position position;
    private Direction direction;

    public Player(Position position) {
        this.position = position;
        direction = Direction.EAST;
    }

    /** Set's the position to the player */
    public void setPosition(Position pos) {
        this.position = pos;
    }

    /** @return the {@link Position} to the player */
    public Position getPosition() {
        return position;
    }

    /** @return the direction the player are facing */
    public Direction getDirection() {
        return direction;
    }

    /** Set's the direction the player are suppose to face */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
