package inf112.skeleton.app.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import inf112.skeleton.app.RallyGame;
import inf112.skeleton.app.enums.Direction;

public class Laser {
    Vector2 startPosition;
    Direction direction;
    private Sound laserSound = Gdx.audio.newSound(Gdx.files.internal("assets/Sound/laser.mp3"));

    public Laser(int x, int y, Direction direction) {
        this.startPosition = new Vector2(x, y);
        this.direction = direction;
    }

    public void fire(RallyGame game) {
        fire(game, startPosition);
    }

    public void fire(RallyGame game, Vector2 position) {
        game.getBoard().addLaser(position, direction);
        if (game.getBoard().hasPlayer(position)) {
            laserSound.play();
            game.getBoard().getPlayer(position).handleDamage(game);
        } else if (game.getBoard().canGo(position, this.direction)) {
            fire(game, game.getBoard().getNeighbourPosition(position, direction));
        }
    }
}
