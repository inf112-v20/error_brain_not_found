package inf112.skeleton.app;

import inf112.skeleton.app.cards.ProgramCard;
import inf112.skeleton.app.enums.Rotate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProgramCardTest {

    private ProgramCard card;

    @Before
    public void setUp() {
        card = new ProgramCard(1, 1, Rotate.NONE, "card1");
    }

    @Test
    public void whenRotationIsSetToNoneItHasNoRotation() {
        assertEquals(Rotate.NONE, card.getRotate());
    }

}
