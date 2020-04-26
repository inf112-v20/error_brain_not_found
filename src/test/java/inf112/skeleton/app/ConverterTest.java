package inf112.skeleton.app;

import inf112.skeleton.app.LAN.Converter;
import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Rotate;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConverterTest {

    private Converter converter = new Converter();

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
        ProgramCard convertedCard = converter.convertToCardAndExtractPlayer(cardString);
        assertTrue("\n Expected: " + cardInfo(card) + "\n" + "Actual: " + cardInfo(convertedCard), isEqualCards(card, convertedCard));
    }

    @Test
    public void fromStringToCardMoveTest() {
        ProgramCard card = new ProgramCard(20, 3, Rotate.NONE, "Move 3");
        String cardString = "1 20 3 NONE Move 3";
        ProgramCard convertedCard = converter.convertToCardAndExtractPlayer(cardString);
        assertTrue("\n Expected: " + cardInfo(card) + "\n" + "Actual: " + cardInfo(convertedCard), isEqualCards(card, convertedCard));
    }




}

