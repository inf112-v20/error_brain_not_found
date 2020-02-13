package inf112.skeleton.app;


import com.badlogic.gdx.Game;

public class Player {

    private Position position;

    public Player(Position position) {
        this.position = position;
    }

    public void setPosition(Position pos) {
        this.position = pos;
    }

    public Position getPosition() {
        return position;
    }

}
