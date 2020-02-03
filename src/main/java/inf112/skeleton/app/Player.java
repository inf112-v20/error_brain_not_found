package inf112.skeleton.app;

import com.badlogic.gdx.graphics.Texture;

public class Player {
    Position position;
    Texture texture;

    public Player(Position position, Texture texture) {
        this.position = position;
        this.texture = texture;
    }

    public Position getPosition() {
        return position;
    }

    public Texture getTexture() {
        return texture;
    }
}
