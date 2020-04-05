package inf112.skeleton.app;

import inf112.skeleton.app.cards.ProgramCard;

import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 *
 * Used to let other players know you program
 *
 * @author Jenny
 * @param <Player> player this program belong to.
 * @param ArrayList<ProgramCard> program for this player
 */
public class Program {

    private Player player;
    private ArrayList<ProgramCard> program;

    public Program(Player player, ArrayList<ProgramCard> program) {
        this.player = player;
        this.program = program;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<ProgramCard> getProgram() {
        return program;
    }

}
