package inf112.skeleton.app;

/**
 *
 * Used to send moves between the sockets.
 *
 * @author Jenny
 * @param <Player> player to be moved.
 * @param <Programcard> Programcard used for this move
 */
public class Move<Player, Programcard> {

    private Player player;
    private Programcard programcard;

    public Move(Player player, Programcard programcard) {
        this.player = player;
        this.programcard = programcard;
    }

    public Player getPlayer() {
        return player;
    }

    public Programcard getProgramcard() {
        return programcard;
    }
}
