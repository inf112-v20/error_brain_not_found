package inf112.skeleton.app;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Rotate;

import java.util.ArrayList;


/**
 * Convert from a string to a programcard and vica verca.
 * Used to send cards between sockets.
 * @author Jenny
 */
public class Converter {

    private int playerNumber;

    /**
     * Give players playernumber and programcard, return info as a string.
     * @param playerNumber
     * @param programCard
     * @return playernumber and info about players card in string
     */
    public String convertToString(int playerNumber, ProgramCard programCard) {
        StringBuilder string = new StringBuilder();
        String player = String.valueOf(playerNumber);
        String prio = String.valueOf(programCard.getPriority());
        String steps = String.valueOf(programCard.getDistance());
        String rotation = String.valueOf(programCard.getRotate());
        String name = programCard.getName();
        string.append(player + " ");
        string.append(prio + " ");
        string.append(steps + " ");
        string.append(rotation+ " ");
        string.append(name);
        return string.toString();
    }

    /**
     * Convert a string to a corresponding programcard. Playernumber to player
     * owning this card is stored in {@link #getPlayerNumber()}
     * @param string
     * @return
     */
    public ProgramCard convertToCardAndExtractPlayer(String string) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            Character character = string.charAt(i);
            if (character == ' ') {
                strings.add(str.toString());
                str = new StringBuilder();
            } else {
                str.append(character);
            }
        }
        this.playerNumber = Integer.parseInt(strings.get(0));
        int prio = Integer.parseInt(strings.get(0));
        int steps = Integer.parseInt(strings.get(0));
        Rotate rotation = getRotation(string);
        String name = getName(string);
        return new ProgramCard(prio, steps, rotation, name);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     *
     * @param string
     * @return name of programcard
     */
    private String getName(String string) {
        if (string.contains("U-turn")) {
            return "U-turn";
        }
        if (string.contains("Right rotate")) {
            return "Right rotate";
        }
        if (string.contains("Left rotate")) {
            return "Left rotate";
        }
        if (string.contains("Move 3")) {
            return "Move 3";
        }
        if (string.contains("Move 1")) {
            return "Move 1";
        }
        if (string.contains("Move 2")) {
            return "Move 2";
        }
        return null;
    }


    /**
     * Get the rotation sent by client
     * @param message
     * @return rotation
     */
    private Rotate getRotation(String message) {
        if (message.contains("UTURN")) {
            return Rotate.UTURN;
        }
        if (message.contains("LEFT")) {
            return Rotate.LEFT;
        }
        if (message.contains("RIGHT")) {
            return Rotate.RIGHT;
        }
        if (message.contains("NONE")) {
            return Rotate.NONE;
        }
        return null;
    }

}
