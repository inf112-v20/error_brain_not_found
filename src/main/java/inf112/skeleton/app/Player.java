package inf112.skeleton.app;


import inf112.skeleton.app.enums.Direction;

public class Player {

    private Position position;
    private Direction direction;

    public Player(Position position) {
        this.position = position;
        direction = Direction.EAST;
    }

    public void setPosition(Position pos) {
        this.position = pos;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
