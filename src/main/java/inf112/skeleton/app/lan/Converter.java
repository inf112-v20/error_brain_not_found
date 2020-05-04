package inf112.skeleton.app.lan;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Rotate;

import java.util.ArrayList;


/**
 * Convert from a string to a programcard and vica verca.
 * Used to send cards between sockets.
 */
public class Converter {

    /**
     * Give players playernumber and programcard, return info as a string.
     * @param playerNumber
     * @param programCard
     * @return playernumber and info about players card in string
     */
    public String convertToString(int playerNumber, ProgramCard programCard) {
        if (programCard == null) {
            System.out.println("no card");
            return "no card";
        }
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
     *
     * @param programCard
     * @return programcard as a string
     */
    public String convertToString(ProgramCard programCard) {
        StringBuilder string = new StringBuilder();
        String prio = String.valueOf(programCard.getPriority());
        String steps = String.valueOf(programCard.getDistance());
        String rotation = String.valueOf(programCard.getRotate());
        String name = programCard.getName();
        string.append(prio + " ");
        string.append(steps + " ");
        string.append(rotation+ " ");
        string.append(name);
        return string.toString();
    }

    /**
     * Convert a string to a corresponding programcard.
     * @param string
     * @return programcard
     * @throws NotProgramCardException
     */
    public ProgramCard convertToCard(String string) throws NotProgramCardException {
        ArrayList<String> strings = splitBySpace(string);
        try {
            int prio = Integer.parseInt(strings.get(0));
            int steps = Integer.parseInt(strings.get(1));
            Rotate rotation = getRotation(string);
            String name = getName(string);
            if (!string.equals(prio + " " + steps + " " + rotation.toString() + " " + name)) {
                throw new NotProgramCardException("This is not a programcard: " +string);
            }
            return new ProgramCard(prio, steps, rotation, name);
        } catch (NumberFormatException error) {
            throw new NotProgramCardException("This is not a programcard: "+string);
        }
    }

    /**
     * Convert a string to a corresponding programcard with a player.
     *
     * @param string
     * @return PlayerAndProgramCard a tuple with playerNumber and card
     * @throws NotProgramCardException
     */
    public PlayerAndProgramCard convertToCardAndExtractPlayer(String string) throws NotProgramCardException {
        try {
            int playerNumber = Character.getNumericValue(string.charAt(0));
            String card = string.substring(2);
            ProgramCard programCard = convertToCard(card);
            return new PlayerAndProgramCard(playerNumber, programCard);
        } catch (NumberFormatException error) {
            throw new NotProgramCardException("This is not a programcard with a player: " + string);
        }
    }

    /**
     * Put words separated by spaces in a list.
     * So "This is an example" becomes ["This", "is", "an", "example"].
     * @param string
     * @return ArrayList<String> List of string with index at spaces.
     */
    public ArrayList<String> splitBySpace(String string) {
        ArrayList<String> strings = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            Character character = string.charAt(i);
            if (character == ' ') {
                strings.add(str.toString());
                str = new StringBuilder();
            } else if (i == string.length()-1) {
                str.append(character);
                strings.add(str.toString());
            } else {
                str.append(character);
            }
        }
        return strings;
    }

    /**
     *
     * @param string
     * @return name of programcard
     */
    private String getName(String string) {
        if (string.contains("U-turn")) {
            return "U-turn";
        } else if (string.contains("Left turn")) {
            return "Left turn";
        } else if (string.contains("Right turn")) {
            return "Right turn";
        } else if (string.contains("Right rotate")) {
            return "Right rotate";
        } else if (string.contains("Left rotate")) {
            return "Left rotate";
        } else if (string.contains("Move 3")) {
            return "Move 3";
        } else if (string.contains("Move 1")) {
            return "Move 1";
        } else if (string.contains("Move 2")) {
            return "Move 2";
        } else if (string.contains("Back up")) {
            return "Back up";
        } else {
            return "Could not match name.";
        }
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
