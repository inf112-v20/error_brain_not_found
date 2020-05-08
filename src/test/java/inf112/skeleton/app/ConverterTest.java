package inf112.skeleton.app;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Messages;
import inf112.skeleton.app.enums.Rotate;
import inf112.skeleton.app.lan.Converter;
import inf112.skeleton.app.lan.NotProgramCardException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConverterTest {

    private final Converter converter = new Converter();

    /**
     * Check if card1 and card2 are equal
     * @param card1
     * @param card2
     * @return true if cards have same prio, distance, rotate and name.
     */
    private boolean isEqualCards(ProgramCard card1, ProgramCard card2) {
        return card1.getPriority() == card2.getPriority() &&
                card1.getDistance() == card2.getDistance() &&
                card1.getRotate().equals(card2.getRotate()) &&
                card1.getName().equals(card2.getName());
    }

    private String cardInfo(ProgramCard card) {
        return "Prio: " + card.getPriority() + " Dist: " + card.getDistance() + " Rot: " + card.getRotate() + " Name:" + card.getName();
    }

    @Test
    public void fromCardToStringRotateTest() {
        ProgramCard card = new ProgramCard(10, 0, Rotate.LEFT, "Left rotate");
        assertEquals("1 10 0 LEFT Left rotate", converter.convertToString(1, card));
    }

    @Test
    public void fromCardToStringMoveTest() {
        ProgramCard card = new ProgramCard(20, 2, Rotate.NONE, "Move 2");
        assertEquals("1 20 2 NONE Move 2", converter.convertToString(1, card));
    }

    @Test
    public void splitIntoListTest() {
        ArrayList<String> list = new ArrayList<>();
        list.add("This");
        list.add("is");
        list.add("a");
        list.add("test.");
        assertEquals(list, converter.splitBySpace("This is a test."));
    }

    @Test
    public void fromStringToCardRotateTest() {
        ProgramCard card = new ProgramCard(10, 0, Rotate.RIGHT, "Right rotate");
        String cardString = "1 10 0 RIGHT Right rotate";
        try {
            String messageFromPlayer = converter.getMessageFromPlayer(cardString);
            ProgramCard convertedcard = converter.convertToCard(messageFromPlayer);
            assertTrue("\n Expected: " + cardInfo(card) + "\n" + "Actual: " + cardInfo(convertedcard), isEqualCards(card, convertedcard));
        } catch (NotProgramCardException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fromStringToCardMoveTest() {
        ProgramCard card = new ProgramCard(20, 3, Rotate.NONE, "Move 3");
        String cardString = "1 20 3 NONE Move 3";
        try {
            String messageFromPlayer = converter.getMessageFromPlayer(cardString);
            ProgramCard convertedcard = converter.convertToCard(messageFromPlayer);
            assertTrue("\n Expected: " + cardInfo(card) + "\n" + "Actual: " + cardInfo(convertedcard), isEqualCards(card, convertedcard));
        } catch (NotProgramCardException e) {
            e.printStackTrace();
        }

    }

    @Test(expected = NotProgramCardException.class)
    public void throwsExceptionWhenNotACardTest() throws NotProgramCardException {
        String notACardsString = "This is not a card";
        converter.convertToCard(notACardsString);
    }

    @Test(expected = NotProgramCardException.class)
    public void throwsExceptionWhenOnlyCardNotCardAndPlayerTest() throws NotProgramCardException {
        String cardString = "1 20 3 NONE Move 3";
        converter.convertToCard(cardString);
    }

    @Test
    public void fromStringToCardTest() {
        ProgramCard card = new ProgramCard(1, 2, Rotate.NONE, "Move 2");
        String cardString = "1 2 NONE Move 2";
        try {
            assertTrue("\n Expected: " + cardInfo(card) + "\n" + "Actual: " + cardInfo(converter.convertToCard(cardString)), isEqualCards(card, converter.convertToCard(cardString)));
        } catch (NotProgramCardException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void backWardCardStringToCardTest() {
        String cardString = "200 -1 NONE Back up";
        ProgramCard card = new ProgramCard(200, -1, Rotate.NONE, "Back up");
        try {
            assertTrue("\n Expected: " + cardInfo(card) + "\n" + "Actual: " + cardInfo(converter.convertToCard(cardString)), isEqualCards(card, converter.convertToCard(cardString)));
        } catch (NotProgramCardException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createPoweringDownMessageFromPlayer3Test() {
        assertEquals("3 POWERING_DOWN", converter.createMessageFromPlayer(3, Messages.POWERING_DOWN));
    }

    @Test
    public void createPoweringUpMessageFromPlayer2Test() {
        assertEquals("2 POWER_UP", converter.createMessageFromPlayer(2, Messages.POWER_UP));
    }

    @Test
    public void isMessageFromPlayerTest() {
        String message = "1 hello I am player 1";
        assertTrue(converter.isMessageFromAnotherPlayer(message));
    }

    @Test
    public void isNotMessageFromPlayerTest() {
        String message = "hello I am player 2";
        assertFalse(converter.isMessageFromAnotherPlayer(message));
    }

    @Test
    public void getPlayerNumberFromMessage() {
        String message = "5 hello I am player 1";
        assertEquals(5, converter.getPlayerNumberFromMessage(message));
    }

    @Test
    public void giveNegativePlayerNumberIfNotMessageFromPlayerTest() {
        assertEquals(-1, converter.getPlayerNumberFromMessage("hello :)"));
    }

    @Test
    public void getMessageFromPlayerTest() {
        assertEquals("Hello I am player 6. :)", converter.getMessageFromPlayer("6 Hello I am player 6. :)"));
    }

    @Test
    public void getMapPathFromMessageTest() {
        assertEquals("My map path", converter.getMapPath(converter.createMapPathMessage("My map path")));
    }

    @Test
    public void getNumberOfPlayersTest() {
        assertEquals(1, converter.getNumbersOfPlayers(converter.createNumberOfPlayersMessage(1)));
    }

}

